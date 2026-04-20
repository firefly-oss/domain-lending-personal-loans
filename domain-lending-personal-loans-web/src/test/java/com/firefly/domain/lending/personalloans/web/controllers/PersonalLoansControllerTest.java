/*
 * Copyright 2025 Firefly Software Solutions Inc
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

package com.firefly.domain.lending.personalloans.web.controllers;

import com.firefly.core.lending.personalloans.sdk.model.PaginationResponse;
import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import com.firefly.domain.lending.personalloans.core.services.PersonalLoansService;
import org.fireflyframework.web.error.config.ErrorHandlingProperties;
import org.fireflyframework.web.error.converter.ExceptionConverterService;
import org.fireflyframework.web.error.service.ErrorResponseNegotiator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PersonalLoansController}.
 *
 * <p>Uses {@code @WebFluxTest} to test only the web layer, with the service mocked.
 */
@WebFluxTest(PersonalLoansController.class)
class PersonalLoansControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PersonalLoansService personalLoansService;

    // Required by GlobalExceptionHandler (org.fireflyframework.web v26.02.07)
    // Non-Optional constructor params not provided by the @WebFluxTest slice
    @MockBean
    private ExceptionConverterService exceptionConverterService;
    @MockBean
    private ErrorHandlingProperties errorHandlingProperties;
    @MockBean
    private ErrorResponseNegotiator errorResponseNegotiator;

    // ── Agreement tests ──────────────────────────────────────────────────────

    @Test
    void createAgreement_returns201() {
        var dto = new PersonalLoanAgreementDTO();
        when(personalLoansService.createAgreement(any())).thenReturn(Mono.just(dto));

        webTestClient.post()
                .uri("/api/v1/personal-loans/agreements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PersonalLoanAgreementDTO.class);
    }

    @Test
    void getAgreement_returns200_whenFound() {
        var agreementId = UUID.randomUUID();
        var dto = new PersonalLoanAgreementDTO();
        when(personalLoansService.getAgreement(agreementId)).thenReturn(Mono.just(dto));

        webTestClient.get()
                .uri("/api/v1/personal-loans/agreements/{id}", agreementId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonalLoanAgreementDTO.class);
    }

    @Test
    void getAgreement_returns404_whenNotFound() {
        var agreementId = UUID.randomUUID();
        when(personalLoansService.getAgreement(agreementId)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/personal-loans/agreements/{id}", agreementId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateAgreement_returns200_whenFound() {
        var agreementId = UUID.randomUUID();
        var dto = new PersonalLoanAgreementDTO();
        when(personalLoansService.updateAgreement(any())).thenReturn(Mono.just(dto));

        webTestClient.put()
                .uri("/api/v1/personal-loans/agreements/{id}", agreementId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonalLoanAgreementDTO.class);
    }

    @Test
    void listAgreements_returns200() {
        var page = new PaginationResponse();
        when(personalLoansService.listAgreements()).thenReturn(Mono.just(page));

        webTestClient.get()
                .uri("/api/v1/personal-loans/agreements")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginationResponse.class);
    }
}
