package com.ppc.payroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import com.ppc.payroll.utils.GroupBy;
import com.ppc.payroll.utils.EmployeeGroupBy;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeDBTest {
    private EmployeeDB employeeDB;

    @BeforeEach
    void setUp() {
        employeeDB = new EmployeeDB();
    }

    @Test
    void testStoreNewEmployee() {
        String[] record = {"1", "E001", "John", "Doe", "Engineer", "ONBOARD", "12-10-2022", "12-10-2022", "Joined"};
        employeeDB.store(record);

        assertEquals(1, employeeDB.employeeCount());

    }

    @Test
    void testStoreExistingEmployeeEvent() {
        String[] onboardRecord = {"1", "E002", "Alice", "Smith", "Developer", "ONBOARD", "10-10-2022", "10-10-2022", "Joined"};
        employeeDB.store(onboardRecord);

        String[] bonusRecord = {"2", "E002", "BONUS", "5000", "11-10-2022", "Performance Bonus"};
        employeeDB.store(bonusRecord);

        assertEquals(1, employeeDB.employeeCount());
        Map<String, List<Event>> eventsByEmp = employeeDB.findTotalSalaryBy(Set.of(EventType.BONUS), EmployeeGroupBy.EMPID);
        assertTrue(eventsByEmp.containsKey("E002"));
        assertEquals(1, eventsByEmp.get("E002").size());
        assertEquals(5000, eventsByEmp.get("E002").get(0).getBonus());
    }

    @Test
    void testFindEmployeesByEvent() {
        String[] onboardRecord = {"1", "E003", "Bob", "Brown", "Manager", "ONBOARD", "10-01-2022", "10-01-2022", "Hired"};
        employeeDB.store(onboardRecord);

        Map<String, List<Event>> eventsByYear = employeeDB.findEmployeesBy(EventType.ONBOARD, GroupBy.YEAR);
        assertEquals(1, eventsByYear.get("2022").size());
    }

    @Test
    void testFindTotalSalaryByMonth() {
        String[] onboardRecord = {"1", "E004", "Eve", "Williams", "Analyst", "ONBOARD", "05-05-2021", "05-05-2021", "Started"};
        employeeDB.store(onboardRecord);

        String[] salaryRecord = {"2", "E004", "SALARY", "80000", "06-06-2021", "June Salary"};
        employeeDB.store(salaryRecord);

        Map<String, List<Event>> salaryByMonth = employeeDB.findTotalSalaryBy(Set.of(EventType.SALARY), GroupBy.MONTH);
        assertTrue(salaryByMonth.containsKey("2021-JUNE"));
        assertEquals(80000, salaryByMonth.get("2021-JUNE").get(0).getSalary());
    }

    @Test
    void testFindAllEmployees() {
        String[] record1 = {"1", "E005", "Sam", "Taylor", "HR", "ONBOARD", "10-07-2020", "10-07-2020", "Hired"};
        String[] record2 = {"2", "E006", "Lisa", "White", "Finance", "ONBOARD", "12-08-2020", "12-08-2020", "Joined"};

        employeeDB.store(record1);
        employeeDB.store(record2);

        List<Employee> allEmployees = employeeDB.findAllEmployees();
        assertEquals(2, allEmployees.size());
    }
}
