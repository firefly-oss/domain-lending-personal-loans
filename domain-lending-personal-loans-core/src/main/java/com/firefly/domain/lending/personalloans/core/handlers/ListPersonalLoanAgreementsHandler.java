package com.firefly.domain.lending.personalloans.core.handlers;

import com.firefly.core.lending.personalloans.sdk.api.PersonalLoanAgreementApi;
import com.firefly.core.lending.personalloans.sdk.model.FilterRequestPersonalLoanAgreementDTO;
import com.firefly.core.lending.personalloans.sdk.model.PaginationResponse;
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
public class ListPersonalLoanAgreementsHandler extends QueryHandler<ListPersonalLoanAgreementsQuery, PaginationResponse> {

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
    protected Mono<PaginationResponse> doHandle(ListPersonalLoanAgreementsQuery query) {
        log.debug("Listing all personal loan agreements");
        return personalLoanAgreementApi.findAll(new FilterRequestPersonalLoanAgreementDTO(), UUID.randomUUID().toString());
    }
}
