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
    public void store(String[] record){
       String empId = record[1];

       if(record[5].trim().equals(EventType.ONBOARD.toString())){
           Event ev = createEvent(record, true);
           events.add(ev);
           employees.add(createEmployee(record));
       }else{
           Event ev = createEvent(record, false);
           events.add(ev);
       }

    }

    private Event createEvent(String[] record, boolean isNewEmployee) {
        Event event = new Event();
        int eventTypeIndex = isNewEmployee ? 5 : 2;
        int eventDataIndex = isNewEmployee ? 6 : 3;
        int eventDateIndex = isNewEmployee ? 7 : 4;
        int notesIndex = isNewEmployee ? 8 : 5;

        event.setEmpId(record[1].trim());
        event.setEvent(EventType.valueOf(record[eventTypeIndex].trim()));

        switch (event.getEvent()) {
            case ONBOARD:
                event.setDoj(parseDate(record[eventDataIndex].trim()));
                break;
            case BONUS:
                event.setBonus(Integer.parseInt(record[eventDataIndex].trim()));
                break;
            case REIMBURSEMENT:
                event.setReimbursement(Integer.parseInt(record[eventDataIndex].trim()));
                break;
            case EXIT:
                event.setDol(parseDate(record[eventDataIndex].trim()));
                break;
            case SALARY:
                event.setSalary(Integer.parseInt(record[eventDataIndex].trim()));
                break;
        }

        event.setEventDate(parseDate(record[eventDateIndex].trim()));
        event.setNotes(record[notesIndex].trim());
        return event;
    }

    private LocalDate parseDate(String dateStr) {
        String[] dateParts = dateStr.split("-");
        return LocalDate.of(
                Integer.parseInt(dateParts[2]),  // Year
                Integer.parseInt(dateParts[0]),  // Month
                Integer.parseInt(dateParts[1])   // Day
        );
    }

    private Employee createEmployee(String[] record) {
        Employee employee = new Employee();
        employee.setSequenceNo(Integer.parseInt(record[0].trim()));
        employee.setEmpId(record[1].trim());
        employee.setfName(record[2].trim());
        employee.setlName(record[3].trim());
        employee.setDesignation(record[4].trim());
        return employee;
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
        System.out.println(events);
        Map<String, List<Event>> res = events.stream()
                .collect(Collectors.groupingBy(event -> String.valueOf(event.getEventDate().getYear())));

        return res;
    }

    public Employee findEmployeeById(String empId) {
        return empLookup.get(empId);
    }



    public long employeesCount(){
       return employees.stream().count();
    }

}
