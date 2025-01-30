package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ppc.payroll.eventType.ONBOARD;
import static com.ppc.payroll.eventType.SALARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class PayrollApplicationTests {
	@Mock
	private EmployeeRepository employeeRepository;  // Mocking the interface

	@InjectMocks
	private EmployeeBrowser employeeBrowser;  // Injecting mock repository into service

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindEmployeesJoined() {
		// Arrange
		Map<Month, List<Event>> mockJoinedMap = new HashMap<>();
		List<Event> events = new ArrayList<>();
		Event event = new Event();
		event.setEmpId("E123");
		event.setEvent(eventType.ONBOARD);
		event.setDoj(LocalDate.of(2023, Month.JANUARY, 10));
		events.add(event);
		mockJoinedMap.put(Month.JANUARY, events);

		Employee employee = new Employee();
		employee.setEmpId("E123");
		employee.setfName("John");
		employee.setlName("Doe");

		when(employeeRepository.findEmployeesJoined()).thenReturn(mockJoinedMap);
		when(employeeRepository.findEmployeeById("E123")).thenReturn(employee);

		// Act
		Map<String, List<EmployeeDTO>> result = employeeBrowser.findEmployeesJoined();

		// Assert
		assertEquals(1, result.size());
		assertTrue(result.containsKey("JANUARY"));
	}

}

