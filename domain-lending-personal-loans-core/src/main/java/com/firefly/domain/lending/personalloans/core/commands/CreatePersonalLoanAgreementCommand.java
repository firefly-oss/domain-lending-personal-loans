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

import com.firefly.core.lending.personalloans.sdk.model.PersonalLoanAgreementDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

/**
 * Command to create a new personal loan agreement in core-lending-personal-loans.
 */
public class CreatePersonalLoanAgreementCommand implements Command<PersonalLoanAgreementDTO> {

    @NotNull
    private final PersonalLoanAgreementDTO dto;

    /**
     * Creates a new CreatePersonalLoanAgreementCommand.
     *
     * @param dto the agreement payload
     */
    public CreatePersonalLoanAgreementCommand(PersonalLoanAgreementDTO dto) {
        this.dto = dto;
    }

    /**
     * Returns the agreement payload.
     *
     * @return the agreement DTO
     */
    public PersonalLoanAgreementDTO getDto() {
        return dto;
    }
}
