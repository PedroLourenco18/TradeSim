package br.com.pedrolourenco.TradeSim.controller.response;

public record PageMetadata(
        int pageNumber,
        int pageSize,
        int pageElements,
        long totalElements,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious
) {
}
