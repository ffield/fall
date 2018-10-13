package ehs.payroll_client.model.Repositories;

public class EmployeeInfo {
	String firstName;
	String lastName;
	Double match401k;
	Double baseRate;
	Double dentalInsurance;
	Double medicalInsurance;
	String fullName;
	String insurancePlanType;
	Integer yearsWorked;
	
	public EmployeeInfo(String fn, String ln, Double f1k, Double baseRate, Double dentalInsurance, Double medicalInsurance,Integer yw,String planType) {
		this.firstName = fn;
		this.lastName = ln;
		this.match401k = f1k;
		this.baseRate = baseRate;
		this.dentalInsurance = dentalInsurance;
		this.medicalInsurance = medicalInsurance;
		this.yearsWorked = yw;
		this.insurancePlanType = planType;
		this.fullName = this.firstName + " " + this.lastName;
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

	public Double getMatch401k() {
		return match401k;
	}

	public void setMatch401k(Double match401k) {
		this.match401k = match401k;
	}

	public Double getBaseRate() {
		return baseRate;
	}

	public void setBaseRate(Double baseRate) {
		this.baseRate = baseRate;
	}

	public Double getDentalInsurance() {
		return dentalInsurance;
	}

	public void setDentalInsurance(Double dentalInsurance) {
		this.dentalInsurance = dentalInsurance;
	}

	public Double getMedicalInsurance() {
		return medicalInsurance;
	}

	public void setMedicalInsurance(Double medicalInsurance) {
		this.medicalInsurance = medicalInsurance;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public void setYearsWorked(Integer yw) {
		this.yearsWorked = yw;
	}
	
	public Integer getYearsWorked() {
		return this.yearsWorked;
	}
	
	public String getInsurancePlan() {
		return this.insurancePlanType;
	}
	
}