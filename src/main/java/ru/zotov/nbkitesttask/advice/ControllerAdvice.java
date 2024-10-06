package ru.zotov.nbkitesttask.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.zotov.nbkitesttask.exceptions.EmailAlreadyExistException;
import ru.zotov.nbkitesttask.exceptions.UserNotFoundException;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {EmailAlreadyExistException.class})
    public ResponseEntity<Object> handleEmailAlreadyExistException(EmailAlreadyExistException ex) {
        return ResponseEntity.badRequest().build();
    }
}
