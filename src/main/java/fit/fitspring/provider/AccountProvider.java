package fit.fitspring.provider;

import fit.fitspring.config.Secret;
import fit.fitspring.controller.mdoel.account.PostLoginRes;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.utils.JwtService;
import fit.fitspring.utils.AES128;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.exception.common.BusinessException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountProvider {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AccountRepository accountRepository;

    //로그인 비밀번호 검사
    public PostLoginRes logIn(String email, String password) throws BusinessException {
        // 이메일로 비밀번호 불러오기
        String pwdEncode = accountRepository.findByEmail(email).get().getPassword();
        String pwdDecode;
        try{
            // 비번 암호 풀기
            pwdDecode = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(pwdEncode);
        } catch (Exception ignored){
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        // 비밀번호가 일치한다면 userIdx, jwt 가져오기
        if(pwdDecode.equals(password)){
            int userIdx = accountRepository.findByEmail(email).get().getId().intValue();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(jwt);
        }
        else{ // 비밀번호가 다르다면
            throw new BusinessException(ErrorCode.FAILED_TO_LOGIN);
        }
    }

    public boolean checkEmail(String email) throws BusinessException {
        try{
            return accountRepository.findByEmail(email).isPresent();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }
}
