package fr.leroymerlin.demodevfest.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "tv-show-rating-api")
public class WebClientTvShowRatingConfig extends WebClientConfig {

	@Bean("apiTvShowRating")
	protected WebClient apiTvShowRating(WebClient.Builder builder) {
		return super.createWebClient(builder);
	}
}
