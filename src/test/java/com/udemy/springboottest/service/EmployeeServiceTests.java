package com.udemy.springboottest.service;

import com.udemy.springboottest.exception.ResourceNotFoundException;
import com.udemy.springboottest.model.Employee;
import com.udemy.springboottest.repository.EmployeeRepository;
import com.udemy.springboottest.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Fuad")
                .lastName("Aliyev")
                .email("fuad@gmail.com")
                .build();
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    //Junit test for saveEmployee method
    @DisplayName("Junit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test for saveEmployee method which throws exception
    @DisplayName("Junit test for saveEmployee method which throws exception")
    @Test
    public void givenExistentEmail_whenSaveEmployee_thenThrowsException() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        //when - action or the behaviour that we are going to test
        Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));
        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    //Junit test for get all employees
    @DisplayName("Junit test for get all employees")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Fuad")
                .lastName("Aliyev")
                .email("fuad@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(Arrays.asList(employee, employee1));
        //when - action or the behaviour that we are going to test
        java.util.List<Employee> allEmployees = employeeService.getAllEmployees();
        //then - verify the output
        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees.size()).isEqualTo(2);
    }

    //Junit test for get all employees (negative scenario)
    @DisplayName("Junit test for get all employees (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //when - action or the behaviour that we are going to test
        java.util.List<Employee> allEmployees = employeeService.getAllEmployees();
        //then - verify the output
        assertThat(allEmployees).isEmpty();
        assertThat(allEmployees.size()).isEqualTo(0);
    }

    //Junit test for get employee by id
    @DisplayName("Junit test for get employee by id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.getEmployeeById(1L).get();
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test for update employee
    @DisplayName("Junit test for update employee")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("aliyev@gmail.com");
        //when - action or the behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("aliyev@gmail.com");
    }

    //Junit test for delete employee
    @DisplayName("Junit test for delete employee")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        //given - precondition or setup
        Long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);
        //when - action or the behaviour that we are going to test
        employeeService.deleteEmployee(employeeId);
        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
