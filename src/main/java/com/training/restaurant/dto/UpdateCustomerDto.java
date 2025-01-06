package com.training.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

public record UpdateCustomerDto(
        @JsonAlias("client_name")
        String name,
        @Email
        @JsonAlias("client_email")
        String email,
        @JsonAlias("client_type")
        String type,
        @Positive
        @JsonAlias("client_age")
        Integer age,
        @JsonAlias("client_phone")
        String phone,
        @JsonAlias("client_address")
        String address
) {
}
