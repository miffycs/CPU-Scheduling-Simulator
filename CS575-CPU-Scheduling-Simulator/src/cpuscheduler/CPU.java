
package cpuscheduler;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


/*
 * @author: Miffy Chen
 * @date:   2018/11/15
 * 
 * CPU.java
 * 
 */

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
	
	// Generate random data for processes???
	private static ArrayList<String> randomData = new ArrayList<>();
	
	private Process preProc = null;
	// Active process that is currently being processed
	private Process activeProc = null;
	
	private double AWT = 0.0;			// Average Wait Time
	private double ATT = 0.0;			// Average Turnaround Time
	
	private String simData = "";		// store data about current Process as it's being run
	private String report = "";			// final report
	
	// Takes user data of the Processes and
	// initialize them in a Priority Queue according to the scheduling method selected
	// 1) process data into readable format
	// 2) create new Processes from the data
	// 3) add Processes to allProcs
	// 4) initialize a Priority Queue from allProcs
	public CPU(String data, String schName) {
		
		// select the scheduling method to be used
		this.sm = setSchMethod(schName);
		this.sm.setScheduler(sm);
		
		// no Process is currently active
		this.activeProc = null;
		
		// create new process to be added, set to null
		Process proc = null;
		int i = 1;				// PID
		double b = 0;			// burstTime
		double d = 0;			// delayTime
		int p = 0;				// priority
		
		// convert input data passed in into an array
		String[] lines = data.split("\n");
		
		for (String line: lines) {
			// split by space
			String[] split = line.split("\\s+");
			
			// match input to data type
			b = Double.parseDouble(split[0]);
			d = Double.parseDouble(split[1]);
			p = (int) Double.parseDouble(split[2]);
			
			// set Process to (int PID, double burstTime, double delayTime, int priority)
			proc = new Process(i, b, d, p);
			
			// increment PID (& for loop counter)
			i++;
			
			// add proc to the arraylist of all processes
			allProcs.add(proc);
		}
		
		// initialize ProcQueue
		initProcQueue(allProcs);
	}
	
	// initialize procQueue, the queue that will contain data after calculation
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
			procQueue.add(proc);
		}
	}
	
	// processes with arrival time and currenttime being the same
	private void initReadyQueue() {
		// new null Process
		Process proc;
		// iterate through procQueue
		for (int i = 0; i < procQueue.size(); i++) {
			// temp store ith element in procQueue
			proc = (Process) procQueue.get(i);
			
			// (arrivalTime - currentTime = 0)
			if (proc.getArrivalTime() - currentTime < 0.1) {
				// add proc to the readyQueue
				readyQueue.add(proc);
				
				// finally add proc to the PriorityQueue that will be executed
				sm.addProc(proc);
			}
		}
		
	}
	
	// refresh readyQueue after process is done
	private void refReadyQueue() {
		// new null Process
		Process proc;
		// iterate through readyQueue
		for (int i = 0; i < readyQueue.size(); i++) {
			// temp store ith element in readyQueue
			proc = (Process) readyQueue.get(i);
			
			// remove process from readyQueue if process is finished
			if (proc.isIsFinished() == true) {
				readyQueue.remove(i);
				// also remove from PriorityQueue that will be executed
				sm.removeProc(proc);
			}
		}
	}
	
	// refresh procQueue after process is done
	private void refProcQueue() {
		// new null Process
		Process proc;
		// iterate through procQueue
		for (int i = 0; i < procQueue.size(); i++) {
			// temp store ith element in procQueue
			proc = (Process) procQueue.get(i);
			// remove process from procQueue if process is finished
			if (proc.isIsFinished() == true) {
				procQueue.remove(i);
				// also remove from PriorityQueue that will be executed
				sm.removeProc(proc);
			}
		}
	}
	
	// set scheduling method from a String
	public static Scheduler setSchMethod(String method) {
		
		String split[] = method.split(":");
		
		switch (split[0]) {
			case "FCFS":
				return new Scheduler_FCFS();
			case "PSJF":
				return new Scheduler_SJF(true);
			case "SJF":
				return new Scheduler_SJF(false);
			// case "Round Robin":
			// return new Scheduler_RR();
		}
		return null;
	}
	
	void Schedule() {
		Process proc = null;
		activeProc = sm.getNextProc(currentTime);
		if (activeProc != preProc && preProc != null) {
			if (cs > 0.4)
				currentTime += (cs - 0.4);
			csCount++;
		}
		if (activeProc != null) {
			activeProc.executing(currentTime);
			simData += activeProc.toString();
			preProc = activeProc;
		}
		for (int i = 0; i < readyQueue.size(); ++i) {
			proc = (Process) readyQueue.get(i);
			if (proc.getPID() != activeProc.getPID()) {
				proc.waiting(currentTime);
			}
		}
	}
	
	private void report() {
		//TODO
	}
	
	public void Simulate() {
		
		boolean check = true;
		
		// text format
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.FLOOR);
		
		while (check) {
			// if empty then done
			if (procQueue.isEmpty()) {
				check = false;
			}
			// if not empty
			else {
				initReadyQueue();
				check = true;
				if (!readyQueue.isEmpty()) {
					Schedule();
					refProcQueue();
					refReadyQueue();
				}
				currentTime += 1e-1;
				currentTime = Double.valueOf(df.format(currentTime));
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
		this.AWT = 0.0;
		this.ATT = 0.0;
		
		for (int i = 0; i < allProcs.size(); i++) {
			proc = (Process) allProcs.get(i);
			proc.resetAll();
		}
		
		procQueue.clear();
		readyQueue.clear();
		initProcQueue(allProcs);
	}
	
	public Process getActiveProc() {
		return activeProc;
	}
	
	public double getCurrentTime() {
		return currentTime;
	}
	
}
