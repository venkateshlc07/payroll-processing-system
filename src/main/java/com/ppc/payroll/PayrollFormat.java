package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;

import java.util.List;

public class PayrollFormat {
    private final EmployeeRepository repository;

    public PayrollFormat(EmployeeRepository repository){
        this.repository = repository;
    }
    public void process(List<String> records){
        for(String rec: records){
            String[] record = rec.split(",");
            repository.store(record);
        }
    }

}
