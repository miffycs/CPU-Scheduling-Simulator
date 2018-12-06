/*
 * @author: Miffy Chen
 * @date:   2018/12/06
 * 
 * UML.java
 * 
 */
package uml;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Font;


public class UML {
	
	private void setAll() {
		// JFrame -> CPU Scheduling...
		// getContentPane()
		
		set_jTabbedPane();
		
		set_menuBar();
	}
	
	private void set_menuBar() {
		menuBar = new JMenuBar();
		menu_file = new JMenu();
		menu_file_importFromFile = new JMenuItem();
		menu_file_exportToFile = new JMenuItem();
		menu_help = new JMenu();
		menu_help_about = new JMenuItem();
	}

	private void set_jTabbedPane() {
		
		jTabbedPane = new JTabbedPane();
		
		set_tab_ProcessInfo();
		set_tab_Simulator();
		
	}

	private void set_tab_ProcessInfo() {
		
		tab_processInfo = new JPanel();
		set_processInfo_addProcess();
		set_processInfo_options();
		set_processInfo_scheduler();
		set_processInfo_display();
		set_processInfo_run();
	}

	private void set_processInfo_display() {
		processDisplayScrollPane = new JScrollPane();
		processDisplayTable = new JTable();
	}
	private void set_processInfo_addProcess() {
		panel_addProcess = new JPanel();
		addProcess_label_arrivalTime = new JLabel();
		addProcess_label_burstTime = new JLabel();
		addProcess_spinner_arrivalTime = new JSpinner();
		addProcess_spinner_burstTime = new JSpinner();
		addProcess_button = new JButton();
	}
	private void set_processInfo_options() {
		panel_options = new JPanel();
		option_importFromFile = new JButton();
		option_exportToFile = new JButton();
		option_removeSelectedProcess = new JButton();
		option_removeAllProcesses = new JButton();
	}
	private void set_processInfo_scheduler() {
		selectScheduler_label = new JLabel();
		selectScheduler = new JComboBox<>();

	}

	private void set_processInfo_run() {
		button_startSimulateProcesses = new JButton();
	}

	private void set_tab_Simulator() {
		tab_simulator = new JPanel();
		
		set_simulator_displayPanel();
		
		CPU_ProgressBar = new JProgressBar();
		simulator_label_CPU_clock = new JLabel();
		simulator_label_CPU_selectedProcess = new JLabel();
		jLabel17 = new JLabel();
		
		set_simulator_speed();
		
		set_simulator_control();
		
		set_simulator_results();
		
	}
	
	private void set_simulator_displayPanel() {
		simulatorDisplayPanel = new JPanel();
		
		simulatorDisplayPanel_label_process = new JLabel();
		toDelete_jLabel5 = new JLabel();
		simulatorDisplayPanel_label_progress = new JLabel();
		simulatorDisplayPanel_label_remainingBurstTime = new JLabel();
		simulatorDisplayPanel_label_totalBurstTime = new JLabel();
		simulatorDisplayPanel_label_schedule = new JLabel();
		simulatorDisplayPanel_label_scheduleSelected = new JLabel();
		simulatorDisplayPanel_label_timeQuantum = new JLabel();
		simulatorDisplayPanel_spinner_timeQuantum = new JSpinner();
	}
	
	private void set_simulator_speed() {
		simulatorSpeedPanel = new JPanel();
		simulatorSpeedSlider = new JSlider();
		simulatorSpeed_label_slow = new JLabel();
		simulatorSpeed_label_fast = new JLabel();
	}
	
	private void set_simulator_control() {
		simulatorControlPanel = new JPanel();
	    control_simulate = new JButton();
		control_pauseResume = new JButton();
		control_step = new JButton();
		control_back = new JButton();
		control_reset = new JButton();
	}
	
	private void set_simulator_results() {
		simulatorResultsPanel = new JPanel();
		simulatorResults_button_detailedResults = new JButton();
		simulatorResults_label_avgWaitingTime = new JLabel();
		simulatorResults_label_avgTurnAroundTime = new JLabel();
		simulatorResults_label_avgWaitingTimeCalc = new JTextField();
		simulatorResults_label_avgTurnAroundTimeCalc = new JTextField();
	}

	private JTabbedPane jTabbedPane;
		private JPanel tab_processInfo;
			private JScrollPane processDisplayScrollPane;
				private JTable processDisplayTable;
			private JPanel panel_addProcess;
				private JLabel addProcess_label_arrivalTime;
				private JLabel addProcess_label_burstTime;
				private JSpinner addProcess_spinner_arrivalTime;
				private JSpinner addProcess_spinner_burstTime;
				private JButton addProcess_button;
			private JPanel panel_options;
				private JButton option_importFromFile;
				private JButton option_exportToFile;
				private JButton option_removeSelectedProcess;
				private JButton option_removeAllProcesses;
			private JLabel selectScheduler_label;
			private JComboBox<String> selectScheduler;
			
			private JButton button_startSimulateProcesses;
	
		private JPanel tab_simulator;
		private JPanel simulatorDisplayPanel;
			private JLabel simulatorDisplayPanel_label_process;
			private JLabel toDelete_jLabel5;
			private JLabel simulatorDisplayPanel_label_progress;
			private JLabel simulatorDisplayPanel_label_remainingBurstTime;
			private JLabel simulatorDisplayPanel_label_totalBurstTime;
			private JLabel simulatorDisplayPanel_label_schedule;
			private JLabel simulatorDisplayPanel_label_scheduleSelected;
			private JLabel simulatorDisplayPanel_label_timeQuantum;
			private JSpinner simulatorDisplayPanel_spinner_timeQuantum;
		private JProgressBar CPU_ProgressBar;
	    private JLabel simulator_label_CPU_clock;
	    private JLabel simulator_label_CPU_selectedProcess;
	    private JLabel jLabel17;
		private JPanel simulatorSpeedPanel;
			private JSlider simulatorSpeedSlider;
			private JLabel simulatorSpeed_label_slow;
			private JLabel simulatorSpeed_label_fast;
		private JPanel simulatorControlPanel;
		    private JButton control_simulate;
			private JButton control_pauseResume;
			private JButton control_step;
			private JButton control_back;
			private JButton control_reset;
		private JPanel simulatorResultsPanel;
			private JButton simulatorResults_button_detailedResults;
			private JLabel simulatorResults_label_avgWaitingTime;
			private JLabel simulatorResults_label_avgTurnAroundTime;
			private JTextField simulatorResults_label_avgWaitingTimeCalc;
			private JTextField simulatorResults_label_avgTurnAroundTimeCalc;
		
			
	private JMenuBar menuBar;
		private JMenu menu_file;
			private JMenuItem menu_file_importFromFile;
			private JMenuItem menu_file_exportToFile;
		private JMenu menu_help;
			private JMenuItem menu_help_about;
	
}