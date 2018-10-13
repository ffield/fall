package ehs.payroll_client.exceptions;

public class EmployeeNotFoundException extends Exception {
	public EmployeeNotFoundException(String message) {
		super(message);
	}
}