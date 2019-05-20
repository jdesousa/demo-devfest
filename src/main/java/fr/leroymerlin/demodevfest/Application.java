package fr.leroymerlin.demodevfest;

import fr.leroymerlin.demodevfest.model.TvShow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.ConfigurableTypeInformationMapper;

import java.io.IOException;
import java.util.HashMap;

/**
 * This is the main class used to start application.
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "fr.leroymerlin.demodevfest")
public class Application {


	static {
		// Enable Netty Http Server access log
		System.setProperty("reactor.netty.http.server.accessLogEnabled", "true");
	}

	/**
	 * Main method used to start application.
	 *
	 * @param args
	 * 		startup arguments.
	 *
	 * @throws IOException
	 * 		IOException
	 */
	public static void main(String[] args) throws IOException {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ConfigurableTypeInformationMapper mapper() {

		HashMap<Class<?>, String> sourceTypeMap = new HashMap<>();

		sourceTypeMap.put(TvShow.class, "tvShow");
		return new ConfigurableTypeInformationMapper(sourceTypeMap);
	}

}