package fr.leroymerlin.demodevfest.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Slf4j
@Configuration
public class LoggingConfig {

	/**
	 * Filter to log an error if application does not handle the exception.
	 *
	 * @return a {@link WebFilter}.
	 */
	@Bean
	WebFilter loggingFilter() {
		return (exchange, chain) -> chain.filter(exchange)
										 .doOnError(e -> log.error("An unexpected error occurred", e));
	}
}