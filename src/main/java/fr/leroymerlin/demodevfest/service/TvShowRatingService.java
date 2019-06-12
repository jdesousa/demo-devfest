package fr.leroymerlin.demodevfest.service;

import fr.leroymerlin.demodevfest.model.TvShowRating;
import fr.leroymerlin.demodevfest.repository.TvShowRatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class TvShowRatingService {

	private TvShowRatingRepository tvShowRatingRepository;
	private CsvFileReader csvFileReader;

	public Flux<TvShowRating> saveAll() {
		return csvFileReader.readTvShowRating()
							.buffer(100)
							.flatMap(tvShows -> tvShowRatingRepository.saveAll(tvShows), 50, 100);
	}

	public Flux<TvShowRating> getAll() {
		return tvShowRatingRepository.findAll();
	}

	public Flux<TvShowRating> findByIds(List<String> tvShowIds) {
		return tvShowRatingRepository.findByTvShowIdIn(tvShowIds);
	}

	public Mono<TvShowRating> findById(String id) {
		return tvShowRatingRepository.findByTvShowId(id);
	}
}
