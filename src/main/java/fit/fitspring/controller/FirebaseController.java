package fit.fitspring.controller;

import fit.fitspring.controller.dto.firebase.FcmTokenDto;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.FirebaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Firebase 관리 API")
@RequestMapping("/api/firebase")
@RequiredArgsConstructor
public class FirebaseController {
    private final FirebaseService firebaseService;

    @Operation(summary = "토큰 저장", description = "해당 유저의 토큰을 저장")
    @PostMapping()
    public BaseResponse<String> storeToken(@RequestBody FcmTokenDto dto){
        firebaseService.storeToken(dto.getEmail(), dto.getToken());
        return new BaseResponse<String>("토큰 저장 완료");
    }

    @Operation(summary = "토큰 삭제", description = "해당 유저의 토큰을 삭제")
    @DeleteMapping("/users/{email}")
    public BaseResponse<String> deleteTokenByUser(@Parameter(description = "이메일") @PathVariable String email){
        firebaseService.deleteTokenByEmail(email);
        return new BaseResponse<String>("토큰 삭제 완료");
    }
}
