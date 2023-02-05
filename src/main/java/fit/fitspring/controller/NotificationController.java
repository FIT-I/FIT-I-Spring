package fit.fitspring.controller;

import fit.fitspring.controller.dto.firebase.FcmMessage;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.FirebaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@Tag(name = "알림 API")
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final FirebaseService firebaseService;

    @Operation(summary = "알림 전송", description = "해당 토큰을 가진 사람에게 알림을 전송한다. title, body 가 내용")
    @PostMapping("")
    public BaseResponse<String> sendNotification(@RequestBody FcmMessage fcmMessage) throws IOException {
        firebaseService.sendMessage(fcmMessage);
        return new BaseResponse<>("전송 완료");
    }

    @Operation(summary = "알림 전송", description = "해당 유저에게 전송한다. By Email")
    @PostMapping("/users/{email}")
    public BaseResponse<String> sendNotificationByEmail(@Parameter(description = "이메일") @RequestParam String email,
                                   @Parameter(description = "알림 제목") @RequestBody String title,
                                   @Parameter(description = "알림 내용") @RequestBody String body) throws Exception {
        firebaseService.sendMessage(email, title, body);
        return new BaseResponse<>("전송 완료");
    }

    @Operation(summary = "알림 전송", description = "해당 유저에게 전송한다. By Id")
    @PostMapping("/users/{email}")
    public ResponseEntity sendNotificationById(@Parameter(description = "Id") @RequestParam Long id,
                                                  @Parameter(description = "알림 제목") @RequestBody String title,
                                                  @Parameter(description = "알림 내용") @RequestBody String body) throws Exception {
        firebaseService.sendMessage(id, title, body);
        return ResponseEntity.ok().build();
    }
}
