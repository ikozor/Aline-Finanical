package com.aline.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j(topic = "Global Exception Handler")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            NotFoundException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<String> handleNotFoundException(ResponseEntityException e) {
        log.error("404 Not Found: {}", e.toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(ResponseEntityException e) {
        log.error("400 Bad Request: {}", e.toString());
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(ResponseEntityException e) {
        log.error("409 Conflict: {}", e.toString());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(UnprocessableException.class)
    public ResponseEntity<String> handleNotCreatedException(ResponseEntityException e) {
        log.error("422 Unprocessable Entity: {}", e.toString());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(e.getMessage());
    }

    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    public ResponseEntity<String> handleForbiddenException(Exception e) {
        log.error("403 Forbidden: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());

    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(ResponseEntityException e) {
        log.error("401 Unauthorized: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(e.toString());
        List<String> fieldErrors = e.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return ResponseEntity
                .badRequest()
                .body(fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> handleConstraintViolation(ConstraintViolationException e) {
        log.error(e.toString());
        List<String> violations = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return ResponseEntity
                .badRequest()
                .body(violations);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleDefaultException(Exception e) {
        e.printStackTrace();
        log.error(e.toString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Please contact your administrator.");
    }

}
