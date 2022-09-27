package com.udemy.springboottest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.springboottest.model.Employee;
import com.udemy.springboottest.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("fuad")
                .lastName("aliyev")
                .email("aliyev@gmail.com")
                .build();
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

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given - precondition or setupE
        Employee employee = Employee.builder()
                .firstName("fuad")
                .lastName("aliyev")
                .email("aliyev@gmail.com")
                .build();
        Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Johnson")
                .email("john@gmail.com")
                .build();
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        employeeList.add(employee1);
        employeeRepository.saveAll(employeeList);
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

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("fuad")
                .lastName("aliyev")
                .email("aliyev@gmail.com")
                .build();
        employeeRepository.save(employee);
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/employees/{id}", employee.getId())
        );
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("fuad")
                .lastName("aliyev")
                .email("aliyev@gmail.com")
                .build();
        employeeRepository.save(employee);
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/employees/{id}", 100L)
        );
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("fuad")
                .lastName("aliyev")
                .email("aliyev@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee =  Employee.builder()
                .firstName("John")
                .lastName("Johnson")
                .email("john@gmail.com")
                .build();
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/employees/{id}", savedEmployee.getId())
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

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception {
        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("fuad")
                .lastName("aliyev")
                .email("aliyev@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee =  Employee.builder()
                .firstName("John")
                .lastName("Johnson")
                .email("john@gmail.com")
                .build();
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/employees/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
        );
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnSuccessMessage() throws Exception {
        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("fuad")
                .lastName("aliyev")
                .email("aliyev@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);
        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", savedEmployee.getId()));
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
