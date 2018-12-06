/*
 * @author: Miffy Chen & James Yu
 * @date:   2018/11/15
 * 
 * CPU.java
 * 
 */

package cpuscheduler;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


/**
 * CPU to run the processes
 */
public class CPU {
	
	private Scheduler sm;				// Scheduling Method
	private double cs = 0.4;			// Context Switch aka overhead
	private int csCount = 0;			// cs count
	private double currentTime = 0.0;
	
	// stores all of the Processes passed in 
	private ArrayList<Process> allProcs = new ArrayList<>();
	// stores processes from allProcs with calculated arrival times
	private ArrayList<Process> procQueue = new ArrayList<>();
	// stores processes from procQueue IF process is ready to be executed
	private ArrayList<Process> readyQueue = new ArrayList<>();
	
	// Generate random data for processes
	private static ArrayList<String> randomData = new ArrayList<>();
	private static ArrayList<String> levels = new ArrayList<>();
	
	private Process preProc = null;
	// Active process that is currently being processed
	private Process activeProc = null;
	
	private double AvgWaitTime = 0.0;
	private double AverageTurnAroundTime = 0.0;
	private double Utility = 0.0;
	private double Potency = 0.0;
	
	private String simData = "";		// store data about current Process as it's being run
	private String report = "";			// final report
	
	/*
	 * Constructor for single level
	 * 
	 * Takes user data of the Processes and initialize them in a Priority Queue,
	 * according to the scheduling method selected.
	 * 
	 * 1) process data into readable format
	 * 2) create new Processes from the data
	 * 3) add Processes to allProcs
	 * 4) initialize a Priority Queue from allProcs
	 */
	public CPU(String data, String schName) {
		
		levels.clear();
		
		// select the scheduling method to be used
		this.sm = setSchMethod(schName);
		this.sm.setScheduler(sm);
		
		// no Process is currently active
		this.activeProc = null;
		
		// create new process to be added, set to null
		Process proc = null;
		int pid = 1;
		double burstTime = 0;
		double delayTime = 0;
		int priority = 0;
		
		// convert input data passed in into an array
		String[] lines = data.split("\n");
		
		for (String line: lines) {
			// split by space
			String[] split = line.split("\\s+");
			
			// match input to data type
			burstTime = Double.parseDouble(split[0]);
			delayTime = Double.parseDouble(split[1]);
			priority = (int) Double.parseDouble(split[2]);
			
			// update proc properties
			proc = new Process(pid, burstTime, delayTime, priority);
			
			// set level of process
			proc.setLevel((int) Double.parseDouble(split[3]));
			
			// increment PID (& for loop counter)
			pid++;
			
			// add proc to the arraylist of all processes
			this.allProcs.add(proc);
		}
		
		// initialize ProcQueue
		initProcQueue(this.allProcs);
	}
	
	/*
	 * Constructor for multi-level
	 */
	public CPU(String data, ArrayList<String> schName, String isPreemptive) {
		levels.addAll(schName);
		
		
		// select the scheduling method to be used
		this.sm = setSchMethod(isPreemptive + "Multi Level");
		this.sm.setScheduler(sm);
		
		// no Process is currently active
		this.activeProc = null;
		
		// create new process to be added, set to null
		Process proc = null;
		int pid = 1;
		double burstTime = 0;
		double delayTime = 0;
		int priority = 0;
		
		// convert input data passed in into an array
		String[] lines = data.split("\n");
		
		for (String line: lines) {
			// split by space
			String[] split = line.split("\\s+");
			
			// match input to data type
			burstTime = Double.parseDouble(split[0]);
			delayTime = Double.parseDouble(split[1]);
			priority = (int) Double.parseDouble(split[2]);
			
			// update proc properties
			proc = new Process(pid, burstTime, delayTime, priority);
			
			// set level of process
			proc.setLevel((int) Double.parseDouble(split[3]));
			
			// increment PID (& for loop counter)
			pid++;
			
			// add proc to the arraylist of all processes
			this.allProcs.add(proc);
		}
		
		// initialize ProcQueue
		initProcQueue(this.allProcs);
	}
	
