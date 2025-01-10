package com.training.restaurant.config.exceptions;

import org.springframework.stereotype.Component;

@Component
public class ExceptionResponse {

   public Boolean error;
   public String message;

    public ExceptionResponse() {
        this.error = error;
        this.message = message;
    }

    public void restart(){
        error = false;
        message= "";
    }
}
