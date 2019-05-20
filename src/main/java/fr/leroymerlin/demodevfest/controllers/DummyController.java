package fr.leroymerlin.demodevfest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Dummy example used to expose services.
 */
@RestController
@Slf4j
@RequestMapping("/dummy")
public class DummyController {

	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Mono<String> getDummy() {
		
		return Mono.just("{ \"dummy\" : \"OK\" }");
	}
}
