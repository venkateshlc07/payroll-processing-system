package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;
import com.ppc.payroll.utils.GroupBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ppc.payroll.EventType.EXIT;
import static com.ppc.payroll.EventType.ONBOARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class PayrollApplicationTests {
	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeBrowser employeeBrowser;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindEmployeesJoined() {
		// Arrange
		Map<String, List<Event>> mockJoinedMap = new HashMap<>();
		List<Event> events = new ArrayList<>();
		Event event = new Event();
		event.setEmpId("E123");
		event.setEvent(ONBOARD);
		event.setDoj(LocalDate.of(2024, Month.JANUARY, 10));
		events.add(event);
		mockJoinedMap.put(event.getDoj().getMonth().toString(), events);

		Employee employee = new Employee();
		employee.setEmpId("E123");
		employee.setfName("John");
		employee.setlName("Doe");

		when(employeeRepository.findEmployeesBy(ONBOARD, GroupBy.MONTH)).thenReturn(mockJoinedMap);
		when(employeeRepository.findEmployeeById("E123")).thenReturn(employee);

		// Act
		Map<String, List<EmployeeDTO>> result = employeeBrowser.findEmployeesBy(ONBOARD, GroupBy.MONTH);

		// Assert
		assertEquals(1, result.size());
		assertTrue(result.containsKey("JANUARY"));
        assertEquals("E123", result.get("JANUARY").get(0).getEmpId());
	}

	@Test
	void testFindEmployeesExited() {
		// Arrange
		Map<String, List<Event>> mockJoinedMap = new HashMap<>();
		List<Event> events = new ArrayList<>();
		Event event = new Event();
		event.setEmpId("E123");
		event.setEvent(EXIT);
		event.setDol(LocalDate.of(2024, Month.OCTOBER, 10));
		events.add(event);
		mockJoinedMap.put(event.getDol().getMonth().toString(), events);

		Employee employee = new Employee();
		employee.setEmpId("E123");
		employee.setfName("John");
		employee.setlName("Doe");

		when(employeeRepository.findEmployeesBy(EXIT, GroupBy.MONTH)).thenReturn(mockJoinedMap);
		when(employeeRepository.findEmployeeById("E123")).thenReturn(employee);

		// Act
		Map<String, List<EmployeeDTO>> result = employeeBrowser.findEmployeesBy(EXIT, GroupBy.MONTH);

		// Assert
		assertEquals(1, result.size());
		assertTrue(result.containsKey("OCTOBER"));
		assertEquals("E123", result.get("OCTOBER").get(0).getEmpId());
	}

}

