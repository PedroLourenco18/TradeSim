package br.com.pedrolourenco.TradeSim.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Generic response wrapper for paginated data.")
public record PagedDataResponse<T> (
        @Schema(description = "Indicates if an error occurred", example = "false")
        boolean error,

        @Schema(description = "A message describing the result", example = "Success")
        String message,

        @Schema(description = "Pagination metadata")
        PageMetadata metadata,

        @Schema(description = "The list of items for the current page")
        List<T> data
) {}
