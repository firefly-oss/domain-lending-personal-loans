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

package com.firefly.domain.lending.personalloans.core.commands;

import com.firefly.core.lending.personalloans.sdk.model.PaginationResponse;
import org.fireflyframework.cqrs.query.Query;

/**
 * Query to list all personal loan agreements.
 */
public class ListPersonalLoanAgreementsQuery implements Query<PaginationResponse> {

    /**
     * Creates a new ListPersonalLoanAgreementsQuery.
     */
    public ListPersonalLoanAgreementsQuery() {
        // No fields — uses empty FilterRequest internally
    }
}
