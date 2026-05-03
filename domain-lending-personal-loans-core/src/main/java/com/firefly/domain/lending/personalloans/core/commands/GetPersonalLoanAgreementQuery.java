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

package com.firefly.domain.lending.personalloans.core.commands;

import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.query.Query;

import java.util.UUID;

/**
 * Query to retrieve a single personal loan agreement by identifier.
 */
public class GetPersonalLoanAgreementQuery implements Query<PersonalLoanAgreementDTO> {

    @NotNull
    private final UUID agreementId;

    /**
     * Creates a new GetPersonalLoanAgreementQuery.
     *
     * @param agreementId the unique identifier of the agreement to fetch
     */
    public GetPersonalLoanAgreementQuery(UUID agreementId) {
        this.agreementId = agreementId;
    }

    /**
     * Returns the agreement identifier.
     *
     * @return the agreement ID
     */
    public UUID getAgreementId() {
        return agreementId;
    }
}
