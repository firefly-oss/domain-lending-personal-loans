package com.firefly.domain.lending.personalloans.core.handlers;

import com.firefly.core.lending.personalloans.sdk.api.PersonalLoanAgreementApi;
import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import com.firefly.domain.lending.personalloans.core.commands.UpdatePersonalLoanAgreementCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link UpdatePersonalLoanAgreementCommand} by delegating to core-lending-personal-loans.
 */
@Slf4j
@CommandHandlerComponent
public class UpdatePersonalLoanAgreementHandler extends CommandHandler<UpdatePersonalLoanAgreementCommand, PersonalLoanAgreementDTO> {

    private final PersonalLoanAgreementApi personalLoanAgreementApi;

    /**
     * Creates a new UpdatePersonalLoanAgreementHandler.
     *
     * @param personalLoanAgreementApi the SDK client for personal loan agreement operations
     */
    public UpdatePersonalLoanAgreementHandler(PersonalLoanAgreementApi personalLoanAgreementApi) {
        this.personalLoanAgreementApi = personalLoanAgreementApi;
    }

    @Override
    protected Mono<PersonalLoanAgreementDTO> doHandle(UpdatePersonalLoanAgreementCommand command) {
        log.debug("Updating personal loan agreement: agreementId={}", command.getAgreementId());
        return personalLoanAgreementApi.update(command.getAgreementId(), command.getDto(), UUID.randomUUID().toString());
    }
}
