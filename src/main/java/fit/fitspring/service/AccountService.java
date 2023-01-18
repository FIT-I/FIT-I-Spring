package fit.fitspring.service;

import fit.fitspring.controller.dto.account.AccountForRegisterDto;
import fit.fitspring.controller.mdoel.account.PostAccountRes;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.exception.account.DuplicatedAccountException;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final AccountRepository accountRepository;
    public PostAccountRes registerAccount(AccountForRegisterDto accountDto) {
        Account account = accountDto.toEntity();
        try {
            String createResult = accountDto.getEmail();
            accountRepository.save(account);
            return new PostAccountRes(createResult);
        } catch (DataIntegrityViolationException e){
            // 중복 이메일 게정 체크
            throw new DuplicatedAccountException();
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
}
