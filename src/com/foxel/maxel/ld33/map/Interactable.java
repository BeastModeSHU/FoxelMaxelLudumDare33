package com.foxel.maxel.ld33.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

import com.foxel.maxel.ld33.constants.Constants;

public abstract class Interactable {

	public float x, y;
	public Circle activationCircle;
	public boolean active = true;
	public boolean activated = false;
	public String id = "";
	protected final int TILESIZE; 
	
	public Interactable(float x, float y) {
		this.x = x;
		this.y = y;
		this.TILESIZE = Constants.TILESIZE;
		activationCircle = new Circle(x, y, Constants.ACTIVATION_RANGE);
	}

	public void activate() {
		if (active)
			activated = true;
	}

	public void confirmActivation() {
		activated = false;
	}

	public Circle getActivationCircle() {
		return activationCircle;
	}

	public abstract void render(Graphics g) throws SlickException;
}