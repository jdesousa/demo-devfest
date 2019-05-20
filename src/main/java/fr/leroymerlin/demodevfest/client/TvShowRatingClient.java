package fr.leroymerlin.demodevfest.client;

import fr.leroymerlin.demodevfest.model.TvShowIds;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class TvShowRatingClient {
	private static String RATING_PATH = "/tvShowRatingByIds";
	private WebClient webClient;

	public Flux<TvShowRating> findTvshowRatingByIds(TvShowIds tvShowIds) {
		StopWatch s = new StopWatch();

		return webClient.post()
						.uri(RATING_PATH)
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.body(BodyInserters.fromObject(tvShowIds))
						.retrieve()
						.bodyToFlux(TvShowRating.class)
						.doOnSubscribe(subscription -> s.start())
						.doOnTerminate(() -> {
							s.stop();
							log.info("load rating in {}", s.getTime(TimeUnit.MILLISECONDS));
						})
						.doOnError(throwable -> {
							s.stop();
							log.error("Error when call Rating api in {}", s.getTime(TimeUnit.MILLISECONDS), throwable);
						});
	}

}
