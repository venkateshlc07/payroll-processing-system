package com.ppc.payroll.repository;

import com.ppc.payroll.Employee;
import com.ppc.payroll.Event;
import com.ppc.payroll.eventType;
import com.ppc.payroll.utils.GroupBy;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface EmployeeRepository {
    void store(String[] record);
    void print();
    long employeeCount();
    Map<String, List<Event>> findEmployeesBy(eventType event, GroupBy groupBy);
    Employee findEmployeeById(String userId);
    List<Employee> findAllEmployees();
    Map<Month, List<Event>> computeSalaryReport();
    Map<String, List<Event>> financialReport();
    Map<Month, List<Event>> amountExpenditure();
}
