package br.com.pedrolourenco.TradeSim.exception;

public class UnprocessableDataException extends RuntimeException {
    public UnprocessableDataException(String message) {
        super(message);
    }
}
