package fr.leroymerlin.demodevfest.controllers;

import fr.leroymerlin.demodevfest.model.TvShow;
import fr.leroymerlin.demodevfest.model.TvShowWithRating;
import fr.leroymerlin.demodevfest.service.TvShowService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@AllArgsConstructor
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

	@GetMapping(value = "/tvShowWithRating", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<TvShowWithRating> getTvShowWithRating() {
		return tvShowService.getTvShowsWithRating();
	}

	@GetMapping(value = "/tvShowWithRatingByIds", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Flux<TvShowWithRating> getTvShowWithRatingById(@RequestParam("ids")List<String> ids) {
		return tvShowService.getTvShowsWithRatingByIds(ids);
	}
}
