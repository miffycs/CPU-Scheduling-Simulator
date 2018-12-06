/*
 * @author: Miffy Chen
 * @author: James Yu
 * @date:   2018/12/06
 * 
 * ViewResults.java
 * 
 */

package cpuschedulingsimulator;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


public class ViewResults extends javax.swing.JFrame {
	
	Process p[];
	Controller controller;
	Color[] processColors;
	
	public ViewResults(Controller controller, Process[] p) {
		initComponents();
		this.p = p;
		this.controller = controller;
		processColors = new Color[p.length];
		this.setSize(200 + (50 * controller.ganttChart.size()), 650);
		
		// fills table
		DefaultTableModel model = (DefaultTableModel) processTable.getModel();
		for (Process process: p) {
			model.addRow(new Object[] { process.getpID(), process.getWaitTime(),
			process.getTurnAroundTime() });
		}
		setLocationRelativeTo(null);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawString("0", 53, 126);
		for (int i = 0; i < controller.ganttChart.size(); i++) {
			g.drawRect(50 + (50 * i), 130, 50, 50);
			g.setColor(controller.ganttChart.get(i).getProcess().getColor());
			g.fillRect(50 + (50 * i), 130, 50, 50);
			g.setColor((Color.black));
			g.drawString(controller.ganttChart.get(i).getProcess().getpID(), 65 + (50 * i), 150);
			g.drawString(String.valueOf(controller.ganttChart.get(i).getEndingPoint()), 90 + (50 * i), 126);
		}
	}
	
	@SuppressWarnings("serial")
	private void initComponents() {
		panel_GanttChart = new JPanel();
		panel_ProcessDetails = new JPanel();
		processScrollPane = new JScrollPane();
		processTable = new JTable();
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("View Results");
		
		panel_GanttChart.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
		
		GroupLayout gl_panel_GanttChart = new GroupLayout(panel_GanttChart);
		panel_GanttChart.setLayout(gl_panel_GanttChart);
		
		gl_panel_GanttChart.setHorizontalGroup(
		        gl_panel_GanttChart.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 646, Short.MAX_VALUE));
		gl_panel_GanttChart.setVerticalGroup(
		        gl_panel_GanttChart.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 248, Short.MAX_VALUE));
		
		panel_ProcessDetails.setBorder(BorderFactory.createTitledBorder("Process Details"));
		
		processTable.setModel(new DefaultTableModel(new Object[][] {
		}, new String[] { "Process Name", "Waiting Time", "Turn Around Time" }) {
			boolean[] canEdit = new boolean[] { false, false, false };
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		processScrollPane.setViewportView(processTable);
		
		GroupLayout gl_panel_ProcessDetails = new GroupLayout(panel_ProcessDetails);
		panel_ProcessDetails.setLayout(gl_panel_ProcessDetails);
		gl_panel_ProcessDetails.setHorizontalGroup(gl_panel_ProcessDetails
		        .createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(GroupLayout.Alignment.TRAILING, gl_panel_ProcessDetails
		                .createSequentialGroup().addContainerGap().addComponent(processScrollPane,
		                        GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
		                .addContainerGap()));
		gl_panel_ProcessDetails.setVerticalGroup(gl_panel_ProcessDetails
		        .createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(GroupLayout.Alignment.TRAILING,
		                gl_panel_ProcessDetails.createSequentialGroup().addContainerGap(20, Short.MAX_VALUE)
		                        .addComponent(processScrollPane, GroupLayout.PREFERRED_SIZE,
		                                210, GroupLayout.PREFERRED_SIZE)
		                        .addContainerGap()));
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
		        .createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(layout.createSequentialGroup().addGroup(layout
		                .createParallelGroup(GroupLayout.Alignment.LEADING)
		                .addComponent(panel_GanttChart, GroupLayout.DEFAULT_SIZE,
		                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                .addGroup(layout.createSequentialGroup().addContainerGap().addComponent(
		                        panel_ProcessDetails, GroupLayout.DEFAULT_SIZE,
		                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
		                .addContainerGap()));
		layout.setVerticalGroup(layout
		        .createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(layout.createSequentialGroup().addContainerGap()
		                .addComponent(panel_GanttChart, GroupLayout.PREFERRED_SIZE,
		                        GroupLayout.DEFAULT_SIZE,
		                        GroupLayout.PREFERRED_SIZE)
		                .addGap(18, 18, 18)
		                .addComponent(panel_ProcessDetails, GroupLayout.PREFERRED_SIZE,
		                        GroupLayout.DEFAULT_SIZE,
		                        GroupLayout.PREFERRED_SIZE)
		                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		pack();
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
//				new ViewResults().setVisible(true);
//			}
//		});
	}
	
	private JPanel panel_GanttChart;
	private JPanel panel_ProcessDetails;
	private JScrollPane processScrollPane;
	private JTable processTable;
}
