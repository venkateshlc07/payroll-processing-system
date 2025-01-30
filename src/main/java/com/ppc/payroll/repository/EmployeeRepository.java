package com.ppc.payroll.repository;

import com.ppc.payroll.Employee;
import com.ppc.payroll.Event;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface EmployeeRepository {
    void store(String[] record);
    void print();
    long employeeCount();
    Map<Month, List<Event>> findEmployeesJoined();
    Map<Month, List<Event>> findEmployeesExited();
    Employee findEmployeeById(String userId);
    List<Employee> findAllEmployees();
    Map<Month, List<Event>> computeSalaryReport();

    Map<String, List<Event>> financialReport();
}
