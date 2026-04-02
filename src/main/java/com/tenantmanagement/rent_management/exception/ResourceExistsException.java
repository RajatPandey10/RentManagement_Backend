package com.tenantmanagement.rent_management.exception;

public class ResourceExistsException extends RuntimeException{

    public ResourceExistsException(String message){
        super(message);
    }

}
