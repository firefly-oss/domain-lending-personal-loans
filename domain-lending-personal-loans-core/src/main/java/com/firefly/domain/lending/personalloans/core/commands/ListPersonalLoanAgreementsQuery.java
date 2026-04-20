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
