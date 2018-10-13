package ehs.payroll_client;
	

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import ehs.payroll_client.controller.HomeController;
import ehs.payroll_client.controller.RootLayoutController;
import ehs.payroll_client.exceptions.EmployeeNotFoundException;
import ehs.payroll_client.exceptions.JobNotFoundException;
import ehs.payroll_client.exceptions.RoleNotFoundException;
import ehs.payroll_client.model.TimesheetParser;
import ehs.payroll_client.model.Repositories.EmployeeRepository;
import ehs.payroll_client.model.Repositories.JobRepository;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class MainApp extends Application {
	private Stage primaryStage;
    private BorderPane rootLayout;
    
    String timesheetPath;
    boolean timesheetCollected = false;
    String employeesPath;
    boolean employeesCollected = false;
    String jobsPath;
    String deductionsPath;
    boolean jobsCollected = false;
    
    JobRepository jobRepo;
    
    EmployeeRepository employeeRepo;
    
    
    /**
     * Constructor
     */ 
    public MainApp() {

    }



    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("EHS");
        
        File properties = new File("properties.txt");
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
        initRootLayout();
        
        showMain();
    }

    /**
     * Initializes the root layout and tries to load the last opened
     * person file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            
            //Navigation
            FXMLLoader menuLoader = new FXMLLoader();
            menuLoader.setLocation(MainApp.class
                    .getResource("view/MenuBar.fxml"));
            MenuBar menuBar = (MenuBar) menuLoader.load();
            
            Menu fileMenu = new Menu("File");
            MenuItem deductionsFilePathSet = new MenuItem("Set Total Deductions Sheet Path");
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Deductions Sheet");
            deductionsFilePathSet.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                    	deductionsPath = file.getAbsolutePath();
                    	PrintWriter writer;
						try {
							writer = new PrintWriter("properties.txt", "UTF-8");
							writer.println(deductionsPath);
	                    	writer.close();
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    }
                }
            });
            fileMenu.getItems().add(deductionsFilePathSet);
            

            
            
            menuBar.getMenus().add(fileMenu);
            // Show the scene containing the root layout.
            
            rootLayout.setTop(menuBar);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showMain() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Home.fxml"));
            
            
          //Creating a GridPane container
            HBox hb = new HBox();
            final TextField date = new TextField();
            date.setText("Enter Date of Time Period's End in MM-DD-YYYY format");
            //hb.getChildren().add(0,date);
            
            rootLayout.setLeft(hb);
            
            VBox mainScreen = (VBox) loader.load();
            mainScreen.setMinWidth(350.0);
            mainScreen.setSpacing(10);
            mainScreen.getStylesheets().add("application/application.css");
            
            Button[] buttons = new Button[4];
            
            Button trackTimeSheetButton = new Button("Upload Track Timesheet");
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Track Timesheet");
            trackTimeSheetButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                    	timesheetPath = file.getAbsolutePath();
                    	timesheetCollected = true;
                    	//pass path to parser
                       // openFile(file);
                    }
                }
            });
            buttons[0] = trackTimeSheetButton;
            
            
            Button employeeInfoButton = new Button("Upload Employee Info");
            final FileChooser employeeFileChooser = new FileChooser();
            employeeFileChooser.setTitle("Open Employee Sheet");
            employeeInfoButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    File file = employeeFileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                    	employeesPath = file.getAbsolutePath();
                    	employeeRepo = new EmployeeRepository(employeesPath);
                    	employeesCollected = true;
                    }
                }
            });
            buttons[1] = employeeInfoButton;
            
            
            Button jobInfoButton = new Button("Upload Job Info");
            jobInfoButton.setMinWidth(100.0);
            final FileChooser jobFileChooser = new FileChooser();
            jobFileChooser.setTitle("Open Job Sheet");
            jobInfoButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    File file = jobFileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                    	jobsPath = file.getAbsolutePath();
                    	jobRepo = new JobRepository(jobsPath);
                    	jobsCollected = true;
                    }
                }
            });
            buttons[2] = jobInfoButton;
            
            Button generateButton = new Button("Generate");
            generateButton.setMinWidth(30.0);
            generateButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    if (jobsCollected && employeesCollected && timesheetCollected && date.getText().matches("\\d{2}-\\d{2}-\\d{4}" )) {
                    	TimesheetParser timesheetParser = new TimesheetParser(jobRepo,employeeRepo,timesheetPath,date.getText());
                    	try {
                    	timesheetParser.initializeData();
                    	}
                    	catch (EmployeeNotFoundException employeeException) {
                    		System.out.println(employeeException.getMessage());
                    		Alert alert = new Alert(AlertType.WARNING);
                    		alert.setContentText(employeeException.getMessage());
                			alert.showAndWait();
                    	}
                    	catch (JobNotFoundException jobException) {
                    		System.out.println(jobException.getMessage());
                    		Alert alert = new Alert(AlertType.WARNING);
                    		alert.setContentText(jobException.getMessage());
                			alert.showAndWait();
                    	}
                    	catch (RoleNotFoundException roleException) {
                    		System.out.println(roleException.getMessage());
                    		Alert alert = new Alert(AlertType.WARNING);
                    		alert.setContentText(roleException.getMessage());
                			alert.showAndWait();
                    	}
                    	File excelFile = new File("Computer Ease Input for week ending on " + date.getText() + ".csv");
                    	File deductionsFile = new File("Deductions for week Ending on " + date.getText() + ".csv");
                    	try {
							getHostServices().showDocument(excelFile.toURI().toURL().toExternalForm());
						} catch (MalformedURLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    	try {
							getHostServices().showDocument(deductionsFile.toURI().toURL().toExternalForm());
						} catch (MalformedURLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    	jobsCollected = false;
                    	employeesCollected = false;
                    	timesheetCollected = false;
             
                    }
                    else {
                    	if (!jobsCollected) {
                    		Alert alert = new Alert(AlertType.WARNING);
                    		alert.setContentText("No Jobsheet Uploaded!");
                			alert.showAndWait();
                    	}
                    	else if (!employeesCollected) {
                    		Alert alert = new Alert(AlertType.WARNING);
                    		alert.setContentText("No Employee Sheet Uploaded!");
                			alert.showAndWait();
                    	}
                    	else if (!timesheetCollected) {
                    		Alert alert = new Alert(AlertType.WARNING);
                    		alert.setContentText("No Timesheet Uploaded!");
                			alert.showAndWait();
                    	}
                    	else if (!date.getText().matches("\\d{2}-\\d{2}-\\d{4}")) {
                    		Alert alert = new Alert(AlertType.WARNING);
                    		alert.setContentText("Date Not Entered or Formatted Incorrectly!");
                			alert.showAndWait();
                    	}
              
                    }
                }
            });
            buttons[3] = generateButton;
            
            
             //Retrieving the observable list of the Tile Pane 
            ObservableList list = mainScreen.getChildren(); 
             
            //Adding the array of buttons to the pane 
            list.add(date);
            
            list.addAll(buttons);
            
         
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(mainScreen);

            // Give the controller access to the main app.
            HomeController controller = loader.getController();
            controller.setMainApp(this);
            
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    



    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

