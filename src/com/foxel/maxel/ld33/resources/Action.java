package com.foxel.maxel.ld33.resources;

import org.newdawn.slick.geom.Vector2f;

public class Action
{
	public float time;
	public Vector2f position;
	public boolean override = false;
	
	public Action(float time, Vector2f position, boolean override)
	{
		this.time = time;
		this.position = position;
		this.override = override;
	}
}
