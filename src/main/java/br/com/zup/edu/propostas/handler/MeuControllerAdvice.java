package br.com.zup.edu.propostas.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.*;

@RestControllerAdvice
public class MeuControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<String> erros = fieldErrors.stream()
                .map(erro ->
                        String.format("O campo %s %s", erro.getField(), erro.getDefaultMessage())
                ).collect(Collectors.toList());


        return ResponseEntity.badRequest().body(erros);
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusException(ResponseStatusException ex) {


        Map<String, String> response = Map.of("erro", requireNonNull(ex.getReason()));

        return ResponseEntity.unprocessableEntity().body(response);
    }


}
