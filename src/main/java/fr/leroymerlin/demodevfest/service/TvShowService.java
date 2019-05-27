package fr.leroymerlin.demodevfest.service;

import fr.leroymerlin.demodevfest.client.TvShowRatingClient;
import fr.leroymerlin.demodevfest.model.TvShow;
import fr.leroymerlin.demodevfest.model.TvShowIds;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import fr.leroymerlin.demodevfest.repository.TvShowRepository;
import fr.leroymerlin.demodevfest.utils.CSVLineSplitOperator;
import fr.leroymerlin.demodevfest.utils.CsvHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.function.Function.identity;

@Service
@AllArgsConstructor
@Slf4j
public class TvShowService {
	private TvShowRepository tvShowRepository;
	private TvShowRatingService tvShowRatingService;
	private TvShowRatingClient tvShowRatingClient;

	private static TvShow tvShow = TvShow.builder()
										 .id("tt0040031")
										 .title("Game of thrones")
										 .build();

	public Mono<TvShow> findById(String id) {
		return tvShowRepository.findById(id)
							   .flatMap(tvShow -> tvShowRatingClient.findTvshowRatingByIds(TvShowIds.builder()
																									.ids(asList(id))
																									.build())
																	.collectList()
																	.map(tvShowRating -> addRatingInformation(tvShow,
																		CollectionUtils.isEmpty(tvShowRating) ? null : tvShowRating.get(0))));
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

	public Flux<TvShow> getTvShowsWithRating() {
		return tvShowRepository.findAll()
							   .buffer(100)
							   .flatMap(tvShows -> tvShowRatingClient.findTvshowRatingByIds(extractIdsFromTvShows(tvShows))
																	 .collectList()
																	 .flatMapMany(tvShowRatings -> createTvShowWithRating(tvShows, tvShowRatings)));
	}

	public Flux<TvShow> getTvShowsWithRatingByIds(List<String> ids) {
		return tvShowRepository.findAllById(ids)
							   .collectList()
							   .flatMapMany(tvShows -> tvShowRatingClient.findTvshowRatingByIds(extractIdsFromTvShows(tvShows))
																		 .collectList()
																		 .flatMapMany(tvShowRating -> createTvShowWithRating(tvShows,
																			 tvShowRating)));
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

	public static void main(String[] args) {

		Mono.just(tvShow)
			.map(TvShow::getTitle);
	}
}
