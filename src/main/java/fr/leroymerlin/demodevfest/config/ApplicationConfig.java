package fr.leroymerlin.demodevfest.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration used to bootstrap the application.
 */
@Getter
@Configuration
public class ApplicationConfig {

	/**
	 * Http server port. Default 8080.
	 */
	@Value("${spring.application.name}")
	private String applicationName;

	/**
	 * Http server port. Default 8080.
	 */
	@Value("${server.port:8080}")
	private int serverPort;

	/**
	 * Server pool min threads. Default 10.
	 */
	@Value("${server.pool.min-threads:10}")
	private int serverMinThreads;

	/**
	 * Server pool max threads. Default 10.
	 */
	@Value("${server.pool.max-threads:10}")
	private int serverMaxThreads;

	/**
	 * Server thread idle timeout. Default 600000 (10 minutes).
	 */
	@Value("${server.pool.idle-timeout:600000}")
	private int serverIdleTimeout;

	/**
	 * Server queue capacity. Default 10.
	 */
	@Value("${server.queue.capacity:10}")
	private int serverQueueCapacity;

	/**
	 * Server queue capacity. Default 10.
	 */
	@Value("${server.queue.grow-by:10}")
	private int serverQueueGrowBy;

	/**
	 * WebClient template request timeout
	 */
	@Value("${httpclient.request-timeout:5000}")
	private int httpClientRequestTimeout;

	@Value("${httpclient.connect-timeout:500}")
	private int httpClientConnectTimeout;

	@Value("${httpclient.pool.max-size:10000}")
	private int httpClientMaxPoolSize;

}
