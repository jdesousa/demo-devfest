package fr.leroymerlin.demodevfest.client;

import fr.leroymerlin.demodevfest.model.TvShowIds;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
public class TvShowRatingClient {
	private static String RATING_PATH = "/tvShowRatingByIds?ids={ids}";
	private WebClient webClient;

	@Value("${tv-show-rating-api.retry-max:3}")
	private Integer retryMax;
	@Value("${tv-show-rating-api.retry-first-backoff:500}")
	private Integer retryFirstBackOff;
	@Value("${tv-show-rating-api.retry-max-backoff:5000}")
	private Integer retryMaxBackOff;

	public TvShowRatingClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public Flux<TvShowRating> findTvshowRatingByIds(TvShowIds tvShowIds) {

		return webClient.get()
						.uri(RATING_PATH, String.join(",", tvShowIds.getIds()))
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.retrieve()
						.bodyToFlux(TvShowRating.class)
						.retryWhen(manageRetry())
						.doOnError(throwable -> {
							log.error("Error when call Rating api", throwable);
						});
	}

	private Retry<Object> manageRetry() {
		return Retry.any()
					.exponentialBackoffWithJitter(Duration.ofMillis(retryFirstBackOff), Duration.ofMillis(retryMaxBackOff))
					.retryMax(retryMax)
					.doOnRetry(objectRetryContext -> log.info("Error occurred, retrying (attempts {}).", objectRetryContext.iteration()));
	}
}
