package br.com.pedrolourenco.TradeSim.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Metadata containing pagination information.")
public record PageMetadata(
        @Schema(description = "The current page number", example = "0")
        int pageNumber,

        @Schema(description = "The number of items per page", example = "10")
        int pageSize,

        @Schema(description = "The number of elements in the current page", example = "10")
        int pageElements,

        @Schema(description = "The total number of elements across all pages", example = "100")
        long totalElements,

        @Schema(description = "The total number of pages", example = "10")
        int totalPages,

        @Schema(description = "Whether this is the first page", example = "true")
        boolean isFirst,

        @Schema(description = "Whether this is the last page", example = "false")
        boolean isLast,

        @Schema(description = "Whether there is a next page", example = "true")
        boolean hasNext,

        @Schema(description = "Whether there is a previous page", example = "false")
        boolean hasPrevious
) {
}