	// Generate the data for a number of random processes
	public static void randProc(int processNum, boolean isMulti, int levelNum) {
//	public static void randProc(int processNum) {
		Process proc;
		randomData.clear();
		for (int i = 0; i < processNum; i++) {
			// PID, meanBurst, stdDevBurst, meanDelay, stdDevDelay
			proc = new Process(i + 1, 8.33, 2.1, 2.46, 0.7);
			
			// generate random level
			proc.setLevel(new Random().nextInt(levelNum)+1);
			
			// add random data as a String, in the format of:
			// burstTime delayTime priority
			randomData.add(proc.getBurstTime() + " "
					+ proc.getDelayTime() + " "
					+ proc.getPriority() + " "
					+ proc.getLevel());
		}
	}
	
	// Initialize procQueue, the queue that will contain data after calculation
	private void initProcQueue(ArrayList<Process> allProcess) {
		// new null Process
		Process proc;
		double arrivalTime = 0;
		
		// iterate through allProcess
		for (int i = 0; i < allProcess.size(); i++) {
			// temp store ith element in allProcess
			proc = (Process) allProcess.get(i);
			
			// set new arrival time, taking into account the delay time
			arrivalTime += proc.getDelayTime();
			proc.setArrivalTime(arrivalTime);
			
			// add proc to the processQueue
			this.procQueue.add(proc);
		}
	}
	
	// Processes with arrival time and currenttime being the same
	private void initReadyQueue() {
		// new null Process
		Process proc;
		// iterate through procQueue
		for (int i = 0; i < this.procQueue.size(); i++) {
			// temp store ith element in procQueue
			proc = (Process) this.procQueue.get(i);
			
			// (arrivalTime - currentTime = 0)
			if (proc.getArrivalTime() - this.currentTime < 0.1) {
				// add proc to the readyQueue
				this.readyQueue.add(proc);
				
				// finally add proc to the PriorityQueue that will be executed
				this.sm.addProc(proc);
			}
		}
		
	}
	
	// Refresh readyQueue after process is done
	private void refReadyQueue() {
		// new null Process
		Process proc;
		// iterate through readyQueue
		for (int i = 0; i < this.readyQueue.size(); i++) {
			// temp store ith element in readyQueue
			proc = (Process) this.readyQueue.get(i);
			
			// remove process from readyQueue if process is finished
			if (proc.isFinished() == true) {
				this.readyQueue.remove(i);
				// also remove from PriorityQueue that will be executed
				this.sm.removeProc(proc);
			}
		}
	}
	
	// Refresh procQueue after process is done
	private void refProcQueue() {
		// new null Process
		Process proc;
		// iterate through procQueue
		for (int i = 0; i < this.procQueue.size(); i++) {
			// temp store ith element in procQueue
			proc = (Process) this.procQueue.get(i);
			// remove process from procQueue if process is finished
			if (proc.isFinished() == true) {
				this.procQueue.remove(i);
				// also remove from PriorityQueue that will be executed
				this.sm.removeProc(proc);
			}
		}
	}
	
	// Schedule
	public void Schedule() {
		Process proc = null;
		this.activeProc = this.sm.getNextProc(this.currentTime);
		if (this.activeProc != this.preProc && this.preProc != null) {
			if (this.cs > 0.4) {
				this.currentTime += (this.cs - 0.4);
			}
			this.csCount++;
		}
		if (this.activeProc != null) {
			this.activeProc.executing(this.currentTime);
			this.simData += this.activeProc.toString();
			this.preProc = this.activeProc;
		}
		for (int i = 0; i < this.readyQueue.size(); ++i) {
			proc = (Process) this.readyQueue.get(i);
			if (proc.getPID() != this.activeProc.getPID()) {
				proc.waiting(this.currentTime);
			}
		}
	}
	
