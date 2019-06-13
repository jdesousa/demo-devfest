package fr.leroymerlin.demodevfest.service;

import fr.leroymerlin.demodevfest.client.TvShowRatingClient;
import fr.leroymerlin.demodevfest.model.TvShow;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import fr.leroymerlin.demodevfest.repository.TvShowRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TvShowService {
	private TvShowRepository tvShowRepository;
	private TvShowRatingClient tvShowRatingClient;
	private CsvFileReader csvFileReader;

	public Mono<TvShow> findById(String id) {
		return tvShowRepository.findById(id)
							   .flatMap(tvShow -> tvShowRatingClient.findTVShowRatingById(id)
																	.map(tvShowRating -> createTVShowWithRating(tvShow, tvShowRating))
																	.defaultIfEmpty(tvShow));
	}

	public Flux<TvShow> findAll() {
		return tvShowRepository.findAll()
							   .buffer(100)
							   .flatMap(this::fetchRating);
	}

	public Flux<TvShow> findByIds(List<String> ids) {
		return tvShowRepository
				   .findAllById(ids)
				   .collectList()
				   .flatMapMany(this::fetchRating);
	}

	public Flux<TvShow> saveAll() {
		return csvFileReader.readTvShow()
							.buffer(100)
							.flatMapSequential(tvShows -> tvShowRepository.saveAll(tvShows), 5, 100);
	}

	private Flux<TvShow> fetchRating(List<TvShow> tvShows) {
		return tvShowRatingClient.findTVShowRatingByIds(extractIdsFromTvShows(tvShows))
								 .groupBy(TvShowRating::getTvShowId)
								 .flatMap(ratingByTvShow -> addTvShowRating(tvShows, ratingByTvShow));
	}

	private TvShow createTVShowWithRating(TvShow tvShow, TvShowRating tvShowRating) {
		if (tvShowRating != null) {
			return tvShow.setAverageRating(tvShowRating.getAverageRating())
						 .setNumVotes(tvShowRating.getNumVotes());
		}
		return tvShow;
	}

	private Flux<TvShow> addTvShowRating(List<TvShow> tvShows, GroupedFlux<String, TvShowRating> ratingByTvShow) {
		Map<String, TvShow> tvShowById = tvShows.stream()
												.collect(Collectors.toMap(TvShow::getId, tvShow -> tvShow));

		return ratingByTvShow.map(tvShowRating -> createTVShowWithRating(tvShowById.get(ratingByTvShow.key()), tvShowRating));
	}

	private List<String> extractIdsFromTvShows(List<TvShow> tvShows) {
		return tvShows.stream()
					  .map(TvShow::getId)
					  .collect(Collectors.toList());

	}

	public Mono<TvShow> findByIdParallel(String id) {
		return Mono.zip(tvShowRepository.findById(id), tvShowRatingClient.findTVShowRatingById(id)
																		 .defaultIfEmpty(TvShowRating.NO_TV_SHOW_RATING))
				   .map(tvShowAndRating -> createTVShowWithRating(tvShowAndRating.getT1(), tvShowAndRating.getT2()));
	}
}
