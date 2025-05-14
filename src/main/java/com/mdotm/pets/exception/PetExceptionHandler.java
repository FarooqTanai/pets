package com.mdotm.pets.exception;


import com.mdotm.pets.model.ErrorCode;
import com.mdotm.pets.model.ErrorResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PetExceptionHandler {

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity handleNotFound(PetNotFoundException ex) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(ErrorCode.NOT_FOUND_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleValidationException(ValidationException ex) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(ErrorCode.VALIDATION_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PetAlreadyExistException.class)
    public ResponseEntity handlePetAlreadyExistException(PetAlreadyExistException ex) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(ErrorCode.ALREADY_EXIST_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity handleGenericException(GenericException ex) {
        ErrorResponseBody  errorResponseBody = new ErrorResponseBody(ErrorCode.GENERIC_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
