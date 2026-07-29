package br.com.pedrolourenco.TradeSim.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request object for filtering data by a date range.")
public record DateRangeRequest(
        @Schema(description = "The start date of the range in yyyy-MM-dd format.", example = "2023-01-01")
        @NotBlank(message = "start date is missing")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Date must be in yyyy-MM-dd format")
        String startDate,

        @Schema(description = "The end date of the range in yyyy-MM-dd format.", example = "2023-12-31")
        @NotBlank(message = "end date is missing")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Date must be in yyyy-MM-dd format")
        String endDate
) {}
