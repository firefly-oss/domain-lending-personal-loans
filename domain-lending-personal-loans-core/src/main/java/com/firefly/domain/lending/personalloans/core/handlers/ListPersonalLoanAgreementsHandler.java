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

package com.firefly.domain.lending.personalloans.core.handlers;

import com.firefly.core.lending.personalloans.sdk.api.PersonalLoanAgreementApi;
import com.firefly.core.lending.personalloans.sdk.model.FilterRequestPersonalLoanAgreementDTO;
import com.firefly.core.lending.personalloans.sdk.model.PaginationResponsePersonalLoanAgreementDTO;
import com.firefly.domain.lending.personalloans.core.commands.ListPersonalLoanAgreementsQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link ListPersonalLoanAgreementsQuery} by listing all agreements from core-lending-personal-loans.
 */
@Slf4j
@QueryHandlerComponent
public class ListPersonalLoanAgreementsHandler extends QueryHandler<ListPersonalLoanAgreementsQuery, PaginationResponsePersonalLoanAgreementDTO> {

    private final PersonalLoanAgreementApi personalLoanAgreementApi;

    /**
     * Creates a new ListPersonalLoanAgreementsHandler.
     *
     * @param personalLoanAgreementApi the SDK client for personal loan agreement operations
     */
    public ListPersonalLoanAgreementsHandler(PersonalLoanAgreementApi personalLoanAgreementApi) {
        this.personalLoanAgreementApi = personalLoanAgreementApi;
    }

    @Override
    protected Mono<PaginationResponsePersonalLoanAgreementDTO> doHandle(ListPersonalLoanAgreementsQuery query) {
        log.debug("Listing all personal loan agreements");
        return personalLoanAgreementApi.findAll(new FilterRequestPersonalLoanAgreementDTO(), UUID.randomUUID().toString());
    }
}
