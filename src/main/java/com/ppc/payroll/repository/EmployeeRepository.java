package com.ppc.payroll.repository;

import com.ppc.payroll.Employee;
import com.ppc.payroll.Event;
import com.ppc.payroll.EventType;
import com.ppc.payroll.utils.EmployeeGroupBy;
import com.ppc.payroll.utils.GroupBy;


import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmployeeRepository {
    void store(String[] record);
    long employeeCount();
    Employee findEmployeeById(String userId);
    Map<String, List<Event>> findEmployeesBy(EventType event, GroupBy groupBy);
    List<Employee> findAllEmployees();
    Map<String, List<Event>> findTotalSalaryBy(Set<EventType> eventType, GroupBy groupBy);
    Map<String, List<Event>> findTotalSalaryBy(Set<EventType> eventType, EmployeeGroupBy employeeGroupBy);
    Map<String, List<Event>> events(GroupBy groupBy);
}
