package fr.leroymerlin.demodevfest.controllers;

import fr.leroymerlin.demodevfest.model.TvShow;
import fr.leroymerlin.demodevfest.service.TvShowService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@AllArgsConstructor
@Slf4j
public class TvShowController {

	private TvShowService tvShowService;

	@GetMapping("ping")
	public String ping() {
		return "pong";
	}

	@PutMapping(value = "/tvShow", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<TvShow> saveAll() {
		return tvShowService.saveAll();
	}

	@GetMapping(value = "/tvShows", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<TvShow> getTvShows(@RequestParam(value = "ids", required = false) List<String> ids) {
		AtomicInteger count = new AtomicInteger(0);

		if (!CollectionUtils.isEmpty(ids)) {
			return tvShowService.findByIds(ids);
		} else {
			return tvShowService.findAll()
								.doOnNext(tvShow -> {
									if (count.incrementAndGet() % 1000 == 0) {
										log.info("Count {}", count.get());
									}
								})
								.doFinally(s -> log.info("Count {}", count.get()));

		}
	}

	@GetMapping(value = "/tvShows/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Mono<TvShow> getTvShowById(@PathVariable String id) {
		return tvShowService.findById(id)
							.doOnNext(tvShow -> log.info("get {}", tvShow))
							.doOnError(throwable -> log.error("Error"))
							.doOnTerminate(() -> log.info("Terminate maybe with errors"))
							.doOnSuccess(tvShow -> log.info("Terminate without errors"))
							.doOnCancel(() -> log.info("Cancelled."))
							.doOnSubscribe(subscription -> log.info("Subscription"));

	}

}
