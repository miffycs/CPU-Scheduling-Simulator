/*
 * @author: Miffy Chen
 * @author: James Yu
 * @date:   2018/12/06
 * 
 * GanttChartComp.java
 * 
 */

package cpuschedulingsimulator;

public class GanttChartComp {
	
	private int startingPoint;
	private int endingPoint;
	
	private Process process;
	
	// constructor
	public GanttChartComp(Process process, int startingPoint, int endingPoint) {
		this.process = process;
		this.startingPoint = startingPoint;
		this.endingPoint = endingPoint;
	}
	
	public int getStartingPoint() {
		return startingPoint;
	}
	
	public int getEndingPoint() {
		return endingPoint;
	}
	
	public void setStartingPoint(int startingPoint) {
		this.startingPoint = startingPoint;
	}
	
	public void setEndingPoint(int endingPoint) {
		this.endingPoint = endingPoint;
	}
	
	public Process getProcess() {
		return process;
	}
	
}
