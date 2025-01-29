package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;

import java.util.List;
import java.util.Map;



public class PayrollApplication {

	public static void main(String[] args) {

		PayRollDataReader payRollDataReader = new PayRollDataReader("Employee_details.txt");
		List<String> records = payRollDataReader.read();
		System.out.println(records);

		EmployeeRepository dbRepository = new EmployeeDB();
 		PayrollFormat payrollFormat = new PayrollFormat(dbRepository);
		payrollFormat.process(records);

		EmployeeBrowser browser = new EmployeeBrowser(dbRepository);
		Map<String, List<EmployeeDTO>> emps = browser.findEmployeesExited();

		for(String k : emps.keySet()){
			List<EmployeeDTO> employee = emps.get(k);
			System.out.println(k);
			employee.stream().forEach(emp -> System.out.println(emp.toString()));
		}

		System.out.println(browser.computeSalaryReport());



		/*
		System.out.println(dbRepository.employeeCount());
		//dbRepository.print();

		System.out.println(dbRepository.findEmployeesJoined());
		System.out.println(dbRepository.findEmployeesExited());

		*/
	}

}
