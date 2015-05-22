package com.polymorphicstudios.actor;

import java.util.ArrayList;

public class Workout {

	private String instruction;
	private String name;
	private int imageID;
	private int workoutID;
	private int timeInSec;
	private String equipment;
	private String rep;
	private ArrayList<Integer> mediaIDs;
	
	
	public Workout(int workoutID, String name, String instruction, String equipment, int imageID)
	{
		this.workoutID = workoutID;
		this.name = name;
		this.instruction = instruction;
		this.equipment = equipment;
		this.imageID = imageID;
		
	}
	
	public Workout(int workoutID, String name, String instruction, String equipment, ArrayList<Integer> mediaIDs)
	{
		this.workoutID = workoutID;
		this.name = name;
		this.instruction = instruction;
		this.equipment = equipment;
		this.mediaIDs = mediaIDs;
	}
	
	public Workout(int workoutID, String name, String instruction, String equipment, String rep, int timeInSec, ArrayList<Integer> mediaIDs)
	{
		this.workoutID = workoutID;
		this.name = name;
		this.instruction = instruction;
		this.equipment = equipment;
		this.mediaIDs = mediaIDs;
		this.rep = rep;
		this.timeInSec = timeInSec;
	}
	
	public ArrayList<Integer> getmediaArray()
	{
		return mediaIDs;
	}
	
	public int getworkoutID()
	{
		return workoutID;
	}
	
	public String getEquipment()
	{
		return equipment;
	}
	
	public int getImageID()
	{
		return imageID;
	}
	
	public int getTimeInSec()
	{
		return timeInSec;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getRep()
	{
		return rep;
	}
	
	public String getInstruction()
	{
		return instruction;
	}

}
