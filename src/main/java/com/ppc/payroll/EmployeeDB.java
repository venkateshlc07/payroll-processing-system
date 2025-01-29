package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDB implements EmployeeRepository {
    private List<Employee> employees;
    private List<Event> events;
    public EmployeeDB(){

        employees = new ArrayList<>();
        events = new ArrayList<>();
    }

    @Override
    public void store(String[] record) {
        System.out.println(Arrays.toString(record));

        Event event = new Event();

        if(!findEmployeeExistsById(record[1].trim())) {
            Employee employee = new Employee();
            employee.setSequenceNo(Integer.parseInt(record[0].trim()));
            employee.setEmpId(record[1].trim());
            employee.setfName(record[2].trim());
            employee.setlName(record[3].trim());
            employee.setDesignation(record[4].trim());

            event.setEmpId(record[1].trim());
            if (record[5].trim().equals(eventType.ONBOARD.toString())) {
                event.setEvent(eventType.ONBOARD);
                String[] date = record[6].trim().split("-");

                event.setDoj(LocalDate.of(
                        Integer.parseInt(date[2]), // Year
                        Integer.parseInt(date[0]), // Month
                        Integer.parseInt(date[1])  // Day
                ));

            } else if (record[5].trim().equals(eventType.BONUS.toString())) {
                event.setEvent(eventType.BONUS);
                event.setBonus(Integer.parseInt(record[6].trim()));
            } else if (record[5].trim().equals(eventType.REIMBURSEMENT.toString())) {
                event.setEvent(eventType.REIMBURSEMENT);
                event.setReimbursement(Integer.parseInt(record[6].trim()));
            } else if (record[5].trim().equals(eventType.EXIT.toString())) {
                event.setEvent(eventType.EXIT);
                String[] date = record[6].trim().split("-");

                event.setDol(LocalDate.of(
                        Integer.parseInt(date[2]), // Year
                        Integer.parseInt(date[0]), // Month
                        Integer.parseInt(date[1])  // Day
                ));

            } else if (record[5].trim().equals(eventType.BONUS.toString())) {
                event.setEvent(eventType.BONUS);
                event.setBonus(Integer.parseInt(record[6].trim()));
            } else if (record[5].trim().equals(eventType.SALARY.toString())) {
                event.setEvent(eventType.SALARY);
                event.setSalary(Integer.parseInt(record[6].trim()));
            }
            String[] date = record[7].trim().split("-");

            event.setEventDate(LocalDate.of(
                    Integer.parseInt(date[2]), // Year
                    Integer.parseInt(date[0]), // Month
                    Integer.parseInt(date[1])  // Day
            ));

            event.setNotes(record[8].trim());
            employees.add(employee);


        }else {
            event.setEmpId(record[1].trim());
            if (record[2].trim().equals(eventType.ONBOARD.toString())) {
                event.setEvent(eventType.ONBOARD);
                String[] date = record[3].trim().split("-");

                event.setDoj(LocalDate.of(
                        Integer.parseInt(date[2]), // Year
                        Integer.parseInt(date[0]), // Month
                        Integer.parseInt(date[1])  // Day
                ));

            } else if (record[2].trim().equals(eventType.REIMBURSEMENT.toString())) {
                event.setEvent(eventType.REIMBURSEMENT);
                event.setReimbursement(Integer.parseInt(record[3].trim()));
            } else if (record[2].trim().equals(eventType.EXIT.toString())) {
                event.setEvent(eventType.EXIT);
                String[] date = record[3].trim().split("-");

                event.setDol(LocalDate.of(
                        Integer.parseInt(date[2]), // Year
                        Integer.parseInt(date[0]), // Month
                        Integer.parseInt(date[1])  // Day
                ));

            } else if (record[2].trim().equals(eventType.BONUS.toString())) {
                event.setEvent(eventType.BONUS);
                event.setBonus(Integer.parseInt(record[3].trim()));
            } else if (record[2].trim().equals(eventType.SALARY.toString())) {
                event.setEvent(eventType.SALARY);
                event.setSalary(Integer.parseInt(record[3].trim()));
            }
            String[] date = record[4].trim().split("-");

            event.setEventDate(LocalDate.of(
                    Integer.parseInt(date[2]), // Year
                    Integer.parseInt(date[0]), // Month
                    Integer.parseInt(date[1])  // Day
            ));

            event.setNotes(record[5].trim());
        }

            events.add(event);

    }

    @Override
    public long employeeCount() {
        return employees.stream().count();
    }

    @Override
    public Map<Month, List<Event>> findEmployeesJoined() {
       // System.out.println(events);
        Map<Month, List<Event>> res = events.stream()
                         .filter(emp -> emp.getEvent().equals(eventType.ONBOARD))
                        .collect(Collectors.groupingBy(event -> event.getDoj().getMonth()));

        return res;
    }

    @Override
    public Map<Month, List<Event>>  findEmployeesExited() {
        Map<Month, List<Event>> res = events.stream()
                .filter(emp -> emp.getEvent().equals(eventType.EXIT))
                .collect(Collectors.groupingBy(event -> event.getDol().getMonth()));

        return res;
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employees;
    }

    public Employee findEmployeeById(String empId){
        for(Employee emp: employees){
            if(emp.getEmpId().equals(empId))
                return emp;
        }

        return null;
    }

    public boolean findEmployeeExistsById(String empId){
        for(Employee emp: employees){
            if(emp.getEmpId().equals(empId))
                return true;
        }

        return false;
    }

    public long employeesCount(){
       return employees.stream().count();
    }
    @Override
    public void print(){
       System.out.println(employees);
        System.out.println(events);
    }
}
