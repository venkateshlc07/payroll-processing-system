package com.ppc.payroll;

public class EmployeeSalarySummary {
    private long totalSalary;
    private long totalEmployees;

    public EmployeeSalarySummary(long totalSalary, long totalEmployees) {
        this.totalSalary = totalSalary;
        this.totalEmployees = totalEmployees;
    }

    @Override
    public String toString() {
        return "EmployeeSalarySummary{" +
                "totalSalary=" + totalSalary +
                ", totalEmployees=" + totalEmployees +
                '}';
    }
}
