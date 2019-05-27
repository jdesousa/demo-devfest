package fr.leroymerlin.demodevfest.service;

import fr.leroymerlin.demodevfest.model.TvShowRating;
import fr.leroymerlin.demodevfest.repository.TvShowRatingRepository;
import fr.leroymerlin.demodevfest.utils.CSVLineSplitOperator;
import fr.leroymerlin.demodevfest.utils.CsvHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.BaseStream;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class TvShowRatingService {

	private TvShowRatingRepository tvShowRatingRepository;

	public Flux<TvShowRating> saveAll() {
		return Flux.using(() -> StreamSupport.stream(new CSVLineSplitOperator(CsvHelper.getReader("series.ratings.tsv")), false), Flux::fromStream,
			BaseStream::close)
				   .map(line -> createTvShowRating(line))
				   .buffer(100)
				   .flatMap(tvShows -> tvShowRatingRepository.saveAll(tvShows), 50, 100);
	}

	private TvShowRating createTvShowRating(String[] line) {
		return TvShowRating.builder()
						   .tvShowId(line[0])
						   .averageRating(Float.valueOf(line[1]))
						   .numVotes(Integer.valueOf(line[2]))
						   .build();
	}

	public Flux<TvShowRating> getAll() {
		return tvShowRatingRepository.findAll();
	}

	public Flux<TvShowRating> findByIds(List<String> tvShowIds) {
		return tvShowRatingRepository.findByTvShowIdIn(tvShowIds);
	}
}
