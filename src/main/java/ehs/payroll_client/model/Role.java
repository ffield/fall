package ehs.payroll_client.model;

//Francis Field

//representation of hours worked at each role
//contains day of week as well as night hours vs day hours
public class Role {
	String laborTypeName;
	String computerEaseLaborClassCode;
	Double hoursWorked;
	String trackingId;
	String dayOfWeek;
	Double dayHours;
	Double nightHours;
	String startTime;
	String stopTime;
	String date;
	
	
	public Role(String nname, Double worked, String trackingID) {
		System.out.println("HOURS : " + worked);
		this.laborTypeName = nname;
		this.hoursWorked = worked;
		this.trackingId = trackingID;
	}
	
	public String getLaborTypeName() {
		return this.laborTypeName;
	}
	
	public Double getHoursWorked() {
		return this.hoursWorked;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Double getDayHours() {
		return dayHours;
	}

	public void setDayHours(Double dayHours) {
		this.dayHours = dayHours;
	}

	public Double getNightHours() {
		return nightHours;
	}

	public void setNightHours(Double nightHours) {
		this.nightHours = nightHours;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public String getComputerEaseLaborClassCode() {
		return computerEaseLaborClassCode;
	}

	public void setComputerEaseLaborClassCode(String computerEaseLaborClassCode) {
		this.computerEaseLaborClassCode = computerEaseLaborClassCode;
	}
	
	public void setDate(String workdate) {
		this.date = workdate;
	}
	
	public String getDate() {
		return this.date;
	}

}