/*
 * @author: Miffy Chen
 * @date:   2018/11/15
 * 
 * Scheduler_FCFS.java
 * 
 */

package cpuscheduler;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;


/**
 * First Come First Served
 */
public class Scheduler_FCFS extends Scheduler {
	
	// Priority Queue of Processes
	private PriorityQueue<Process> pq;
	
	/**
	 * Constructor
	 */
	public Scheduler_FCFS() {
		
		// initializing pq to be a new PriorityQueue
		// use Comparator to compare processes by their arrival time
		pq = new PriorityQueue<>(new Comparator<Process>() {
			
			@Override
			public int compare(Process o1, Process o2) {
				return (o1.getArrivalTime() >= o2.getArrivalTime()) ? 1 : -1;
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
		
		// create Iterator of Processes
		Iterator<Process> itr = pq.iterator();
		
		// while there are more Processes to be executed
		while (itr.hasNext()) {
			// using the current method of scheduling,
			// add next Process to the PriorityQueue after this current Process
			method.addProc(itr.next());
			// remove Process added from the Iterator
			itr.remove();
		}
	}
	
	@Override
	public Process getNextProc(double currentTime) {
		return pq.peek();
	}
	
	@Override
	public boolean isProcLeft() {
		return !pq.isEmpty();
	}
	
	@Override
	public String getName() {
		return "FCFS";
	}
	
}
