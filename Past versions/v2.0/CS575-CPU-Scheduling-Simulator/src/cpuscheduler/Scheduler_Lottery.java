/*
 * @author: Miffy Chen & James Yu
 * @date:   2018/11/19
 * 
 * Scheduler_Lottery.java
 * 
 */

package cpuscheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/**
 * Lottery
 */
public class Scheduler_Lottery extends Scheduler {
	
	private ArrayList<Process> procList;
	private Random gen;
	
	// Constructor
	public Scheduler_Lottery() {
		procList = new ArrayList<>();
		gen = new Random(System.nanoTime());
	}
	
	@Override
	public void addProc(Process p) {
		procList.add(p);
	}
	
	@Override
	public boolean removeProc(Process p) {
		return procList.remove(p);
	}
	
	@Override
	public void setScheduler(Scheduler method) {
		Iterator<Process> itr = procList.iterator();
		while (itr.hasNext()) {
			method.addProc(itr.next());
			itr.remove();
		}
	}
	
	@Override
	public Process getNextProc(double currentTime) {
		return procList.get(gen.nextInt(procList.size()));
	}
	
	@Override
	public boolean isProcLeft() {
		return !procList.isEmpty();
	}
	
	@Override
	public String getName() {
		return "Lottery";
	}
	
}
