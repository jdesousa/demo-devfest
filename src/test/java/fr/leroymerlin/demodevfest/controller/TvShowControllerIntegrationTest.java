package fr.leroymerlin.demodevfest.controller;

import fr.leroymerlin.demodevfest.IntegrationTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = IntegrationTestConfiguration.class)
@AutoConfigureWebTestClient
class TvShowControllerIntegrationTest  {

	@Autowired
	protected WebTestClient webTestClient;

	@Test
	@DisplayName("When finding a Tv Show by id, then return the corresponding tv show with rating aggregated")
	void getById() {

		webTestClient.get()
					 .uri("/tvShows/{id}", "tt0041002")
					 .exchange()
					 .expectStatus()
					 .isOk()
					 .expectBody()
					 .jsonPath(".id").isEqualTo("tt0041002")
					 .jsonPath(".title").isEqualTo("Arthur Godfrey and His Friends")
					 .jsonPath(".averageRating").isEqualTo(7.5)
					 .jsonPath(".numVotes").isEqualTo(13);
	}

}
