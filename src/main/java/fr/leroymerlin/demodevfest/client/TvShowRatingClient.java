package fr.leroymerlin.demodevfest.client;

import fr.leroymerlin.demodevfest.model.TvShowIds;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class TvShowRatingClient {
	private static String RATINGS_PATH = "/tvShowRatingByIds";
	private static String RATING_PATH = "/tvShowRating/{id}";

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

	public Flux<TvShowRating> findTVShowRatingByIds(List<String> tvShowIds) {
		return webClient.get()
						.uri(builder -> builder.path(RATINGS_PATH)
											   .queryParam("ids", tvShowIds.toArray())
											   .build())
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.retrieve()
						.bodyToFlux(TvShowRating.class)
						.retryWhen(manageRetry())
						.doOnError(throwable -> log.error("Error when call Rating api", throwable));
	}

	public Mono<TvShowRating> findTVShowRatingById(String id) {
		return webClient.get()
						.uri(RATING_PATH, id)
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.retrieve()
						.bodyToMono(TvShowRating.class)
						.retryWhen(manageRetry())
						.doOnError(throwable -> log.error("Error when call Rating api", throwable));
	}

	private Retry<Object> manageRetry() {
		return Retry.onlyIf(exception -> !(exception instanceof WebClientResponseException))
					.fixedBackoff(Duration.ofMillis(retryFirstBackOff))
					.retryMax(retryMax)
					.doOnRetry(objectRetryContext -> log.info("Error occurred, retrying (attempts {}).", objectRetryContext.iteration()));
	}
}
