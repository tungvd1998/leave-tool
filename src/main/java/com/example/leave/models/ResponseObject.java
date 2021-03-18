package com.example.leave.models;

public class ResponseObject {
    private Object data;
    private String message;
    boolean isSuccess = false;
    public <T> ResponseObject(T obj){
        if(obj != null){
            this.isSuccess = true;
            this.data = obj;
        }
    }

}
