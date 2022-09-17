package com.junit.examples.junit.controller;

import static org.hamcrest.CoreMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.examples.junit.model.Employee;
import com.junit.examples.junit.service.EmployeeService;

@WebMvcTest
public class EmployeeControllerTests {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	EmployeeService employeeService;
	
	@Autowired
	ObjectMapper mapper; 
	
	@Test
	public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws JsonProcessingException, Exception {
		
		//given
		Employee employee = Employee.builder().firstName("Manoj").lastName("Singh").email("mymail@gmail.com").build();
		BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
		.willAnswer((invocation)->invocation.getArgument(0));
		
		// when
		ResultActions response =mockMvc.perform(MockMvcRequestBuilders.post("/api/employees").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(employee)));
	
		// then
		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
			.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())));
		
	}
	
	@Test
	public void givenEmployeeObject_whenfindAllEmployee_thenReturnAllEmployee() throws JsonProcessingException, Exception {
		
		//given 
		List<Employee> employeeList = new ArrayList<Employee>();
		Employee employee = Employee.builder().firstName("Manoj").lastName("Singh").email("mymail@gmail.com").build();
		Employee employee1 = Employee.builder().firstName("Tony").lastName("Kapida").email("tonymail@gmail.com").build();
		employeeList.add(employee);
		employeeList.add(employee1);
		
		BDDMockito.given(employeeService.getAllEmployees()).willReturn(employeeList);
		
		// when 
		
		ResultActions response  = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));
	
		// then
		response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
	}
	
	@Test
	public void giveEmployeeId_whenEmployeeFetch_returnEmployeObject() throws Exception{
		
		// given
			Employee employee = Employee.builder().id(1001L).firstName("Manoj").lastName("Singh").email("mymail@gmail.com").build();
			BDDMockito.given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
		// when
			ResultActions response =mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employee.getId()));
		// then
			response.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
			.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())));
			
	}
	
	@Test
	public void giveEmployeeId_whenEmployeeFetch_returnEmployeObjectNotFound() throws Exception{
		
		// given
			Employee employee = Employee.builder().id(1001L).firstName("Manoj").lastName("Singh").email("mymail@gmail.com").build();
			BDDMockito.given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.empty());
		// when
			ResultActions response =mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employee.getId()));
		// then
			response.andExpect(MockMvcResultMatchers.status().isNotFound());
			
	}
	
	
	@Test
	public void giveEmployeeId_whenEmployeeUpdate_returnEmployeObject() throws Exception{
		
		// given
			Employee employee = Employee.builder().id(1001L).firstName("Manoj").lastName("Singh").email("mymail@gmail.com").build();
			Employee employee1 = Employee.builder().id(1001L).firstName("Akshita").lastName("Singh").email("akshita@gmail.com").build();
			
			BDDMockito.given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
			
			BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
			.willAnswer((invocation)->invocation.getArgument(0));
		// when
			ResultActions response =mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employee.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(employee1)));
			
		// then
			response.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee1.getFirstName())));

			
	}
	
	@Test
	public void giveEmployeeId_whenEmployeeUpdate_returnEmployeNotFound() throws Exception{
		
		// given
			Employee employee = Employee.builder().id(1001L).firstName("Manoj").lastName("Singh").email("mymail@gmail.com").build();
			Employee employee1 = Employee.builder().id(1002L).firstName("Akshita").lastName("Singh").email("akshita@gmail.com").build();
			
			BDDMockito.given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
			
			BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
			.willAnswer((invocation)->invocation.getArgument(0));
		// when
			ResultActions response =mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employee1.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(employee1)));
			
		// then
			response.andExpect(MockMvcResultMatchers.status().isNotFound());

			
	}
	
	@Test
	public void giveEmployeeId_whenEmployeeDelete_returnSuccess() throws Exception{
		
		// given
			BDDMockito.willDoNothing().given(employeeService).deleteEmployee(1001L);
		// when
			ResultActions response =mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", 1001L));
			
		// then
			response.andExpect(MockMvcResultMatchers.status().isOk());

			
	}
}
