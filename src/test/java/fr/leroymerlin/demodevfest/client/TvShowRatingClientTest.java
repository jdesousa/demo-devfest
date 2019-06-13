package fr.leroymerlin.demodevfest.client;

import com.google.common.net.MediaType;
import fr.leroymerlin.demodevfest.IntegrationTestConfiguration;
import fr.leroymerlin.demodevfest.model.TvShowRating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.matchers.Times;
import org.mockserver.model.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(classes = IntegrationTestConfiguration.class)
@ActiveProfiles("client")
class TvShowRatingClientTest extends AbstractClientIntegrationTest {

	@Autowired
	private TvShowRatingClient cut;

	@BeforeEach
	void resetServer() {
		mockServer.reset();
	}

	@Test
	@DisplayName("When calling rating api without error, then retrieve the lif of TvShowRating")
	void loadRatingWithoutRetry() throws IOException {
		String response = AbstractClientIntegrationTest.getBodyByFileName("rating-response.json");

		// Without Error
		mockServer.when(request()
							.withMethod("GET")
							.withPath("/tvShowRatingByIds")
							.withQueryStringParameters(
								Parameter.param("ids", "1")))
				  .respond(response().withStatusCode(HttpStatus.OK.value())
									 .withBody(response, MediaType.JSON_UTF_8));
		Flux<TvShowRating> result = cut.findTVShowRatingByIds(asList("1"));

		StepVerifier.create(result)
					.expectNextMatches(tvShowRating -> tvShowRating.getNumVotes()
																   .equals(43))
					.verifyComplete();
	}

	@Test
	@DisplayName("When calling rating api with 2 calls failed, then retrieve the lif of TvShowRating after 2 attempts")
	void loadRatingWithRetry() throws IOException {
		String response = AbstractClientIntegrationTest.getBodyByFileName("rating-response.json");

		// With 2 Errors
		mockServer.when(request()
							.withMethod("GET")
							.withPath("/tvShowRatingByIds")
							.withQueryStringParameters(
								Parameter.param("ids", "2")), Times.exactly(2))
				  .respond(response().withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));

		mockServer.when(request()
							.withMethod("GET")
							.withPath("/tvShowRatingByIds")
							.withQueryStringParameters(
								Parameter.param("ids", "2")))
				  .respond(response().withStatusCode(HttpStatus.OK.value())
									 .withBody(response, MediaType.JSON_UTF_8));
		Flux<TvShowRating> result = cut.findTVShowRatingByIds(asList("2"));

		StepVerifier.create(result)
					.expectNextMatches(tvShowRating -> tvShowRating.getNumVotes()
																   .equals(43))
					.verifyComplete();
	}

	@Test
	@DisplayName("When calling rating api with 4 calls failed, then failed after 3 attempts")
	void loadRatingErrors() throws IOException {
		String response = AbstractClientIntegrationTest.getBodyByFileName("rating-response.json");

		// With 4 Errors
		mockServer.when(request()
							.withMethod("GET")
							.withPath("/tvShowRatingByIds")
							.withQueryStringParameters(
								Parameter.param("ids", "3")), Times.exactly(4))
				  .respond(response().withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
		Flux<TvShowRating> result = cut.findTVShowRatingByIds(asList("3"));

		StepVerifier.create(result)
					.expectError()
					.verify();
	}
}
