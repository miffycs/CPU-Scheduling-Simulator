/*
 * @author: Miffy Chen
 * @author: James Yu
 * @date:   2018/12/06
 * 
 * Main.java
 * 
 */

package cpuschedulingsimulator;

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
import javax.swing.LayoutStyle.ComponentPlacement;

public class Main extends JFrame {
	
	// Thread for algo
	Thread thread = null;
	
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
	private JLabel simulator_label_CPU_selectedProcess_selected;
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
		
	
	private void setAll() {
		// JFrame -> CPU Scheduling...
		// getContentPane()
		set_jTabbedPane();
		set_menuBar();
	}
	
	private void set_jTabbedPane() {
		
		jTabbedPane = new JTabbedPane();
		// switch between Panes: ProcessInfo and Simulator
		jTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				onClick_tabChange(evt);
			}
		});
		
		set_tab_ProcessInfo();
		set_tab_Simulator();
	}
	
	private void set_tab_ProcessInfo() {
		tab_processInfo = new JPanel();
		tab_processInfo.setPreferredSize(new Dimension(300, 400));
		
		set_processInfo_display();
		set_processInfo_addProcess();
		set_processInfo_options();
		set_processInfo_scheduler();
		set_processInfo_run();
		
		tab_processInfo.getAccessibleContext().setAccessibleName("");
	}
	
	private void set_processInfo_display() {
		processDisplayScrollPane = new JScrollPane();
		processDisplayTable = new JTable();
		
		processDisplayTable.setModel(new DefaultTableModel(new Object[][] {
		}, new String[] { "Process", "Arrival Time", "Burst Time" }));
		
		processDisplayScrollPane.setViewportView(processDisplayTable);
	}
	
	private void set_processInfo_addProcess() {
		panel_addProcess = new JPanel();
		addProcess_label_arrivalTime = new JLabel();
		addProcess_label_burstTime = new JLabel();
		addProcess_spinner_arrivalTime = new JSpinner();
		addProcess_spinner_burstTime = new JSpinner();
		addProcess_button = new JButton();
		
		panel_addProcess.setBorder(BorderFactory.createTitledBorder("Add new"));
		

//		addProcess_label_arrivalTime.setFont(new Font("Tahoma", 1, 18)); // NOI18N
		addProcess_label_arrivalTime.setText("Enter Arrival Time");
		addProcess_label_burstTime.setText("Enter Burst Time");
		
		addProcess_button.setText("Add new process");
		addProcess_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_addProcess(evt);
			}
		});
		
		addProcess_spinner_arrivalTime.setModel(new SpinnerNumberModel(0, 0, 500, 1));
		addProcess_spinner_burstTime.setModel(new SpinnerNumberModel(1, 1, 500, 1));
	}
	
	private void set_processInfo_options() {
		panel_options = new JPanel();
		option_importFromFile = new JButton();
		option_exportToFile = new JButton();
		option_removeSelectedProcess = new JButton();
		option_removeAllProcesses = new JButton();
		
		panel_options.setBorder(BorderFactory.createTitledBorder("Options"));
		
		option_importFromFile.setText("Import Data From File");
		option_importFromFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_options_import(evt);
			}
		});
		
		option_exportToFile.setText("Export Data To File");
		option_exportToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_options_export(evt);
			}
		});
		
		option_removeSelectedProcess.setText("Remove Selected Process");
		option_removeSelectedProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_removeSelectedProcess(evt);
			}
		});
		
		option_removeAllProcesses.setText("Remove All Processes");
		option_removeAllProcesses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_removeAllProcesses(evt);
			}
		});
	}
	
	// ??????
	private void set_processInfo_scheduler() {
		selectScheduler_label = new JLabel();
		selectScheduler = new JComboBox<>();
		
		selectScheduler_label.setText("Select Algorithm:");
		
		selectScheduler.setModel(new DefaultComboBoxModel<>(
		        new String[] { "FCFS", "SJF (Pre-emptive)", "Round Robin" }));
		selectScheduler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_selectScheduler(evt);
			}
		});
		selectScheduler.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				jComboBox1PropertyChange(evt);
			}
		});
		
	}
	
	private void set_processInfo_run() {
		button_startSimulateProcesses = new JButton();
		
//		button_startSimulateProcesses.setFont(new Font("Tahoma", 1, 18));
		button_startSimulateProcesses.setIcon(new ImageIcon(Main.class.getResource("/cpuschedulingsimulator/play.png")));
		button_startSimulateProcesses.setText("Proceed to Simulator");
		button_startSimulateProcesses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_runSimulationFromProcessInfo(evt);
			}
		});
	}
	
	private void set_tab_Simulator() {
		tab_simulator = new JPanel();
		set_simulator_displayPanel();
		CPU_ProgressBar = new JProgressBar();
		simulator_label_CPU_clock = new JLabel();
		simulator_label_CPU_clock.setText("CPU Clock");
		simulator_label_CPU_selectedProcess = new JLabel();
		simulator_label_CPU_selectedProcess.setText("CPU Selected Process:");
		simulator_label_CPU_selectedProcess_selected = new JLabel();
		set_simulator_speed();
		set_simulator_control();
		set_simulator_results();
	}
	
	private void set_simulator_displayPanel() {
		simulatorDisplayPanel = new JPanel();
		simulatorDisplayPanel_label_process = new JLabel();
		simulatorDisplayPanel_label_progress = new JLabel();
		simulatorDisplayPanel_label_remainingBurstTime = new JLabel();
		simulatorDisplayPanel_label_totalBurstTime = new JLabel();
		simulatorDisplayPanel_label_schedule = new JLabel();
		simulatorDisplayPanel_label_scheduleSelected = new JLabel();
		simulatorDisplayPanel_label_timeQuantum = new JLabel();
		simulatorDisplayPanel_spinner_timeQuantum = new JSpinner();
		
		simulatorDisplayPanel.setBorder(BorderFactory.createTitledBorder("Simulator"));
		
//		simulatorDisplayPanel_label_process.setFont(new Font("Tahoma", 1, 30));
		simulatorDisplayPanel_label_process.setText("Process");
		
//		simulatorDisplayPanel_label_progress.setFont(new Font("Tahoma", 1, 13));
		simulatorDisplayPanel_label_progress.setText("Progress");
		
//		simulatorDisplayPanel_label_remainingBurstTime.setFont(new Font("Tahoma", 1, 13));
		simulatorDisplayPanel_label_remainingBurstTime.setText("Remaining BT");
		
//		simulatorDisplayPanel_label_totalBurstTime.setFont(new Font("Tahoma", 1, 13));
		simulatorDisplayPanel_label_totalBurstTime.setText("Total BT");
		
//		simulatorDisplayPanel_label_schedule.setFont(new Font("Tahoma", 1, 13));
		simulatorDisplayPanel_label_schedule.setText("Scheduling:");
		
//		simulatorDisplayPanel_label_scheduleSelected.setFont(new Font("Tahoma", 1, 13));
		simulatorDisplayPanel_label_scheduleSelected.setText("N/A");
		
//		simulatorDisplayPanel_label_timeQuantum.setFont(new Font("Tahoma", 1, 13));
		simulatorDisplayPanel_label_timeQuantum.setText("Time Quantum");
		
		simulatorDisplayPanel_spinner_timeQuantum.setModel(new SpinnerNumberModel(1, 1, null, 1));
	}
	
	private void set_simulator_speed() {
		simulatorSpeedPanel = new JPanel();
		simulatorSpeedSlider = new JSlider();
		simulatorSpeed_label_slow = new JLabel();
		simulatorSpeed_label_fast = new JLabel();
		
		simulatorSpeedPanel.setBorder(BorderFactory.createTitledBorder("Speed"));
		simulatorSpeedSlider.setValue(30);
		simulatorSpeedSlider.setInverted(true);
		simulatorSpeedSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				onClick_simulatorSpeedSlider(evt);
			}
		});		
		simulatorSpeed_label_slow.setText("Slow");
		simulatorSpeed_label_fast.setText("Fast");
	}
	
	private void set_simulator_control() {
		simulatorControlPanel = new JPanel();
		control_simulate = new JButton();
		control_pauseResume = new JButton();
		control_step = new JButton();
		control_back = new JButton();
		control_reset = new JButton();
		
		simulatorControlPanel.setBorder(BorderFactory.createTitledBorder("Control"));
		
		control_simulate.setText("Simulate");
		control_simulate.setEnabled(false);
		control_simulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_control_Simulate(evt);
			}
		});
		
		control_pauseResume.setText("Pause/Resume");
		control_pauseResume.setEnabled(false);
		control_pauseResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_control_PauseResume(evt);
			}
		});
		
		control_step.setText("Step");
		control_step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_control_step(evt);
			}
		});
		
		control_back.setText("Go Back");
		control_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_control_goBack(evt);
			}
		});
		
		control_reset.setText("Reset");
		control_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_control_reset(evt);
			}
		});
	}
	
	private void set_simulator_results() {
		simulatorResultsPanel = new JPanel();
		simulatorResults_button_detailedResults = new JButton();
		simulatorResults_label_avgWaitingTime = new JLabel();
		simulatorResults_label_avgTurnAroundTime = new JLabel();
		simulatorResults_label_avgWaitingTimeCalc = new JTextField();
		simulatorResults_label_avgTurnAroundTimeCalc = new JTextField();
		
		simulatorResultsPanel.setBorder(BorderFactory.createTitledBorder("Result"));
		
		simulatorResults_label_avgWaitingTime.setText("Average Waiting Time");
		
		simulatorResults_label_avgTurnAroundTime.setText("Average Turn Around Time");
		
		simulatorResults_label_avgWaitingTimeCalc.setEditable(false);
		simulatorResults_label_avgWaitingTimeCalc.setText("N/A");
		
		simulatorResults_label_avgTurnAroundTimeCalc.setEditable(false);
		simulatorResults_label_avgTurnAroundTimeCalc.setText("N/A");
		
		simulatorResults_button_detailedResults.setText("View Detailed Results");
		simulatorResults_button_detailedResults.setEnabled(false);
		simulatorResults_button_detailedResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_openResults(evt);
			}
		});
	}
	
	private void set_menuBar() {
		menuBar = new JMenuBar();
		
		menu_file = new JMenu();
		menu_file.setText("File");
		
		menu_file_importFromFile = new JMenuItem();
		menu_file_importFromFile.setText("Import from file");
		menu_file_importFromFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_file_import(evt);
			}
		});
		
		menu_file_exportToFile = new JMenuItem();
		menu_file_exportToFile.setText("Export to file");
		menu_file_exportToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_file_export(evt);
			}
		});
		
		menu_help = new JMenu();
		menu_help.setText("Help");
		
		menu_help_about = new JMenuItem();
		menu_help_about.setText("About");
		menu_help_about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClick_help_about(evt);
			}
		});
		
		menu_file.add(menu_file_importFromFile);
		menu_file.add(menu_file_exportToFile);
		menuBar.add(menu_file);
		
		menu_help.add(menu_help_about);
		menuBar.add(menu_help);
	}
	
	
	public Main() {
		setResizable(true);
		initComponents();
		setLocationRelativeTo(null);
		setSecondTabComponents(false);
		simulatorDisplayPanel_spinner_timeQuantum.setVisible(false);
		simulatorDisplayPanel_label_timeQuantum.setVisible(false);
		this.setSize(780, 600);
		
	}
	
	private void initComponents() {
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("CPU Scheduling Simulator");
		
		// set labels, buttons, etc
		setAll();
		
		// set layout
		getContentPane().setLayout(new AbsoluteLayout());
		
		
		set_jTabbedPane();
		
		set_tab_ProcessInfo();
		
		set_processInfo_display();
		
		set_processInfo_addProcess();
		
		// ***** Set Layout
		GroupLayout gl_panel_addProcess = new GroupLayout(panel_addProcess);
		panel_addProcess.setLayout(gl_panel_addProcess);
		gl_panel_addProcess.setHorizontalGroup(gl_panel_addProcess
		        .createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(gl_panel_addProcess.createSequentialGroup().addGroup(gl_panel_addProcess
		                .createParallelGroup(GroupLayout.Alignment.LEADING)
		                .addGroup(gl_panel_addProcess.createSequentialGroup().addContainerGap()
		                        .addGroup(gl_panel_addProcess
		                                .createParallelGroup(GroupLayout.Alignment.LEADING)
		                                .addComponent(addProcess_label_arrivalTime)
		                                .addComponent(addProcess_label_burstTime))
		                        .addGap(18, 18, 18)
		                        .addGroup(gl_panel_addProcess
		                                .createParallelGroup(GroupLayout.Alignment.LEADING, false)
		                                .addComponent(addProcess_spinner_arrivalTime)
		                                .addComponent(addProcess_spinner_burstTime)))
		                .addGroup(gl_panel_addProcess.createSequentialGroup().addGap(53, 53, 53)
		                        .addComponent(addProcess_button)))
		                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_panel_addProcess.setVerticalGroup(gl_panel_addProcess
		        .createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(gl_panel_addProcess.createSequentialGroup().addContainerGap()
		                .addGroup(gl_panel_addProcess
		                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
		                        .addComponent(addProcess_label_arrivalTime).addComponent(
		                                addProcess_spinner_arrivalTime, GroupLayout.PREFERRED_SIZE,
		                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
		                .addGroup(gl_panel_addProcess
		                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
		                        .addComponent(addProcess_label_burstTime).addComponent(
		                                addProcess_spinner_burstTime, GroupLayout.PREFERRED_SIZE,
		                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
		                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                .addComponent(addProcess_button).addContainerGap()));
		// Set Layout Done
		
		
		set_processInfo_options();
		
		
		// *** Set Layout
		GroupLayout gl_panel_options = new GroupLayout(panel_options);
		panel_options.setLayout(gl_panel_options);
		gl_panel_options.setHorizontalGroup(gl_panel_options
		        .createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(gl_panel_options.createSequentialGroup().addContainerGap()
		                .addGroup(gl_panel_options
		                        .createParallelGroup(GroupLayout.Alignment.LEADING, false)
		                        .addComponent(option_removeSelectedProcess,
		                                GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
		                        .addComponent(option_importFromFile, GroupLayout.DEFAULT_SIZE,
		                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 48,
		                        Short.MAX_VALUE)
		                .addGroup(gl_panel_options
		                        .createParallelGroup(GroupLayout.Alignment.LEADING, false)
		                        .addComponent(option_removeAllProcesses, GroupLayout.DEFAULT_SIZE,
		                                146, Short.MAX_VALUE)
		                        .addComponent(option_exportToFile, GroupLayout.DEFAULT_SIZE,
		                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		                .addContainerGap()));
		gl_panel_options.setVerticalGroup(
		        gl_panel_options.createParallelGroup(GroupLayout.Alignment.LEADING)
		                .addGroup(gl_panel_options.createSequentialGroup().addContainerGap()
		                        .addGroup(gl_panel_options
		                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
		                                .addComponent(option_importFromFile)
		                                .addComponent(option_exportToFile))
		                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                        .addGroup(gl_panel_options
		                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
		                                .addComponent(option_removeSelectedProcess)
		                                .addComponent(option_removeAllProcesses))
		                        .addContainerGap(30, Short.MAX_VALUE)));
		
		set_processInfo_scheduler();
		set_processInfo_run();
		
		
		
		GroupLayout gl_tab_processInfo = new GroupLayout(tab_processInfo);
		gl_tab_processInfo.setHorizontalGroup(
			gl_tab_processInfo.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tab_processInfo.createSequentialGroup()
					.addGroup(gl_tab_processInfo.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_tab_processInfo.createSequentialGroup()
							.addGap(5)
							.addComponent(panel_addProcess, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addGroup(gl_tab_processInfo.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_options, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_tab_processInfo.createSequentialGroup()
									.addGap(6)
									.addComponent(selectScheduler_label)
									.addGap(18)
									.addComponent(selectScheduler, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))))
						.addComponent(processDisplayScrollPane, GroupLayout.PREFERRED_SIZE, 725, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_tab_processInfo.createSequentialGroup()
							.addGap(219)
							.addComponent(button_startSimulateProcesses, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(45, Short.MAX_VALUE))
		);
		gl_tab_processInfo.setVerticalGroup(
			gl_tab_processInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tab_processInfo.createSequentialGroup()
					.addGap(13)
					.addComponent(processDisplayScrollPane, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_tab_processInfo.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_addProcess, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_tab_processInfo.createSequentialGroup()
							.addComponent(panel_options, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addGroup(gl_tab_processInfo.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_tab_processInfo.createSequentialGroup()
									.addGap(3)
									.addComponent(selectScheduler_label))
								.addComponent(selectScheduler, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(39)
					.addComponent(button_startSimulateProcesses, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addGap(286))
		);
		tab_processInfo.setLayout(gl_tab_processInfo);
		

		
		set_tab_Simulator();
		set_simulator_displayPanel();
		

		GroupLayout gl_simulatorDisplayPanel = new GroupLayout(simulatorDisplayPanel);
		gl_simulatorDisplayPanel.setHorizontalGroup(
			gl_simulatorDisplayPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_simulatorDisplayPanel.createSequentialGroup()
					.addGap(31)
					.addComponent(simulatorDisplayPanel_label_process)
					.addGap(111)
					.addComponent(simulatorDisplayPanel_label_progress)
					.addGap(31)
					.addGroup(gl_simulatorDisplayPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_simulatorDisplayPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 276, Short.MAX_VALUE)
							.addGroup(gl_simulatorDisplayPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_simulatorDisplayPanel.createSequentialGroup()
									.addComponent(simulatorDisplayPanel_label_timeQuantum)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(simulatorDisplayPanel_spinner_timeQuantum, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_simulatorDisplayPanel.createSequentialGroup()
									.addComponent(simulatorDisplayPanel_label_schedule)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(simulatorDisplayPanel_label_scheduleSelected))))
						.addGroup(gl_simulatorDisplayPanel.createSequentialGroup()
							.addGap(66)
							.addComponent(simulatorDisplayPanel_label_remainingBurstTime)
							.addGap(97)
							.addComponent(simulatorDisplayPanel_label_totalBurstTime)
							.addContainerGap(131, Short.MAX_VALUE))))
		);
		gl_simulatorDisplayPanel.setVerticalGroup(
			gl_simulatorDisplayPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_simulatorDisplayPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_simulatorDisplayPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(simulatorDisplayPanel_label_process)
						.addComponent(simulatorDisplayPanel_label_remainingBurstTime)
						.addComponent(simulatorDisplayPanel_label_totalBurstTime)
						.addComponent(simulatorDisplayPanel_label_progress))
					.addPreferredGap(ComponentPlacement.RELATED, 257, Short.MAX_VALUE)
					.addGroup(gl_simulatorDisplayPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(simulatorDisplayPanel_label_schedule)
						.addComponent(simulatorDisplayPanel_label_scheduleSelected))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_simulatorDisplayPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(simulatorDisplayPanel_label_timeQuantum)
						.addComponent(simulatorDisplayPanel_spinner_timeQuantum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(3))
		);
		simulatorDisplayPanel.setLayout(gl_simulatorDisplayPanel);
		
		set_simulator_control();
		
		// *** Set Layout
		
		GroupLayout gl_simulatorControlPanel = new GroupLayout(simulatorControlPanel);
		simulatorControlPanel.setLayout(gl_simulatorControlPanel);
		gl_simulatorControlPanel.setHorizontalGroup(gl_simulatorControlPanel
		        .createParallelGroup( GroupLayout.Alignment.LEADING)
		        .addGroup(gl_simulatorControlPanel.createSequentialGroup().addGroup(gl_simulatorControlPanel
		                .createParallelGroup( GroupLayout.Alignment.LEADING)
		                .addGroup(gl_simulatorControlPanel.createSequentialGroup().addContainerGap()
		                        .addComponent(control_simulate).addGap(1, 1, 1).addComponent(control_pauseResume)
		                        .addPreferredGap( LayoutStyle.ComponentPlacement.RELATED)
		                        .addComponent(control_step,  GroupLayout.PREFERRED_SIZE, 72,
		                                 GroupLayout.PREFERRED_SIZE))
		                .addGroup(gl_simulatorControlPanel.createSequentialGroup().addGap(102, 102, 102)
		                        .addComponent(control_reset)
		                        .addPreferredGap( LayoutStyle.ComponentPlacement.RELATED)
		                        .addComponent(control_back)))
		                .addContainerGap(40, Short.MAX_VALUE)));
		gl_simulatorControlPanel.setVerticalGroup(gl_simulatorControlPanel
		        .createParallelGroup( GroupLayout.Alignment.LEADING)
		        .addGroup(gl_simulatorControlPanel.createSequentialGroup().addGroup(gl_simulatorControlPanel
		                .createParallelGroup( GroupLayout.Alignment.BASELINE)
		                .addComponent(control_simulate).addComponent(control_pauseResume).addComponent(control_step))
		                .addPreferredGap( LayoutStyle.ComponentPlacement.RELATED,
		                         GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                .addGroup(gl_simulatorControlPanel
		                        .createParallelGroup( GroupLayout.Alignment.BASELINE)
		                        .addComponent(control_back).addComponent(control_reset))
		                .addGap(109, 109, 109)));
		
		
		
		set_simulator_speed();
		
		GroupLayout gl_simulatorSpeedPanel = new GroupLayout(simulatorSpeedPanel);
		simulatorSpeedPanel.setLayout(gl_simulatorSpeedPanel);
		gl_simulatorSpeedPanel.setHorizontalGroup(gl_simulatorSpeedPanel
		        .createParallelGroup( GroupLayout.Alignment.LEADING)
		        .addGroup(gl_simulatorSpeedPanel.createSequentialGroup().addContainerGap()
		                .addGroup(gl_simulatorSpeedPanel
		                        .createParallelGroup( GroupLayout.Alignment.LEADING)
		                        .addGroup(gl_simulatorSpeedPanel.createSequentialGroup()
		                                .addComponent(simulatorSpeedSlider,
		                                         GroupLayout.PREFERRED_SIZE, 160,
		                                         GroupLayout.PREFERRED_SIZE)
		                                .addContainerGap( GroupLayout.DEFAULT_SIZE,
		                                        Short.MAX_VALUE))
		                        .addGroup(gl_simulatorSpeedPanel.createSequentialGroup()
		                                .addComponent(simulatorSpeed_label_slow)
		                                .addPreferredGap(
		                                         LayoutStyle.ComponentPlacement.RELATED,
		                                         GroupLayout.DEFAULT_SIZE,
		                                        Short.MAX_VALUE)
		                                .addComponent(simulatorSpeed_label_fast).addGap(21, 21, 21)))));
		gl_simulatorSpeedPanel.setVerticalGroup(gl_simulatorSpeedPanel
		        .createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(gl_simulatorSpeedPanel.createSequentialGroup()
		                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                .addComponent(simulatorSpeedSlider, GroupLayout.PREFERRED_SIZE, 24,
		                        GroupLayout.PREFERRED_SIZE)
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                .addGroup(gl_simulatorSpeedPanel
		                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
		                        .addComponent(simulatorSpeed_label_slow)
		                        .addComponent(simulatorSpeed_label_fast))
		                .addContainerGap()));
		
		
		set_simulator_results();
		
		
		GroupLayout gl_simulatorResultsPanel = new GroupLayout(simulatorResultsPanel);
		simulatorResultsPanel.setLayout(gl_simulatorResultsPanel);
		gl_simulatorResultsPanel.setHorizontalGroup(gl_simulatorResultsPanel
		        .createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(gl_simulatorResultsPanel.createSequentialGroup().addContainerGap()
		                .addGroup(gl_simulatorResultsPanel
		                        .createParallelGroup(GroupLayout.Alignment.LEADING)
		                        .addGroup(gl_simulatorResultsPanel.createSequentialGroup()
		                                .addComponent(simulatorResults_label_avgWaitingTime)
		                                .addPreferredGap(
		                                         LayoutStyle.ComponentPlacement.UNRELATED)
		                                .addComponent(simulatorResults_label_avgWaitingTimeCalc,
		                                         GroupLayout.PREFERRED_SIZE, 56,
		                                         GroupLayout.PREFERRED_SIZE))
		                        .addGroup(gl_simulatorResultsPanel.createSequentialGroup()
		                                .addGroup(gl_simulatorResultsPanel
		                                        .createParallelGroup(
		                                                 GroupLayout.Alignment.TRAILING, false)
		                                        .addComponent(simulatorResults_button_detailedResults,
		                                                 GroupLayout.Alignment.LEADING,
		                                                 GroupLayout.DEFAULT_SIZE,
		                                                 GroupLayout.DEFAULT_SIZE,
		                                                Short.MAX_VALUE)
		                                        .addComponent(simulatorResults_label_avgTurnAroundTime,
		                                                 GroupLayout.Alignment.LEADING,
		                                                 GroupLayout.DEFAULT_SIZE,
		                                                 GroupLayout.DEFAULT_SIZE,
		                                                Short.MAX_VALUE))
		                                .addPreferredGap(
		                                         LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(simulatorResults_label_avgTurnAroundTimeCalc,
		                                         GroupLayout.PREFERRED_SIZE, 56,
		                                         GroupLayout.PREFERRED_SIZE)))
		                .addContainerGap(96, Short.MAX_VALUE)));
		gl_simulatorResultsPanel.setVerticalGroup(gl_simulatorResultsPanel
		        .createParallelGroup( GroupLayout.Alignment.LEADING)
		        .addGroup(gl_simulatorResultsPanel.createSequentialGroup().addContainerGap()
		                .addGroup(gl_simulatorResultsPanel
		                        .createParallelGroup( GroupLayout.Alignment.BASELINE)
		                        .addComponent(simulatorResults_label_avgWaitingTime).addComponent(simulatorResults_label_avgWaitingTimeCalc,
		                                 GroupLayout.PREFERRED_SIZE,
		                                 GroupLayout.DEFAULT_SIZE,
		                                 GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap( LayoutStyle.ComponentPlacement.UNRELATED)
		                .addGroup(gl_simulatorResultsPanel
		                        .createParallelGroup( GroupLayout.Alignment.BASELINE)
		                        .addComponent(simulatorResults_label_avgTurnAroundTime).addComponent(simulatorResults_label_avgTurnAroundTimeCalc,
		                                 GroupLayout.PREFERRED_SIZE,
		                                 GroupLayout.DEFAULT_SIZE,
		                                 GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap( LayoutStyle.ComponentPlacement.RELATED)
		                .addComponent(simulatorResults_button_detailedResults).addContainerGap(24, Short.MAX_VALUE)));
		
		GroupLayout gl_tab_simulator = new GroupLayout(tab_simulator);
		tab_simulator.setLayout(gl_tab_simulator);
		gl_tab_simulator.setHorizontalGroup(gl_tab_simulator
		        .createParallelGroup( GroupLayout.Alignment.LEADING)
		        .addGroup(gl_tab_simulator.createSequentialGroup().addContainerGap()
		                .addGroup(gl_tab_simulator
		                        .createParallelGroup( GroupLayout.Alignment.LEADING)
		                        .addComponent(simulatorDisplayPanel,  GroupLayout.PREFERRED_SIZE,
		                                 GroupLayout.DEFAULT_SIZE,
		                                 GroupLayout.PREFERRED_SIZE)
		                        .addGroup(gl_tab_simulator.createSequentialGroup()
		                                .addGroup(gl_tab_simulator
		                                        .createParallelGroup(
		                                                 GroupLayout.Alignment.LEADING)
		                                        .addComponent(CPU_ProgressBar,
		                                                 GroupLayout.PREFERRED_SIZE, 308,
		                                                 GroupLayout.PREFERRED_SIZE)
		                                        .addComponent(simulator_label_CPU_clock)
		                                        .addGroup(gl_tab_simulator.createSequentialGroup()
		                                                .addComponent(simulator_label_CPU_selectedProcess)
		                                                .addPreferredGap(
		                                                         LayoutStyle.ComponentPlacement.UNRELATED)
		                                                .addComponent(simulator_label_CPU_selectedProcess_selected))
		                                        .addComponent(simulatorSpeedPanel,
		                                                 GroupLayout.PREFERRED_SIZE,
		                                                 GroupLayout.DEFAULT_SIZE,
		                                                 GroupLayout.PREFERRED_SIZE))
		                                .addPreferredGap(
		                                         LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(gl_tab_simulator
		                                        .createParallelGroup(
		                                                 GroupLayout.Alignment.LEADING)
		                                        .addComponent(simulatorControlPanel,
		                                                 GroupLayout.PREFERRED_SIZE,
		                                                 GroupLayout.DEFAULT_SIZE,
		                                                 GroupLayout.PREFERRED_SIZE)
		                                        .addComponent(simulatorResultsPanel,
		                                                 GroupLayout.PREFERRED_SIZE,
		                                                 GroupLayout.DEFAULT_SIZE,
		                                                 GroupLayout.PREFERRED_SIZE))))
		                .addContainerGap(69, Short.MAX_VALUE)));
		gl_tab_simulator.setVerticalGroup(gl_tab_simulator
		        .createParallelGroup( GroupLayout.Alignment.LEADING)
		        .addGroup(gl_tab_simulator.createSequentialGroup().addContainerGap()
		                .addComponent(simulatorDisplayPanel,  GroupLayout.PREFERRED_SIZE,
		                         GroupLayout.DEFAULT_SIZE,
		                         GroupLayout.PREFERRED_SIZE)
		                .addPreferredGap( LayoutStyle.ComponentPlacement.RELATED)
		                .addGroup(gl_tab_simulator
		                        .createParallelGroup( GroupLayout.Alignment.TRAILING)
		                        .addGroup(gl_tab_simulator.createSequentialGroup()
		                                .addComponent(simulator_label_CPU_clock)
		                                .addPreferredGap(
		                                         LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(CPU_ProgressBar,
		                                         GroupLayout.PREFERRED_SIZE, 54,
		                                         GroupLayout.PREFERRED_SIZE)
		                                .addPreferredGap(
		                                         LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(gl_tab_simulator
		                                        .createParallelGroup(
		                                                 GroupLayout.Alignment.BASELINE)
		                                        .addComponent(simulator_label_CPU_selectedProcess).addComponent(simulator_label_CPU_selectedProcess_selected)))
		                        .addComponent(simulatorControlPanel,  GroupLayout.PREFERRED_SIZE, 101,
		                                 GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap( LayoutStyle.ComponentPlacement.RELATED)
		                .addGroup(gl_tab_simulator
		                        .createParallelGroup( GroupLayout.Alignment.LEADING)
		                        .addComponent(simulatorResultsPanel,  GroupLayout.PREFERRED_SIZE,
		                                 GroupLayout.DEFAULT_SIZE,
		                                 GroupLayout.PREFERRED_SIZE)
		                        .addComponent(simulatorSpeedPanel,  GroupLayout.PREFERRED_SIZE, 87,
		                                 GroupLayout.PREFERRED_SIZE))
		                .addContainerGap(63, Short.MAX_VALUE)));
		
		
		jTabbedPane.addTab("Process Information", tab_processInfo);
		jTabbedPane.addTab("Simulator", tab_simulator);
		
		getContentPane().add(jTabbedPane, new AbsoluteConstraints(12, 0, 810, 731));
		set_menuBar();
		setJMenuBar(menuBar);
		
		pack();
	}
	
	JLabel labelForProcess[];
	JProgressBar progress[];
	JLabel remainBt[];
	JLabel lblBt[];
	Process p[];
	
	void setEnableTabComponents(boolean enabled, JPanel jPanel) {
		for (Component component: jPanel.getComponents()) {
			component.setEnabled(enabled);
		}
	}
	
	void setFirstTabComponents(boolean enabled) {
		setEnableTabComponents(enabled, tab_processInfo);
		setEnableTabComponents(enabled, panel_addProcess);
		setEnableTabComponents(enabled, panel_options);
		processDisplayTable.setEnabled(enabled);
		menu_file_importFromFile.setEnabled(enabled);
		menu_file_exportToFile.setEnabled(enabled);
	}
	
	void setSecondTabComponents(boolean enabled) {
		setEnableTabComponents(enabled, tab_simulator);
		setEnableTabComponents(enabled, simulatorControlPanel);
		setEnableTabComponents(enabled, simulatorSpeedPanel);
		
	}
	
	Controller controller;
	private void onClick_simulatorSpeedSlider(ChangeEvent evt) {
		if (controller != null) {
			controller.setDelay(simulatorSpeedSlider.getValue());
		}
	}
	
	private void onClick_control_goBack(ActionEvent evt) {
		setFirstTabComponents(true);
		setSecondTabComponents(false);
		for (Component c: labelForProcess) {
			panelProcess.remove(c);
		}
		for (Component c: progress) {
			panelProcess.remove(c);
		}
		for (Component c: lblBt) {
			panelProcess.remove(c);
		}
		for (Component c: remainBt) {
			panelProcess.remove(c);
		}
		scrollPane.remove(panelProcess);
		panelProcess.repaint();
		panelProcess.revalidate();
		scrollPane.repaint();
		scrollPane.revalidate();
		simulatorDisplayPanel.remove(scrollPane);
		simulatorDisplayPanel.repaint();
		simulatorDisplayPanel.revalidate();
		jTabbedPane.setSelectedIndex(0);
	}
	
	void initSimulator() {
		Component miscComponents[] = new Component[5];
		miscComponents[0] = simulatorResults_label_avgWaitingTimeCalc;
		miscComponents[1] = simulatorResults_label_avgTurnAroundTimeCalc;
		miscComponents[2] = simulator_label_CPU_selectedProcess_selected;
		miscComponents[3] = simulatorResults_button_detailedResults;
		miscComponents[4] = simulatorDisplayPanel_spinner_timeQuantum;
		simulatorDisplayPanel_spinner_timeQuantum.setEnabled(false);
		
		controller = new Controller(p, CPU_ProgressBar, simulatorSpeedSlider.getValue(), panelProcess,
		        miscComponents, simulatorControlPanel);
		
		switch (selectScheduler.getSelectedIndex()) {
			case 0:
				Sch_FCFS fcfs = new Sch_FCFS(controller);
				controller.setAlgo(fcfs);
				thread = new Thread(fcfs);
				break;
			case 1:
				Sch_SJF sjf = new Sch_SJF(controller);
				controller.setAlgo(sjf);
				thread = new Thread(sjf);
				break;
			case 2:
				Sch_RoundRobin roundRobin = new Sch_RoundRobin(controller,
				        Integer.parseInt(simulatorDisplayPanel_spinner_timeQuantum.getValue().toString()));
				controller.setAlgo(roundRobin);
				thread = new Thread(roundRobin);
				break;
		}
		resetAction();
		thread.start();
	}
	
	private void onClick_control_Simulate(ActionEvent evt) {
		
		initSimulator();
		controller.setCont(false);
		
		control_simulate.setEnabled(false);
		control_step.setEnabled(false);
		control_back.setEnabled(false);
		control_pauseResume.setEnabled(true);
		control_reset.setEnabled(false);
		
	}
	
	private void resetAction() {
		for (int i = 0; i < p.length; i++) {
			p[i].setCbt(0);
			p[i].setFinished(false);
		}
		CPU_ProgressBar.setValue(0);
		simulatorResults_label_avgTurnAroundTimeCalc.setText("N/A");
		simulatorResults_label_avgWaitingTimeCalc.setText("N/A");
		control_simulate.setEnabled(true);
		control_step.setEnabled(true);
		control_back.setEnabled(true);
		controller.setClock(0);
		controller.setFinishedSimulation(false);
		simulatorResults_button_detailedResults.setEnabled(false);
	}
	
	private void onClick_control_PauseResume(ActionEvent evt) {
		
		if (controller.isPaused()) {
			controller.setPaused(false);
		} else {
			controller.setPaused(true);
		}
		
	}
	
	private void onClick_control_step(ActionEvent evt) {
		if (controller == null)
			initSimulator();
		
		else if (controller.isFinishedSimulation() && control_simulate.isEnabled()) //jButton3 for verification bc that boolean was originally meant for use some other purpose
			initSimulator();
		
		controller.setCont(true);
		controller.algo.clock();
		if (!controller.isFinishedSimulation()) {
			control_simulate.setEnabled(false);
			control_pauseResume.setEnabled(false);
			control_back.setEnabled(false);
			simulatorDisplayPanel_spinner_timeQuantum.setEnabled(false);
		}
		else {
			control_step.setEnabled(true);
			control_reset.setEnabled(true);
			control_pauseResume.setEnabled(false);
			simulatorDisplayPanel_spinner_timeQuantum.setEnabled(true);
		}
	}
	
	private void onClick_control_reset(ActionEvent evt) {
		resetAction();
	}
	
	private void onClick_openResults(ActionEvent evt) {
		new ViewResults(controller, p).setVisible(true);
	}
	
	private void jComboBox1PropertyChange(PropertyChangeEvent evt) {
		// TODO add your handling code here:
	}
	
	private void onClick_selectScheduler(ActionEvent evt) {
		// TODO add your handling code here:
	}
	
	JPanel panelProcess;
	JScrollPane scrollPane;
	
	private void onClick_runSimulationFromProcessInfo(ActionEvent evt) {
		
		// if, no process data is given, show add process error
		if (processDisplayTable.getRowCount() <= 0) {
			JOptionPane.showMessageDialog(this, "There are no process in the list.",
			        "No processes!", JOptionPane.INFORMATION_MESSAGE);
		}
		
		// else, process data
		else {
			setFirstTabComponents(false);
			control_simulate.setEnabled(true);
			setSecondTabComponents(true);
			simulatorDisplayPanel.repaint();
			simulatorDisplayPanel.revalidate();
			
			panelProcess = new JPanel();
			GridLayout glayout = new GridLayout(processDisplayTable.getRowCount(), 3);
			glayout.setHgap(30);
			panelProcess.setLayout(glayout);
			panelProcess.setLocation(20, 56);
			
			int pCount = processDisplayTable.getModel().getRowCount();
			labelForProcess = new JLabel[pCount];
			progress = new JProgressBar[pCount];
			remainBt = new JLabel[pCount];
			lblBt = new JLabel[pCount];
			p = new Process[pCount];
			final int DIF_COMP = 50;
			for (int i = 0; i < pCount; i++) {
				int bt = Integer.parseInt(processDisplayTable.getModel().getValueAt(i, 2).toString());
				int at = Integer.parseInt(processDisplayTable.getModel().getValueAt(i, 1).toString());
				String name = processDisplayTable.getModel().getValueAt(i, 0).toString();
				labelForProcess[i] = new JLabel(name);
				labelForProcess[i].setLocation(simulatorDisplayPanel_label_process.getX(),
				        simulatorDisplayPanel_label_process.getY() + ((i + 1) * DIF_COMP));
				//labelForProcess[i].setSize(20,20);
				panelProcess.add(labelForProcess[i]);
				
				progress[i] = new JProgressBar();
				progress[i].setLocation(simulatorDisplayPanel_label_progress.getX(), simulatorDisplayPanel_label_progress.getY() + ((i + 1) * DIF_COMP));
				progress[i].setSize(20, 30);
				
				panelProcess.add(progress[i]);
				
				remainBt[i] = new JLabel(String.valueOf(bt));
				remainBt[i].setLocation(simulatorDisplayPanel_label_remainingBurstTime.getX(), simulatorDisplayPanel_label_remainingBurstTime.getY() + ((i + 1) * DIF_COMP));
				// remainBt[i].setSize(20,20);
				panelProcess.add(remainBt[i]);
				
				lblBt[i] = new JLabel(String.valueOf(bt));
				lblBt[i].setLocation(simulatorDisplayPanel_label_totalBurstTime.getX(), simulatorDisplayPanel_label_totalBurstTime.getY() + ((i + 1) * DIF_COMP));
				//  lblBt[i].setSize(20,20);
				panelProcess.add(lblBt[i]);
				
				progress[i].setMaximum(Integer.parseInt(lblBt[i].getText()));
				
				// Create new process
				p[i] = new Process(name, bt, at, remainBt[i], progress[i]);
			}
			
			if (selectScheduler.getSelectedIndex() == 2) {
				simulatorDisplayPanel_spinner_timeQuantum.setVisible(true);
				simulatorDisplayPanel_label_timeQuantum.setVisible(true);
			}
			
			scrollPane = new JScrollPane(panelProcess);
			scrollPane.setLocation(20, 56);
			scrollPane.setSize(700, 250);
			simulatorDisplayPanel.add(scrollPane);
			scrollPane.repaint();
			scrollPane.revalidate();
			simulatorDisplayPanel.repaint();
			simulatorDisplayPanel.revalidate();
			
			simulatorDisplayPanel_label_scheduleSelected.setText(selectScheduler.getSelectedItem().toString());
			jTabbedPane.setSelectedIndex(1);
		}
	}
	
	private void onClick_addProcess(ActionEvent evt) {
		DefaultTableModel tableModel = (DefaultTableModel) processDisplayTable.getModel();
		String processNumber = "P"
		        + String.valueOf(tableModel.getRowCount()).replaceFirst("^0+(?!$)", "");
		tableModel.addRow(new String[] {processNumber, addProcess_spinner_arrivalTime.getValue().toString(),
		addProcess_spinner_burstTime.getValue().toString() });
	}
	
	private void onClick_options_import(ActionEvent evt) {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			DefaultTableModel tableModel = (DefaultTableModel) processDisplayTable.getModel();
			try (BufferedReader in = new BufferedReader(
			        new FileReader(fileChooser.getSelectedFile().getAbsolutePath()))) {
				String readLine;
				while ((readLine = in.readLine()) != null) {
					String processName = "P"
					        + String.valueOf(tableModel.getRowCount()).replaceFirst("^0+(?!$)", "");
					String pairs[] = readLine.split(",");
					int at = Integer.parseInt(pairs[0]);
					int bt = Integer.parseInt(pairs[1]);
					tableModel.addRow(
					        new String[] { processName, String.valueOf(at), String.valueOf(bt) });
				}
				
			}
			catch (FileNotFoundException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
			catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
	}
	
	private void onClick_options_export(ActionEvent evt) {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", ".txt");
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			DefaultTableModel tableModel = (DefaultTableModel) processDisplayTable.getModel();
			try (BufferedWriter in = new BufferedWriter(new FileWriter(
			        fileChooser.getSelectedFile().getAbsolutePath().concat(".txt")))) {
				String readLine;
				
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					in.write(tableModel.getValueAt(i, 1) + ",");
					in.write(tableModel.getValueAt(i, 2).toString());
					in.newLine();
				}
				
			}
			catch (FileNotFoundException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
			catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
			JOptionPane.showMessageDialog(this, "Export successfully!", "Export success!",
			        JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	private void onClick_tabChange(ChangeEvent evt) {
		if (jTabbedPane.getSelectedIndex() == 0) {
			this.setSize(780, 600);
		} else {
			this.setSize(780, 800);
		}
		this.repaint();
		this.revalidate();
	}
	
	private void onClick_removeSelectedProcess(ActionEvent evt) {
		DefaultTableModel tableModel = (DefaultTableModel) processDisplayTable.getModel();
		tableModel.removeRow(processDisplayTable.getSelectedRow());
	}
	
	private void onClick_removeAllProcesses(ActionEvent evt) {
		DefaultTableModel tableModel = (DefaultTableModel) processDisplayTable.getModel();
		tableModel.setRowCount(0);
	}
	
	private void onClick_file_import(ActionEvent evt) {
		option_importFromFile.doClick();
	}
	
	private void onClick_file_export(ActionEvent evt) {
		option_exportToFile.doClick();
	}
	
	private void onClick_help_about(ActionEvent evt) {
		new About().setVisible(true);
	}
	
	private static void setNimbusUI() {
		try {
			for (UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (ClassNotFoundException ex) {
			Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
		}
		catch (InstantiationException ex) {
			Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
		}
		catch (IllegalAccessException ex) {
			Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
		}
		catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	

	public static void main(String args[]) {
		
		setNimbusUI();
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
				new Main().setVisible(true);
//			}
//		});
	}
	
}
