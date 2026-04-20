package com.firefly.domain.lending.personalloans.core.commands;

import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

/**
 * Command to create a new personal loan agreement in core-lending-personal-loans.
 */
public class CreatePersonalLoanAgreementCommand implements Command<PersonalLoanAgreementDTO> {

    @NotNull
    private final PersonalLoanAgreementDTO dto;

    /**
     * Creates a new CreatePersonalLoanAgreementCommand.
     *
     * @param dto the agreement payload
     */
    public CreatePersonalLoanAgreementCommand(PersonalLoanAgreementDTO dto) {
        this.dto = dto;
    }

    /**
     * Returns the agreement payload.
     *
     * @return the agreement DTO
     */
    public PersonalLoanAgreementDTO getDto() {
        return dto;
    }
}
