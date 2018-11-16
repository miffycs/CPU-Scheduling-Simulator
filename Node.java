
/*
 * @author: Miffy Chen
 * @date:   2018/11/15
 * 
 * Node.java
 * 
 */

/**
 * 
 */
public class Node {
	
	// Time moves in increments of 0.1
	
	private double curTime;			// = stores a copy of the current time in the node
	
	private int PID = 0;
	private int priority = 0;
	private String processName = "";
	private String processDesc = "";
	
	private double arrivalTime = 0.0;
	private double startTime = 0.0;
	private double finishTime = 0.0;
	private double burstTime = 0.0;
	private double totalBurstTime = 0.0;
	private double delayTime = 0.0;
	private double waitTime = 0.0;
	private double responseTime = 0.0;		// = start time - arrival time
	private double turnAroundTime = 0.0;	// = finish time - arrival time
	
	private boolean isArrived = false;
	private boolean isStarted = false;
	private boolean isActive = false;
	private boolean isFinished = false;
	
	public Node() {
		
	}
	
	public Node(int PID, double burstTime, double delayTime, int priority) {
		this.PID = PID;
		this.burstTime = burstTime;
		this.totalBurstTime = burstTime;
		this.delayTime = delayTime;
		this.priority = priority;
	}
	
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
	
	public String getProcessName() {
		return this.processName;
	}
	
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
	public String getProcessDesc() {
		return this.processDesc;
	}
	
	public void setProcessDesc(String processDesc) {
		this.processDesc = processDesc;
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
	
	public boolean isIsArrived() {
		return this.isArrived;
	}
	
	public void setIsArrived(boolean isArrived) {
		this.isArrived = isArrived;
	}
	
	public boolean isIsStarted() {
		return this.isStarted;
	}
	
	public void setIsStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	public boolean isIsActive() {
		return this.isActive;
	}
	
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean isIsFinished() {
		return this.isFinished;
	}
	
	public void setIsFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
}
