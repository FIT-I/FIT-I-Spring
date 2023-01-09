package fit.fitspring.controller;

import fit.fitspring.controller.dto.account.AccountForRegisterDto;
import fit.fitspring.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /* 테스트 용 */
    @GetMapping
    public String test(){
        return "1";
    }

    /**
     * 회원 가입
     * @Param : AccountForRegisterDto
     * @Response : 200(성공) or 400
     * */
    @PostMapping
    public ResponseEntity registerUser(@RequestBody AccountForRegisterDto accountDto){
        accountService.registerAccount(accountDto);
        return ResponseEntity.ok().build();
    }
}
