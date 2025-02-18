package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;
import com.ppc.payroll.utils.EmployeeGroupBy;
import com.ppc.payroll.utils.GroupBy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

public class EmployeeBrowser {
    private final EmployeeRepository employeeRepository;

    public EmployeeBrowser(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Map<String, List<EmployeeDTO>> findEmployeesBy(EventType event, GroupBy groupBy){
        Map<String, List<Event>> employeesBy = employeeRepository.findEmployeesBy(event, groupBy);

        Map<String, List<EmployeeDTO>> res = new HashMap<>();
        for (Map.Entry<String, List<Event>> entry : employeesBy.entrySet()) {
            String month = entry.getKey();
            List<Event> events = entry.getValue();
            List<EmployeeDTO> employeeDTOS = new ArrayList<>();

            for (Event e : events) {
                Employee emp = employeeRepository.findEmployeeById(e.getEmpId());
                employeeDTOS.add(new EmployeeDTO(emp.getEmpId(),
                        emp.getfName(), emp.getlName()));
            }

            res.put(month, employeeDTOS);
        }

        return res;
    }

    Map<String, EmployeeSalarySummary> findTotalSalaryBy(Set<EventType> eventType, GroupBy groupBy){
        Map<String, List<Event>> computeSalaryReport =
                employeeRepository.findTotalSalaryBy(eventType, groupBy);

        Map<String, EmployeeSalarySummary> monthListHashMap = new HashMap<>();

        for(Map.Entry<String, List<Event>> entry : computeSalaryReport.entrySet()){
            String month = entry.getKey();
            List<Event> events = entry.getValue();

            long employeesCnt = events.size();

            long computedSalary  = events.stream().mapToInt(event -> event.getSalary()).sum();

            EmployeeSalarySummary summary = new EmployeeSalarySummary(computedSalary, employeesCnt);
            monthListHashMap.put(month, summary);

        }

        return monthListHashMap;
    }

    List<EmployeeFinancialReport> findTotalSalaryBy(Set<EventType> eventType, EmployeeGroupBy employeeGroupBy){
        Map<String, List<Event>> financialReport = employeeRepository.findTotalSalaryBy(eventType,  employeeGroupBy);

        List<EmployeeFinancialReport> res = new ArrayList<>();

        for(Map.Entry<String, List<Event>> entry : financialReport.entrySet()){
            String empId = entry.getKey();
            List<Event> events = entry.getValue();

            long computedSalary  = events.stream().mapToInt(event -> event.getSalary()).sum();

            Employee emp = employeeRepository.findEmployeeById(empId);
            EmployeeDTO employeeDTO = new EmployeeDTO(emp.getEmpId(), emp.getfName(), emp.getlName());

            EmployeeFinancialReport employeeFinancialReport = new EmployeeFinancialReport(employeeDTO);
            employeeFinancialReport.setTotalAmountPaid(computedSalary);

            res.add(employeeFinancialReport);

        }

        return res;
    }

    public long employeeCount(){
        return employeeRepository.employeeCount();
    }

    public  Map<String, EmployeeSalarySummary> findTotalExpenditure(Set<EventType> eventType, GroupBy groupBy){
        Map<String, List<Event>> amountExpenditure = employeeRepository
                .findTotalSalaryBy(eventType, groupBy);
        Map<String, EmployeeSalarySummary> res = new HashMap<>();

        for(Map.Entry<String, List<Event>> entry : amountExpenditure.entrySet()){
            String month = entry.getKey();
            List<Event> events = entry.getValue();

            long computedSalary  = events.stream().
                    mapToLong(event -> event.getSalary() + event.getReimbursement() + event.getBonus())
                    .sum();

            EmployeeSalarySummary salarySummary = new EmployeeSalarySummary(computedSalary, events.size());

            res.put(month, salarySummary);

        }

        return res;

    }

    Map<String, List<Event>> events(GroupBy groupBy){
       return employeeRepository.events(groupBy);
    }

}


