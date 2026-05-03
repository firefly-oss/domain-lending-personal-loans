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

package com.firefly.domain.lending.personalloans.web.controllers;

import com.firefly.core.lending.personalloans.sdk.model.PaginationResponsePersonalLoanAgreementDTO;
import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import com.firefly.domain.lending.personalloans.core.commands.CreatePersonalLoanAgreementCommand;
import com.firefly.domain.lending.personalloans.core.commands.UpdatePersonalLoanAgreementCommand;
import com.firefly.domain.lending.personalloans.core.services.PersonalLoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST controller exposing the personal loans domain API.
 *
 * <p>All endpoints are fully reactive ({@code Mono}) and follow the platform convention
 * of using UUID path variables for resource identification.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/personal-loans")
@Tag(name = "Personal Loans", description = "Domain-level orchestration for personal loan workflows")
public class PersonalLoansController {

    private final PersonalLoansService personalLoansService;

    /**
     * Creates a new personal loan agreement.
     *
     * @param dto the agreement payload
     * @return 201 Created with the new agreement, or 400 on invalid input
     */
    @Operation(summary = "Create agreement", description = "Creates a new personal loan agreement.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agreement created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PersonalLoanAgreementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PostMapping(value = "/agreements", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PersonalLoanAgreementDTO>> createAgreement(
            @RequestBody PersonalLoanAgreementDTO dto) {
        return personalLoansService.createAgreement(new CreatePersonalLoanAgreementCommand(dto))
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result));
    }

    /**
     * Retrieves a personal loan agreement by its identifier.
     *
     * @param agreementId the unique identifier of the agreement
     * @return 200 with the agreement, or 404 if not found
     */
    @Operation(summary = "Get agreement", description = "Retrieves a personal loan agreement by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agreement found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PersonalLoanAgreementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content)
    })
    @GetMapping(value = "/agreements/{agreementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PersonalLoanAgreementDTO>> getAgreement(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable UUID agreementId) {
        return personalLoansService.getAgreement(agreementId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * Updates an existing personal loan agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @param dto         the updated agreement payload
     * @return 200 with the updated agreement, or 404 if not found
     */
    @Operation(summary = "Update agreement", description = "Updates an existing personal loan agreement.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agreement updated",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PersonalLoanAgreementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content)
    })
    @PutMapping(value = "/agreements/{agreementId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PersonalLoanAgreementDTO>> updateAgreement(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable UUID agreementId,
            @RequestBody PersonalLoanAgreementDTO dto) {
        return personalLoansService.updateAgreement(new UpdatePersonalLoanAgreementCommand(agreementId, dto))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * Lists all personal loan agreements.
     *
     * @return 200 with a paginated list of agreements
     */
    @Operation(summary = "List agreements", description = "Lists all personal loan agreements.")
    @ApiResponse(responseCode = "200", description = "Agreements listed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PaginationResponsePersonalLoanAgreementDTO.class)))
    @GetMapping(value = "/agreements", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponsePersonalLoanAgreementDTO>> listAgreements() {
        return personalLoansService.listAgreements()
                .map(ResponseEntity::ok);
    }
}
