package fr.leroymerlin.demodevfest.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.leroymerlin.demodevfest.controllers.exceptions.ResourceNotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@Endpoint(id = "swagger")
public class SwaggerEndpointConfig {

	@Getter(lazy = true)
	private final String swaggerContent = readSwaggerFile();

	private String readSwaggerFile() {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = null;

		try {
			jsonNode = mapper.readTree(this.getClass().getResourceAsStream("/swagger/swagger.json"));
		} catch (IOException e) {
			log.warn("unable to load the swagger file", e);
		}

		return jsonNode == null ? null : jsonNode.toString();
	}

	/**
	 * Get json File for api documentation
	 *
	 * @return json file for api documentation
	 */
	@ReadOperation
	public String getSwagger(){
		String swaggerContent = getSwaggerContent();
		if (swaggerContent == null) {
			throw new ResourceNotFoundException();
		}
		return swaggerContent;
	}
}