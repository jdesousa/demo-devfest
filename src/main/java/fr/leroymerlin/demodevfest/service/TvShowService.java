package fr.leroymerlin.demodevfest.service;

import fr.leroymerlin.demodevfest.model.TvShow;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import fr.leroymerlin.demodevfest.model.TvShowWithRating;
import fr.leroymerlin.demodevfest.repository.TvShowRepository;
import fr.leroymerlin.demodevfest.utils.CSVLineSplitOperator;
import fr.leroymerlin.demodevfest.utils.CsvHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.function.Function.identity;

@Service
@AllArgsConstructor
@Slf4j
public class TvShowService {
	private TvShowRepository tvShowRepository;
	private TvShowRatingService tvShowRatingService;

	public Mono<TvShow> findById(String id) {
		return tvShowRepository.findById(id);
	}

	public Flux<TvShow> findAll() {
		return tvShowRepository.findAll();
	}

	public Flux<TvShow> saveAll() {
		return Flux.using(() -> StreamSupport.stream(new CSVLineSplitOperator(CsvHelper.getReader("series.tsv")), false), Flux::fromStream,
			BaseStream::close)
				   .map(this::createTvShow)
				   .buffer(100)
				   .flatMap(tvShows -> tvShowRepository.saveAll(tvShows), 1, 100);
	}

	private TvShow createTvShow(String[] line) {
		return TvShow.builder()
					 .id(line[0])
					 .title(line[3])
					 .build();
	}

	public Flux<TvShowWithRating> getTvShowsWithRating() {
		return tvShowRepository.findAll()
							   .limitRate(1000)
							   .buffer(500)
							   .doOnEach(listSignal -> log.info("buffer 100 tv shows"))
							   .flatMap(tvShows -> tvShowRatingService.findByIds(extractIdsFromTvShows(tvShows))
																	  .collectList()
																	  .flatMapMany(tvShowRatings -> createTvShowWithRating(tvShows, tvShowRatings)));
	}

	private Flux<TvShowWithRating> createTvShowWithRating(List<TvShow> tvShows, List<TvShowRating> tvShowRatings) {
		Map<String, TvShowRating> tvshowRatingsByTvShowId = tvShowRatings.stream()
																		 .collect(Collectors.toMap(TvShowRating::getTvShowId, identity()));

		return Flux.fromIterable(tvShows)
				   .flatMap(tvShow -> Mono.just(TvShowWithRating.builder()
																.tvShow(tvShow)
																.tvShowRating(tvshowRatingsByTvShowId.get(tvShow.getId()))
																.build()));

	}

	private List<String> extractIdsFromTvShows(List<TvShow> tvShows) {
		return tvShows.stream()
					  .map(TvShow::getId)
					  .collect(Collectors.toList());
	}
}
