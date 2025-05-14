package com.mdotm.pets.exception;

public class PetAlreadyExistException extends RuntimeException{
    public PetAlreadyExistException(String message) {
        super(message);
    }
}
