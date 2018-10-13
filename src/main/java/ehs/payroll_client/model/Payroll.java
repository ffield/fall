package ehs.payroll_client.model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import ehs.payroll_client.exceptions.RoleNotFoundException;

//Francis Field

//class used to keep track of all Employees and compensation

public class Payroll {
	Collection<Employee> employees;
	String weekDate;
	
	
	public Payroll(Collection<Employee> generatedPayroll,String date) {
		this.employees = generatedPayroll;
		this.weekDate = date;
	}
	
	public void printOutput(){
		for (Employee e: employees) {
			Double totalHours = 0.0;
			for (Job j : e.jobsWorkedByEmployee) {
				if (e.getRoles(j)!=null) {
					for (Role r : e.getRoles(j)) {
						if (!r.trackingId.equals("LUNCH") && !r.trackingId.equals("Lunch-LUNCH")) {
						totalHours += r.hoursWorked;
						}
					}
					
				}
			}
			System.out.println("TOTAL HOURS: " + totalHours);
		}
	}
	
	public Double calculateRate( Employee employee, Job job, Role role )throws RoleNotFoundException {
			try {
			Double prevailingWage = job.getRate(role.getLaborTypeName());
			Double basePay = employee.getBasePayRate();
			Double healthInsuranceDeduction = employee.getHealthInsuranceAddition();
			if (employee.insurancePlan.equals("FAML")) {
				healthInsuranceDeduction += .96;
			}
			else if (employee.insurancePlan.equals("EE")) {
				healthInsuranceDeduction += .48;
			}
			Double dentalInsuranceDeduction = employee.getDentalInsuranceAddition();
			//System.out.println("Dental Deduction = " + dentalInsuranceDeduction);
			Double vacationDeduction = employee.getVacationAddition();
			Double holidayDeduction = employee.getHolidayAddition();
			Double match401K = employee.get401kMatch(job, role.getLaborTypeName());
			Double deduction = vacationDeduction + holidayDeduction + dentalInsuranceDeduction + healthInsuranceDeduction;
			//System.out.println("Prevailing Wage: " + prevailingWage);
			//System.out.println("Base Pay: " + basePay);
			//System.out.println("Deduction: " + deduction);
			Double rate = (prevailingWage - deduction) / (1 + match401K);
			if (rate < basePay) {
				rate = basePay + deduction;
			}
			//System.out.println("Rate: " + rate);
		//	System.out.println("Rate: " + rate);
			return rate;
			}
			catch (NullPointerException ne) {
				throw new RoleNotFoundException("Role " + role.getComputerEaseLaborClassCode() + " determined to be missing from job " + job.jobName + ". Please add this role to jobsheet and try again.");
			}
			}
	

	public void generateOutput() throws IOException, RoleNotFoundException {
	            CSVWriter csvWriter = new CSVWriter(new FileWriter("Computer Ease Input for week ending on " + weekDate + ".csv"));
//	            
//	            String[] headerRecord = {"First Name","Last Name","ID", "Day", "Job", "Tracking ID", "Labor Type","Class Code", "Rate", "Prevailing Wage",
//	            		"Start Time","Stop Time","Hours Worked","Night Hours","Day Hours"};
//	            csvWriter.writeNext(headerRecord);
//	            
	            for (Employee e: employees) {
	    			String firstName = e.getFirstName();
	    			String lastName = e.getLastName();
	    			for (Job j : e.jobsWorkedByEmployee) {
	    				if (e.getRoles(j)!=null) {
	    					for (Role r : e.getRoles(j)) {
	    						System.out.println("Role r has " + (r.dayHours + r.nightHours) + " hours and an id of " + r.computerEaseLaborClassCode);
	    						if (!r.trackingId.equals("Lunch") && !r.trackingId.equals("Lunch-LUNCH")) {
	    							String job = j.jobName;
	    							String trackingId = r.trackingId;
	    							String laborType = r.getLaborTypeName();
	    							Double actualRate = calculateRate(e,j,r);
	    							if (r.nightHours > 0.0 && r.dayHours == 0.0) {
	    								actualRate += 1.00;
	    								Double hours = r.hoursWorked;
		    							String date = r.getDate();
		    							String department = j.getDepartment();
		    							String[] record = {e.getComputerEaseID(),laborType,job,trackingId,department,String.valueOf(hours),String.valueOf(actualRate),date};
		    							csvWriter.writeNext(record);
	    							}
	    							else if (r.nightHours > 0.0 && r.dayHours > 0.0) {
	    								Double hours = r.hoursWorked;
		    							String date = r.getDate();
		    							String department = j.getDepartment();
		    							String[] record = {e.getComputerEaseID(),laborType,job,trackingId,department,String.valueOf(hours),String.valueOf(actualRate) + 1.0,date};
		    							csvWriter.writeNext(record);
		    							hours = r.dayHours;
		    							String[] nRecord = {e.getComputerEaseID(),laborType,job,trackingId,department,String.valueOf(hours),String.valueOf(actualRate),date};
		    							csvWriter.writeNext(nRecord);
	    							}
	    							else {
	    							Double hours = r.getHoursWorked();
	    							String date = r.getDate();
	    							String department = j.getDepartment();
	    							String[] record = {e.getComputerEaseID(),laborType,job,trackingId,department,String.valueOf(hours),String.valueOf(actualRate),date};
	    							csvWriter.writeNext(record);
	    							}
	    							}
	    						
	    						}
	    					}
	    					
	    				}
	    			}
	    			csvWriter.close();
	    		}
	

