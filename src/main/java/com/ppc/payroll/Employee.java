package com.ppc.payroll;

public class Employee {
    private int sequenceNo;
    private String empId;
    private String fName;
    private String lName;
    private String designation;
    public int getSequenceNo() {
        return sequenceNo;
    }
    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }
    public String getEmpId() {
        return empId;
    }
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    public String getfName() {
        return fName;
    }
    public void setfName(String fName) {
        this.fName = fName;
    }
    public String getlName() {
        return lName;
    }
    public void setlName(String lName) {
        this.lName = lName;
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "sequenceNo=" + sequenceNo +
                ", empId='" + empId + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", designation='" + designation + '\'' +
                '}';
    }
}
