package fit.fitspring.controller;

import fit.fitspring.controller.dto.firebase.FcmTokenDto;
import fit.fitspring.controller.dto.notification.NotificationSendDto;
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

    @Operation(summary = "토큰 저장", description = "해당 유저의 토큰을 저장")
    @PostMapping()
    public ResponseEntity storeToken(@RequestBody FcmTokenDto tokenDto){

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "토큰 업데이트", description = "해당 유저의 토큰을 갱신")
    @PutMapping()
    public ResponseEntity updateToken(@RequestBody FcmTokenDto tokenDto){

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "토큰 삭제", description = "해당 토큰을 삭제")
    @DeleteMapping("/{fcmToken}")
    public ResponseEntity deleteTokenByTokenId(@Parameter(description = "토큰 값") @PathVariable String fcmToken){

        return ResponseEntity.ok().build();
    }
    @Operation(summary = "토큰 삭제", description = "해당 유저의 토큰을 삭제")
    @DeleteMapping("/users/{email}}")
    public ResponseEntity deleteTokenByUser(@Parameter(description = "이메일") @PathVariable String email){

        return ResponseEntity.ok().build();
    }
}
