package fr.leroymerlin.demodevfest.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public abstract class WebClientConfig {
	/**
	 * The base url for request the remote service.
	 */
	protected String baseUrl;

	/**
	 * The optional api key needed to authenticate to the remote service.
	 */
	protected String apiKey;

	/**
	 * The max connection timeout (default 1500ms).
	 */
	protected Duration connectTimeout = Duration.ofMillis(1500);

	/**
	 * The max read timeout (default 5000ms).
	 * Should be set at the max read timeout across all types of requests.
	 */
	protected Duration readTimeout = Duration.ofMillis(5000);

	/**
	 * The max write timeout (default 5000ms).
	 * Should be set at the max write timeout across all types of requests.
	 */
	protected Duration writeTimeout = Duration.ofMillis(5000);

	/**
	 * Either socket keep alive should be active or not (default : true).
	 */
	protected boolean keepAlive = true;

	/**
	 * Either tcp no delay should be active or not (default : true).
	 */
	protected boolean tcpNoDelay = true;

	public WebClient createWebClient(WebClient.Builder builder) {

		HttpClient httpClient =
			HttpClient.create()
					  .secure()
					  .tcpConfiguration(tcpClient -> tcpClient
														 .doOnConnected(connection -> connection.addHandlerLast(
															 new ReadTimeoutHandler((int) this.readTimeout.toMillis(),
																 TimeUnit.MILLISECONDS)))
														 .doOnConnected(connection -> connection.addHandlerLast(
															 new WriteTimeoutHandler((int) this.writeTimeout.toMillis(),
																 TimeUnit.MILLISECONDS)))
														 .option(ChannelOption.SO_KEEPALIVE, this.keepAlive)
														 .option(ChannelOption.TCP_NODELAY, this.tcpNoDelay)
														 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
															 (int) this.connectTimeout.toMillis()));

		return builder.clientConnector(new ReactorClientHttpConnector(httpClient))
					  .baseUrl(this.baseUrl)
					  .build();
	}
}
