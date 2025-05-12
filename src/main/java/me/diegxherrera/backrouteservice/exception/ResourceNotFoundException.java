package me.diegxherrera.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, UUID id) {
        super(resource + " with ID " + id + " not found");
    }

    public ResourceNotFoundException(String resource, String identifier) {
        super(resource + " with identifier " + identifier + " not found");
    }
}