package com.training.restaurant.controllers;

import com.training.restaurant.dto.customer.CreateCustomerDto;
import com.training.restaurant.dto.customer.CustomerDto;
import com.training.restaurant.dto.customer.UpdateCustomerDto;
import com.training.restaurant.models.Customer;
import com.training.restaurant.services.customer.CustomerServiceImpl;
import com.training.restaurant.utils.CustomerConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerControllerTest {

    private CreateCustomerDto createCustomerDto;
    private CustomerDto customerDto;
    private Customer customer;
    private List<Customer> customersList;
    private UpdateCustomerDto updateCustomerDto;

    private final WebTestClient webTestClient;
    private final CustomerServiceImpl customerService;

    public CustomerControllerTest(){
        customerService = mock(CustomerServiceImpl.class);
        webTestClient = WebTestClient.bindToController(new CustomerController(customerService)).build();
    }

    @BeforeEach
    void setUp() {
        createCustomerDto = new CreateCustomerDto("Pedro perez",
                "pedrop@gmail.com",
                23,
                "1234567890",
                "Calle luna calle sol 123 Caracas");

        customer = CustomerConverter.toCreateACustomer(createCustomerDto);
        customerDto = CustomerConverter.toCustomerDto(customer);

        customersList = List.of(
                CustomerConverter.toCreateACustomer(new CreateCustomerDto("Juan Martinez", "juanmartinez@example.com", 28, "3123456789", "Avenida Siempre Viva 742 Bogota")),
                CustomerConverter.toCreateACustomer(new CreateCustomerDto("Maria Lopez", "marial@gmail.com", 30, "0987654321", "Calle real 456 Valencia")),
                CustomerConverter.toCreateACustomer(new CreateCustomerDto("Ana Torres", "anatorres@example.com", 22, "3156789012", "Diagonal 23 #56-78 Barranquilla")),
                CustomerConverter.toCreateACustomer(new CreateCustomerDto("Laura Gomez", "lauragomez@example.com", 35, "3209876543", "Calle del Sol 123 Medellin")),
                CustomerConverter.toCreateACustomer(new CreateCustomerDto("Carlos Rivera", "carlosrivera@example.com", 40, "3001234567", "Carrera 45 #12-34 Cali"))

        );

        updateCustomerDto = new UpdateCustomerDto("Pedro perez","pedrop@gmail.com","frecuente",25,"1234567890","Calle luna calle sol 123 Caracas");
    }
    @Test
    @DisplayName("Should be able to create a customer")
    void createClient() {

        when(customerService.createCustomer(any(CreateCustomerDto.class))).thenReturn(customer);

        webTestClient
                .post()
                .uri("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createCustomerDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CustomerDto.class)
                .value(customerDto1 -> {
                    assertEquals(customerDto.name(), customerDto1.name());
                    assertEquals(customerDto.email(), customerDto1.email());
                    assertEquals(customerDto.type(), customerDto1.type());
                    assertEquals(customerDto.phone(), customerDto1.phone());
                });

        Mockito.verify(customerService).createCustomer(any(CreateCustomerDto.class));

    }

    @Test
    @DisplayName("Should be get all customers")
    void findAllClients() {

        when(customerService.findAllCustomers()).thenReturn(customersList);

        webTestClient
                .get()
                .uri("/customer")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerDto.class)
                .hasSize(5)
                .value(customerDtos -> {
                    assertEquals(5, customerDtos.size());
                    assertEquals(customersList.get(0).getName(), customerDtos.get(0).name());
                    assertEquals(customersList.get(0).getEmail(), customerDtos.get(0).email());
                    assertEquals(customersList.get(0).getType().typeToString(), customerDtos.get(0).type());
                    assertEquals(customersList.get(0).getPhone(), customerDtos.get(0).phone());
                });

        Mockito.verify(customerService).findAllCustomers();
    }

    @Test
    @DisplayName("Should be find a customer by id")
    void findClientById() {

        when(customerService.findCustomerById(anyLong())).thenReturn(customer);

        webTestClient
                .get()
                .uri("/customer/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDto.class)
                .value( customerDto1 -> {
                    assertEquals(customerDto.id(), customerDto1.id());
                    assertEquals(customerDto.name(), customerDto1.name());
                    assertEquals(customerDto.email(), customerDto1.email());
                    assertEquals(customerDto.type(), customerDto1.type());
                    assertEquals(customerDto.phone(), customerDto1.phone());
                });

        Mockito.verify(customerService).findCustomerById(anyLong());
    }

    @Test
    @DisplayName("Should be update a customer")
    void updateCustomer() {

        doNothing().when(customerService).updateCustomer(anyLong(), any(UpdateCustomerDto.class));

        webTestClient
                .put()
                .uri("/customer/{id}",1L)
                .bodyValue(updateCustomerDto)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Should be delete a customer")
    void deleteCustomer() {

        doNothing().when(customerService).removeCustomer(anyLong());

        webTestClient
                .delete()
                .uri("/customer/{id}",1L)
                .exchange()
                .expectStatus().isNoContent();
    }
}