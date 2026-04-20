/*
 * Copyright 2025 Firefly Software Solutions Inc
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
