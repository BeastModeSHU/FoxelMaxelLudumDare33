package com.foxel.maxel.ld33.map;

import org.newdawn.slick.geom.Circle;

public class Noisemaker extends Interactable {
	
	public Circle distractionCircle;
	
	public Noisemaker(float x, float y, float range)
	{
		super(x, y);
		distractionCircle = new Circle(x, y, range);
		id = "noisemaker";
	}
	
	public Circle getDistractionCircle()
	{
		return distractionCircle;
	}
}