	public void generateDeductionsSheet() throws IOException, RoleNotFoundException {
		CSVWriter csvWriter = new CSVWriter(new FileWriter("Deductions for week Ending on " + weekDate + ".csv"));
		String[] header = {"Full Name","Job","Role","Hours Worked at Job","Total Pay For Hours Worked","Deduction for 401k","Deduction for Medical","Deduction for Dental","Deduction for Holiday Pay"};
		csvWriter.writeNext(header);
        for (Employee e: employees) {
			String firstName = e.getFirstName();
			String lastName = e.getLastName();
			for (Job j : e.jobsWorkedByEmployee) {
				if (e.getRoles(j)!=null) {
					for (Role r : e.getRoles(j)) {
						if (!r.trackingId.equals("LUNCH") && !r.trackingId.equals("Lunch-LUNCH")) {
							String fullname = firstName + " " + lastName;
							//System.out.println("Employee: " + fullname);
							String job = j.jobName;
							String role = r.laborTypeName;
							Double paidHoursD = r.getHoursWorked();
							//System.out.println("Hours: " + String.valueOf(paidHoursD));
							Double actualRate = calculateRate(e,j,r);
							//System.out.println("Rate: " + String.valueOf(actualRate));
							Double totalMoney = paidHoursD * actualRate;
							//System.out.println("Total: " + String.valueOf(totalMoney));
							Double totalMedicalDeduction = paidHoursD * (e.getMedicalInsuranceCost()/40.0);
							//System.out.println("Medical Deduction: " + String.valueOf(totalMedicalDeduction));
							Double totalDentalDeduction = paidHoursD * e.getDentalInsuranceAddition();
							//System.out.println("Dental Deduction: " + String.valueOf(totalDentalDeduction));
							Double total401kDeduction = paidHoursD * e.get401kMatch(j,r.getLaborTypeName());
							Double holidayDeduction = paidHoursD * e.getHolidayAddition(); 
//							System.out.println("Multiplier 401: " + String.valueOf(e.get401kMatch(j,r.getLaborTypeName())));
//							System.out.println("401k Deduction: " + String.valueOf(total401kDeduction));
							String paidHours = String.valueOf(r.getHoursWorked());
							String medical = String.valueOf(totalMedicalDeduction);
							String dental = String.valueOf(totalDentalDeduction);
							String total401k = String.valueOf(total401kDeduction);
							String holiday = String.valueOf(holidayDeduction);
							String[] record = {fullname,job,role,paidHours,String.valueOf(totalMoney),total401k,medical,dental,holiday};
							csvWriter.writeNext(record);
							}
						
						}
					}
					
				}
			}
        csvWriter.close();
        
        //FIND TOTAL DEDUCTIONS SHEET
        File properties = new File("properties.txt");
        String deductionsPath = null;
        
        if(properties.exists() && !properties.isDirectory()) { 
        	FileInputStream fstream;
			try {
				fstream = new FileInputStream("properties.txt");
	        	DataInputStream in = new DataInputStream(fstream);
	        	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        	String strLine;
	        	//Read File Line By Line
	        	while ((strLine = br.readLine()) != null)   {
	        	  // split string and call your function
	        		deductionsPath = strLine;
	        	}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else {
        	PrintWriter writer = new PrintWriter("properties.txt", "UTF-8");
        	writer.println("Total Deductions.csv");
        	writer.close();
        }
        
        //reading through deductions file, adding from output file when necessary
        FileWriter pw = new FileWriter(deductionsPath,true); 
        CSVWriter deductionsWriter = new CSVWriter(pw);

        CSVReader outputReader = new CSVReader(new FileReader("Deductions for week Ending on " + weekDate + ".csv"));
        Map<String,Integer> outputDictionary = new HashMap<String,Integer>();
        //iterate through csv, finding indices of desired values first,
        String[] outputLine;
        outputLine = outputReader.readNext();;
        for (int i = 0; i < outputLine.length; i++) {
        	outputDictionary.put(outputLine[i],i);
        }
        
        while ((outputLine = outputReader.readNext()) != null) {
        	deductionsWriter.writeNext(outputLine);

        }
        
        //PARSE THROUGH
        
        //close
        deductionsWriter.close();
        outputReader.close();

	}
	
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
}
	
	
	
