package fit.fitspring.controller;

import fit.fitspring.controller.dto.account.AccountForRegisterDto;
import fit.fitspring.controller.dto.account.RegisterDto;
import fit.fitspring.controller.mdoel.account.PostAccountRes;
import fit.fitspring.controller.mdoel.account.PostLoginRes;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.controller.mdoel.account.*;
import static fit.fitspring.utils.ValidationRegex.isRegexEmail;

import fit.fitspring.provider.AccountProvider;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@RestController
@Tag(name = "계정 API")
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private final AccountService accountService;
    @Autowired
    private final AccountProvider accountProvider;

    /* 테스트 용 */
    @Operation(summary = "테스트", description = "테스트")
    @GetMapping("/test")
    public String test(){
        return "10";
    }

    /**
     * 회원 가입
     * @Param : AccountForRegisterDto
     * @Response : 200(성공) or 400
     * */
//    @PostMapping
//    public ResponseEntity registerUser(@RequestBody AccountForRegisterDto accountDto){
//        accountService.registerAccount(accountDto);
//        return ResponseEntity.ok().build();
//    }

    @Operation(summary = "회원가입", description = "회원가입(Request)")
    @PostMapping
    public BaseResponse<PostAccountRes> registerUser(@RequestBody RegisterDto registerDto){

        // 이메일, 비밀번호, 이름이 입력 되지 않았을 경우 에러 코드 리턴
        if (registerDto.getEmail() == null) {
            return new BaseResponse<>(ErrorCode.POST_ACCOUNTS_EMPTY_EMAIL);
        }

        // 이름이 입력되지 않았을 경우 에러 리턴
        if (registerDto.getName() ==null) {
            return new BaseResponse<>(ErrorCode.POST_ACCOUNTS_EMPTY_NAME);
        }

        // 이메일 형식이 맞지 않은 경우 에러 코드 리턴
        if (!isRegexEmail(registerDto.getEmail())){
            return new BaseResponse<>(ErrorCode.POST_ACCOUNTS_INVALID_EMAIL);
        }

        // DB에 저장
        try {
            AccountForRegisterDto accountForRegisterDto = new AccountForRegisterDto(registerDto.getName(), registerDto.getEmail(), registerDto.getPassword(), registerDto.getAccountType());
            PostAccountRes postAccountRes = accountService.registerAccount(accountForRegisterDto);
            return new BaseResponse<>(postAccountRes);
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }

        //return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인(미완)", description = "로그인(Request)")
    @PostMapping("/{email}/{password}")
    public BaseResponse<PostLoginRes> userLogin(@Parameter(description = "이메일")@PathVariable String email,
                                    @Parameter(description = "비밀번호")@PathVariable String password){
        // validation 넣어야 함
        try{
            PostLoginRes postLoginRes = accountProvider.logIn(email, password);
            return new BaseResponse<>(postLoginRes);
        } catch (BusinessException e){
            return new BaseResponse<>(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        //return ResponseEntity.ok().build();
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
}
