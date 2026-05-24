package com.example.nrcarcenter.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> badRequest(IllegalArgumentException e, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(problem(
                400, "Validation Error", e.getMessage(), req.getRequestURI()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> beanValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        Map<String, Object> p = problem(400, "Validation Error", "Invalid input", req.getRequestURI());
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fe -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));
        p.put("errors", fieldErrors);
        return ResponseEntity.badRequest().body(p);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> server(Exception e, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem(
                500, "Server Error", e.getMessage(), req.getRequestURI()
        ));
    }

    private static Map<String, Object> problem(int status, String title, String detail, String instance) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("timestamp", Instant.now().toString());
        m.put("status", status);
        m.put("title", title);
        m.put("detail", detail);
        m.put("instance", instance);
        return m;
    }
}
