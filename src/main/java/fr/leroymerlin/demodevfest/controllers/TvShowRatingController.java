package fr.leroymerlin.demodevfest.controllers;

import fr.leroymerlin.demodevfest.model.TvShowIds;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import fr.leroymerlin.demodevfest.service.TvShowRatingService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@AllArgsConstructor
public class TvShowRatingController {

	private TvShowRatingService tvShowRatingService;

	@PutMapping(value = "/tvShowRating", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<TvShowRating> saveAll() {
		return tvShowRatingService.saveAll();
	}

	@GetMapping(value = "/tvShowRating", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<TvShowRating> getAll() {
		return tvShowRatingService.getAll();
	}

	@PostMapping(value = "/tvShowRatingByIds", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Flux<TvShowRating> getByIds(@RequestBody TvShowIds tvShowIds) {
		return tvShowRatingService.findByIds(tvShowIds.getIds());
	}

	@GetMapping(value = "/tvShowRatingByIds", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Flux<TvShowRating> getByIds(@RequestParam("ids") List<String> ids) {
		return tvShowRatingService.findByIds(ids);
	}

	@GetMapping(value = "/tvShowRating/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Mono<TvShowRating> getByIds(@PathVariable("id") String id) {
		return tvShowRatingService.findById(id);
	}
}
