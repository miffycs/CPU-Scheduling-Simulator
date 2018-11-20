
package cpuscheduler;

/*
 * @author: Miffy Chen
 * @date:   2018/11/15
 * 
 * Scheduler.java
 * 
 */

/**
 * Abstract class for all scheduling methods
 */
public abstract class Scheduler {
	
	// the current active process
	protected Process activeProc;
	
	// add a process to the queue
	public abstract void addProc(Process p);
	
	// remove a process from the queue
	public abstract boolean removeProc(Process p);
	
	// using current method of scheduling, add all Processes to the PriorityQueue
	public abstract void setScheduler(Scheduler method);
	
	// get next process in queue
	public abstract Process getNextProc(double currentTime);
	
	// there are more processes to be executed
	public abstract boolean isProcLeft();
	
	// return name of scheduling method
	public abstract String getName();
	
	// CPU is finished running all the processes in the PriorityQueue
	public boolean isJobFinished() {
		if (activeProc != null)
			return activeProc.isIsFinished();
		return true;
	}
}
