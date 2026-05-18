package br.com.pedrolourenco.TradeSim.controller.response;

public record DataListMetadata(
    int pageNumber,
    int pageSize,
    int totalPages,
    int totalElements
) {
}
