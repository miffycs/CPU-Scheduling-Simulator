/*
 * @author: Miffy Chen & James Yu
 * @date:   2018/11/15
 * 
 * Process.java
 * 
 */

package cpuscheduler;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;


/**
 * Data of each process in queue
 */
public class Process {
	
	// CPU time (time moves in increments of 0.1)
	private double curTime;			// = stores a copy of the current time in the process
	
	// Process properties
	private int PID = 0;			// process ID
	private int priority = 0;		// process priority
	private int level = 0;			// process level
	
	// Process statuses
	private boolean isArrived = false;		// process has arrived
	private boolean isStarted = false;		// process has started
	private boolean isActive = false;		// process is the current active process
	private boolean isFinished = false;		// process is finished
	
	// Process times
	private double arrivalTime = 0.0;		// arrival time
	private double startTime = 0.0;			// start time
	private double finishTime = 0.0;		// finish time
	private double burstTime = 0.0;			// burst time
	private double totalBurstTime = 0.0;	// total burst time
	private double delayTime = 0.0;			// delay time
	private double waitTime = 0.0;			// wait time
	private double responseTime = 0.0;		// response time = start time - arrival time
	private double turnAroundTime = 0.0;	// turnAaround time = finish time - arrival time
	
	// Default constructor
	public Process(int PID, double burstTime, double delayTime, int priority) {
		this.PID = PID;
		this.burstTime = burstTime;
		this.totalBurstTime = burstTime;
		this.delayTime = delayTime;
		this.priority = priority;
	}
	
	// Create random processes by the CPU
	public Process(int PID, double meanB, double stdDevB, double meanD, double stdDevD) {
		/*
		 *  Passing in:
		 *  PID = #
		 *  Mean Burst = 8.33
		 *  Std Dev Burst = 2.1
		 *  Mean Delay = 2.46
		 *  Std Dev Delay = 0.7
		 */
		
		// set values to parameter
		this.PID = PID;
		
		// using random generator to come up with 
		Random gen = new Random(System.nanoTime());
		
		// show decimals to the tenth digit, and round up
		DecimalFormat df = new DecimalFormat("#.00");
		df.setRoundingMode(RoundingMode.CEILING);
		
		// set times
		// gen.nextGaussian() returns double of -1.0 to 1.0
		this.burstTime = Double.valueOf(df.format(gen.nextGaussian() * stdDevB + meanB));
		this.totalBurstTime = this.burstTime;
		this.delayTime = Double.valueOf(df.format(gen.nextGaussian() * stdDevD + meanD));
		this.priority = (int) Math.round((Math.random() * 9));
	}
	
	/* 
	 * Process is being executed by the CPU
	 * Time moves in increments of 0.1
	 */
	public void executing(double timeNow) {
		
		// is actively executing
		isActive = true;
		
		// if (timeNow = arrivalTime = 0)
		if (Math.abs(timeNow - arrivalTime) < 0.1) {
			isArrived = true;
		}
		
		// if (burstTime = totalBurstTime)
		if (Math.abs(burstTime - totalBurstTime) < 0.1) {
			isStarted = true;
			startTime = timeNow;
			responseTime = startTime - arrivalTime;
		}
		
		// move time by 0.1
		if (curTime != timeNow) {
			// process executed for 0.1 sec
			burstTime -= 0.1;
			// update turnaround time
			turnAroundTime += 0.1;
		}
		
		// if process is done, set to finished
		if (Math.abs(burstTime) < 0.1) {
			isFinished = true;
			finishTime = timeNow;
		}
		
		// set current time to parameter time
		curTime = timeNow;
	}
	
	/* 
	 * Process is waiting and not being executed by the CPU
	 * Time moves in increments of 0.1
	 */
	public void waiting(double timeNow) {
		
		// is not actively executing
		isActive = false;
		
		// move time by 0.1
		if (curTime != timeNow) {
			// add 0.1 to wait time
			waitTime += 0.1;
			// add 0.1 to turnaround time
			turnAroundTime += 0.1;
		}
		
		// if (timeNow = arrivalTime)
		if (Math.abs(timeNow - arrivalTime) < 0.1) {
			// update isArrived to true
			isArrived = true;
		}
		
		// set current time to parameter time
		curTime = timeNow;
	}
	
	
	// toString method that returns the data of this Process
	@SuppressWarnings("null")
	@Override
	public String toString() {
		
		String rtn = PID + " "
				+ burstTime + " "
				+ totalBurstTime + " "
				//"Delay Time  : " + delayTime + "\n" +
		        + priority + " "
				+ arrivalTime + " "
		        + startTime + " "
				+ finishTime + " "
		        + waitTime + " "
				+ turnAroundTime + " "
		        + responseTime + "\n";
		
		return this != null ? rtn : "NULL";
	}
	
	// Keep Process properties but reset the statuses & times
	public void resetAll() {
		
		// reset status
		this.isActive = false;
		this.isStarted = false;
		this.isFinished = false;
		this.isArrived = false;
		
		// reset times
		this.burstTime = this.totalBurstTime;
		this.startTime = 0.0;
		this.waitTime = 0.0;
		this.responseTime = 0.0;
		this.turnAroundTime = 0.0;
	}
	
	
	/*
	 * Getters & Setters
	 */
	
	public int getPID() {
		return this.PID;
	}
	public void setPID(int PID) {
		this.PID = PID;
	}
	
	public int getPriority() {
		return this.priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getLevel() {
		return this.level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public boolean isArrived() {
		return this.isArrived;
	}
	public void setIsArrived(boolean isArrived) {
		this.isArrived = isArrived;
	}
	
	public boolean isStarted() {
		return this.isStarted;
	}
	public void setIsStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	public boolean isActive() {
		return this.isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
	public void setIsFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	public double getArrivalTime() {
		return this.arrivalTime;
	}
	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public double getStartTime() {
		return this.startTime;
	}
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	
	public double getFinishTime() {
		return this.finishTime;
	}
	public void setFinishTime(double finishTime) {
		this.finishTime = finishTime;
	}
	
	public double getBurstTime() {
		return this.burstTime;
	}
	public void setBurstTime(double burstTime) {
		this.burstTime = burstTime;
	}
	
	public double getTotalBurstTime() {
		return this.totalBurstTime;
	}
	public void setTotalBurstTime(double totalBurstTime) {
		this.totalBurstTime = totalBurstTime;
	}
	
	public double getDelayTime() {
		return this.delayTime;
	}
	public void setDelayTime(double delayTime) {
		this.delayTime = delayTime;
	}
	
	public double getWaitTime() {
		return this.waitTime;
	}
	public void setWaitTime(double waitTime) {
		this.waitTime = waitTime;
	}
	
	public double getResponseTime() {
		return this.responseTime;
	}
	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}
	
	public double getTurnAroundTime() {
		return this.turnAroundTime;
	}
	public void setTurnAroundTime(double turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}
	
}
