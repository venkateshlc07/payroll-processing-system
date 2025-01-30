package com.ppc.payroll;

public class EmployeeFinancialReport {
    private final EmployeeDTO employeeDTO;
    private long totalAmountPaid;

    public EmployeeFinancialReport(EmployeeDTO employeeDTO){
        this.employeeDTO = employeeDTO;
    }

    public String getEmpId() {
        return employeeDTO.getEmpId();
    }

    public String getfName() {
        return employeeDTO.getfName();
    }

    public String getlName() {
        return employeeDTO.getlName();
    }

    public long getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(long totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    @Override
    public String toString() {
        return "EmployeeFinancialReport{" +
                "employeeDTO=" + employeeDTO +
                ", totalAmountPaid=" + totalAmountPaid +
                '}';
    }
}
