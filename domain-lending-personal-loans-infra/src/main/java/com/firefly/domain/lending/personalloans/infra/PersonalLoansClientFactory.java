/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
