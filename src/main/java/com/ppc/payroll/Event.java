package com.ppc.payroll;

import java.time.LocalDate;

public class Event {
    private String empId;
    private eventType event;
    private LocalDate doj;
    private int salary;
    private int bonus;
    private LocalDate dol;
    private  int reimbursement;
    private LocalDate eventDate;
    private String notes;
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public eventType getEvent() {
        return event;
    }

    public void setEvent(eventType event) {
        this.event = event;
    }

    public LocalDate getDoj() {
        return doj;
    }

    public void setDoj(LocalDate doj) {
        this.doj = doj;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public LocalDate getDol() {
        return dol;
    }

    public void setDol(LocalDate dol) {
        this.dol = dol;
    }

    public int getReimbursement() {
        return reimbursement;
    }

    public void setReimbursement(int reimbursement) {
        this.reimbursement = reimbursement;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Event{" +
                "empId='" + empId + '\'' +
                ", event=" + event +
                ", doj='" + doj + '\'' +
                ", salary=" + salary +
                ", bonus=" + bonus +
                ", dol='" + dol + '\'' +
                ", reimbursement=" + reimbursement +
                ", eventDate='" + eventDate + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
