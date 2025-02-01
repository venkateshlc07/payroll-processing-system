package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;
import com.ppc.payroll.utils.EmployeeGroupBy;
import com.ppc.payroll.utils.GroupBy;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDB implements EmployeeRepository {
    private final List<Employee> employees;
    private final List<Event> events;
    private final Map<String, Employee> empLookup;
    public EmployeeDB(){

        employees = new ArrayList<>();
        events = new ArrayList<>();
        empLookup = new HashMap<>();
    }

    @Override
    public void store(String[] record) {
        Event event = new Event();

        if(!findEmployeeExistsById(record[1].trim())) {
            Employee employee = new Employee();
            employee.setSequenceNo(Integer.parseInt(record[0].trim()));
            employee.setEmpId(record[1].trim());
            employee.setfName(record[2].trim());
            employee.setlName(record[3].trim());
            employee.setDesignation(record[4].trim());

            event.setEmpId(record[1].trim());
            if (record[5].trim().equals(EventType.ONBOARD.toString())) {
                event.setEvent(EventType.ONBOARD);
                String[] date = record[6].trim().split("-");

                event.setDoj(LocalDate.of(
                        Integer.parseInt(date[2]), // Year
                        Integer.parseInt(date[0]), // Month
                        Integer.parseInt(date[1])  // Day
                ));

            } else if (record[5].trim().equals(EventType.BONUS.toString())) {
                event.setEvent(EventType.BONUS);
                event.setBonus(Integer.parseInt(record[6].trim()));
            } else if (record[5].trim().equals(EventType.REIMBURSEMENT.toString())) {
                event.setEvent(EventType.REIMBURSEMENT);
                event.setReimbursement(Integer.parseInt(record[6].trim()));
            } else if (record[5].trim().equals(EventType.EXIT.toString())) {
                event.setEvent(EventType.EXIT);
                String[] date = record[6].trim().split("-");

                event.setDol(LocalDate.of(
                        Integer.parseInt(date[2]), // Year
                        Integer.parseInt(date[0]), // Month
                        Integer.parseInt(date[1])  // Day
                ));

            } else if (record[5].trim().equals(EventType.BONUS.toString())) {
                event.setEvent(EventType.BONUS);
                event.setBonus(Integer.parseInt(record[6].trim()));
            } else if (record[5].trim().equals(EventType.SALARY.toString())) {
                event.setEvent(EventType.SALARY);
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
            //for quick lookup
            empLookup.put(employee.getEmpId(), employee);


        }else {
            event.setEmpId(record[1].trim());
            if (record[2].trim().equals(EventType.ONBOARD.toString())) {
                event.setEvent(EventType.ONBOARD);
                String[] date = record[3].trim().split("-");

                event.setDoj(LocalDate.of(
                        Integer.parseInt(date[2]), // Year
                        Integer.parseInt(date[0]), // Month
                        Integer.parseInt(date[1])  // Day
                ));

            } else if (record[2].trim().equals(EventType.REIMBURSEMENT.toString())) {
                event.setEvent(EventType.REIMBURSEMENT);
                event.setReimbursement(Integer.parseInt(record[3].trim()));
            } else if (record[2].trim().equals(EventType.EXIT.toString())) {
                event.setEvent(EventType.EXIT);
                String[] date = record[3].trim().split("-");

                event.setDol(LocalDate.of(
                        Integer.parseInt(date[2]), // Year
                        Integer.parseInt(date[0]), // Month
                        Integer.parseInt(date[1])  // Day
                ));

            } else if (record[2].trim().equals(EventType.BONUS.toString())) {
                event.setEvent(EventType.BONUS);
                event.setBonus(Integer.parseInt(record[3].trim()));
            } else if (record[2].trim().equals(EventType.SALARY.toString())) {
                event.setEvent(EventType.SALARY);
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
    public Map<String, List<Event>> findEmployeesBy(EventType event, GroupBy groupBy) {
        return events.stream()
                .filter(ev -> ev.getEvent().equals(event))
                .collect(Collectors.groupingBy(e -> {
                    if (groupBy == GroupBy.MONTH) {
                        return e.getEvent() == EventType.ONBOARD ? e.getDoj().getMonth().toString() : e.getDol().getMonth().toString();
                    } else {
                        return e.getEvent() == EventType.ONBOARD ? String.valueOf(e.getDoj().getYear()) : String.valueOf(e.getDol().getYear());
                    }
                }));

    }

    @Override
    public List<Employee> findAllEmployees() {
        return employees;
    }

    @Override
    public Map<String, List<Event>> findTotalSalaryBy(Set<EventType> eventType, GroupBy groupBy) {

        Map<String, List<Event>> res = events.stream()
                .filter(event -> eventType.contains(event.getEvent()))
                .collect(Collectors.groupingBy(
                        event -> {
                            if(groupBy == GroupBy.MONTH){
                                return event.getEventDate().getYear() + "-" + event.getEventDate().getMonth().toString();
                            }
                            else{
                                return String.valueOf(event.getEventDate().getYear());
                            }
                        }
                ));

        return res;
    }

    @Override
    public Map<String, List<Event>> findTotalSalaryBy(Set<EventType> eventType, EmployeeGroupBy employeeGroupBy) {
        Map<String, List<Event>> res = events.stream()
                .filter(event -> eventType.contains(event.getEvent()))
                .collect(Collectors.groupingBy(event -> {
                    if(employeeGroupBy == EmployeeGroupBy.EMPID){
                        return event.getEmpId();
                        //any other case just groupBy empID
                    }else{return event.getEmpId();}

                }
                ));
        return res;
    }


    @Override
    public Map<String, List<Event>> events(GroupBy groupBy) {
        Map<String, List<Event>> res = events.stream()
                .collect(Collectors.groupingBy(event -> String.valueOf(event.getEventDate().getYear())));

        return res;
    }

    public Employee findEmployeeById(String empId) {
        return empLookup.get(empId);
    }

    public boolean findEmployeeExistsById(String empId) {
        return employees.contains(empId);
    }

    public long employeesCount(){
       return employees.stream().count();
    }

}
