package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EmployeeConfiguration {
    @Autowired
    EmployeeRepository employeeRepository;

    @Bean
    public EmployeeBrowser initDB(){
        PayRollDataReader payRollDataReader =  new PayRollDataReader("Employee_details.txt");
        List<String> records = payRollDataReader.read();
        PayrollFormat payrollFormat = new PayrollFormat(employeeRepository);
        payrollFormat.process(records);
        EmployeeBrowser browser = new EmployeeBrowser(employeeRepository);
        return  browser;
    }


}
