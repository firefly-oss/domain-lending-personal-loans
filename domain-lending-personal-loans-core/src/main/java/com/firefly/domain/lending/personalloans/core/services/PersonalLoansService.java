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
