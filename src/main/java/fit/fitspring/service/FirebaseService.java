package fit.fitspring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fit.fitspring.controller.dto.firebase.FcmMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FirebaseService {
    private final ObjectMapper objectMapper;
    private final WebClient firebaseWebClient;

    public String sendMessage(FcmMessage message){
        //String msg = objectMapper.writeValueAsString(message);
        return firebaseWebClient.post()
                .body(Mono.just(message),FcmMessage.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
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
        return objectMapper.writeValueAsString(fcmMessage);
    }

}
