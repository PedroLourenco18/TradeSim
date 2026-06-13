package br.com.pedrolourenco.TradeSim.controller;

import br.com.pedrolourenco.TradeSim.controller.response.BasicResponse;
import br.com.pedrolourenco.TradeSim.controller.response.FieldsErrorResponse;
import br.com.pedrolourenco.TradeSim.exception.ConflictDataException;
import br.com.pedrolourenco.TradeSim.exception.InternalErrorException;
import br.com.pedrolourenco.TradeSim.exception.ResourceNotFoundException;
import br.com.pedrolourenco.TradeSim.exception.UnprocessableDataException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(ConflictDataException.class)
    public ResponseEntity<BasicResponse> handleConflictDataException(ConflictDataException e){
        BasicResponse response = new BasicResponse(
                true,
                e.getMessage()
        );

        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BasicResponse> handleResourceNotFoundException(ResourceNotFoundException e){
        BasicResponse response = new BasicResponse(
                true,
                e.getMessage()
        );

        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(UnprocessableDataException.class)
    public ResponseEntity<BasicResponse> handleUnprocessableDataException(UnprocessableDataException e){
        BasicResponse response = new BasicResponse(
                true,
                e.getMessage()
        );

        return ResponseEntity.status(422).body(response);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<BasicResponse> handleInternalErrorException(InternalErrorException e){
        BasicResponse response = new BasicResponse(
                true,
                e.getMessage()
        );

        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldsErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e){
        List<FieldError> fieldErrors     = e.getFieldErrors();
        var fields = fieldErrors
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError ->
                                fieldError.getDefaultMessage() == null ? "" : fieldError.getDefaultMessage(),
                                (msg1, msg2) -> msg1
                ));

        FieldsErrorResponse response = new FieldsErrorResponse(
                true,
                "Campos Inválidos",
                fields
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BasicResponse> handleNotReadableException(HttpMessageNotReadableException e) {
        BasicResponse response = new BasicResponse(
                true,
                "Formato de requisição inválido"
        );

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BasicResponse> handleBadCredentialsException(
            BadCredentialsException e) {
        BasicResponse response = new BasicResponse(
                true,
                "Usuario não autenticado"
        );

        return ResponseEntity.status(401).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicResponse> handleException(Exception e){
        BasicResponse response = new BasicResponse(
                true,
                "Houve um erro interno"
        );

        return ResponseEntity.status(500).body(response);
    }
}
