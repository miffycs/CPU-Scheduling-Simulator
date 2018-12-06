/*
 * @author: Miffy Chen
 * @author: James Yu
 * @date:   2018/12/06
 * 
 * Sch_SJF.java
 * 
 */

package cpuschedulingsimulator;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Sch_SJF extends Algo implements Runnable {
	
	Controller controller;
	boolean finishedGanttChart = false;
	
	void addtoGanttChart() {
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
		while (controller.clock <= controller.totalBT) {
			this.clock();
		}
	}
	
	public void clock() {
		boolean ganttC = false;
		int tp = controller.p.length;
		while (controller.isPaused()) {
			System.out.println("Paused!");
		}
		
		// find any remaining process and assign it
		int lastShortest = controller.p[0].getRemainingBT();
		for (Process p: controller.p) {
			if (!p.isFinished())
				lastShortest = p.getRemainingBT();
		}
		
		Process currentSelected = controller.selectProcess;
		for (int i = 0; i < tp; i++) {
			if (!controller.p[i].isFinished()
			        && controller.p[i].getArrivalTime() <= controller.clock
			        && (controller.p[i].getRemainingBT() <= lastShortest)) {
				
				currentSelected = controller.p[i];
				lastShortest = controller.p[i].getRemainingBT();
			}
		}
		if (!currentSelected.equals(controller.selectProcess)) {
			addtoGanttChart();
			ganttC = true;
			
		}
		controller.selectProcess = currentSelected;
		if (controller.selectProcess == null) {
			// Work finished
		}
		else {
			controller.selectProcess.run();
		}
		
		controller.update();
		System.out.println(controller.clock);
		
		if (controller.clock >= controller.totalBT && !ganttC && !finishedGanttChart) {
			addtoGanttChart();
			finishedGanttChart = true;
		}
		
		sleep();
	}
	
	public Sch_SJF(Controller controller) {
		this.controller = controller;
	}
	
	private void sleep() {
		try {
			Thread.sleep((long) (controller.delay * 1000));
		} catch (InterruptedException ex) {
			Logger.getLogger(Sch_FCFS.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
