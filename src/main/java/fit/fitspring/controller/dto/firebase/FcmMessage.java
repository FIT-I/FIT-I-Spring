package fit.fitspring.controller.dto.firebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessage {


    private boolean validate_only;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
    }

    /**
     * FCM 표준 타이틀 // https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send
     *
     * */
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
