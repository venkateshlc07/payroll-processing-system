package com.ppc.payroll;

public class EmployeeSalarySummary {
    private long totalSalary;
    private long totalEmployees;

    public EmployeeSalarySummary(long totalSalary, long totalEmployees) {
        this.totalSalary = totalSalary;
        this.totalEmployees = totalEmployees;
    }

    public long getTotalSalary() {
        return totalSalary;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    @Override
    public String toString() {
        return "EmployeeSalarySummary{" +
                "totalSalary=" + totalSalary +
                ", totalEmployees=" + totalEmployees +
                '}';
    }
}
