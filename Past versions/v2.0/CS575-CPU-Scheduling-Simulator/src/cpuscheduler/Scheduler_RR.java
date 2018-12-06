/*
 * @author: Miffy Chen & James Yu
 * @date:   2018/11/18
 * 
 * Scheduler_RR.java
 * 
 */

package cpuscheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;


/**
 * Round Robin
 */
public class Scheduler_RR extends Scheduler {
	
	private double quantum;
	private double curTimeQuantum;
	private int currProc;
	
	private PriorityQueue<Process> pq;
	private ArrayList<Process> rrList;
	
	// Constructor
	public Scheduler_RR(double q) {
		// initializing pq to be a new PriorityQueue
		// use Comparator to compare processes by their arrival time
		pq = new PriorityQueue<Process>(new Comparator<Process>() {
			
			@Override
			public int compare(Process o1, Process o2) {
				return (o1.getArrivalTime() >= o2.getArrivalTime()) ? 1 : -1;
			}
		});
		
		rrList = new ArrayList<Process>();
		quantum = q;
		curTimeQuantum = 0.0;
		activeProc = null;
		currProc = 0;
	}
	
	@Override
	public void addProc(Process p) {
		pq.add(p);
	}
	
	@Override
	public boolean removeProc(Process p) {
		return (pq.remove(p) || rrList.remove(p));
	}
	
	@Override
	public void setScheduler(Scheduler method) {
		// add head of pq to rrList
		while (pq.size() > 0) {
			rrList.add(pq.poll());
		}
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
		while (pq.size() > 0) {
			rrList.add(pq.poll());
		}
		
		if (quantum <= 0.1) {
			activeProc = null;
		}
		else if (rrList.size() > 0) {
			if (activeProc == null) {
				activeProc = rrList.get(currProc);
				curTimeQuantum = 0;
			}
			else if ((quantum - curTimeQuantum < 0.1) && !activeProc.isFinished()) {
				currProc = (currProc + 1) % rrList.size();
				activeProc = rrList.get(currProc);
				curTimeQuantum = 0;
			}
			else if (activeProc.isFinished()) {
				if (currProc == rrList.size()) {
					currProc--;
				}
				activeProc = rrList.get(currProc);
				curTimeQuantum = 0;
			}
			curTimeQuantum += 0.1;
		}
		else {
			activeProc = null;
		}
		return activeProc;
		
	}
	
	@Override
	public boolean isProcLeft() {
		return !pq.isEmpty();
	}
	
	@Override
	public String getName() {
		return "RR";
	}
	
}
