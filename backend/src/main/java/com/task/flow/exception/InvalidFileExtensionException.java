package com.task.flow.exception;

public class InvalidFileExtensionException extends RuntimeException{
    public InvalidFileExtensionException() {}

    public InvalidFileExtensionException(String message){
        super(message);
    }
}
