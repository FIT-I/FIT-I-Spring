package fit.fitspring.controller;

import fit.fitspring.controller.dto.account.*;
import fit.fitspring.controller.mdoel.account.PostAccountRes;
import fit.fitspring.controller.mdoel.account.PostLoginRes;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;

import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.AccountService;
import fit.fitspring.utils.ValidationRegex;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import static fit.fitspring.utils.ValidationRegex.*;


@Slf4j
@RestController
@Tag(name = "계정 API")
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private final AccountService accountService;
    @Autowired
    //private final AccountProvider accountProvider;

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

    @Operation(summary = "로그인", description = "로그인(Request)")
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> userLogin(@RequestBody LoginReqDto loginDto){
        //이메일 형식이 올바른가?
        if(isRegexEmail(loginDto.getEmail()) == false) {
            return new BaseResponse<>(ErrorCode.POST_ACCOUNTS_INVALID_EMAIL);
        }

        // 이메일이 DB에 존재하는가
        if(accountService.checkEmail(loginDto.getEmail()) == false) {
            return new BaseResponse<>(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        try{
            PostLoginRes postLoginRes = accountService.login(loginDto.getEmail(), loginDto.getPassword());
            return new BaseResponse<>(postLoginRes);
        } catch (BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
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

    @Operation(summary = "이용약관수락(미완)", description = "이용약관수락")
    @PostMapping("/terms")
    public ResponseEntity acceptTermsOfUse(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "비밀번호변경(미완)", description = "비밀번호변경(Request)")
    @PatchMapping("/{password}")
    public ResponseEntity modifyPassword(@Parameter(description = "비밀번호")@PathVariable String password){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그아웃(미완)", description = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity userLogout(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계정탈퇴(미완)", description = "계정탈퇴")
    @PatchMapping("/close")
    public ResponseEntity userCloseAccount(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계정상태수정(미완)", description = "계정상태수정(Request)")
    @PatchMapping("/state/{state}")
    public ResponseEntity modifyAccountState(@Parameter(description = "상태('A: Active(활성상태), D: Disabled(비활성화 상태), W: Withdrawal(탈퇴한 상태)'")@PathVariable String state){
        return ResponseEntity.ok().build();
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
