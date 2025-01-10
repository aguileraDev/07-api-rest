package com.training.restaurant.controllers;

import com.training.restaurant.config.exceptions.BadRequestException;
import com.training.restaurant.config.exceptions.ExceptionResponse;
import com.training.restaurant.config.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerControllerTest {

    private ExceptionResponse exceptionResponse;

    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;

    @BeforeEach
    public void setUp() {
        exceptionResponse = new ExceptionResponse();
    }
    @Test
    @DisplayName("Should be able to handle not found exception")
    public void testHandleNotFoundException() {
        NotFoundException notFoundException = new NotFoundException("Error Message");
        ResponseEntity<Void> response = exceptionHandlerController.handleNotFoundException(notFoundException);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should be able to handle bad request exception")
    public void testHandleBadRequestException() {
        BadRequestException badRequestException = new BadRequestException("Error message");
        ResponseEntity<ExceptionResponse> response = exceptionHandlerController.handleBadRequestException(badRequestException);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
}
