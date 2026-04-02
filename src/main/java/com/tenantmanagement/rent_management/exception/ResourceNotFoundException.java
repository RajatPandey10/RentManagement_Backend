package com.tenantmanagement.rent_management.exception;

public class ResourceNotFoundException extends ResourceExistsException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
