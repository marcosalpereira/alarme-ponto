package org.marcosoft.alarm;

import java.util.Observable;

public class BeepConfig extends Observable {
	
	private String command;
	private int times;
	private int pause;
	
	public BeepConfig(String command, int times, int pause) {
		this.command = command;
		this.times = times;
		this.pause = pause;
	}

	public String getCommand() {
		return command;
	}

	public int getTimes() {
		return times;
	}

	public int getPause() {
		return pause;
	}
		
}
