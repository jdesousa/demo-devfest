package fr.leroymerlin.demodevfest.client;

import com.google.common.base.Charsets;
import fr.leroymerlin.demodevfest.IntegrationTestConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(classes = IntegrationTestConfiguration.class)
@ExtendWith(SpringExtension.class)
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

