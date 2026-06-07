package br.com.pedrolourenco.TradeSim.controller.response;

import java.util.List;

public record PagedDataResponse<T> (
        boolean error,
        String message,
        PageMetadata metadata,
        List<T> data
) {}