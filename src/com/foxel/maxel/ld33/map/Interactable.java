package com.foxel.maxel.ld33.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import com.foxel.maxel.ld33.constants.Constants;

public abstract class Interactable {

	protected final String ID;
	protected final float x, y;
	protected Circle activationCircle;
	protected boolean active = true;
	protected boolean activated = false;
	
	protected final int TILESIZE;

	public Interactable(float x, float y, String ID) {
		this.x = x;
		this.y = y;
		this.ID = ID;
		this.TILESIZE = Constants.TILESIZE;
		activationCircle = new Circle(x, y, Constants.ACTIVATION_RANGE);
	}

	public void activate() {
		if (!activated)
			activated = true;
	}

	public void deactivate() {
		if (activated)
			activated = false;
	}
	
	public boolean isActivated(){ 
		return activated;
	}
	public Circle getActivationCircle() {
		return activationCircle;
	}

	public abstract void render(Graphics g) throws SlickException;

	public Vector2f getLocation() {
		return new Vector2f(x, y);
	}
}