package fr.leroymerlin.demodevfest.service;

import fr.leroymerlin.demodevfest.client.TvShowRatingClient;
import fr.leroymerlin.demodevfest.model.TvShow;
import fr.leroymerlin.demodevfest.model.TvShowIds;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import fr.leroymerlin.demodevfest.repository.TvShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TvShowServiceTest {

	private TvShowService cut;

	@Mock
	private TvShowRepository tvShowRepository;
	@Mock
	private TvShowRatingClient tvShowRatingClient;
	@Mock
	private CsvFileReader csvFileReader;

	@BeforeEach
	void setUp() {
		cut = new TvShowService(tvShowRepository, tvShowRatingClient, csvFileReader);
	}

	@Test
	@DisplayName("When finding all Tv Show, then return all tv show with rating aggregated")
	void findAll() {
		TvShow tvShow1 = TvShow.builder()
							   .id("1")
							   .title("az")
							   .build();
		TvShow tvShow2 = TvShow.builder()
							   .id("2")
							   .title("qs")
							   .build();
		TvShow tvShow3 = TvShow.builder()
							   .id("3")
							   .title("fg")
							   .build();

		TvShowRating rating1 = TvShowRating.builder()
										   .tvShowId("1")
										   .numVotes(12)
										   .averageRating(34.5f)
										   .build();
		TvShowRating rating3 = TvShowRating.builder()
										   .tvShowId("3")
										   .numVotes(64)
										   .averageRating(1.5f)
										   .build();

		TvShowIds ratingParams = TvShowIds.builder()
										  .ids(asList("1", "2", "3"))
										  .build();

		when(tvShowRepository.findAll()).thenReturn(Flux.just(tvShow1, tvShow2, tvShow3));

		when(tvShowRatingClient.findTvshowRatingByIds(ratingParams)).thenReturn(Flux.just(rating1, rating3));

		Flux<TvShow> result = cut.findAll();

		StepVerifier.create(result)
					.expectNext(tvShow1.withNumVotes(rating1.getNumVotes())
									   .withAverageRating(rating1.getAverageRating()))
					.expectNext(tvShow2)
					.expectNext(tvShow3.withNumVotes(rating3.getNumVotes())
									   .withAverageRating(rating3.getAverageRating()))
					.verifyComplete();

		verify(tvShowRepository).findAll();
		verify(tvShowRatingClient).findTvshowRatingByIds(ratingParams);
	}

	@Test
	@DisplayName("When finding tv show by id, then return the tv show with rating aggregated")
	void findById() {
		String idToFind = "1";

		TvShow tvShow1 = TvShow.builder()
							   .id("1")
							   .title("az")
							   .build();

		TvShowIds ratingParams = TvShowIds.builder()
										  .ids(asList("1"))
										  .build();

		TvShowRating rating1 = TvShowRating.builder()
										   .tvShowId("1")
										   .numVotes(12)
										   .averageRating(34.5f)
										   .build();

		when(tvShowRepository.findById(idToFind)).thenReturn(Mono.just(tvShow1));

		when(tvShowRatingClient.findTvshowRatingByIds(ratingParams)).thenReturn(Flux.just(rating1));

		Mono<TvShow> result = cut.findById(idToFind);

		StepVerifier.create(result)
					.expectNext(tvShow1.withAverageRating(rating1.getAverageRating())
									   .withNumVotes(rating1.getNumVotes()))
					.verifyComplete();

		verify(tvShowRepository).findById(idToFind);
		verify(tvShowRatingClient).findTvshowRatingByIds(ratingParams);
	}

	@Test
	@DisplayName("When finding all Tv Show, then return all tv show with rating aggregated")
	void findByIds() {
		List<String> idsToFind = asList("1", "2", "3");
		TvShow tvShow1 = TvShow.builder()
							   .id("1")
							   .title("az")
							   .build();
		TvShow tvShow2 = TvShow.builder()
							   .id("2")
							   .title("qs")
							   .build();
		TvShow tvShow3 = TvShow.builder()
							   .id("3")
							   .title("fg")
							   .build();

		TvShowRating rating1 = TvShowRating.builder()
										   .tvShowId("1")
										   .numVotes(12)
										   .averageRating(34.5f)
										   .build();
		TvShowRating rating3 = TvShowRating.builder()
										   .tvShowId("3")
										   .numVotes(64)
										   .averageRating(1.5f)
										   .build();

		TvShowIds ratingParams = TvShowIds.builder()
										  .ids(idsToFind)
										  .build();

		when(tvShowRepository.findAllById(idsToFind)).thenReturn(Flux.just(tvShow1, tvShow2, tvShow3));

		when(tvShowRatingClient.findTvshowRatingByIds(ratingParams)).thenReturn(Flux.just(rating1, rating3));

		Flux<TvShow> result = cut.findByIds(idsToFind);

		StepVerifier.create(result)
					.expectNext(tvShow1.withNumVotes(rating1.getNumVotes())
									   .withAverageRating(rating1.getAverageRating()))
					.expectNext(tvShow2)
					.expectNext(tvShow3.withNumVotes(rating3.getNumVotes())
									   .withAverageRating(rating3.getAverageRating()))
					.verifyComplete();

		verify(tvShowRepository).findAllById(idsToFind);
		verify(tvShowRatingClient).findTvshowRatingByIds(ratingParams);
	}

	@Test
	@DisplayName("When saving all Tv Show, then return all tv show saved")
	void saveAll() {
		List<String> idsToFind = asList("1", "2", "3");
		TvShow tvShow1 = TvShow.builder()
							   .id("1")
							   .title("az")
							   .build();
		TvShow tvShow2 = TvShow.builder()
							   .id("2")
							   .title("qs")
							   .build();
		TvShow tvShow3 = TvShow.builder()
							   .id("3")
							   .title("fg")
							   .build();

		List<TvShow> tvShowsToSave = asList(tvShow1, tvShow2, tvShow3);

		when(csvFileReader.readTvShow()).thenReturn(Flux.fromIterable(tvShowsToSave));
		when(tvShowRepository.saveAll(tvShowsToSave)).thenReturn(Flux.fromIterable(tvShowsToSave));

		Flux<TvShow> result = cut.saveAll();

		StepVerifier.create(result)
					.expectNext(tvShow1, tvShow2, tvShow3)
					.verifyComplete();

		verify(csvFileReader).readTvShow();
		verify(tvShowRepository).saveAll(tvShowsToSave);
	}
}