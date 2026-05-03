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

package com.firefly.domain.lending.personalloans.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Domain-level DTO representing a summary of a personal loan agreement,
 * composed from {@code core-lending-personal-loans} data for experience-layer clients.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Personal loan agreement summary for domain-level consumers")
public class PersonalLoanAgreementSummaryDTO {

    @Schema(description = "Agreement identifier")
    private UUID agreementId;

    @Schema(description = "Application identifier this agreement originated from")
    private UUID applicationId;

    @Schema(description = "Purpose of the loan")
    private String loanPurpose;

    @Schema(description = "Current agreement status")
    private String status;

    @Schema(description = "Rate type: FIXED or VARIABLE")
    private String rateType;

    @Schema(description = "Interest rate applied to the loan")
    private BigDecimal interestRate;
}
