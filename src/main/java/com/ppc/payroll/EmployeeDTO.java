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

    public String getEmpId() {
        return empId;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
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
