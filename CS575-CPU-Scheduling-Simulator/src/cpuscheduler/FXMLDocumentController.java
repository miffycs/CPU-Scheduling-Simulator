/*
 * @author: Miffy Chen
 * @date:   2018/11/20
 * 
 * FXMLDocumentController.java
 * 
 */

package cpuscheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * 
 */
public class FXMLDocumentController implements Initializable {
	
	@FXML
	private Label status;
	@FXML
	private Button run;
	@FXML
	private Button randomInput;
	@FXML
	private Button reloadFile;
	@FXML
	private TextArea input;
	@FXML
	private ChoiceBox schMethod;
	@FXML
	private TextField simSpeed;
	@FXML
	private TextField cs;
	@FXML
	private TextField quantum;
	
//	private static ArrayList<String> schLevels = new ArrayList<>();
	private static String prevInput = "";
	private static CPU cpu;
	private static double speed;
	
	// "run" button
	@FXML
	private void handleRunButtonAction(ActionEvent event) {
		
		// if input is validated
		if (validate() == "OK") {
			status.setText("OK");
			status.setTextFill(Color.DARKGREEN);
			// get String of scheduling method
			String method = schMethod.getValue().toString();
			// if method = RR, set quantum first
			if (schMethod.getValue().equals("Round Robin"))
				method += ":" + quantum.getText();
			// if generating random input
			if (input.getText().startsWith("Random")) {
				status.setText("Error: Randomize First (press Random Input button)");
				status.setTextFill(Color.RED);
			}
			else {
				cpu = new CPU(input.getText(), method);
				prevInput = input.getText();
				cpu.Simulate();
				speed = Double.parseDouble(simSpeed.getText());
				try {
					Parent root = FXMLLoader.load(getClass().getResource("Simulation.fxml"));
					Scene scene = new Scene(root);
					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					stage.setScene(scene);
					stage.show();
				}
				catch (IOException ex) {
					Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null,
					        ex);
				}
			}
		}
		else {
			status.setText(validate());
			status.setTextFill(Color.RED);
		}
		
	}
	
	// generate random process data (set of 5)
	@FXML
	private void handleRandomInputButtonAction(ActionEvent event) {
		// get String of scheduling method
		String method = schMethod.getValue().toString();
		// if method = RR, set quantum first
		if (schMethod.getValue().equals("Round Robin"))
			method += ":" + quantum.getText();
		// if generating random input
		if (input.getText().startsWith("Random")) {
			status.setText("OK");
			status.setTextFill(Color.DARKGREEN);
			
			// split random input into readable format
			String[] line = input.getText().split("\n");
			String[] split = line[0].split("\\s+");
			CPU.randProc(Integer.valueOf(split[1]));

			String res = "";
			for (String string: cpu.getRandomData()) {
				res += string + "\n";
			}
			input.setText(res);
		}
		// if input is not random data
		else {
			// 5 sets of random process data 
			CPU.randProc(Integer.valueOf(5));//, false, 1);
			String res = "";
			for (String string: cpu.getRandomData()) {
				res += string + "\n";
			}
			input.setText(res);
		}
		
	}
	
	// load input from a text file
	@FXML
	private void handleReloadFileButtonAction(ActionEvent event) {
		
		// load file from any *.txt
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)",
		        "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		
		Stage primaryStage = new Stage();
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			
			String s = "";
			String res = "";
			double b = 0;			// burstTime
			double d = 0;			// delayTime
			int p = 0;				// priority
			try {
				BufferedReader input = new BufferedReader(new FileReader(file));
				while ((s = input.readLine()) != null) {
					// split by space
					String[] split = s.split("\\s+");
					// match input to data type
					b = Double.parseDouble(split[0]);
					d = Double.parseDouble(split[1]);
					p = Integer.parseInt(split[2]);
					res += b + " " + d + " " + p + "\n";
				}
				this.input.setText(res);
			}
			catch (Exception e) {
				status.setText("Error: Bad Input File");
				status.setTextFill(Color.RED);
			}
		}
		
	}
	
	@FXML
	private void choiceBoxAction(ActionEvent event) {
		
		if (schMethod.getValue().equals("Round Robin")) {
			quantum.setDisable(false);
		}
		else {
			quantum.setDisable(true);
		}
		
		input.setPrefHeight(650);
		run.setTranslateY(5);
		reloadFile.setTranslateY(5);
		randomInput.setTranslateY(5);
	}
	
	
	// not sure used by what
	@FXML
	public EventHandler<KeyEvent> numericValidation(final Integer max_Lengh) {
		return new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent e) {
				TextField txt_TextField = (TextField) e.getSource();
				if (txt_TextField.getText().length() >= max_Lengh) {
					e.consume();
				}
				if (e.getCharacter().matches("[0-9.]")) {
					if (txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")) {
						e.consume();
					}
					else if (txt_TextField.getText().length() == 0
					        && e.getCharacter().matches("[.]")) {
						e.consume();
					}
				}
				else {
					e.consume();
				}
			}
		};
	}
	
	// validate input data
	@FXML
	public String validate() {
		
		String inputCheck = input.getText();
		String lines[] = inputCheck.split("\n");
		
		if (lines.length == 0) {
			return "Error: No Input";
		}
		else if (lines[0].startsWith("Random")) {
			String split[] = lines[0].split("\\s+");
			try {
				Integer.valueOf(split[1]);
			}
			catch (Exception e) {
				return "Error: Bad Input for Random";
			}
		}
		else {
			try {
				for (String line: lines) {
					// split by space
					String[] split = line.split("\\s+");
					// match input to data type
					Double.parseDouble(split[0]);
					Double.parseDouble(split[1]);
					Integer.parseInt(split[2]);
				}
			}
			catch (Exception e) {
				return "Error: Bad Input";
			}
		}
		
		if (Double.parseDouble(quantum.getText()) < 0.2) {
			return "Error: minimum value for quantum is 0.2";
		}
		if (Double.parseDouble(cs.getText()) < 0.4) {
			return "Error: minimum value for quantum is 0.4";
		}
		
		return "OK";
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		input.setText(prevInput);
		
		simSpeed.addEventFilter(KeyEvent.KEY_TYPED, numericValidation(2));
		cs.addEventFilter(KeyEvent.KEY_TYPED, numericValidation(5));
		quantum.addEventFilter(KeyEvent.KEY_TYPED, numericValidation(5));
		quantum.setDisable(true);
		
		// TODO
		schMethod.getItems().removeAll(schMethod.getItems());
		schMethod.getItems().addAll("FCFS", "PSJF", "SJF", "Preemptive Priority", "Priority",
		        "Round Robin", "Lottery");
		schMethod.getSelectionModel().select("FCFS");
		
		input.setPrefHeight(650);
		
		run.setTranslateY(20);
		reloadFile.setTranslateY(20);
		randomInput.setTranslateY(20);
		
	}
	
	public static CPU getCpu() {
		return cpu;
	}
	
	public static double getSpeed() {
		return speed;
	}
}
