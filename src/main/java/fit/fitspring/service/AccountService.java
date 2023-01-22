package fit.fitspring.service;

import fit.fitspring.config.Secret;
import fit.fitspring.controller.dto.account.AccountForRegisterDto;
import fit.fitspring.controller.mdoel.account.PostAccountRes;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.exception.account.DuplicatedAccountException;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.utils.JwtService;
import fit.fitspring.utils.AES128;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private final JwtService jwtService;


    private final AccountRepository accountRepository;
    public PostAccountRes registerAccount(AccountForRegisterDto accountDto) throws BusinessException {
        String pwd;
        String email = accountDto.getEmail();

        // 비번 암호화 + 이메일 중복 확인
        try {
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(accountDto.getPassword()); // 비번 암호화
            accountDto.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BusinessException(ErrorCode.DUPLICATE_ACCOUNT);
        }

        Account account = accountDto.toEntity();

        // 회원가입 정보 저장, jwt 생성, 결과 반환(userIdx, jwt)
        try{
            accountRepository.save(account); // 일단 데이터 저장
            int userIdx = accountRepository.findByEmail(email).get().getId().intValue(); // 데이터 저장하면서 자동 생성된 id 가져오기
            String jwt = jwtService.createJwt(userIdx); // 그 아이디로 jwt 생성
            return new PostAccountRes(userIdx, jwt); // 요청 결과 반환
            //return new PostAccountRes(30, "tmp_jwt");
        } catch (DataIntegrityViolationException e){ // 중복 이메일 게정 체크
            throw new DuplicatedAccountException();
        }
        catch(Exception exception){
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
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
}
