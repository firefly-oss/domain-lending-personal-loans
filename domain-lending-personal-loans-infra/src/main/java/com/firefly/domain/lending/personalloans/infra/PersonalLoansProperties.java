package com.firefly.domain.lending.personalloans.infra;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the core-lending-personal-loans downstream service.
 * Bound from {@code api-configuration.core-lending.personal-loans} in application.yaml.
 *
 * <p>No {@code @Configuration} annotation — the application class carries
 * {@code @ConfigurationPropertiesScan} to register all {@code @ConfigurationProperties} beans.
 */
@ConfigurationProperties(prefix = "api-configuration.core-lending.personal-loans")
@Data
public class PersonalLoansProperties {

    private String basePath;
}
