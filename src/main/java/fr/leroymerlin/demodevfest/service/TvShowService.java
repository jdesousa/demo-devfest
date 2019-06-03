package fr.leroymerlin.demodevfest.service;

import fr.leroymerlin.demodevfest.client.TvShowRatingClient;
import fr.leroymerlin.demodevfest.model.TvShow;
import fr.leroymerlin.demodevfest.model.TvShowIds;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import fr.leroymerlin.demodevfest.repository.TvShowRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.function.Function.identity;

@Service
@AllArgsConstructor
@Slf4j
public class TvShowService {
	private TvShowRepository tvShowRepository;
	private TvShowRatingClient tvShowRatingClient;
	private CsvFileReader csvFileReader;

	public Mono<TvShow> findById(String id) {
		return tvShowRepository
				   .findById(id)
				   .flatMap(tvShow -> tvShowRatingClient.findTvshowRatingByIds(TvShowIds.builder()
																						.ids(asList(id))
																						.build())
														.collectList()
														.map(tvShowRating -> addRatingInformation(tvShow, tvShowRating.get(0))));
	}

	public Flux<TvShow> findAll() {
		return tvShowRepository.findAll()
							   .buffer(100)
							   .flatMap(tvShows -> tvShowRatingClient.findTvshowRatingByIds(extractIdsFromTvShows(tvShows))
																	 .collectList()
																	 .flatMapMany(tvShowRatings -> createTvShowWithRating(tvShows, tvShowRatings)));
	}

	public Flux<TvShow> saveAll() {
		return csvFileReader.readTvShow()
							.buffer(100)
							.flatMap(tvShows -> tvShowRepository.saveAll(tvShows), 5, 100);
	}

	public Flux<TvShow> findByIds(List<String> ids) {
		return tvShowRepository
				   .findAllById(ids)
				   .collectList()
				   .flatMapMany(tvShows -> tvShowRatingClient.findTvshowRatingByIds(extractIdsFromTvShows(tvShows))
															 .collectList()
															 .flatMapMany(tvShowRating -> createTvShowWithRating(tvShows, tvShowRating)));
	}

	private Flux<TvShow> createTvShowWithRating(List<TvShow> tvShows, List<TvShowRating> tvShowRatings) {
		Map<String, TvShowRating> tvshowRatingsByTvShowId = tvShowRatings.stream()
																		 .collect(Collectors.toMap(TvShowRating::getTvShowId, identity()));

		return Flux.fromIterable(tvShows)
				   .map(tvShow -> addRatingInformation(tvShow, tvshowRatingsByTvShowId.get(tvShow.getId())));
	}

	private TvShow addRatingInformation(TvShow tvShow, TvShowRating tvShowRating) {
		if (tvShowRating != null) {
			return tvShow.setAverageRating(tvShowRating.getAverageRating())
						 .setNumVotes(tvShowRating.getNumVotes());
		}
		return tvShow;
	}

	private TvShowIds extractIdsFromTvShows(List<TvShow> tvShows) {
		return TvShowIds.builder()
						.ids(tvShows.stream()
									.map(TvShow::getId)
									.collect(Collectors.toList()))
						.build();

	}
}
