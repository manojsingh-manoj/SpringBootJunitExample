package com.junit.examples.junit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.junit.examples.junit.model.Employee;

@DataJpaTest
public class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	// Junit test for save employee operation
	@DisplayName("SaveEmployeeTest")
	@Test
	public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
		
		//given -- precondition
		Employee employee = Employee.builder()
				.firstName("Manoj")
				.lastName("Stingh")
				.email("manojsingh.manoj@gmail.com")
				.build();
		
		//when -- action, the behaviour that we are going to test
		Employee employeeSaved = employeeRepository.save(employee);
		
		//then -- verify the output
		assertThat(employeeSaved).isNotNull();
		assertThat(employeeSaved.getId()).isGreaterThan(0);
		
		
	}
	
	@DisplayName("GetEmployeeTest")
	@Test
	public void givenEmployeeObjects_whenGet_thenReturnAllEmployee() {
		
		
		// given
		
		Employee employee1 = Employee.builder()
				.firstName("Akshita")
				.lastName("Singh")
				.email("manojsingh.manoj@gmail.com")
				.build();
		
		Employee employee2 = Employee.builder()
				.firstName("Akshat")
				.lastName("Singh")
				.email("manojsingh.manoj@gmail.com")
				.build();
		
		Employee employee3 = Employee.builder()
				.firstName("Manoj")
				.lastName("Singh")
				.email("manojsingh.manoj@gmail.com")
				.build();
		
		employeeRepository.save(employee1);
		employeeRepository.save(employee2);
		employeeRepository.save(employee3);
		
		// when
		
		List<Employee> employeeList = employeeRepository.findAll();
				
		//then 
		assertThat(employeeList).isNotNull();
		assertThat(employeeList.size()).isEqualTo(3);
		
	}
	@DisplayName("GetEmployeeByIdTest")
	@Test
	public void giveEmployeeId_whenFindEmployeeById_thenReturnEmployee() {
		
		// given
		Employee employee1 = Employee.builder()
				.firstName("Akshita")
				.lastName("Singh")
				.email("manojsingh.manoj@gmail.com")
				.build();
		
			Employee savedEmployee = employeeRepository.save(employee1);
		
		// when
			Optional<Employee> employeeById = employeeRepository.findById(savedEmployee.getId());
		//then
			assertThat(employeeById).isNotNull();
			assertThat(employeeById.get().getFirstName()).isEqualTo("Akshita");
	}
	
	@Test
	public void givenEmployeeObject_whenEmployeeRetreivedByEmail_thenEmployeeReturns() {
		
		// given
		Employee employee1 = Employee.builder()
				.firstName("Akshita")
				.lastName("Singh")
				.email("manojsingh.manoj@gmail.com")
				.build();
		
		employeeRepository.save(employee1);
			
		// when
			Optional<Employee> employeeByEmail= employeeRepository.findByEmail("manojsingh.manoj@gmail.com");
		//then
			assertThat(employeeByEmail).isNotNull();
			assertThat(employeeByEmail.get().getFirstName()).isEqualTo("Akshita");
	}
	
	@Test
	public void givenEmployeeObject_whenUpdateEmail_thenEmployeeReturns() {
		
		// given
		Employee employee1 = Employee.builder()
				.firstName("Akshita")
				.lastName("Singh")
				.email("manojsingh.manoj@gmail.com")
				.build();
		
			Employee savedEmployee = employeeRepository.save(employee1);
			
		// when
			Optional<Employee> employeeByEmailOptional= employeeRepository.findById(savedEmployee.getId());
			Employee employeeSaved  = employeeByEmailOptional.get();
			employeeSaved.setEmail("aakshitasingh.akshita@gmail.com");
			Employee updatedEmployee = employeeRepository.save(employeeSaved);
			
		//then
			assertThat(updatedEmployee).isNotNull();
			assertThat(updatedEmployee.getEmail()).isEqualTo("aakshitasingh.akshita@gmail.com");
	}
	
	@Test
	public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
		
		// given
		Employee employee1 = Employee.builder()
				.firstName("Akshita")
				.lastName("Singh")
				.email("akshita.singh@gmail.com")
				.build();
		
		Employee employee2 = Employee.builder()
				.firstName("Akshat")
				.lastName("Singh")
				.email("manojsingh.manoj@gmail.com")
				.build();
		
		Employee employee3 = Employee.builder()
				.firstName("Manoj")
				.lastName("Singh")
				.email("manojsingh.manoj@gmail.com")
				.build();
		
		employeeRepository.save(employee1);
		employeeRepository.save(employee2);
		employeeRepository.save(employee3);
			
		// when
			employeeRepository.deleteByEmail("akshita.singh@gmail.com");
			Optional<Employee> employeeByEmailOptional= employeeRepository.findByEmail("akshita.singh@gmail.com");
						
		//then
			assertThat(employeeByEmailOptional).isEmpty();
	}
}
