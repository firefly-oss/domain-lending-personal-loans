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
