package iuh.fit.se.iwork4se.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@EnableJpaAuditing // Bật JPA Auditing để @CreatedDate hoạt động
public class AppConfig {}
