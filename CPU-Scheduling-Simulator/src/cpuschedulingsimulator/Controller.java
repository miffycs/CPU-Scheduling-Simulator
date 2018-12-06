/*
 * @author: Miffy Chen
 * @author: James Yu
 * @date:   2018/12/06
 * 
 * Controller.java
 * 
 */

package cpuschedulingsimulator;

import java.awt.Component;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;


public class Controller extends JFrame {
	
	private boolean paused;
	JProgressBar clockCycle;
	Process[] p;
	double delay;
	JPanel jPanel;
	int clock;
	int totalBT;
	boolean cont;
	JPanel jPanelControlButtons;
	Process selectProcess;
	Component[] miscComponents;
	Algo algo;
	LinkedList<GanttChartComp> ganttChart = new LinkedList<>();;
	private boolean finishedSimulation;
	
	void setAlgo(Algo algo) {
		this.algo = algo;
	}
	
	public void setClock(int clock) {
		this.clock = clock;
	}
	
	Controller(Process[] p, JProgressBar clockCycle, int delay, JPanel jPanel,
	        Component[] miscComponents, JPanel jPanelControlButtons) {
		this.clock = 0;
		int tp = p.length;
		this.clockCycle = clockCycle;
		this.p = p;
		this.finishedSimulation = false;
		setDelay(delay);
		this.jPanel = jPanel;
		this.jPanelControlButtons = jPanelControlButtons;
		
		selectProcess = p[0];
		totalBT = 0;
		paused = false;
		for (int i = 0; i < tp; i++) {
			totalBT += p[i].getBurstTime();
		}
		clockCycle.setMaximum(totalBT);
		this.miscComponents = miscComponents;
		this.cont = false;
		
	}
	
	public void setCont(boolean cont) {
		this.cont = cont;
	}
	
	public void setFinishedSimulation(boolean finishedSimulation) {
		this.finishedSimulation = finishedSimulation;
		this.clock = 0;
	}
	
	public boolean isFinishedSimulation() {
		return finishedSimulation;
	}
	
	void update() {
		clock++;
		clockCycle.setValue(clock);
		JLabel cpuSelectedProcess = (JLabel) miscComponents[2];
		cpuSelectedProcess.setText(this.selectProcess.getpID());
		if (clock >= totalBT) {
			
			if (!finishedSimulation) {
				
				for (Component comp: jPanelControlButtons.getComponents()) {
					if (comp.isEnabled()) {
						comp.setEnabled(false);
					}
					else {
						comp.setEnabled(true);
					}
				}
				miscComponents[3].setEnabled(true);
				miscComponents[4].setEnabled(true);
				cpuSelectedProcess.setText("All process finished!");
				finishedSimulation = true;
			}
			calculateResult();
		}
		
	}
	
	void calculateResult() {
		// Turn around time & Wait time
		double avgTurnAroundTime = 0.0;
		int[] turnaround = new int[p.length];
		int waiting[] = new int[p.length];
		for (int i = 0; i < p.length; i++) {
			for (GanttChartComp comp: ganttChart) {
				if (comp.getProcess() == p[i]) {
					turnaround[i] = comp.getEndingPoint() - comp.getProcess().getArrivalTime();
					p[i].setTurnAroundTime(turnaround[i]);
				}
			}
			waiting[i] = turnaround[i] - p[i].getBurstTime();
			p[i].setWaitTime(waiting[i]);
		}
		double avgWaitTime = 0.0;
		for (int i = 0; i < p.length; i++) {
			avgTurnAroundTime += turnaround[i];
			avgWaitTime += waiting[i];
		}
		
		avgTurnAroundTime = (double) (avgTurnAroundTime / p.length);
		avgWaitTime = (double) (avgWaitTime / p.length);
		JTextField turnAroundLabel = (JTextField) miscComponents[1];
		turnAroundLabel.setText(String.valueOf(avgTurnAroundTime));
		JTextField waitingTime = (JTextField) miscComponents[0];
		waitingTime.setText(String.valueOf(avgWaitTime));
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public void setDelay(int delay) {
		this.delay = (double) (delay / 50.0);
	}
	
}
