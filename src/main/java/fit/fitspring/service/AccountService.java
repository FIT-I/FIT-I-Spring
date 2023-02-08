package fit.fitspring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fit.fitspring.controller.dto.account.*;
import fit.fitspring.domain.account.*;
import fit.fitspring.domain.trainer.*;
import fit.fitspring.exception.account.AccountNotFoundException;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.account.DuplicatedAccountException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.jwt.TokenProvider;
import fit.fitspring.utils.AES128;
import fit.fitspring.utils.S3Uploader;
import fit.fitspring.utils.ValidationRegex;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final S3Uploader s3Uploader;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TrainerRepository trainerRepository;
    private final SchoolRepository schoolRepository;
    private final LevelRepository levelRepository;
    private final TermAgreeRepository termAgreeRepository;
    private final TermRepository termRepository;
    @Autowired
    private final RedisTemplate redisTemplate;
    //private final RedisUtil redisUtil;

    @Value("{user.info.pw.key}") String pwKey;

    // 로그인 Service
    public TokenDto login(String email, String password) throws JsonProcessingException {
        // 0-1. 비밀번호 풀기
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(AccountNotFoundException::new);
        String pwdEncode = account.getPassword();
        String pwdDecode;

        // 0-2. 탈퇴 계정인지 확인하기
        if (account.getUserState().equals("D")){
            throw new BusinessException(ErrorCode.DELETE_ACCOUNT);
        }

        try{
            // 비번 암호 풀기
            pwdDecode = new AES128(pwKey).decrypt(pwdEncode);
        } catch (Exception ignored){
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        if(pwdDecode.equals(password)) {
            // 1. ID/pwd 를 기반으로 Authentication 객체 생성
            //    이때 authentication은 인증 여부를 확인하는 authenticated 값이 false
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(email,pwdEncode);

            // 2. 실제 검증 (사용자 비밀번호 체크)
            //    authenticate 메서드 실행 => CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 바탕으로 JWT 토큰 생성
            TokenDto jwt = tokenProvider.createToken(authentication);
            return jwt;
        }
        else{
            throw new BusinessException(ErrorCode.FAILED_TO_LOGIN);
        }
    }

    @Transactional
    public String registerCustomer(RegisterCustomerDto registerDto) throws BusinessException {

        // 이름 null 확인 + 공백 확인
        if (registerDto.getName() ==null || registerDto.getName().trim().length() == 0 ) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_NAME);
        }
        // eamil null 확인
        if (registerDto.getEmail() == null) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_EMAIL);
        }
        // email 형식 확인
        if (!ValidationRegex.isRegexEmail(registerDto.getEmail())){
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_INVALID_EMAIL);
        }
        // pwd null 확인 + 공백 확인
        if (registerDto.getPassword() ==null || registerDto.getPassword().trim().length() == 0 ) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_NAME);
        }
        // db 중복 이메일 체크
        if (checkEmail(registerDto.getEmail()) == true) {
            // 탈퇴한 전적이 있는 계정인가?
            if(checkState(registerDto.getEmail()).equals("D")){
                throw new BusinessException(ErrorCode.DENIED_REGISTER);
            }
            // 이미 가입된 계정인가? (active, inactive)
            else{
                throw new DuplicatedAccountException();
            }
        }


        String pwd;
        String email = registerDto.getEmail();

        // 비번 암호화 + 이메일 중복 확인
        try {
            pwd = new AES128(pwKey).encrypt(registerDto.getPassword()); // 비번 암호화
            registerDto.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BusinessException(ErrorCode.DUPLICATE_ACCOUNT);
        }
        Account account = registerDto.toEntity();


        try{ // 회원가입 정보 저장, jwt 생성, 결과 반환(userIdx, jwt)
            // 1. 일단 데이터 저장
            accountRepository.save(account);
            // 2. 저장한 user에서 userIdx 뽑아오기
            Account user = accountRepository.findByEmail(email).orElseThrow();
            // 3. 약관 동의
            acceptTerm(true, true, true, user);

            return (registerDto.getName() + " 님, 환영합니다."); // 가입한 회원의 이름 반환
        } catch (DataIntegrityViolationException e){ // 중복 이메일 게정 체크
            throw new DuplicatedAccountException();
        } catch(Exception exception){
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    public String registerValidation(RegisterValidationDto registerDto){
        // 이름 null 확인 + 공백 확인
        if (registerDto.getName() ==null || registerDto.getName().trim().length() == 0 ) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_NAME);
        }
        // eamil null 확인
        else if (registerDto.getEmail() == null || registerDto.getEmail().trim().length() == 0) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_EMAIL);
        }
        // email 형식 확인
        else if (!ValidationRegex.isRegexEmail(registerDto.getEmail())){
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_INVALID_EMAIL);
        }
        // pwd null 확인 + 공백 확인
        else if (registerDto.getPassword() ==null || registerDto.getPassword().trim().length() == 0 ) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_PWD);
        }
        // db 중복 이메일 체크
        else if (checkEmail(registerDto.getEmail()) == true) {
            // 탈퇴한 전적이 있는 계정인가?
            if(checkState(registerDto.getEmail()).equals("D")){
                throw new BusinessException(ErrorCode.DENIED_REGISTER);
            }
            // 이미 가입된 계정인가? (active, inactive)
            else{
                throw new DuplicatedAccountException();
            }
        }
        else{
            return "validation에 통과하였습니다.";
        }
    }

    @Transactional
    public String registerTrainer(RegisterTrainerDto registerDto) throws BusinessException {
        // 이름 null 확인 + 공백 확인
        if (registerDto.getName() ==null || registerDto.getName().trim().length() == 0 ) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_NAME);
        }
        // eamil null 확인
        if (registerDto.getEmail() == null || registerDto.getEmail().trim().length() == 0) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_EMAIL);
        }
        // email 형식 확인
        if (!ValidationRegex.isRegexEmail(registerDto.getEmail())){
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_INVALID_EMAIL);
        }
        // pwd null 확인 + 공백 확인
        if (registerDto.getPassword() ==null || registerDto.getPassword().trim().length() == 0 ) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_PWD);
        }
        // 전공 입력 확인 + 공백 확인
        if (registerDto.getMajor() ==null || registerDto.getMajor().trim().length() == 0 ) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_MAJOR);
        }
        // db 중복 이메일 체크
        if (checkEmail(registerDto.getEmail()) == true) {
            // 탈퇴한 전적이 있는 계정인가?
            if(checkState(registerDto.getEmail()).equals("D")){
                throw new BusinessException(ErrorCode.DENIED_REGISTER);
            }
            // 이미 가입된 계정인가? (active, inactive)
            else{
                throw new DuplicatedAccountException();
            }
        }


        String pwd;
        String email = registerDto.getEmail();
        try {
            pwd = new AES128(pwKey).encrypt(registerDto.getPassword());
            registerDto.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BusinessException(ErrorCode.DUPLICATE_ACCOUNT);
        }

        Account account = registerDto.toEntity();
        // Trainer
        Trainer trainer = new Trainer();
        trainer.setMajor(registerDto.getMajor());
        trainer.setGrade(0);
        trainer.setSchool(convertEmailToSchool(registerDto.getEmail()));
        trainer.setCategory(Category.PERSONAL_PT);
        trainer.setUser(account);
        trainer.setPriceHour(10000);
        Optional<Level> optional = levelRepository.findById(1L);
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.NO_EXIST_LEVEL_DATA);
        }
        trainer.setLevel(optional.get());
        // UserImg
        UserImg userImg = new UserImg();
        userImg.setProfile("trainerProfile");
        userImg.setTrainer(trainer);

        trainer.setUserImg(userImg);
        account.setTrainer(trainer);

        try{ // 회원가입 정보 저장, jwt 생성, 결과 반환(userIdx, jwt)
            // 1. 일단 데이터 저장
            accountRepository.save(account);
            // 2. 저장한 user에서 userIdx 뽑아오기
            Account user = accountRepository.findByEmail(email).orElseThrow();
            // 3. 약관 동의
            acceptTerm(true, true, true, user);
        } catch (DataIntegrityViolationException e){ // 중복 이메일 게정 체크
            throw new DuplicatedAccountException();
        } catch(Exception exception){
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }

        return (registerDto.getName() + " 님, 환영합니다."); // 가입한 회원의 이름 반환
    }


    public String getCertificationNumber(String email) throws BusinessException{
        String number = "";
        for(int i = 0; i < 6; i++){
            number += Integer.toString((int)(Math.random() * 9));
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("fitiofficial@naver.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("[Fit-I] 인증번호");
        simpleMailMessage.setText(number);
        try {
            javaMailSender.send(simpleMailMessage);
            return number;
        } catch (Exception e){
            throw new BusinessException(ErrorCode.EMAIL_SENDING_ERROR);
        }
    }
    public Account getByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }


    public List<Account> findAllByEmails(List<String> emails){
        return accountRepository.findByEmailIn(emails);
    }

    public Account findById(Long userId) {
        return accountRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
    }
    public Account getById(Long id){
        return accountRepository.getReferenceById(id);
    }


    // db에 email존재 여부 확인
    public boolean checkEmail(String email) throws BusinessException {
        try{
            return accountRepository.findByEmail(email).isPresent();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    // db에 존재하는 email의 status 확인
    public String checkState(String email) throws BusinessException {
        try{
            return accountRepository.findByEmail(email).get().getUserState();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    public String convertEmailToSchool(String email){
        email = email.substring(email.indexOf("@") + 1);
        Optional<School> optional = schoolRepository.findByEmail(email);
        if(optional.isEmpty()){
            return "미처리";
        } else{
            return optional.get().getName();
        }
    }

    public String getUserPassword(String email) throws BusinessException{
        Optional<Account> optional = accountRepository.findByEmail(email);
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_EMAIL);
        }
        try{
            String pwdDecode = new AES128(pwKey).decrypt(optional.get().getPassword());
            return pwdDecode;
        } catch (Exception ignored){
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    // 토큰 재발급
    public TokenDto reissue(String accessToken, String refreshToken) throws JsonProcessingException {
        // 토큰에서 이메일 추출
        String email = tokenProvider.getEmail(accessToken);
        System.out.println("email in token: " + email);

        // 이메일로 비번 추출
        String pwd = accountRepository.findByEmail(email).get().getPassword();

        // refreshToken의 만료 시간 유효성 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }

        // 이메일 + pwd로 새로운 토큰 생성
        // 1. ID/pwd 를 기반으로 Authentication 객체 생성
        //    이때 authentication은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(email,pwd);

        // 2. 실제 검증 (사용자 비밀번호 체크)
        //    authenticate 메서드 실행 => CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 바탕으로 JWT 토큰 생성
        TokenDto jwt = tokenProvider.createToken(authentication);

        // 4. 기존 토큰 로그아웃
        logout(accessToken, refreshToken);

        return jwt;
    }


    //로그아웃
    public void logout(String accessToken, String refreshToken){
//        redisUtil.setBlackList(accessToken, "accessToken", 1800);
//        redisUtil.setBlackList(refreshToken, "refreshToken", 60400);
        Long expirationAccess = tokenProvider.getExpiration(accessToken);
        Long expirationRefresh = tokenProvider.getExpiration(refreshToken);

        redisTemplate.opsForValue().set(accessToken, "logout", expirationAccess, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(refreshToken, "logout", expirationRefresh, TimeUnit.MILLISECONDS);
    }

    @Transactional
    public String deleteAccount(Long userIdx){
        // on(Active), off(Inactive) 상태 확인하기
        Account user = accountRepository.findById(userIdx).orElseThrow();
        String status = accountRepository.findById(userIdx).get().getUserState();
        try{
            if(status.equals("D")){
                return "이미 탈퇴한 계정입니다.";
            }
            else {
                user.modifyState("D");
                return accountRepository.findById(userIdx).get().getEmail() + " 계정이 탈퇴되었습니다.";
            }
        } catch(Exception e){
            throw new BusinessException(ErrorCode.DB_MODIFY_ERROR);
        }

    }

    @Transactional
    public void modifyPassword(Long userIdx, ModifyPassword pwdM){
        Account user = accountRepository.findById(userIdx).orElseThrow();
        try{
            // 1. 비밀번호 변경
            String pwdEncode = new AES128(pwKey).encrypt(pwdM.getPassword());
            user.modifyPassword(pwdEncode);

            // 2. 기존의 인증 객체 삭제하기 - 로그아웃과 유사
            logout(pwdM.getAccessToken(), pwdM.getRefreshToken());
        } catch(Exception e) {
            throw new BusinessException(ErrorCode.DB_MODIFY_ERROR);
        }
    }

    @Transactional
    public void acceptTerm(Boolean term1, Boolean term2, Boolean term3, Account account){
        TermAgree termAgree1 = new TermAgree();
        TermAgree termAgree2 = new TermAgree();
        TermAgree termAgree3 = new TermAgree();

        termAgree1.setUser(account);
        termAgree2.setUser(account);
        termAgree3.setUser(account);

        termAgree1.setTerm(termRepository.findById(1L).orElseThrow());
        termAgree2.setTerm(termRepository.findById(2L).orElseThrow());
        termAgree3.setTerm(termRepository.findById(3L).orElseThrow());

        termAgree1.setAgree(true);
        termAgree2.setAgree(true);
        termAgree3.setAgree(true);

        termAgreeRepository.save(termAgree1);
        termAgreeRepository.save(termAgree2);
        termAgreeRepository.save(termAgree3);
    }

    public String emailValidationCheck(String email) {
        // eamil null 확인
        if (email == null || email.trim().length() == 0) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_EMAIL);
        }
        // email 형식 확인
        else if (!ValidationRegex.isRegexEmail(email)){
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_INVALID_EMAIL);
        }
        // db 중복 이메일 체크
        else if (checkEmail(email) == true) {
            // 탈퇴한 전적이 있는 계정인가?
            if(checkState(email).equals("D")){
                throw new BusinessException(ErrorCode.DENIED_REGISTER);
            }
            // 이미 가입된 계정인가? (active, inactive)
            else{
                throw new DuplicatedAccountException();
            }
        }
        else{
            return "validation에 통과하였습니다.";
        }
    }
}
