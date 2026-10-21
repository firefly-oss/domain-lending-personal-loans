/*
 * Copyright 2026 Firefly Software Foundation
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

package io.fireflysoftware.domain.lending.personalloans.core.workflows;

import io.fireflysoftware.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import io.fireflysoftware.domain.lending.personalloans.core.StoredAgreement;
import io.fireflysoftware.domain.lending.personalloans.core.commands.LoadPersonalLoanAgreementCommand;
import io.fireflysoftware.domain.lending.personalloans.core.commands.UpdatePersonalLoanAgreementCommand;
import io.fireflysoftware.domain.lending.personalloans.core.handlers.LoadPersonalLoanAgreementHandler;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.orchestration.core.context.ExecutionContext;
import org.fireflyframework.orchestration.saga.annotation.Saga;
import org.fireflyframework.orchestration.saga.annotation.SagaStep;
import org.fireflyframework.orchestration.saga.annotation.StepEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/** Updates the agreement and writes the previous body back if a later step fails. */
@Saga(name = UpdatePersonalLoanAgreementSaga.NAME)
@Service
public class UpdatePersonalLoanAgreementSaga {
    public static final String NAME = "UpdatePersonalLoanAgreementSaga";
    public static final String LOAD = "load";
    public static final String STEP = "update";
    public static final String COMPENSATE = "restore";

    private final CommandBus commandBus;

    public UpdatePersonalLoanAgreementSaga(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @SagaStep(id = LOAD)
    @StepEvent(type = "personalloans.agreement.loaded")
    public Mono<StoredAgreement> load(LoadPersonalLoanAgreementCommand command, ExecutionContext ctx) {
        return commandBus.<StoredAgreement>send(command)
                .doOnNext(current -> ctx.putVariable("previous", current));
    }

    @SagaStep(id = STEP, dependsOn = LOAD, compensate = COMPENSATE)
    @StepEvent(type = "personalloans.agreement.updated")
    public Mono<StoredAgreement> update(UpdatePersonalLoanAgreementCommand command, ExecutionContext ctx) {
        return commandBus.<PersonalLoanAgreementDTO>send(command)
                .map(LoadPersonalLoanAgreementHandler::stored);
    }

    public Mono<Void> restore(StoredAgreement updated, ExecutionContext ctx) {
        StoredAgreement previous = ctx.getVariableAs("previous", StoredAgreement.class);
        if (previous == null || previous.id() == null || previous.body() == null) {
            return Mono.empty();
        }
        return commandBus.send(new UpdatePersonalLoanAgreementCommand(previous.id(), previous.body())).then();
    }
}
