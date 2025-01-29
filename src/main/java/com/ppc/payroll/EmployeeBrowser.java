package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeBrowser {
    private final EmployeeRepository employeeRepository;

    public EmployeeBrowser(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Map<String, List<EmployeeDTO>> findEmployeesJoined() {
        Map<Month, List<Event>> employeesJoined = employeeRepository.findEmployeesJoined();
        List<Employee> employees = employeeRepository.findAllEmployees();

        Map<String, List<EmployeeDTO>> res = new HashMap<>();
        for (Map.Entry<Month, List<Event>> entry : employeesJoined.entrySet()) {
            Month month = entry.getKey();
            List<Event> events = entry.getValue();
            List<EmployeeDTO> employeeDTOS = new ArrayList<>();

            for (Event event : events) {
                Employee emp = employeeRepository.findEmployeeById(event.getEmpId());
                employeeDTOS.add(new EmployeeDTO(emp.getEmpId(),
                        emp.getfName(), emp.getlName()));
            }

            res.put(month.name(), employeeDTOS);
        }

        return res;
    }

    public Map<String, List<EmployeeDTO>> findEmployeesExited() {

        Map<Month, List<Event>> employeesExited = employeeRepository.findEmployeesExited();
        List<Employee> employees = employeeRepository.findAllEmployees();

        Map<String, List<EmployeeDTO>> res = new HashMap<>();
        for (Map.Entry<Month, List<Event>> entry : employeesExited.entrySet()) {
            Month month = entry.getKey();
            List<Event> events = entry.getValue();
            List<EmployeeDTO> employeeDTOS = new ArrayList<>();

            for (Event event : events) {
                Employee emp = employeeRepository.findEmployeeById(event.getEmpId());
                employeeDTOS.add(new EmployeeDTO(emp.getEmpId(),
                        emp.getfName(), emp.getlName()));
            }

            res.put(month.name(), employeeDTOS);
        }

        return res;
    }
}


