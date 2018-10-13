package ehs.payroll_client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//Francis Field

//Record used to keep track of Jobs workers are at and their rates


public class Job {

	
	String jobName;
	Map<String,Double> jobRates;
	Collection<String> jobWorkTypes;
	String department;
	
	public Job(String name) {
		this.jobName = name;
		this.jobRates = new HashMap<String,Double>();
		this.jobWorkTypes = new ArrayList<String>();
	}
	
	public void addRate(String type, Double amount) {
		this.jobRates.put(type, amount);
	}
	
	public Double getRate(String type) {
		return this.jobRates.get(type);
	}
	
	public void addWorkType(String work) {
		this.jobWorkTypes.add(work);
	}
	
	public Collection<String> getWorkTypes() {
		return this.jobWorkTypes;
	}
	
	public void setDepartment(String dep) {
		this.department = dep;
	}
	
	public String getDepartment() {
		return this.department;
	}
}