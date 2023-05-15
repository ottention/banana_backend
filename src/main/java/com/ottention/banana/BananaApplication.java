package com.ottention.banana;

import com.ottention.banana.config.GoogleConfig;
import com.ottention.banana.config.JwtConfig;
import com.ottention.banana.config.KakaoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties({JwtConfig.class, KakaoConfig.class})
public class BananaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BananaApplication.class, args);
	}

}
