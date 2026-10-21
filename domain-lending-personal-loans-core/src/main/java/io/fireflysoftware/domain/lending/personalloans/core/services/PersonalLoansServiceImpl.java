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

package io.fireflysoftware.domain.lending.personalloans.core.services;

import io.fireflysoftware.core.lending.personalloans.sdk.model.PaginationResponsePersonalLoanAgreementDTO;
import io.fireflysoftware.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import io.fireflysoftware.domain.lending.personalloans.core.StoredAgreement;
import io.fireflysoftware.domain.lending.personalloans.core.commands.CreatePersonalLoanAgreementCommand;
import io.fireflysoftware.domain.lending.personalloans.core.commands.GetPersonalLoanAgreementQuery;
import io.fireflysoftware.domain.lending.personalloans.core.commands.ListPersonalLoanAgreementsQuery;
import io.fireflysoftware.domain.lending.personalloans.core.commands.LoadPersonalLoanAgreementCommand;
import io.fireflysoftware.domain.lending.personalloans.core.commands.UpdatePersonalLoanAgreementCommand;
import io.fireflysoftware.domain.lending.personalloans.core.workflows.RegisterPersonalLoanAgreementSaga;
import io.fireflysoftware.domain.lending.personalloans.core.workflows.UpdatePersonalLoanAgreementSaga;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.query.QueryBus;
import org.fireflyframework.orchestration.saga.engine.SagaEngine;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import org.fireflyframework.orchestration.saga.engine.StepInputs;
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

    private final SagaEngine engine;
    private final QueryBus queryBus;

    @Override
    public Mono<PersonalLoanAgreementDTO> createAgreement(CreatePersonalLoanAgreementCommand command) {
        StepInputs inputs = StepInputs.builder()
                .forStepId(RegisterPersonalLoanAgreementSaga.STEP, command)
                .build();
        return engine.execute(RegisterPersonalLoanAgreementSaga.NAME, inputs)
                .flatMap(result -> outcome(result, RegisterPersonalLoanAgreementSaga.STEP))
                .map(StoredAgreement::body);
    }

    @Override
    public Mono<PersonalLoanAgreementDTO> getAgreement(UUID agreementId) {
        return queryBus.query(new GetPersonalLoanAgreementQuery(agreementId));
    }

    @Override
    public Mono<PersonalLoanAgreementDTO> updateAgreement(UpdatePersonalLoanAgreementCommand command) {
        StepInputs inputs = StepInputs.builder()
                .forStepId(UpdatePersonalLoanAgreementSaga.LOAD, new LoadPersonalLoanAgreementCommand(command.getAgreementId()))
                .forStepId(UpdatePersonalLoanAgreementSaga.STEP, command)
                .build();
        return engine.execute(UpdatePersonalLoanAgreementSaga.NAME, inputs)
                .flatMap(result -> outcome(result, UpdatePersonalLoanAgreementSaga.STEP))
                .map(StoredAgreement::body);
    }

    @Override
    public Mono<PaginationResponsePersonalLoanAgreementDTO> listAgreements() {
        return queryBus.query(new ListPersonalLoanAgreementsQuery());
    }

    private static Mono<StoredAgreement> outcome(SagaResult result, String stepId) {
        if (result.isFailed()) {
            Throwable error = result.error().orElseGet(() ->
                    new IllegalStateException("Saga " + result.sagaName() + " failed"));
            return Mono.error(error);
        }
        return Mono.justOrEmpty(result.resultOf(stepId, StoredAgreement.class))
                .switchIfEmpty(Mono.error(new IllegalStateException("Saga step " + stepId + " produced no result")));
    }
}
