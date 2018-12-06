/*
 * @author: Miffy Chen
 * @author: James Yu
 * @date:   2018/12/06
 * 
 * Sch_RoundRobin.java
 * 
 */

package cpuschedulingsimulator;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Sch_RoundRobin extends Algo implements Runnable {
	
	Controller controller;
	
	void addToGanttChart() {
		GanttChartComp ganttChartComp = new GanttChartComp(controller.selectProcess,
		        controller.selectProcess.getCbt() - controller.selectProcess.getBurstTime(),
		        (controller.clock));
		controller.ganttChart.add(ganttChartComp);
		
	}
	
	@Override
	public void run() {
		if (controller.cont) {
		} else {
			start();
		}
		
	}
	
	void start() {
		while (controller.clock < controller.totalBT) {
			this.clock();
		}
	}
	
	public void clock() {
		int tp = controller.p.length;
		while (controller.isPaused()) {
			System.out.println("Paused!");
		}
		if (controller.selectProcess != null && !controller.selectProcess.isFinished()) {
			for (int i = 0; i < timeQuantum; i++) {
				if (!controller.selectProcess.isFinished()) {
					controller.selectProcess.run();
					controller.update();
					
					sleep();
				}
			}
			addToGanttChart();
		}
		controller.selectProcess = controller.p[(controller.selectProcess.getID() + 1) % (tp)];
	}
	
	public int timeQuantum;
	
	public Sch_RoundRobin(Controller controller, int timeQuantum) {
		this.controller = controller;
		this.timeQuantum = timeQuantum;
	}
	
	void sleep() {
		try {
			Thread.sleep((long) (controller.delay * 1000));
		} catch (InterruptedException ex) {
			Logger.getLogger(Sch_FCFS.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
