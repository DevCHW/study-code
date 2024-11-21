package com.devchw.javaconcurrency.service;

public class AlreadyOrderedProductException extends RuntimeException {
    public AlreadyOrderedProductException(String message) {
        super(message);
    }
}
