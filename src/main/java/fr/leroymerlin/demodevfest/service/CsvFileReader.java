package fr.leroymerlin.demodevfest.service;

import fr.leroymerlin.demodevfest.model.TvShow;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import fr.leroymerlin.demodevfest.utils.CSVLineSplitOperator;
import fr.leroymerlin.demodevfest.utils.CsvHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.stream.BaseStream;
import java.util.stream.StreamSupport;

@Service
public class CsvFileReader {

	public Flux<TvShow> readTvShow() {
		return readFile("series.tsv")
				   .map(this::createTvShow);
	}

	public Flux<TvShowRating> readTvShowRating() {
		return readFile("series.ratings.tsv")
				   .map(line -> createTvShowRating(line));
	}

	private Flux<String[]> readFile(String fileName) {
		return Flux.using(() -> StreamSupport.stream(new CSVLineSplitOperator(CsvHelper.getReader(fileName)), false), Flux::fromStream,
			BaseStream::close);
	}

	private TvShowRating createTvShowRating(String[] line) {
		return TvShowRating.builder()
						   .tvShowId(line[0])
						   .averageRating(Float.valueOf(line[1]))
						   .numVotes(Integer.valueOf(line[2]))
						   .build();
	}

	private TvShow createTvShow(String[] line) {
		return TvShow.builder()
					 .id(line[0])
					 .title(line[3])
					 .build();
	}
}
