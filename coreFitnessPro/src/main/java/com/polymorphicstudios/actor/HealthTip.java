package com.polymorphicstudios.actor;

public class HealthTip {

	private String title;
	private String content;
	
	public HealthTip(String title, String content)
	{
		this.title = title;
		this.content= content;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
}
