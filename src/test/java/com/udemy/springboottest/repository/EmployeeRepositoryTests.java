package com.udemy.springboottest.repository;

import com.udemy.springboottest.model.Employee;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Fuad")
                .lastName("Aliyev")
                .email("fuad@gmail.com")
                .build();
    }

    //Junit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee()  {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .name("Fuad")
//                .surname("Aliyev")
//                .mail("aliyevfuad@gmail.com")
//                .build();
        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //Junit test for get all employees operation
    @DisplayName("Junit test for get all employees operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .name("Fuad")
//                .surname("Aliyev")
//                .mail("aliyevfuad@gmail.com")
//                .build();

        Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Thomson")
                .email("johnthom@gmail.com")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee1);
        //when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();
        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //Junit test for get employee by id operation
    @DisplayName("Junit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .name("Fuad")
//                .surname("Aliyev")
//                .mail("aliyevfuad@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();
        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //Junit test for get employee by email operation
    @DisplayName("Junit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .name("Fuad")
//                .surname("Aliyev")
//                .mail("aliyevfuad@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();
        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //Junit test for update employee operation
    @DisplayName("Junit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .name("Fuad")
//                .surname("Aliyev")
//                .mail("aliyevfuad@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("fuad@gmail.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);
        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("fuad@gmail.com");
    }

    //Junit test for delete employee operation
    @DisplayName("Junit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployee() {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .name("Fuad")
//                .surname("Aliyev")
//                .mail("aliyevfuad@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or the behaviour that we are going to test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        //then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    //JUnit test for custom query using JPQL with index
    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Fuad")
//                .lastName("Aliyev")
//                .email("fuad@gmail.com")
//                .build();
        employeeRepository.save(employee);
        String fistName = "Fuad";
        String lastName = "Aliyev";
        //when - action or behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQL(fistName, lastName);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using JPQL with named params
    @DisplayName("JUnit test for custom query using JPQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Fuad")
//                .lastName("Aliyev")
//                .email("fuad@gmail.com")
//                .build();
        employeeRepository.save(employee);
        String fistName = "Fuad";
        String lastName = "Aliyev";
        //when - action or behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(fistName, lastName);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using native SQL with index
    @DisplayName("JUnit test for custom query using native SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Fuad")
//                .lastName("Aliyev")
//                .email("fuad@gmail.com")
//                .build();
        employeeRepository.save(employee);
        String fistName = "Fuad";
        String lastName = "Aliyev";
        //when - action or behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQL(fistName, lastName);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using native SQL with named params
    @DisplayName("JUnit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Fuad")
//                .lastName("Aliyev")
//                .email("fuad@gmail.com")
//                .build();
        employeeRepository.save(employee);
        String fistName = "Fuad";
        String lastName = "Aliyev";
        //when - action or behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamed(fistName, lastName);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}
