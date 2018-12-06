/*
 * @author: Miffy Chen & James Yu
 * @date:   2018/11/28
 * 
 * Scheduler_Multilevel.java
 * 
 */

package cpuscheduler;

import java.util.ArrayList;


/**
 * Multi-level
 */
public class Scheduler_Multilevel extends Scheduler {
	
	private ArrayList<Scheduler> levels;
	private boolean preemptive;
	
	// Constructor
	public Scheduler_Multilevel(ArrayList<String> lvls, boolean isPreemptive) {
		levels = new ArrayList<>();
		for (String lvl: lvls) {
			levels.add(CPU.setSchMethod(lvl));
		}
		this.preemptive = isPreemptive;
		activeProc = null;
	}
	
	@Override
	public void addProc(Process p) {
		levels.get(p.getLevel() - 1).addProc(p);
	}
	
	@Override
	public boolean removeProc(Process p) {
		for (int i = 0; i < levels.size(); i++) {
			if (levels.get(i).removeProc(p)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void setScheduler(Scheduler method) {
		for (int i = 0; i < levels.size(); i++) {
			levels.get(i).setScheduler(method);
		}
	}
	
	@Override
	public Process getNextProc(double currentTime) {
		if (activeProc != null && activeProc.isFinished()) {
			activeProc = null;
		}
		if (isPreemptive() || activeProc == null) {
			for (int i = 0; i < levels.size(); i++) {
				if (levels.get(i).isProcLeft()) {
					activeProc = levels.get(i).getNextProc(currentTime);
					break;
				}
			}
		}
		return activeProc;
	}
	
	public boolean isPreemptive() {
		return preemptive;
	}
	
	@Override
	public boolean isProcLeft() {
		return false;
	}
	
	@Override
	public String getName() {
		return !isPreemptive() ? "Multi Level" : "Preemptive Multi Level";
	}
	
}
