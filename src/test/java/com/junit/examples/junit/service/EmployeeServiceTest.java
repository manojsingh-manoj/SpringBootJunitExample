package com.junit.examples.junit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.junit.examples.junit.exception.ResourceNotFoundException;
import com.junit.examples.junit.model.Employee;
import com.junit.examples.junit.repository.EmployeeRepository;
import com.junit.examples.junit.service.impl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	
	@Mock
	private EmployeeRepository employeeRepository;
	
	@InjectMocks
	private EmployeeServiceImpl employeeService;
	
	private Employee employee;
	
	@BeforeEach
	public void setUp() {
		employee = Employee.builder()
				.id(1001L)
				.firstName("Akshita")
				.lastName("Singh")
				.email("manojsingh.manoj@gmail.com")
				.build();
		//employeeRepository = Mockito.mock(EmployeeRepository.class);
		//employeeService = new EmployeeServiceImpl(employeeRepository);
	}
	
	@Test
	public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployee() {
		
		// given 
		given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
		given(employeeRepository.save(employee)).willReturn(employee);
		// when 
		
		Employee employeeSaved= employeeService.saveEmployee(employee);
		
		// then
		assertThat(employeeSaved).isNotNull();
		assertThat(employeeSaved.getId()).isEqualTo(1001L);
		
	}
	
	@Test
	public void givenEmployeeObject_whenSaveEmployee_thenThrowException() {
	
		// given
			given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
		//	given(employeeRepository.save(employee)).willReturn(employee);
			
		// when
			Assertions.assertThrows(ResourceNotFoundException.class, () -> 	{
				employeeService.saveEmployee(employee);
			});
		
			// then
			verify(employeeRepository, never()).save(any(Employee.class));
	}
	
	@Test
	public void givenEmployeeList_whenFindAllEmployee_thenReturnAllEmployee() {
		
		// given
		given(employeeRepository.findAll()).willReturn(Arrays.asList(employee));
		// when
		List<Employee> employeeList = employeeService.getAllEmployees();
		
		// then
		assertThat(employeeList).isNotNull();
		assertThat(employeeList.size()).isEqualTo(1);
	}
	
	@Test
	public void givenEmployee_whenFindEmployeeById_thenEmployee() {
		
		// given
			
		given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
		// when 
		Optional<Employee>	employeeReturned = employeeService.getEmployeeById(employee.getId());
		// then
		assertThat(employeeReturned).isNotNull();
		assertThat(employeeReturned.get().getFirstName()).isEqualTo("Akshita");
	}
	
	@Test
	public void givenEmployee_whenUpdateEmployee_thenReturnEmployee() {
		
		// given
			
		given(employeeRepository.save(employee)).willReturn(employee);
		employee.setFirstName("manoj");
		// when 
		Employee	employeeReturned = employeeService.updateEmployee(employee);
		// then
		assertThat(employeeReturned).isNotNull();
		assertThat(employeeReturned.getFirstName()).isEqualTo("manoj");
	}
	
	@Test
	public void givenEmployee_whenDeleteEmployee_thenvalidate() {
		
		// given
			
		doNothing().when(employeeRepository).deleteById(1001L);
		//when
		employeeService.deleteEmployee(1001L);
		//then
		verify(employeeRepository,times(1)).deleteById(1001L);
	}
}
