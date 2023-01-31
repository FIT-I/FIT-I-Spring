package fit.fitspring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fit.fitspring.controller.dto.account.*;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;

import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.AccountService;
import fit.fitspring.service.CommunalService;
import fit.fitspring.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

import static fit.fitspring.utils.ValidationRegex.*;


@Slf4j
@RestController
@Tag(name = "계정 API")
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final CommunalService communalService;



    /* 테스트 용 */
    @Operation(summary = "테스트", description = "테스트")
    @GetMapping("/test")
    public String test(){
        return "success";
    }

    /**
     * 회원 가입
     * @Param : AccountForRegisterDto
     * @Response : 200(성공) or 400
     * */
    @Operation(summary = "고객 회원가입", description = "고객 회원가입(Request/Response)")
    @PostMapping("/customer")
    public BaseResponse<String> registerCustomer(@RequestBody RegisterCustomerDto registerDto){
        // DB에 저장
        try {
            return new BaseResponse<>(accountService.registerCustomer(registerDto));
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "트레이너 회원가입", description = "트레이너 회원가입(Request/Response)")
    @PostMapping("/trainer")
    public BaseResponse<String> registerTrainer(@RequestBody RegisterTrainerDto registerDto){
        // DB에 저장
        try {
            return new BaseResponse<>(accountService.registerTrainer(registerDto));
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "로그인", description = "로그인(Request) accessToken, requestToken 발급")
    @PostMapping("/login")
    public BaseResponse<TokenDto> userLogin(@RequestBody LoginReqDto loginDto) {
        //이메일 형식이 올바른가?
        if (isRegexEmail(loginDto.getEmail()) == false) {
            return new BaseResponse<>(ErrorCode.POST_ACCOUNTS_INVALID_EMAIL);
        }

        // 이메일이 DB에 존재하는가
        if (accountService.checkEmail(loginDto.getEmail()) == false) {
            return new BaseResponse<>(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        try {
            TokenDto postLoginRes = accountService.login(loginDto.getEmail(), loginDto.getPassword());
            return new BaseResponse<>(postLoginRes);
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        } catch (JsonProcessingException e){
           return new BaseResponse<>(ErrorCode.WRONG_JWT);
        }
    }

    @Operation(summary = "토큰 재발급", description = "accessToken이 만료되었을 때 refresh 토큰의 만료 시간이 유효하다면 토큰(accessToken, refreshToken) 재발급.")
    @PostMapping("/reissue")
    public BaseResponse<TokenDto> reissue(@RequestBody TokenDto reqTokenDto){
        try {
            TokenDto tokenDto = accountService.reissue(reqTokenDto.getAccessToken(), reqTokenDto.getRefreshToken());
            return new BaseResponse<>(tokenDto);
        } catch (BusinessException | JsonProcessingException e){
            return new BaseResponse<>(ErrorCode.ISSUE_JWT);
        }
    }

    @Operation(summary = "인증메일전송", description = "인증메일전송(Request/Response)")
    @GetMapping("/email/{email}")
    public BaseResponse<String> sendCertificationEmail(@Parameter(description = "이메일")@PathVariable String email){
        try{
            String number = accountService.getCertificationNumber(email);
            return new BaseResponse<>(number);
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "카카오로그인(미완)", description = "카카오로그인(Request)")
    @PostMapping("/kakao")
    public ResponseEntity userKakaoLogin(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "애플로그인(미완)", description = "애플로그인(Request)")
    @PostMapping("/apple")
    public ResponseEntity userAppleLogin(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "네이버로그인(미완)", description = "네이버로그인(Request)")
    @PostMapping("/naver")
    public ResponseEntity userNaverLogin(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이용약관수락", description = "(입력값) 각 약관에 대하여 동의 항목은 true, 비동의 항목은 false로 주시면 됩니다. (반환값) true: 필수 약관에 모두 동의, false: 필수 약관에 동의하지 않은 항목이 있음")
    @PostMapping("/terms")
    public BaseResponse<Boolean> acceptTermsOfUse(@RequestBody AcceptTermReqDto acceptTermReqDto){
        try {
            Boolean term1 = acceptTermReqDto.getTerm1();
            Boolean term2 = acceptTermReqDto.getTerm2();
            Boolean term3 = acceptTermReqDto.getTerm3();

            if (term1 == false || term1 == null){
                return new BaseResponse<>(false);
            }
            else if (term2 == false || term2 == null){
                return new BaseResponse<>(false);
            }
            else if (term3 == false || term3 == null){
                return new BaseResponse<>(false);
            }
            else{
                return new BaseResponse<>(true);
            }

            //accountService.acceptTerm(term1, term2, term3);
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "비밀번호변경", description = "비밀번호가 변경되면 필연적으로 인증객체도 변경되기 때문에 다시 로그인을 해야 합니다.")
    @PatchMapping("/password")
    public BaseResponse<String> modifyPassword(@RequestBody ModifyPassword pwdM){
        try {
            Long userIdx = SecurityUtil.getLoginUserId();
            accountService.modifyPassword(userIdx, pwdM);
            return new BaseResponse<>("비밀번호가 변경되었습니다. 다시 로그인해주세요.");
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping("/logout")
    public BaseResponse<String> userLogout(@RequestBody TokenDto reqTokenDto){
        try{
            accountService.logout(reqTokenDto.getAccessToken(), reqTokenDto.getRefreshToken());
            return new BaseResponse<>("로그아웃 되었습니다.");
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
        //return ResponseEntity.ok().build();
    }

    @Operation(summary = "계정탈퇴", description = "user 테이블의 user_state를 D로 변경한다. (D는 Delete를 의미한다.)")
    @PatchMapping("/close")
    public BaseResponse<String> userCloseAccount(){
        try{
            Long userIdx = SecurityUtil.getLoginUserId();
            String result = accountService.deleteAccount(userIdx);
            return new BaseResponse<>(result);
        } catch (BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }


    @Operation(summary = "계정 비밀번호 조회", description = "비밀번호 찾기 뷰(인증메일 확인 후) - 계정 비밀번호 조회 API(Request/Response)")
    @GetMapping("/password/{email}")
    public BaseResponse<String> registerCustomer(@Parameter(description = "유저 이메일")@PathVariable String email){
        try {
            return new BaseResponse<>(accountService.getUserPassword(email));
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }
}
