package com.firefly.domain.lending.personalloans.core.commands;

import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.query.Query;

import java.util.UUID;

/**
 * Query to retrieve a single personal loan agreement by identifier.
 */
public class GetPersonalLoanAgreementQuery implements Query<PersonalLoanAgreementDTO> {

    @NotNull
    private final UUID agreementId;

    /**
     * Creates a new GetPersonalLoanAgreementQuery.
     *
     * @param agreementId the unique identifier of the agreement to fetch
     */
    public GetPersonalLoanAgreementQuery(UUID agreementId) {
        this.agreementId = agreementId;
    }

    /**
     * Returns the agreement identifier.
     *
     * @return the agreement ID
     */
    public UUID getAgreementId() {
        return agreementId;
    }
}
