package fit.fitspring.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Configuration
public class OpenApiConfig {
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .useDefaultResponseMessages(false)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("fit.fitspring.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//}

    private static final String API_NAME = "Fit-I API";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "Fit-I API 명세서";

    @Value("${fcm.token.name}")
    private String firebasePath;

    @Value("${fcm.api.url}")
    private String FIREBASE_URL;


    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version(API_VERSION)
                .title(API_NAME)
                .description(API_DESCRIPTION);

        return new OpenAPI()
                .info(info);
    }

    @Bean
    public WebClient firebaseWebClient() throws IOException {
        return WebClient.builder()
                .baseUrl(FIREBASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();
    }
    @PostConstruct
    public void init(){
        try{
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebasePath).getInputStream())).build();

            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebasePath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}