package com.maveric.demo.exception;

public class CustomFeignException extends RuntimeException {
    public CustomFeignException(String s) {
        super(s);

    }
}