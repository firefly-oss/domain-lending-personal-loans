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
import io.fireflysoftware.domain.lending.personalloans.core.commands.CreatePersonalLoanAgreementCommand;
import io.fireflysoftware.domain.lending.personalloans.core.commands.DeletePersonalLoanAgreementCommand;
import io.fireflysoftware.domain.lending.personalloans.core.handlers.LoadPersonalLoanAgreementHandler;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.orchestration.core.context.ExecutionContext;
import org.fireflyframework.orchestration.saga.annotation.Saga;
import org.fireflyframework.orchestration.saga.annotation.SagaStep;
import org.fireflyframework.orchestration.saga.annotation.StepEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/** Creates the agreement and deletes it if a later step fails. */
@Saga(name = RegisterPersonalLoanAgreementSaga.NAME)
@Service
public class RegisterPersonalLoanAgreementSaga {
    public static final String NAME = "RegisterPersonalLoanAgreementSaga";
    public static final String STEP = "register";
    public static final String COMPENSATE = "remove";

    private final CommandBus commandBus;

    public RegisterPersonalLoanAgreementSaga(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @SagaStep(id = STEP, compensate = COMPENSATE)
    @StepEvent(type = "personalloans.agreement.registered")
    public Mono<StoredAgreement> register(CreatePersonalLoanAgreementCommand command, ExecutionContext ctx) {
        return commandBus.<PersonalLoanAgreementDTO>send(command)
                .map(LoadPersonalLoanAgreementHandler::stored)
                .doOnNext(created -> ctx.putVariable("created", created));
    }

    public Mono<Void> remove(StoredAgreement created) {
        if (created == null || created.id() == null) {
            return Mono.empty();
        }
        return commandBus.send(new DeletePersonalLoanAgreementCommand(created.id()));
    }
}
