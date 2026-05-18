package br.com.pedrolourenco.TradeSim.controller.response;

public record AuthResponse(
        boolean error,
        String token
) {
}
