package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;
import com.ppc.payroll.utils.EmployeeGroupBy;
import com.ppc.payroll.utils.GroupBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static com.ppc.payroll.EventType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the Payroll application.
 *
 * <p>This test class verifies the functionality of employee salary calculations,
 * event tracking, and payroll-related queries.</p>
 *
 * <p>It includes tests for:</p>
 * <ul>
 *     <li>Finding employees by event types (e.g., onboarding, exit)</li>
 *     <li>Generating salary reports grouped by different criteria (e.g., month, employee ID)</li>
 *     <li>Validating financial transactions such as bonuses, reimbursements, and salaries</li>
 *     <li>Ensuring correct aggregation of payroll-related data</li>
 * </ul>
 *
 * <p>Mocks are used to simulate database interactions via {@link EmployeeRepository}.</p>
 *
 * @author Venkatesh Chakravarti
 * @version 1.0
 *
 */
class PayrollApplicationTests {
	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeBrowser employeeBrowser;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// Helper method to create an Employee
	private Employee createEmployee(String empId, String firstName, String lastName) {
		Employee employee = new Employee();
		employee.setEmpId(empId);
		employee.setfName(firstName);
		employee.setlName(lastName);
		return employee;
	}

	// Helper method to create an Event
	private Event createEvent(String empId, EventType eventType, int year, Month month, int day, int salary) {
		Event event = new Event();
		event.setEmpId(empId);
		event.setEvent(eventType);
		event.setSalary(salary);
		event.setEventDate(LocalDate.of(year, month, day));
		return event;
	}

	@Test
	void testFindEmployeesCount() {
		when(employeeRepository.employeeCount()).thenReturn(5L);

		long count = employeeBrowser.employeeCount();

		assertEquals(5L, count);
	}

	@Test
	void testFindEmployeesJoined() {
		Event event = createEvent("E123", ONBOARD, 2024, Month.JANUARY, 10, 0);
		Employee employee = createEmployee("E123", "John", "Doe");

		Map<String, List<Event>> mockJoinedMap = Map.of(event.getEventDate().getMonth().toString(), List.of(event));

		when(employeeRepository.findEmployeesBy(ONBOARD, GroupBy.MONTH)).thenReturn(mockJoinedMap);
		when(employeeRepository.findEmployeeById("E123")).thenReturn(employee);

		Map<String, List<EmployeeDTO>> result = employeeBrowser.findEmployeesBy(ONBOARD, GroupBy.MONTH);

		assertEquals(1, result.size());
		assertTrue(result.containsKey("JANUARY"));
		assertEquals("E123", result.get("JANUARY").get(0).getEmpId());
	}

	@Test
	void testFindEmployeesExited() {
		Event event = createEvent("E123", EXIT, 2024, Month.OCTOBER, 10, 0);
		Employee employee = createEmployee("E123", "John", "Doe");

		Map<String, List<Event>> mockExitedMap = Map.of(event.getEventDate().getMonth().toString(), List.of(event));

		when(employeeRepository.findEmployeesBy(EXIT, GroupBy.MONTH)).thenReturn(mockExitedMap);
		when(employeeRepository.findEmployeeById("E123")).thenReturn(employee);

		Map<String, List<EmployeeDTO>> result = employeeBrowser.findEmployeesBy(EXIT, GroupBy.MONTH);

		assertEquals(1, result.size());
		assertTrue(result.containsKey("OCTOBER"));
		assertEquals("E123", result.get("OCTOBER").get(0).getEmpId());
	}

