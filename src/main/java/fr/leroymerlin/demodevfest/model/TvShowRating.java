package fr.leroymerlin.demodevfest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TvShowRating {
	private String tvShowId;
	private Float averageRating;
	private Integer numVotes;

	public static TvShowRating NO_TV_SHOW_RATING = TvShowRating.builder()
														.numVotes(0)
														.averageRating(0f)
														.build();
}
