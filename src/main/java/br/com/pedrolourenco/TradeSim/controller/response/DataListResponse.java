package br.com.pedrolourenco.TradeSim.controller.response;

import org.springframework.data.domain.Page;

public record DataListResponse<T>(
        boolean error,
        DataListMetadata metadata,
        Page<T> page
) {
}
