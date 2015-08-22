package com.foxel.maxel.ld33.resources;

import org.newdawn.slick.geom.Vector2f;

public class Action {
	public int index;
	public float time;
	public Vector2f position;
	
	public Action(int index, float time, Vector2f position)
	{
		this.index = index;
		this.time = time;
		this.position = position;
	}
}
