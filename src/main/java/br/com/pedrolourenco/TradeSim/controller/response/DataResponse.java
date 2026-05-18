package br.com.pedrolourenco.TradeSim.controller.response;

public record DataResponse<T> (
        boolean error,
        String message,
        T data
){}