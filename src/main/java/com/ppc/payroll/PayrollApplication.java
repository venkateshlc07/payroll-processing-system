package com.ppc.payroll;

import com.ppc.payroll.repository.EmployeeRepository;
import com.ppc.payroll.utils.GroupBy;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ppc.payroll.utils.GroupBy.MONTH;


public class PayrollApplication {

	public static void main(String[] args) {

		PayRollDataReader payRollDataReader = new PayRollDataReader("Employee_details.txt");
		List<String> records = payRollDataReader.read();
		System.out.println(records);

		EmployeeRepository dbRepository = new EmployeeDB();
 		PayrollFormat payrollFormat = new PayrollFormat(dbRepository);
		payrollFormat.process(records);

		EmployeeBrowser browser = new EmployeeBrowser(dbRepository);
		Map<String, EmployeeSalarySummary> computeSalaryReport = browser.findTotalSalaryBy(Set.of(EventType.SALARY), MONTH);

		System.out.println(computeSalaryReport);

		System.out.println(browser.findTotalExpenditure(Set.of(EventType.SALARY,EventType.REIMBURSEMENT,EventType.BONUS), GroupBy.MONTH));

	}

}
