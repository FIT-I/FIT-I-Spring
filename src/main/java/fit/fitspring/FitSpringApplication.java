package fit.fitspring;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.ForwardedHeaderFilter;

@EnableJpaAuditing
@SpringBootApplication
@OpenAPIDefinition(servers = {
        @Server(url = "${custom.https.server}", description = "Security Verification server url"),
        @Server(url = "${custom.http.server}", description = "Generated server url"),
        @Server(url = "${custom.localhost.server}"+"${server.port}", description = "Generated server url"),
})
public class FitSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitSpringApplication.class, args);
    }

}
