package br.com.pedrolourenco.TradeSim.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Generic wrapper for API responses.")
public record DataResponse<T> (
        @Schema(description = "Indicates if the request resulted in an error.", example = "false")
        boolean error,

        @Schema(description = "A message providing additional context or error details.", example = "Operation successful")
        String message,

        @Schema(description = "The actual payload of the response.")
        T data
){}
