package fr.leroymerlin.demodevfest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TvShowRating {
	private String tvShowId;
	private Float averageRating;
	private  Integer numVotes;
}
