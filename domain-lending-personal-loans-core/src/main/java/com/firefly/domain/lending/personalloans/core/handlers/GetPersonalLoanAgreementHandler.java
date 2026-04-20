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
import com.firefly.domain.lending.personalloans.core.commands.GetPersonalLoanAgreementQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link GetPersonalLoanAgreementQuery} by fetching the agreement from core-lending-personal-loans.
 */
@Slf4j
@QueryHandlerComponent
public class GetPersonalLoanAgreementHandler extends QueryHandler<GetPersonalLoanAgreementQuery, PersonalLoanAgreementDTO> {

    private final PersonalLoanAgreementApi personalLoanAgreementApi;

    /**
     * Creates a new GetPersonalLoanAgreementHandler.
     *
     * @param personalLoanAgreementApi the SDK client for personal loan agreement operations
     */
    public GetPersonalLoanAgreementHandler(PersonalLoanAgreementApi personalLoanAgreementApi) {
        this.personalLoanAgreementApi = personalLoanAgreementApi;
    }

    @Override
    protected Mono<PersonalLoanAgreementDTO> doHandle(GetPersonalLoanAgreementQuery query) {
        log.debug("Fetching personal loan agreement: agreementId={}", query.getAgreementId());
        return personalLoanAgreementApi.getById(query.getAgreementId(), UUID.randomUUID().toString());
    }
}
