/*
 * @author: Miffy Chen & James Yu
 * @date:   2018/11/19
 * 
 * Scheduler_Priority.java
 * 
 */

package cpuscheduler;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;


/**
 * Preemptive Priority
 */
public class Scheduler_Priority extends Scheduler {
	
	private boolean preemptive;
	private PriorityQueue<Process> pq;
	
	// Constructor
	public Scheduler_Priority(boolean isPreemptive) {
		preemptive = isPreemptive;
		pq = new PriorityQueue<>(new Comparator<Process>() {
			
			@Override
			public int compare(Process o1, Process o2) {
				return (o1.getPriority() >= o2.getPriority()) ? 1 : -1;
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
		if (((isPreemptive() && pq.peek().isArrived()) || activeProc == null
		        || activeProc.isFinished())) {
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
		return !isPreemptive() ? "Priority" : "Preemptive Priority";
	}
	
}
