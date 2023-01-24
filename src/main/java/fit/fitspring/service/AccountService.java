package fit.fitspring.service;

import fit.fitspring.config.Secret;
import fit.fitspring.controller.dto.account.AccountForRegisterDto;
import fit.fitspring.controller.dto.account.RegisterCustomerDto;
import fit.fitspring.controller.dto.account.RegisterTrainerDto;
import fit.fitspring.controller.mdoel.account.PostAccountRes;
import fit.fitspring.controller.mdoel.account.PostLoginRes;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.domain.account.School;
import fit.fitspring.domain.account.SchoolRepository;
import fit.fitspring.domain.trainer.Trainer;
import fit.fitspring.domain.trainer.TrainerRepository;
import fit.fitspring.domain.trainer.UserImg;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.account.DuplicatedAccountException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.jwt.TokenProvider;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.utils.AES128;
import fit.fitspring.utils.S3Uploader;
import fit.fitspring.utils.ValidationRegex;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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

    @Value("{user.info.pw.key}") String pwKey;

    // 로그인 Service
    public PostLoginRes login(String email, String password) {
        // 0. 비밀번호 풀기
        String pwdEncode = accountRepository.findByEmail(email).get().getPassword();
        String pwdDecode;

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
            String jwt = tokenProvider.createToken(authentication);
            return new PostLoginRes(jwt);
        }
        else{
            throw new BusinessException(ErrorCode.FAILED_TO_LOGIN);
        }
    }

    public String registerCustomer(RegisterCustomerDto registerDto) throws BusinessException {

        // 이메일, 비밀번호, 이름이 입력 되지 않았을 경우 에러 코드 리턴
        if (registerDto.getEmail() == null) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_EMAIL);
        }
        // 이름이 입력되지 않았을 경우 에러 리턴
        if (registerDto.getName() ==null) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_NAME);
        }
        // 이메일 형식이 맞지 않은 경우 에러 코드 리턴
        if (!ValidationRegex.isRegexEmail(registerDto.getEmail())){
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_INVALID_EMAIL);
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
            accountRepository.save(account); // 일단 데이터 저장
            return (registerDto.getName() + " 님, 환영합니다."); // 가입한 회원의 이름 반환
        } catch (DataIntegrityViolationException e){ // 중복 이메일 게정 체크
            throw new DuplicatedAccountException();
        } catch(Exception exception){
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    public String registerTrainer(RegisterTrainerDto registerDto) throws BusinessException {
        if (registerDto.getEmail() == null) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_EMAIL);
        }
        if (registerDto.getName() ==null) {
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_EMPTY_NAME);
        }
        if (!ValidationRegex.isRegexEmail(registerDto.getEmail())){
            throw new BusinessException(ErrorCode.POST_ACCOUNTS_INVALID_EMAIL);
        }

        String pwd;
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
        trainer.setSchool(convertEmailToSchool(registerDto.getEmail())); // 학교 추가 필요
        trainer.setUser(account);
        // UserImg
        UserImg userImg = new UserImg();
        userImg.setProfile("trainerProfile");
        userImg.setTrainer(trainer);

        trainer.setUserImg(userImg);
        account.setTrainer(trainer);

        try{ // 회원가입 정보 저장, jwt 생성, 결과 반환(userIdx, jwt)
            accountRepository.save(account); // 일단 데이터 저장
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
}
