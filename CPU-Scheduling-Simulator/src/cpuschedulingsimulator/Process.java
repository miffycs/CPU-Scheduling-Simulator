
package cpuschedulingsimulator;

import java.awt.Color;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JProgressBar;


public class Process {
	
	private String pID;
	private int burstTime;
	private int arrivalTime;
	
	private int cbt;
	private boolean isFinished;
	
	private JLabel remainingBurstTime;
	private JProgressBar count;
	private int remainingBT;
	
	private int waitTime;
	private int turnAroundTime;
	private Color color;
	
	public Process(String pID, int burstTime, int arrivalTime, JLabel remainingBurstTime, JProgressBar count) {
		
		this.pID = pID;
		this.burstTime = burstTime;
		this.arrivalTime = arrivalTime;
		
		this.cbt = 0;
		this.isFinished = false;
		
		this.remainingBurstTime = remainingBurstTime;
		this.count = count;
		this.remainingBT = 0;
		
		this.waitTime = 0;
		this.turnAroundTime = 0;
		
		this.color = randomColorGenerator();
	}
	
	
	public String getpID() {
		return this.pID;
	}
	public int getID() {
		String getChar = "";
		for (char c: this.pID.toCharArray()) {
			if (Character.isDigit(c)) {
				getChar = getChar + c;
			}
		}
		return Integer.parseInt(getChar);
	}
	public void setpID(String pID) {
		this.pID = pID;
	}
	
	public int getBurstTime() {
		return this.burstTime;
	}
	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}
	
	public int getArrivalTime() {
		return this.arrivalTime;
	}
	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getCbt() {
		return this.cbt;
	}	
	public void setCbt(int cbt) {
		
		this.cbt = cbt;
		checkIfFinished();
		updateGUI();
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}


	public JLabel getRemainingBurstTime() {
		return this.remainingBurstTime;
	}
	public void setRemainingBurstTime(JLabel remainingBurstTime) {
		this.remainingBurstTime = remainingBurstTime;
	}
	
	public JProgressBar getCount() {
		return this.count;
	}
	public void setCount(JProgressBar count) {
		this.count = count;
	}
	
	public int getRemainingBT() {
		return this.remainingBT;
	}
	public void setRemainingBT(int remainingBT) {
		this.remainingBT = remainingBT;
	}
	
	public void run() {
		if (this.cbt < this.burstTime) {
			this.cbt++;
		}
		checkIfFinished();
		updateGUI();
	}
	
	private void checkIfFinished() {
		this.isFinished = (this.cbt >= this.burstTime);
	}
	
	private void updateGUI() {
		this.remainingBT = this.burstTime - this.cbt;
		this.remainingBurstTime.setText(String.valueOf(this.remainingBT));
		this.count.setValue(this.cbt);
	}
	
	public int getWaitTime() {
		return this.waitTime;
	}
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}
	
	public int getTurnAroundTime() {
		return this.turnAroundTime;
	}
	public void setTurnAroundTime(int turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}
	
	private static Color randomColorGenerator() {
		Random rand = new Random();
		float red = rand.nextFloat();
		float green = rand.nextFloat();
		float blue = rand.nextFloat();
		return new Color(red, green, blue);
	}
	public Color getColor() {
		return this.color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
}
