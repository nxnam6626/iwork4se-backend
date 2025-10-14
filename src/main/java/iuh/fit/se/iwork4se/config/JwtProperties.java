package iuh.fit.se.iwork4se.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String alg = "HS256";
    private String secret;
    private String issuer = "iwork4se";
    private long accessExpSeconds = 900;
    private long refreshExpSeconds = 2_592_000;
    private long clockSkewSeconds = 30;
}
