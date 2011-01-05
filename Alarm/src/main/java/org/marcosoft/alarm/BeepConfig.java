package org.marcosoft.alarm;

import java.util.Observable;

public class BeepConfig extends Observable {
	
	private final String command;
	private final int times;
	private final int pause;
	
	private boolean mute;
	
	public BeepConfig(String command, int times, int pause) {
		this.command = command;
		this.times = times;
		this.pause = pause;
	}
	
	public void setMute(boolean mute) {
		this.mute = mute;
	}
	
	public boolean isMute() {
		return mute;
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
