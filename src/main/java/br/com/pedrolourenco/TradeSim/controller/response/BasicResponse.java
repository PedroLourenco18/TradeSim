package br.com.pedrolourenco.TradeSim.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Generic wrapper for simple API responses without a payload.")
public record BasicResponse(
        @Schema(description = "Indicates if the request resulted in an error.", example = "false")
        boolean error,

        @Schema(description = "A message providing additional context or error details.", example = "Operation successful")
        String message
) {
}
