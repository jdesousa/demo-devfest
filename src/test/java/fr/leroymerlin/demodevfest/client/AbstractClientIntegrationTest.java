package fr.leroymerlin.demodevfest.client;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockserver.integration.ClientAndServer;

import java.io.IOException;
import java.io.InputStream;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

//@SpringBootTest(classes = IntegrationClientTestConfiguration.class)
//@ExtendWith(SpringExtension.class)
public abstract class AbstractClientIntegrationTest {
	static ClientAndServer mockServer;

	@BeforeAll
	protected static void setUpClass() throws IOException {
		mockServer = startClientAndServer(1080);
	}

	@AfterAll
	protected static void stopMockServer() {
		if (mockServer.isRunning()) {
			mockServer.close();
			mockServer.stop();
		}
	}

	protected static String getBodyByFileName(String filename) throws IOException {
		InputStream notFoundStream = AbstractClientIntegrationTest.class.getClassLoader()
																		.getResourceAsStream(filename);
		return IOUtils.toString(notFoundStream, Charsets.UTF_8);
	}
}

