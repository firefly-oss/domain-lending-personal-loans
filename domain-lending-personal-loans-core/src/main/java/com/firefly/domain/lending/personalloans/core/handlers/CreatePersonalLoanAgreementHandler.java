package com.firefly.domain.lending.personalloans.core.handlers;

import com.firefly.core.lending.personalloans.sdk.api.PersonalLoanAgreementApi;
import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import com.firefly.domain.lending.personalloans.core.commands.CreatePersonalLoanAgreementCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link CreatePersonalLoanAgreementCommand} by delegating to core-lending-personal-loans.
 */
@Slf4j
@CommandHandlerComponent
public class CreatePersonalLoanAgreementHandler extends CommandHandler<CreatePersonalLoanAgreementCommand, PersonalLoanAgreementDTO> {

    private final PersonalLoanAgreementApi personalLoanAgreementApi;

    /**
     * Creates a new CreatePersonalLoanAgreementHandler.
     *
     * @param personalLoanAgreementApi the SDK client for personal loan agreement operations
     */
    public CreatePersonalLoanAgreementHandler(PersonalLoanAgreementApi personalLoanAgreementApi) {
        this.personalLoanAgreementApi = personalLoanAgreementApi;
    }

    @Override
    protected Mono<PersonalLoanAgreementDTO> doHandle(CreatePersonalLoanAgreementCommand command) {
        log.debug("Creating personal loan agreement");
        return personalLoanAgreementApi.create(command.getDto(), UUID.randomUUID().toString());
    }
}
