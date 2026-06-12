package br.com.pedrolourenco.TradeSim.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DateRangeRequest(
        @NotBlank(message = "data inicial esta faltando")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Data deve estar no formato yyyy-MM-dd")
        String startDate,

        @NotBlank(message = "data final esta faltando")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Data deve estar no formato yyyy-MM-dd")
        String endDate
) {}