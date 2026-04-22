package com.anish.email.handler;

import com.anish.email.dto.ApiResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HandlerMethodValidationException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<Object> handleValidationExceptions(Exception e) {
        log.error("Validation Error: {}", e.getMessage(), e);
        return buildValidationResponse(e);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex) {

        log.error("Missing request part: {}", ex.getRequestPartName(), ex);
        Map<String, List<String>> errorMap = new HashMap<>();
        errorMap.put(ex.getRequestPartName(), List.of("is required"));

        ApiResponseDto response = new ApiResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                formatErrors(errorMap),
                false
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleServerError(Exception e) {
        log.error("Server error: {}", e.getMessage(), e);
        ApiResponseDto response = new ApiResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Failed to queue the email",
                Boolean.FALSE
        );
        return ResponseEntity.internalServerError().body(response);
    }


    private Map<String, List<String>> extractErrors(Exception ex) {
        Map<String, List<String>> errorMap = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException e) {
            e.getBindingResult().getFieldErrors().forEach(error ->
                    errorMap
                            .computeIfAbsent(error.getField(), k -> new ArrayList<>())
                            .add(error.getDefaultMessage())
            );
        } else if (ex instanceof HandlerMethodValidationException e) {
            e.getParameterValidationResults().forEach(paramResult ->
                    paramResult.getResolvableErrors().forEach(error -> {
                        String fieldName;
                        if (error instanceof FieldError fieldError) {
                            fieldName = fieldError.getField();
                        } else {
                            fieldName = paramResult.getMethodParameter().getParameterName();
                        }
                        errorMap
                                .computeIfAbsent(fieldName, k -> new ArrayList<>())
                                .add(error.getDefaultMessage());
                    })
            );

        } else if (ex instanceof ConstraintViolationException e) {
            e.getConstraintViolations().forEach(violation -> {
                String field = violation.getPropertyPath().toString();
                if (field.contains(".")) {
                    field = field.substring(field.lastIndexOf('.') + 1);
                }
                errorMap
                        .computeIfAbsent(field, k -> new ArrayList<>())
                        .add(violation.getMessage());
            });
        }

        return errorMap;
    }

    private ResponseEntity<Object> buildValidationResponse(Exception ex) {
        Map<String, List<String>> errorMap = extractErrors(ex);
        ApiResponseDto response = new ApiResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                formatErrors(errorMap),
                false
        );
        return ResponseEntity.badRequest().body(response);
    }

    private String formatErrors(Map<String, List<String>> errorMap) {
        StringBuilder errors = new StringBuilder();
        errorMap.forEach((field, messages) -> {
            for (String message : messages) {
                errors.append(field)
                        .append(" : ")
                        .append(message)
                        .append("; ");
            }
        });
        return errors.toString();
    }
}
