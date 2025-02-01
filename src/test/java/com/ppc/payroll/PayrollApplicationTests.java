package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;
import com.ppc.payroll.utils.EmployeeGroupBy;
import com.ppc.payroll.utils.GroupBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ppc.payroll.EventType.*;
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

	@Test
	void testMonthlySalaryReport(){
		Map<String, List<Event>> mockJoinedMap = new HashMap<>();

		List<Event> events = new ArrayList<>();
		Event event = new Event();
		event.setEmpId("E123");
		event.setEvent(SALARY);
		event.setSalary(4000);
		event.setEventDate(LocalDate.of(2024, Month.FEBRUARY, 10));
		events.add(event);
		mockJoinedMap.put(event.getEventDate().getMonth().toString(), events);

		List<Event> events2 = new ArrayList<>();
		Event event2 = new Event();
		event2.setEmpId("E124");
		event2.setEvent(SALARY);
		event2.setSalary(5000);
		event2.setEventDate(LocalDate.of(2024, Month.JANUARY, 11));
		events2.add(event2);
		mockJoinedMap.put(event2.getEventDate().getMonth().toString(), events2);

		Event event3 = new Event();
		event3.setEmpId("E125");
		event3.setEvent(SALARY);
		event3.setSalary(1000);
		event3.setEventDate(LocalDate.of(2024, Month.JANUARY, 11));

		mockJoinedMap.get(event3.getEventDate().getMonth().toString()).add(event3);

		when(employeeRepository.findTotalSalaryBy(Set.of(SALARY), GroupBy.MONTH)).thenReturn(mockJoinedMap);

		Map<String, EmployeeSalarySummary> result = employeeBrowser.findTotalSalaryBy(Set.of(SALARY), GroupBy.MONTH);

		assertEquals(2, result.size());
		assertEquals(6000, result.get("JANUARY").getTotalSalary());
		assertEquals(2, result.get("JANUARY").getTotalEmployees());
	}

	@Test
	void testEmployeeSalaryReport() {
		Map<String, List<Event>> mockJoinedMap = new HashMap<>();

		List<Event> events = new ArrayList<>();
		Employee employee1 = new Employee();
		employee1.setEmpId("emp101");
		employee1.setfName("Bill");
		employee1.setlName("Gates");

		Event event = new Event();
		event.setEmpId("emp101");
		event.setEvent(SALARY);
		event.setSalary(4000);
		event.setEventDate(LocalDate.of(2024, Month.FEBRUARY, 10));
		events.add(event);
		mockJoinedMap.put(event.getEmpId(), events);

		List<Event> events2 = new ArrayList<>();
		Employee employee2 = new Employee();
		employee2.setEmpId("emp102");
		employee2.setfName("Steve");
		employee2.setlName("Jobs");

		Event event2 = new Event();
		event2.setEmpId("emp102");
		event2.setEvent(SALARY);
		event2.setSalary(5000);
		event2.setEventDate(LocalDate.of(2024, Month.JANUARY, 10));
		events2.add(event2);


		Event event3 = new Event();
		event3.setEmpId("emp102");
		event3.setEvent(SALARY);
		event3.setSalary(5000);
		event3.setEventDate(LocalDate.of(2024, Month.MARCH, 10));
		events2.add(event3);
		mockJoinedMap.put(event2.getEmpId(), events2);


		when(employeeRepository.findTotalSalaryBy(Set.of(SALARY), EmployeeGroupBy.EMPID)).thenReturn(mockJoinedMap);
		when(employeeRepository.findEmployeeById("emp101")).thenReturn(employee1);
		when(employeeRepository.findEmployeeById("emp102")).thenReturn(employee2);

		List<EmployeeFinancialReport> employeeFinancialReports = employeeBrowser.findTotalSalaryBy(Set.of(SALARY), EmployeeGroupBy.EMPID);

		assertEquals(2, employeeFinancialReports.size());
		assertEquals(10000L, employeeFinancialReports.stream()
				.filter(emp -> emp.getEmpId().equals("emp102"))  // Filter employees with empId "emp102"
				.mapToLong(EmployeeFinancialReport::getTotalAmountPaid)  // Extract total amount paid
				.findFirst().getAsLong()
		);


	}

	@Test
	void testEmployeeTotalExpenditureMonthly() {
		Map<String, List<Event>> mockJoinedMap = new HashMap<>();

		List<Event> events = new ArrayList<>();
		Employee employee1 = new Employee();
		employee1.setEmpId("emp101");
		employee1.setfName("Bill");
		employee1.setlName("Gates");

		Event event = new Event();
		event.setEmpId("emp101");
		event.setEvent(SALARY);
		event.setSalary(4000);
		event.setEventDate(LocalDate.of(2024, Month.FEBRUARY, 10));
		events.add(event);

		Event eventBill = new Event();
		eventBill.setEmpId("emp101");
		eventBill.setEvent(BONUS);
		eventBill.setSalary(1000);
		eventBill.setEventDate(LocalDate.of(2024, Month.FEBRUARY, 25));
		events.add(eventBill);

		Event reimbBill = new Event();
		reimbBill.setEmpId("emp101");
		reimbBill.setEvent(REIMBURSEMENT);
		reimbBill.setSalary(1000);
		reimbBill.setEventDate(LocalDate.of(2024, Month.FEBRUARY, 27));
		events.add(reimbBill);

		mockJoinedMap.put(eventBill.getEventDate().getMonth().toString(), events);

		when(employeeRepository.findTotalSalaryBy(Set.of(SALARY, BONUS), GroupBy.MONTH)).thenReturn(mockJoinedMap);

		Map<String, EmployeeSalarySummary> employeeSalarySummaryMap = employeeBrowser.findTotalExpenditure(Set.of(SALARY, BONUS), GroupBy.MONTH);

		assertEquals(1, employeeSalarySummaryMap.size());
		assertEquals(6000, employeeSalarySummaryMap.get("FEBRUARY").getTotalSalary());

	}



}

