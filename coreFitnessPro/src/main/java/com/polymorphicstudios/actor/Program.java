package com.polymorphicstudios.actor;

public class Program {

	private String name;
	private int id;
	private String time;
	private int enabled;
	private int pWId;
	private int level;
	
	
	public Program(int id, String name, String time, int pWId, int level, int enabled)
	{
		this.id = id;
		this.name = name;
		this.time = time;
		this.pWId = pWId;
		this.level = level;
		this.enabled = enabled;
	}
	
	public int getProgramId()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getTime()
	{
		return this.time;
	}
	
	public int getProgramWorkoutId()
	{
		return this.pWId;
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	public int getEnabled()
	{
		return this.enabled;
	}
}
