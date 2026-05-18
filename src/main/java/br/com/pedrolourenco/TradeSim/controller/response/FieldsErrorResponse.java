package br.com.pedrolourenco.TradeSim.controller.response;

import java.util.Map;

public record FieldsErrorResponse (
        boolean error,
        String message,
        Map<String, String> fields
) { }
