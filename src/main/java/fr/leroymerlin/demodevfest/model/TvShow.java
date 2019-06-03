package fr.leroymerlin.demodevfest.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Wither;

@Data
@Builder
@Wither
public class TvShow {

	private String id;
	private String title;

	// Rating informations
	private Float averageRating;
	private  Integer numVotes;
}
