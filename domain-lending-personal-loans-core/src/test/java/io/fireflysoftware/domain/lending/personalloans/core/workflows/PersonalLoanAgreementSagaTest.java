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
import io.fireflysoftware.domain.lending.personalloans.core.commands.UpdatePersonalLoanAgreementCommand;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.orchestration.core.context.ExecutionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonalLoanAgreementSagaTest {

    @Mock
    private CommandBus commandBus;

    @Test
    void createCompensatesByDeletingTheAgreement() {
        RegisterPersonalLoanAgreementSaga saga = new RegisterPersonalLoanAgreementSaga(commandBus);
        UUID id = UUID.randomUUID();
        PersonalLoanAgreementDTO created = org.mockito.Mockito.mock(PersonalLoanAgreementDTO.class);
        when(created.getPersonalLoanAgreementId()).thenReturn(id);
        when(commandBus.send(any(CreatePersonalLoanAgreementCommand.class))).thenReturn(Mono.just(created));
        when(commandBus.send(any(DeletePersonalLoanAgreementCommand.class))).thenReturn(Mono.empty());

        ExecutionContext ctx = ExecutionContext.forSaga(RegisterPersonalLoanAgreementSaga.NAME, "test");
        StoredAgreement stored = saga.register(new CreatePersonalLoanAgreementCommand(created), ctx).block();
        saga.remove(stored).block();

        verify(commandBus).send(argThat(command -> {
            Object value = command;
            return value instanceof DeletePersonalLoanAgreementCommand deleted && id.equals(deleted.agreementId());
        }));
    }

    @Test
    void updateCompensatesByRestoringThePreviousBody() {
        UpdatePersonalLoanAgreementSaga saga = new UpdatePersonalLoanAgreementSaga(commandBus);
        UUID id = UUID.randomUUID();
        PersonalLoanAgreementDTO previous = new PersonalLoanAgreementDTO().applicationId(UUID.randomUUID());
        PersonalLoanAgreementDTO updated = new PersonalLoanAgreementDTO();
        when(commandBus.send(any(UpdatePersonalLoanAgreementCommand.class))).thenReturn(Mono.just(updated));

        ExecutionContext ctx = ExecutionContext.forSaga(UpdatePersonalLoanAgreementSaga.NAME, "test");
        ctx.putVariable("previous", new StoredAgreement(id, previous));
        saga.restore(new StoredAgreement(id, updated), ctx).block();

        verify(commandBus).send(argThat(command -> {
            Object value = command;
            return value instanceof UpdatePersonalLoanAgreementCommand restored
                    && id.equals(restored.getAgreementId())
                    && restored.getDto() == previous;
        }));
    }
}
