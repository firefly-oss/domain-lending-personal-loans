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

package com.firefly.domain.lending.personalloans.core.services;

import com.firefly.core.lending.personalloans.sdk.model.PaginationResponse;
import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import com.firefly.domain.lending.personalloans.core.commands.CreatePersonalLoanAgreementCommand;
import com.firefly.domain.lending.personalloans.core.commands.UpdatePersonalLoanAgreementCommand;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Domain service interface for personal loans orchestration.
 *
 * <p>Coordinates calls to {@code core-lending-personal-loans} via CQRS commands and queries,
 * exposing a domain-level API for experience-layer consumers.
 */
public interface PersonalLoansService {

    /**
     * Creates a new personal loan agreement.
     *
     * @param command the create agreement command
     * @return the created agreement
     */
    Mono<PersonalLoanAgreementDTO> createAgreement(CreatePersonalLoanAgreementCommand command);

    /**
     * Retrieves a personal loan agreement by its identifier.
     *
     * @param agreementId the unique identifier of the agreement
     * @return the agreement, or empty if not found
     */
    Mono<PersonalLoanAgreementDTO> getAgreement(UUID agreementId);

    /**
     * Updates an existing personal loan agreement.
     *
     * @param command the update agreement command
     * @return the updated agreement
     */
    Mono<PersonalLoanAgreementDTO> updateAgreement(UpdatePersonalLoanAgreementCommand command);

    /**
     * Lists all personal loan agreements.
     *
     * @return a paginated list of agreements
     */
    Mono<PaginationResponse> listAgreements();
}
