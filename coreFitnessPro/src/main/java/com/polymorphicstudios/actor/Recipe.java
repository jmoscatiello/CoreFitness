package com.polymorphicstudios.actor;

public class Recipe {

	private String name;
	private String summary;
	private String ingredients;
	private String instructions;
	private String serving;
	private String im_name;
    private int id;

    public Recipe(String name, String summary, String ingredients, String instructions, String serving, String im_name) {
        this.name = name;
        this.summary = summary;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.im_name = im_name;
        this.serving = serving;
    }

    public Recipe(int id, String name, String im_name)
    {
        this.id = id;
        this.name = name;
        this.im_name = im_name;
    }


    public Recipe()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getIm_name() {
        return im_name;
    }

    public void setIm_name(String im_name) {
        this.im_name = im_name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
	
	public String getName()
	{
		return this.name;
	}
	
	public String getSummary()
	{
		return this.summary;
	}
	
	public String getServing()
	{
		return this.serving;
	}
	
	public String getInstructions()
	{
		return this.instructions;
	}
	
	public String getIngredients()
	{
		return this.ingredients;
	}
	
	public String getImageName()
	{
		return this.im_name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
}
