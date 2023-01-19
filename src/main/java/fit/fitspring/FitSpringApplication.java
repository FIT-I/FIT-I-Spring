package fit.fitspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FitSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitSpringApplication.class, args);
    }

}
