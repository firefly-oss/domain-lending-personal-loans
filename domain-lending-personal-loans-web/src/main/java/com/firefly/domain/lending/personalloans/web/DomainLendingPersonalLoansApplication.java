package com.firefly.domain.lending.personalloans.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Entry point for the domain-lending-personal-loans microservice.
 *
 * <p>This service is the orchestration layer for personal loan workflows.
 * It coordinates calls to {@code core-lending-personal-loans} via CQRS commands and exposes
 * a domain-level REST API for experience-layer consumers.
 */
@SpringBootApplication(
        scanBasePackages = {
                "com.firefly.domain.lending.personalloans",
                "org.fireflyframework.web"
        }
)
@EnableWebFlux
@ConfigurationPropertiesScan(basePackages = "com.firefly.domain.lending.personalloans")
@OpenAPIDefinition(
        info = @Info(
                title = "${spring.application.name}",
                version = "${spring.application.version}",
                description = "${spring.application.description}",
                contact = @Contact(
                        name = "${spring.application.team.name}",
                        email = "${spring.application.team.email}"
                )
        ),
        servers = {
                @Server(
                        url = "http://core.getfirefly.io/domain-personal-loans",
                        description = "Development Environment"
                ),
                @Server(
                        url = "/",
                        description = "Local Development Environment"
                )
        }
)
public class DomainLendingPersonalLoansApplication {

    /**
     * Application entry point.
     *
     * @param args command-line arguments passed to the Spring Boot application
     */
    public static void main(String[] args) {
        SpringApplication.run(DomainLendingPersonalLoansApplication.class, args);
    }
}
