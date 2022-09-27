package com.udemy.springboottest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.springboottest.model.Employee;
import com.udemy.springboottest.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean //spring create mock instance and add to application context so app context injects it to controller
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Fuad")
                .lastName("Aliyev")
                .email("fuad@gmail.com")
                .build();
    }

    //Junit test for create employee operation
    @DisplayName("Junit test for create employee operation")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                .post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
        );
        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    //Junit test for get all employees operation
    @DisplayName("unit test for get all employees operation")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given - precondition or setupE
        Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Johnson")
                .email("john@gmail.com")
                .build();
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        employeeList.add(employee1);
        given(employeeService.getAllEmployees()).willReturn(employeeList);
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                .get("/api/employees")
        );
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
    }

    //Junit test for get employee by id operation (positive scenario)
    @DisplayName("Junit test for get employee by id operation (positive scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        Long employeeId = 1L;
        //given - precondition or setup
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                .get("/api/employees/{id}", employeeId)
        );
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    //Junit test for get employee by id operation (negative scenario)
    @DisplayName("Junit test for get employee by id operation (negative scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        Long employeeId = 1L;
        //given - precondition or setup
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/employees/{id}", employeeId)
        );
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    //Junit test for update employee (positive scenario)
    @DisplayName("Junit test for update employee (positive scenario)")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given - precondition or setup
        Long employeeId = 1L;
        Employee updatedEmployee =  Employee.builder()
                .firstName("John")
                .lastName("Johnson")
                .email("john@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
        );
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));
    }

    //Junit test for update employee (negative scenario)
    @DisplayName("Junit test for update employee (negative scenario)")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception {
        //given - precondition or setup
        Long employeeId = 1L;
        Employee updatedEmployee =  Employee.builder()
                .firstName("John")
                .lastName("Johnson")
                .email("john@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
        );
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    //Junit test for delete employee
    @DisplayName("Junit test for delete employee")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnSuccessMessage() throws Exception {
        //given - precondition or setup
        Long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", employeeId));
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
