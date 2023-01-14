package fit.fitspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fit.fitspring.controller.dto.firebase.FcmMessage;
import fit.fitspring.domain.firebase.FCMToken;
import fit.fitspring.domain.firebase.FirebaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FirebaseService {
    private final ObjectMapper objectMapper;
    private final WebClient firebaseWebClient;
    private final FirebaseRepository firebaseRepository;

    /**
     * Desc: 사전에 설정된 firebaseWebClient 를 이용, 해당 토큰을 가진 디바이스에 알림을 보내는 함수
     * */
    public void sendMessage(FcmMessage message){
        //String msg = objectMapper.writeValueAsString(message);
        firebaseWebClient.post()
                .body(Mono.just(message), FcmMessage.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    /* 이메일을 통해 토큰을 찾고 해당 토큰으로 알림을 전송 */
    public void sendMessage(String email, String title, String body){
        String token = firebaseRepository.findByUserEmail(email).orElse(getNewToken(email)).getToken();
        sendMessage(makeMessage(token, title, body));
    }

    /**
     * TODO : 해당 유저가 모종의 이유로 토큰을 발급받지 못한 경우 토큰을 발급받고 저장한다.
     * */
    private FCMToken getNewToken(String email) {
        // TODO : firebase에서 토큰 발급
        String token = getNewTokenFromFirebase(email);
        FCMToken newTokenSet = FCMToken.builder()
                .user_email(email)
                .token(token)
                .build();
        return firebaseRepository.save(newTokenSet);
    }
    private String getNewTokenFromFirebase(String email){
        // TODO
        return "new token";
    }

    private FcmMessage makeMessage(String targetToken, String title, String body){
        return FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )
                        .build()
                )
                .validate_only(false)
                .build();
    }

}
