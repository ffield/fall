package ehs.payroll_client.controller;

import ehs.payroll_client.MainApp;
import javafx.fxml.FXML;

public class RootLayoutController {
	private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void handleExit() {
        System.exit(0);
    }
}