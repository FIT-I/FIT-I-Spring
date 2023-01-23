package fit.fitspring.service;

import fit.fitspring.controller.mdoel.account.PostLoginRes;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.jwt.TokenProvider;
import fit.fitspring.utils.S3Uploader;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    // 로그인 Service
    public PostLoginRes login(String email, String password) {
        // 1. ID/pwd 를 기반으로 Authentication 객체 생성
        //    이때 authentication은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(email,password);

        // 2. 실제 검증 (사용자 비밀번호 체크)
        //    authenticate 메서드 실행 => CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 바탕으로 JWT 토큰 생성
        String jwt = tokenProvider.createToken(authentication);
        return new PostLoginRes(jwt);
    }

//    public PostAccountRes registerAccount(AccountForRegisterDto accountDto) throws BusinessException {
//        String pwd;
//        String email = accountDto.getEmail();
//
//        // 비번 암호화 + 이메일 중복 확인
//        try {
//            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(accountDto.getPassword()); // 비번 암호화
//            accountDto.setPassword(pwd);
//        } catch (Exception ignored) {
//            throw new BusinessException(ErrorCode.DUPLICATE_ACCOUNT);
//        }
//
//        Account account = accountDto.toEntity();
//
//        // 회원가입 정보 저장, jwt 생성, 결과 반환(userIdx, jwt)
//        try{
//            accountRepository.save(account); // 일단 데이터 저장
//            int userIdx = accountRepository.findByEmail(email).get().getId().intValue(); // 데이터 저장하면서 자동 생성된 id 가져오기
//            String jwt = jwtService.createJwt(userIdx); // 그 아이디로 jwt 생성
//            String userName = accountRepository.findByEmail(email).get().getName(); // 가입한 회원의 이름 반환
//            return new PostAccountRes(userName); // 요청 결과 반환
//            //return new PostAccountRes(30, "tmp_jwt");
//        } catch (DataIntegrityViolationException e){ // 중복 이메일 게정 체크
//            throw new DuplicatedAccountException();
//        }
//        catch(Exception exception){
//            throw new BusinessException(ErrorCode.DATABASE_ERROR);
//        }
//    }

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


    // db에 email존재 여부 확인
    public boolean checkEmail(String email) throws BusinessException {
        try{
            return accountRepository.findByEmail(email).isPresent();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

}
