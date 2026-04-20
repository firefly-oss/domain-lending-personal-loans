package com.firefly.domain.lending.personalloans.core.commands;

import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to update an existing personal loan agreement.
 */
public class UpdatePersonalLoanAgreementCommand implements Command<PersonalLoanAgreementDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final PersonalLoanAgreementDTO dto;

    /**
     * Creates a new UpdatePersonalLoanAgreementCommand.
     *
     * @param agreementId the unique identifier of the agreement to update
     * @param dto         the updated agreement payload
     */
    public UpdatePersonalLoanAgreementCommand(UUID agreementId, PersonalLoanAgreementDTO dto) {
        this.agreementId = agreementId;
        this.dto = dto;
    }

    /**
     * Returns the agreement identifier.
     *
     * @return the agreement ID
     */
    public UUID getAgreementId() {
        return agreementId;
    }

    /**
     * Returns the updated agreement payload.
     *
     * @return the agreement DTO
     */
    public PersonalLoanAgreementDTO getDto() {
        return dto;
    }
}
