package fr.leroymerlin.demodevfest;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootConfiguration
@ComponentScan(value = "fr.leroymerlin.demodevfest")
@EnableAutoConfiguration
@PropertySource("classpath:application.yaml")
public class IntegrationTestConfiguration {
}
