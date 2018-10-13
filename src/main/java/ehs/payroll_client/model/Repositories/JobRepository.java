//Francis Field
package ehs.payroll_client.model.Repositories;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVReader;

//used to keep track of Jobs and wages imported via CSV
public class JobRepository {
	
	Map<String, JobInfo> jobs;
	
	
	public JobRepository(String filePath) {
		jobs = new HashMap<String,JobInfo>();
		//String csvFile = "src/main/resources/jobs.csv";
        CSVReader reader = null;
        
        try {
        	//Initialiing CSV Reader
            reader = new CSVReader(new FileReader(filePath));
            Map<String,Integer> dictionary = new HashMap<String,Integer>();
            //iterate through csv, finding indices of desired values first,
            String[] line;
            line = reader.readNext();;
            for (int i = 0; i < line.length; i++) {
        		dictionary.put(line[i],i);
            }
        		
        	//iterate through rest, collecting info
            while ((line = reader.readNext()) != null) {
            	if (!jobs.containsKey(line[dictionary.get("Job")])) {
            		//System.out.println("Logging job " + line[dictionary.get("Job")] + " with role " + line[dictionary.get("Role")]);
            		jobs.put(line[dictionary.get("Job")], new JobInfo(line[dictionary.get("Job")],line[dictionary.get("Department")]));
            		jobs.get(line[dictionary.get("Job")]).addWage(line[dictionary.get("Role")], Double.valueOf(line[dictionary.get("Wage")]));
            		//System.out.println("Logged Role " + line[dictionary.get("Role")] + " with wage " + jobs.get(line[dictionary.get("Job")]).getWage(line[dictionary.get("Role")]) );
            	}
            	else {
            		//System.out.println(");
            		jobs.get(line[dictionary.get("Job")]).addWage(line[dictionary.get("Role")], Double.valueOf(line[dictionary.get("Wage")]));
            		//.out.println("Logged Role " + line[dictionary.get("Role")] + " with wage " + jobs.get(line[dictionary.get("Job")]).getWage(line[dictionary.get("Role")]) );
            	}
            }
            	
    
        } catch (IOException e) {
            e.printStackTrace();
     }

	}
	
	public JobInfo getInfo(String id) {
		return this.jobs.get(id);
	}
	
	
}