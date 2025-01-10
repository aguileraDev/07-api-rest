package com.training.restaurant.services.customer;

import com.training.restaurant.dto.customer.CreateCustomerDto;
import com.training.restaurant.dto.customer.UpdateCustomerDto;
import com.training.restaurant.models.Customer;
import com.training.restaurant.models.CustomerType;
import com.training.restaurant.repositories.CustomerRepository;
import com.training.restaurant.utils.CustomerConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CreateCustomerDto createCustomerDto;
    private UpdateCustomerDto updateCustomerDto;

    @BeforeEach
    void setUp() {
        createCustomerDto = new CreateCustomerDto("Ana Torres", "anatorres@example.com", 22, "3156789012", "Diagonal 23 #56-78 Barranquilla");
        customer = CustomerConverter.toCreateACustomer(createCustomerDto);
        updateCustomerDto = new UpdateCustomerDto("Pedro perez", "pedrop@gmail.com", "frecuente", 25, "1234567890", "Calle luna calle sol 123 Caracas");
    }

    @Test
    @DisplayName("Should be able to create a customer")
    void createCustomer() {


        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer customerDb = customerService.createCustomer(createCustomerDto);

        assertNotNull(customerDb);
        assertEquals(customer.getName(),customerDb.getName());
        assertEquals(customer.getEmail(),customerDb.getEmail());

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should be able to get a list of customers")
    void findAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> customers = customerService.findAllCustomers();

        assertNotNull(customers);
        assertEquals(1,customers.size());
        assertEquals(customer.getName(),customers.get(0).getName());
        assertEquals(customer.getEmail(),customers.get(0).getEmail());

        verify(customerRepository).findAll();
    }

    @Test
    @DisplayName("Should be able to save a customer")
    void saveCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer customerDb = customerService.saveCustomer(customer);

        assertNotNull(customerDb);
        assertEquals(customer.getName(),customerDb.getName());
        assertEquals(customer.getEmail(),customerDb.getEmail());

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should be able to find a customer by id")
    void findCustomerById() {
        when(customerRepository.findByIdAndIsActiveTrue(anyLong())).thenReturn(Optional.of(customer));

        Customer customerDb = customerService.findCustomerById(1L);

        assertNotNull(customerDb);
        assertEquals(customer.getName(),customerDb.getName());
        assertEquals(customer.getEmail(),customerDb.getEmail());

        verify(customerRepository).findByIdAndIsActiveTrue(anyLong());
    }

    @Test
    @DisplayName("Should be able to remove a customer")
    void removeCustomer() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.removeCustomer(1L);

        verify(customerRepository).findById(anyLong());
        verify(customerRepository).save(any(Customer.class));

    }


    @Test
    @DisplayName("Should be able to update a customer")
    void updateCustomer() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.updateCustomer(1L, updateCustomerDto);

        assertEquals(updateCustomerDto.name(), customer.getName());
        assertEquals(updateCustomerDto.email(), customer.getEmail());
        assertEquals(updateCustomerDto.type(), customer.getType().typeToString());
        assertEquals(updateCustomerDto.age(), customer.getAge());
        assertEquals(updateCustomerDto.phone(), customer.getPhone());
        assertEquals(updateCustomerDto.address(), customer.getAddress());

        verify(customerRepository).findById(anyLong());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should update all customer fields when all fields are provided")
    void updateCustomerFields_ShouldUpdateAllFields() {
        customerService.updateCustomerFields(customer, updateCustomerDto);

        assertEquals(updateCustomerDto.name(), customer.getName());
        assertEquals(updateCustomerDto.email(), customer.getEmail());
        assertEquals(updateCustomerDto.type(), customer.getType().typeToString());
        assertEquals(updateCustomerDto.age(), customer.getAge());
        assertEquals(updateCustomerDto.phone(), customer.getPhone());
        assertEquals(updateCustomerDto.address(), customer.getAddress());
    }

    @Test
    @DisplayName("Should update customer name when name is provided")
    void updateNameIfPresent_ShouldUpdateName() {
        String newName = "Laura García";

        customerService.updateNameIfPresent(customer, newName);

        assertEquals(newName, customer.getName());
    }

    @Test
    @DisplayName("Should not update customer name when name is null")
    void updateNameIfPresent_ShouldNotUpdateNameWhenNull() {
        customerService.updateNameIfPresent(customer, null);

        assertEquals(createCustomerDto.name(), customer.getName());
    }

    @Test
    @DisplayName("Should update customer email when email is provided")
    void updateEmailIfPresent_ShouldUpdateEmail() {
        String newEmail = "lauragarcia@example.com";

        customerService.updateEmailIfPresent(customer, newEmail);

        assertEquals(newEmail, customer.getEmail());
    }

    @Test
    @DisplayName("Should not update customer email when email is null")
    void updateEmailIfPresent_ShouldNotUpdateEmailWhenNull() {
        customerService.updateEmailIfPresent(customer, null);

        assertEquals(createCustomerDto.email(), customer.getEmail());
    }

    @Test
    @DisplayName("Should update customer type when type is provided")
    void updateTypeIfPresent_ShouldUpdateType() {
        String newType = "frecuente";

        customerService.updateTypeIfPresent(customer, newType);

        assertEquals(CustomerType.fromString(newType), customer.getType());
    }

    @Test
    @DisplayName("Should update customer phone when phone is provided")
    void updatePhoneIfPresent_ShouldUpdatePhone() {
        String newPhone = "9876543210";

        customerService.updatePhoneIfPresent(customer, newPhone);

        assertEquals(newPhone, customer.getPhone());
    }

    @Test
    @DisplayName("Should update customer address when address is provided")
    void updateAddressIfPresent_ShouldUpdateAddress() {
        String newAddress = "Av. Bellavista";

        customerService.updateAddressIfPresent(customer, newAddress);

        assertEquals(newAddress, customer.getAddress());
    }

    @Test
    @DisplayName("Should not update customer address when address is null")
    void updateAddressIfPresent_ShouldNotUpdateAddressWhenNull() {
        customerService.updateAddressIfPresent(customer, null);

        assertEquals(createCustomerDto.address(), customer.getAddress());
    }

}