//Francis Field
package ehs.payroll_client.model.Repositories;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;

//class used to handle data on Employees imported via CSV
public class EmployeeRepository {
    
	Map<String, EmployeeInfo> employees;
	
	
	public EmployeeRepository(String filePath) {
		this.employees = new HashMap<String,EmployeeInfo>();
		String csvFile = filePath;
        CSVReader reader = null;
        
        try {
        	//Initialiing CSV Reader
            reader = new CSVReader(new FileReader(csvFile));
            Map<String,Integer> dictionary = new HashMap<String,Integer>();
            //iterate through csv, finding indices of desired values first,
            String[] line;
            line = reader.readNext();;
            for (int i = 0; i < line.length; i++) {
        		dictionary.put(line[i],i);
            }
        		
        	//iterate through rest, collecting info
            while ((line = reader.readNext()) != null) {
            	String fn = line[dictionary.get("First Name")];
            	String ln = line[dictionary.get("Last Name")];
            	Double baseRate = Double.valueOf(line[dictionary.get("Base Rate")]);
            	Double medical = Double.valueOf(line[dictionary.get("Medical Insurance Cost")]);
            	Double dental = Double.valueOf(line[dictionary.get("Dental Insurance Cost")]);
            	Double f1k = Double.valueOf(line[dictionary.get("401k Match")]);
            	Integer yw = Integer.valueOf(line[dictionary.get("Years Worked")]);
            	String planType = line[dictionary.get("Medical Plan")];
            	EmployeeInfo newEmployee = new EmployeeInfo(fn,ln,f1k,baseRate,dental,medical,yw,planType);
            	System.out.println("Employee Added: " + fn + " " + ln);
            	this.employees.put(newEmployee.fullName,newEmployee);
            }
            	
    
        } catch (IOException e) {
            e.printStackTrace();
     }
        


	}
	
	public EmployeeInfo getInfo(String fullName) {
		return this.employees.get(fullName);
	}
	

	
}