	@Test
	void testMonthlySalaryReport() {
		Event event1 = createEvent("E123", SALARY, 2024, Month.FEBRUARY, 10, 4000);
		Event event2 = createEvent("E124", SALARY, 2024, Month.JANUARY, 11, 5000);
		Event event3 = createEvent("E125", SALARY, 2024, Month.JANUARY, 11, 1000);

		Map<String, List<Event>> mockSalaryMap = new HashMap<>();
		mockSalaryMap.put("FEBRUARY", List.of(event1));
		mockSalaryMap.put("JANUARY", Arrays.asList(event2, event3));

		when(employeeRepository.findTotalSalaryBy(Set.of(SALARY), GroupBy.MONTH)).thenReturn(mockSalaryMap);

		Map<String, EmployeeSalarySummary> result = employeeBrowser.findTotalSalaryBy(Set.of(SALARY), GroupBy.MONTH);

		assertEquals(2, result.size());
		assertEquals(6000, result.get("JANUARY").getTotalSalary());
		assertEquals(2, result.get("JANUARY").getTotalEmployees());
	}

	@Test
	void testEmployeeSalaryReport() {
		Event event1 = createEvent("emp101", SALARY, 2024, Month.FEBRUARY, 10, 4000);
		Event event2 = createEvent("emp102", SALARY, 2024, Month.JANUARY, 10, 5000);
		Event event3 = createEvent("emp102", SALARY, 2024, Month.MARCH, 10, 5000);

		Map<String, List<Event>> mockSalaryMap = new HashMap<>();
		mockSalaryMap.put("emp101", List.of(event1));
		mockSalaryMap.put("emp102", List.of(event2, event3));

		Employee employee1 = createEmployee("emp101", "Bill", "Gates");
		Employee employee2 = createEmployee("emp102", "Steve", "Jobs");

		when(employeeRepository.findTotalSalaryBy(Set.of(SALARY), EmployeeGroupBy.EMPID)).thenReturn(mockSalaryMap);
		when(employeeRepository.findEmployeeById("emp101")).thenReturn(employee1);
		when(employeeRepository.findEmployeeById("emp102")).thenReturn(employee2);

		List<EmployeeFinancialReport> employeeFinancialReports = employeeBrowser.findTotalSalaryBy(Set.of(SALARY), EmployeeGroupBy.EMPID);

		assertEquals(2, employeeFinancialReports.size());
		assertEquals(10000L, employeeFinancialReports.stream()
				.filter(emp -> emp.getEmpId().equals("emp102"))
				.mapToLong(EmployeeFinancialReport::getTotalAmountPaid)
				.findFirst().getAsLong()
		);
	}

	@Test
	void testEmployeeTotalExpenditureMonthly() {
		Event salaryEvent = createEvent("emp101", SALARY, 2024, Month.FEBRUARY, 10, 4000);
		Event bonusEvent = createEvent("emp101", BONUS, 2024, Month.FEBRUARY, 25, 1000);
		Event reimbursementEvent = createEvent("emp101", REIMBURSEMENT, 2024, Month.FEBRUARY, 27, 1000);

		Map<String, List<Event>> mockExpenditureMap = Map.of("FEBRUARY", List.of(salaryEvent, bonusEvent, reimbursementEvent));

		when(employeeRepository.findTotalSalaryBy(Set.of(SALARY, BONUS), GroupBy.MONTH)).thenReturn(mockExpenditureMap);

		Map<String, EmployeeSalarySummary> result = employeeBrowser.findTotalExpenditure(Set.of(SALARY, BONUS), GroupBy.MONTH);

		assertEquals(1, result.size());
		assertEquals(6000, result.get("FEBRUARY").getTotalSalary());
	}

	@Test
	void testEmployeeFinancialEventsReport() {
		Event bonusEvent = createEvent("emp101", BONUS, 2024, Month.FEBRUARY, 25, 1000);
		Event reimbursementEvent = createEvent("emp101", REIMBURSEMENT, 2024, Month.FEBRUARY, 27, 1000);

		Map<String, List<Event>> financialReport = Map.of("2024", List.of(bonusEvent, reimbursementEvent));

		when(employeeRepository.events(GroupBy.YEAR)).thenReturn(financialReport);

		Map<String, List<Event>> result = employeeBrowser.events(GroupBy.YEAR);

		assertEquals(1, result.size());
		assertEquals("emp101", financialReport.get("2024").get(0).getEmpId());

	}
}
