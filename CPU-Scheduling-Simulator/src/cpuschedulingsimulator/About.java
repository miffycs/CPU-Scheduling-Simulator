/*
 * @author: Miffy Chen
 * @author: James Yu
 * @date:   2018/12/06
 * 
 * About.java
 * 
 */

package cpuschedulingsimulator;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingConstants;


public class About extends JFrame {
	
	// Variables
	private GroupLayout layout = new GroupLayout(getContentPane());
	private JLabel projectTitle = new JLabel();
	private JLabel className = new JLabel();
	private JLabel projectID = new JLabel();
	private JLabel authors = new JLabel();
	private JButton exit = new JButton();
	
	// Constructor
	public About() {
		setResizable(false);
		initComponents();
		setLocationRelativeTo(null);
	}
	
	// Initialize Form
	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("About CPU Scheduling Simulator");
		
		setLabels();
		setButtons();
		setLayout();
		
		getContentPane().setLayout(layout);
		pack();
	}
	
	private void setLayout() {
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
		        .addGroup(layout.createSequentialGroup().addGap(150).addComponent(projectTitle).addGap(150))
		        .addGroup(layout.createSequentialGroup().addGap(191).addComponent(className))
		        .addGroup(layout.createSequentialGroup().addGap(257).addComponent(projectID))
		        .addGroup(layout.createSequentialGroup().addGap(174).addComponent(authors))
		        .addGroup(layout.createSequentialGroup().addGap(276).addComponent(exit)));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
		        .addGroup(layout.createSequentialGroup().addGap(40).addComponent(projectTitle).addGap(20)
		        .addComponent(className).addGap(16)
		        .addComponent(projectID).addGap(30)
		        .addComponent(authors).addGap(20)
		        .addComponent(exit).addGap(40)));
	}
	
	private void setLabels() {
		
		projectTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
		projectTitle.setText("CPU Scheduling Simulator");
		
		className.setFont(new Font("Tahoma", Font.PLAIN, 20));
		className.setText("CS575 Operating Systems");
		
		projectID.setFont(new Font("Tahoma", Font.PLAIN, 18));
		projectID.setText("Final Project");
		
		authors.setFont(new Font("Tahoma", Font.BOLD, 20));
		authors.setText("by Miffy Chen & James Yu");
	}
	
	private void setButtons() {
		exit.setFont(new Font("Tahoma", Font.PLAIN, 18));
		exit.setText("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				exitActionPerformed(evt);
			}
		});
	}
	
	private void exitActionPerformed(ActionEvent evt) {
		this.dispose();
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
				new About().setVisible(true);
//			}
//		});
	}
	
}