	// Generate text report and save to (String)report
	private void report() {
		// new null Process
		Process proc = null;
		// process count
		int procCount = 0;
		
		for (int i = 0; i < this.allProcs.size(); i++) {
			proc = (Process) this.allProcs.get(i);
			
			if (proc.isFinished()) {
				procCount++;
				double waited = proc.getWaitTime();
				double turned = proc.getTurnAroundTime();
				this.AvgWaitTime += waited;
				this.AverageTurnAroundTime += turned;
			}
		}
		
		if (procCount > 0) {
			this.AvgWaitTime /= (double) procCount;
			this.AverageTurnAroundTime /= (double) procCount;
		}
		else {
			this.AvgWaitTime = 0.0;
			this.AverageTurnAroundTime = 0.0;
		}
		
		this.Utility = ((this.currentTime - (this.cs * this.csCount)) / this.currentTime) * 100;
		this.Potency = this.currentTime / procCount;
		
		this.report = "Average Wait Time: " + String.format("%.1f", this.AvgWaitTime)
				+ "\nAverage Turn Around Time: " + String.format("%.1f", this.AverageTurnAroundTime)
		        + "\nUtility: " + String.format("%.1f", this.Utility) + "%"
		        + "\nPotency: " + String.format("%.1f", this.Potency);
	}
	
	// Set scheduling method from a String
	public static Scheduler setSchMethod(String method) {
		
		String split[] = method.split(":");
		
		switch (split[0]) {
			case "FCFS":
				return new Scheduler_FCFS();
			// ***** spell out PSJF
			case "PSJF": // Pre-emptive SJF
				return new Scheduler_SJF(true);
			case "SJF":
				return new Scheduler_SJF(false);
			// ***** switch order?
			case "Preemptive Priority":
				return new Scheduler_Priority(true);
			case "Priority":
				return new Scheduler_Priority(false);
			case "Round Robin":
				return new Scheduler_RR(Double.valueOf(split[1]));
			case "Lottery":
				return new Scheduler_Lottery();
			case "Multi Level":
				return new Scheduler_Multilevel(levels, false);
			case "Preemptive Multi Level":
				return new Scheduler_Multilevel(levels, true);
		}
		return null;
	}
	
	// Simulation
	public void Simulate() {
		
		boolean check;
		check = true;
		
		// show decimals to the tenth digit, and round down
		DecimalFormat df = new DecimalFormat("#.00");
		df.setRoundingMode(RoundingMode.FLOOR);
		
		while (check) {
			// if empty then done
			if (this.procQueue.isEmpty()) {
				check = false;
			}
			// if not empty
			else {
				initReadyQueue();
				check = true;
				if (!this.readyQueue.isEmpty()) {
					Schedule();
					refProcQueue();
					refReadyQueue();
				}
				this.currentTime += 0.1;
				this.currentTime = Double.valueOf(df.format(this.currentTime));
			}
		}
		report();
		resetAll();
	}
	
	public void resetAll() {
		Process proc;
		
		this.activeProc = null;
		this.sm = null;
		this.currentTime = 0;
		this.csCount = 0;
		this.AvgWaitTime = 0.0;
		this.AverageTurnAroundTime = 0.0;
		this.Utility = 0.0;
		this.Potency = 0.0;
		
		for (int i = 0; i < this.allProcs.size(); i++) {
			proc = (Process) this.allProcs.get(i);
			proc.resetAll();
		}
		
		this.procQueue.clear();
		this.readyQueue.clear();
		initProcQueue(this.allProcs);
	}
	
	public Process getActiveProc() {
		return this.activeProc;
	}
	
	public double getCurrentTime() {
		return this.currentTime;
	}
	
	public String getSimData() {
		return this.simData;
	}
	
	public String getReport() {
		return this.report;
	}
	
	public void setCs(double cs) {
		if (cs > 0.4) {
			this.cs = cs;
		}
	}
	
	public void resetSimData() {
		this.simData = "";
	}
	
	public void resetReport() {
		this.report = "";
	}
	
	public ArrayList<Process> getAllProcs() {
		return this.allProcs;
	}
	
	public static ArrayList<String> getRandomData() {
		return randomData;
	}
}
