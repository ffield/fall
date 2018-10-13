package ehs.payroll_client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//Francis Field

//representation of employee
public class Employee {
	String firstName;
	String lastName;
	String computerEaseID;
	Collection<Job> jobsWorkedByEmployee;
	Map<Job,Collection<Role>> rolesPerformedByEmployee = new HashMap<Job,Collection<Role>>();
	//Map<String,Day> hours = new HashMap<String, Day>();
	Integer hoursWorked = 40;
	Double percentage401k = .04;
	Double medicalInsuranceCost = 100.0;
	Double dentalInsuranceCost = 100.0;
	Integer yearsWorked = 3;
	Double basePayRate = 20.0;
	String insurancePlan;
	
	public Employee (String fn, String ln) {
		this.firstName = fn;
		this.lastName = ln; 
		this.jobsWorkedByEmployee = new ArrayList<Job>();
//		this.rolesPerformedByEmployee = rolesPerformed;
//		this.percentage401k = recordedPercentage401k;
	}
	
	public String toString() {
		String fullname = this.firstName + " " + this.lastName;
		return fullname;
	}
	
	public String getFullName() {
		String fullname = this.firstName + " " + this.lastName;
		return fullname;
	}
	
	public boolean jobPresent(String jnumber) {
		for (Job job : jobsWorkedByEmployee) {
			if (job.jobName.equals(jnumber)) {
				return true;
			}
		}
		return false;
	}
	
	public void addJob(Job j) {
		this.jobsWorkedByEmployee.add(j);
	}
	
	public void logRole(Job j, Collection<Role> role) {
		this.rolesPerformedByEmployee.put(j, role);
	}
	
	public Collection<Role> getRoles(Job j) {
		return this.rolesPerformedByEmployee.get(j);
	}
	
	public Collection<Job> getJobsWorkedByEmployee() {
		return this.jobsWorkedByEmployee;
	}
	
	public Double getHealthInsuranceAddition() {
		Double addition = medicalInsuranceCost / hoursWorked;
		return addition;
	}
	
	public Double getDentalInsuranceAddition() {
		Double addition = dentalInsuranceCost / hoursWorked;
		return addition;
	}
	
	public Double getVacationAddition() {
		if (yearsWorked < 2) {
			return basePayRate * (40.0/1920.0);
		}
		else if (yearsWorked >= 2 && yearsWorked <= 9) {
			return basePayRate * (80.0/1920.0);
		}
		else if (yearsWorked >= 10) {
			return basePayRate * (120.0/1920.0);
		}
		else {
			return 0.0;
		}
	}
	
	public Double getHolidayAddition() {
		return basePayRate * .025;
	}
	
	public Double get401kMatch(Job j, String role) {
		Double jobWage = j.getRate(role);
		if (jobWage  != null) {
		return percentage401k;
		}
		else {
			System.out.println("Wage is null for " + role + " at " + j.jobName);
			return 0.0;
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Map<Job, Collection<Role>> getRolesPerformedByEmployee() {
		return rolesPerformedByEmployee;
	}

	public void setRolesPerformedByEmployee(Map<Job, Collection<Role>> rolesPerformedByEmployee) {
		this.rolesPerformedByEmployee = rolesPerformedByEmployee;
	}

	public Integer getHoursWorked() {
		return hoursWorked;
	}

	public void setHoursWorked(Integer hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	public String getComputerEaseID() {
		return computerEaseID;
	}

	public void setComputerEaseID(String computerEaseID) {
		this.computerEaseID = computerEaseID;
	}

	public Double getPercentage401k() {
		return percentage401k;
	}

	public void setPercentage401k(Double percentage401k) {
		this.percentage401k = percentage401k;
	}

	public Double getMedicalInsuranceCost() {
		return medicalInsuranceCost;
	}

	public void setMedicalInsuranceCost(Double medicalInsuranceCost) {
		this.medicalInsuranceCost = medicalInsuranceCost;
	}

	public Double getDentalInsuranceCost() {
		return dentalInsuranceCost;
	}

	public void setDentalInsuranceCost(Double dentalInsuranceCost) {
		this.dentalInsuranceCost = dentalInsuranceCost;
	}

	public Integer getYearsWorked() {
		return yearsWorked;
	}

	public void setYearsWorked(Integer yearsWorked) {
		this.yearsWorked = yearsWorked;
	}

	public Double getBasePayRate() {
		return basePayRate;
	}

	public void setBasePayRate(Double basePayRate) {
		this.basePayRate = basePayRate;
	}

	public void setJobsWorkedByEmployee(Collection<Job> jobsWorkedByEmployee) {
		this.jobsWorkedByEmployee = jobsWorkedByEmployee;
	}
	
	
	
	
}