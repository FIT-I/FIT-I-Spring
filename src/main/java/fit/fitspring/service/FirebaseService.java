package fit.fitspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import fit.fitspring.controller.dto.firebase.FcmMessage;
import fit.fitspring.controller.dto.firebase.FcmTokenDto;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.domain.firebase.FCMToken;
import fit.fitspring.domain.firebase.FirebaseRepository;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.response.BaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FirebaseService {
    @Value("${fcm.token.name}")
    private String firebasePath;
    private String accessToken;

    private final AccountService accountService;
    private final WebClient firebaseWebClient;
    private final FirebaseRepository firebaseRepository;
    private final AccountRepository accountRepository;

    @PostConstruct
    public void init(){
        try{
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebasePath).getInputStream())).build();

            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
            }

            accessToken = getAccessToken();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void storeToken(String email, String token) {
        Account account = accountService.getByEmail(email);
        if (account.getFcmToken().getToken().equals(token)){
            throw new BusinessException(ErrorCode.ALREADY_HAS_FCM_TOKEN);
        }
        FCMToken tokenEntity = FCMToken.builder()
                .account(account)
                .token(token)
                .build();
        try {
            firebaseRepository.save(tokenEntity);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SOMEONE_HAS_FCM_TOKEN);
        }
    }

    public void deleteTokenByEmail(String email) {
        firebaseRepository.findByAccount(accountService.getByEmail(email))
                .ifPresent(firebaseRepository::delete);
    }

    /**
     * Desc: 사전에 설정된 firebaseWebClient 를 이용, 해당 토큰을 가진 디바이스에 알림을 보내는 함수
     * */
    public void sendMessage(FcmMessage message) throws IOException {
        try {
            sendMessage(message, accessToken);
        } catch (Exception e) {
            this.accessToken = getAccessToken();
            sendMessage(message, accessToken);
        }
    }

    /* 이메일을 통해 토큰을 찾고 해당 토큰으로 알림을 전송 */
    public void sendMessage(String email, String title, String body) throws Exception {
        String token = findByEmail(email).getToken();
        sendMessage(makeMessage(token, title, body));
    }

    public void sendMessage(FcmMessage message, String accessToken) throws IOException {
        firebaseWebClient.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(Mono.just(message), FcmMessage.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

    }

    private FCMToken findByEmail(String email){
        Account account = accountService.getByEmail(email);
        return firebaseRepository.findByAccount(account).orElseThrow(EntityNotFoundException::new);
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
    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebasePath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();

    }
}
