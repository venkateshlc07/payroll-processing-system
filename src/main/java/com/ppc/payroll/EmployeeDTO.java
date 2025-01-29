package com.ppc.payroll;

public class EmployeeDTO {
    private String empId;
    private String fName;
    private String lName;

    public EmployeeDTO(String empId, String fName, String lName) {
        this.empId = empId;
        this.fName = fName;
        this.lName = lName;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "empId='" + empId + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                '}';
    }
}
