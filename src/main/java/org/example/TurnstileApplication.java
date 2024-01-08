package org.example;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(title = "Turnstile API", version = "1.0",
        license = @License(name = "Apache-2.0 license",
            url = "https://www.apache.org/licenses/LICENSE-2.0"),
        description = """
            An API for the application on manipulating travel cards transactions via
            turnstiles
        """
    ),
    servers = @Server(url = "http://localhost:8080", description = "development server")
)
@SecurityScheme(
    name = "bearer_token",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    description = "Enter the token given after successful POST /auth/authenticate",
    bearerFormat = "JWT"
)
public class TurnstileApplication {
  public static void main(String[] args) {
    SpringApplication.run(TurnstileApplication.class, args);
  }
}