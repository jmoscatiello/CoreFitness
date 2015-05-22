package com.polymorphicstudios.actor;

public class WeekDay {

	private int day;
	private int programID;
	private String programName;
	private int complete;
	private int enabled;
	private String timeTaken;
	private int level;
	
	public WeekDay(int day, int programID, String programName, String timeTaken, int level, int complete, int enabled)
	{
		this.day = day;
		this.programID = programID;
		this.programName = programName;
		this.complete = complete;
		this.enabled = enabled;
		this.timeTaken = timeTaken;
		this.level = level;
	}
	
	public int getDay()
	{
		return day;
	}
	
	public String getTimeTaken()
	{
		return timeTaken;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public int getEnabled()
	{
		return enabled;
	}
	
	public int getProgramID()
	{
		return programID;
	}
	
	public String getProgramName()
	{
		return programName;
	}
	
	public int getComplete()
	{
		return complete;
	}
}
