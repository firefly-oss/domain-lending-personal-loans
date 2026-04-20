package com.firefly.domain.lending.personalloans.core.services;

import com.firefly.core.lending.personalloans.sdk.model.PaginationResponse;
import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import com.firefly.domain.lending.personalloans.core.commands.CreatePersonalLoanAgreementCommand;
import com.firefly.domain.lending.personalloans.core.commands.GetPersonalLoanAgreementQuery;
import com.firefly.domain.lending.personalloans.core.commands.ListPersonalLoanAgreementsQuery;
import com.firefly.domain.lending.personalloans.core.commands.UpdatePersonalLoanAgreementCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.cqrs.query.QueryBus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Default implementation of {@link PersonalLoansService}.
 *
 * <p>Acts as a thin dispatcher: creates the appropriate Command or Query and sends it
 * via the CQRS bus, leaving all SDK interactions to the individual handlers.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalLoansServiceImpl implements PersonalLoansService {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Override
    public Mono<PersonalLoanAgreementDTO> createAgreement(CreatePersonalLoanAgreementCommand command) {
        return commandBus.send(command);
    }

    @Override
    public Mono<PersonalLoanAgreementDTO> getAgreement(UUID agreementId) {
        return queryBus.query(new GetPersonalLoanAgreementQuery(agreementId));
    }

    @Override
    public Mono<PersonalLoanAgreementDTO> updateAgreement(UpdatePersonalLoanAgreementCommand command) {
        return commandBus.send(command);
    }

    @Override
    public Mono<PaginationResponse> listAgreements() {
        return queryBus.query(new ListPersonalLoanAgreementsQuery());
    }
}
