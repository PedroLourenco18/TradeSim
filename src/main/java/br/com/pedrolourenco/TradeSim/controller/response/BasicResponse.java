package br.com.pedrolourenco.TradeSim.controller.response;

public record BasicResponse(
        boolean error,
        String message
) {
}
