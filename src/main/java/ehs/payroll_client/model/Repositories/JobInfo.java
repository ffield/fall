//Representation of job
package ehs.payroll_client.model.Repositories;

import java.util.HashMap;
import java.util.Map;

public class JobInfo {
	String jobID;
	Map<String,Double> wages;
	String department;
	
	public JobInfo(String id, String department) {
		this.jobID = id;
		this.wages = new HashMap<String,Double>();
		this.department = department;
	}
	
	public void addWage(String role, Double wage) {
		this.wages.put(role,wage);
	}
	
	public Double getWage(String role) {
		return this.wages.get(role);
	}
	
	public String getDepartment(){
		return this.department;
	}
	
	
	
}