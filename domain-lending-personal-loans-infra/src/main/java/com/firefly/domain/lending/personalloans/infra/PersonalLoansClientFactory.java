package com.firefly.domain.lending.personalloans.infra;

import com.firefly.core.lending.personalloans.sdk.api.PersonalLoanAgreementApi;
import com.firefly.core.lending.personalloans.sdk.invoker.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Creates and exposes WebClient-based API clients for {@code core-lending-personal-loans}.
 *
 * <p>Uses {@code @Component} (not {@code @Configuration}) per banking platform convention.
 * Base path is injected via {@link PersonalLoansProperties}.
 */
@Component
public class PersonalLoansClientFactory {

    private final ApiClient apiClient;

    /**
     * Creates a new factory and configures the shared {@link ApiClient} with the base path from properties.
     *
     * @param properties the personal loans configuration properties
     */
    public PersonalLoansClientFactory(PersonalLoansProperties properties) {
        this.apiClient = new ApiClient();
        this.apiClient.setBasePath(properties.getBasePath());
    }

    /**
     * Creates the {@link PersonalLoanAgreementApi} client bean.
     *
     * @return the personal loan agreement API client
     */
    @Bean
    public PersonalLoanAgreementApi personalLoanAgreementApi() {
        return new PersonalLoanAgreementApi(apiClient);
    }
}
