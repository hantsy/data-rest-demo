package com.example.demo;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super(String.format("post: %s not found", id));
    }
}
