package com.polymorphicstudios.actor;

public class Routine {

	int id;
	String name;
	
	public Routine(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public String getRoutineName()
	{
		return name;
	}
	
	public int getID()
	{
		return id;
	}
	
}
