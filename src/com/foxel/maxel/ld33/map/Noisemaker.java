package com.foxel.maxel.ld33.map;

import org.newdawn.slick.geom.Rectangle;

public class Noisemaker {
	
	public float x, y;
	public float range;
	public Rectangle collider;
	
	public Noisemaker(float x, float y, float range)
	{
		this.x = x;
		this.y = y;
		this.range = range;
	}
}
