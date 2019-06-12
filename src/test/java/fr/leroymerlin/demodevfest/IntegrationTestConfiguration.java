package fr.leroymerlin.demodevfest;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan(value = "fr.leroymerlin.demodevfest")
@EnableAutoConfiguration
//@PropertySource("classpath:application-test.yaml")
public class IntegrationTestConfiguration {
}
