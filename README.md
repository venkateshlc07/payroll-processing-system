# **Payroll Processing System**

This is a simple payroll processing system implemented in Java. It allows storing, processing, and retrieving payroll-related information such as employees' salaries, bonuses, reimbursements, and onboarding details.

## **Features:**


* Store employee records from a CSV file
* Retrieve employee count
* Find employees by event type (onboarding, salary, bonus, etc.)
* Calculate total salaries by month or year.
* Generate financial reports based on employee salary history.
* Handle payroll events like salary, bonus, reimbursement, and exit.
* No external database used (in-memory storage).



**Prerequisites:**

Java 8 or later
Maven (optional for building)

**Running the Application:**

Fork & Clone the repository:
```
git clone https://github.com/yourusername/payroll-system.git


```



**Sample Employee Data (CSV)**

Example format of employees.csv:
```
1, emp101, Bill, Gates, Software Engineer, ONBOARD, 1-11-2022, 10-10-2022, “Bill Gates is going to join DataOrb on 1st November as a SE.”
2, emp102, Steve, Jobs, Architect, ONBOARD, 1-10-2022, 10-10-2022, “Steve Jobs joined DataOrb on 1st October as an Architect.”
3, emp103, Martin, Fowler, Software Engineer, ONBOARD, 12-10-2022, 10-10-2022, “Martin has joined DataOrb as a SE.”
4, emp102, SALARY, 3000, 10-10-2022, “Oct Salary of Steve.”
5, emp101, SALARY, 4000, 11-11-2022, “Oct Salary of Bill.”
5, emp101, SALARY, 4000, 12-11-2022, “Oct Salary of Bill.”
6, emp103, SALARY, 5000, 11-11-2022, “Oct Salary of Martin.”
7, emp102, EXIT, 10-10-2022, 10-10-2022, “Steve Left”
8, emp101, EXIT, 11-11-2022, 11-11-2022, “Bill Left”
````

### **Key Classes**

**EmployeeDB:** Stores and processes employee records in memory.

**EmployeeBrowser:** Provides behaviours to query employees by event type etc. and generate reports. (Aggregations)

**PayrollFormat:** Parses CSV records and stores them in memory.

**PayRollDataReader:** Reads employee data from a file.

**EmployeeRepository:** Hides DB internal storage & provide nice abstraction to interact similar to JPA.