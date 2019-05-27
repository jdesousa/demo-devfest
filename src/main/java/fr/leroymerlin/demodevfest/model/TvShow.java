package fr.leroymerlin.demodevfest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TvShow {

	private String id;
	private String title;

	// Rating informations
	private Float averageRating;
	private  Integer numVotes;
}
