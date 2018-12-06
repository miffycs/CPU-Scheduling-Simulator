/*
 * @author: Miffy Chen & James Yu
 * @date:   2018/11/15
 * 
 * Scheduler_SJF.java
 * 
 */

package cpuscheduler;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;


/**
 * Shortest Job First
 */
public class Scheduler_SJF extends Scheduler {
	
	private boolean preemptive;
	private PriorityQueue<Process> pq;
	
	// Constructor
	public Scheduler_SJF(boolean isPreemptive) {
		preemptive = isPreemptive;
		pq = new PriorityQueue<>(new Comparator<Process>() {
			
			@Override
			public int compare(Process o1, Process o2) {
				return (o1.getBurstTime() >= o2.getBurstTime()) ? 1 : -1;
			}
		});
	}
	
	@Override
	public void addProc(Process p) {
		pq.add(p);
	}
	
	@Override
	public boolean removeProc(Process p) {
		return pq.remove(p);
	}
	
	@Override
	public void setScheduler(Scheduler method) {
		Iterator<Process> itr = pq.iterator();
		while (itr.hasNext()) {
			method.addProc(itr.next());
			itr.remove();
		}
	}
	
	@Override
	public Process getNextProc(double currentTime) {
		if ((isPreemptive() && pq.peek().isArrived()) || activeProc == null
		        || activeProc.isFinished()) {
			activeProc = pq.peek();
		}
		return activeProc;
	}
	
	@Override
	public boolean isProcLeft() {
		return !pq.isEmpty();
	}
	
	public boolean isPreemptive() {
		return preemptive;
	}
	
	@Override
	public String getName() {
		return !isPreemptive() ? "SJF" : "Premetive SJF";
	}
	
}
