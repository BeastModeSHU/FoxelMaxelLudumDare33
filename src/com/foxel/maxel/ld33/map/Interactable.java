package com.foxel.maxel.ld33.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import com.foxel.maxel.ld33.constants.Constants;

public abstract class Interactable {

	protected final int ID;
	protected final float x, y;
	protected Circle activationCircle;
	protected boolean active = true;
	protected boolean activated = false;
	protected final int TILESIZE;
	protected Image image;
	protected Image interactIcon;
	protected boolean buttonShow = false;

	public Interactable(float x, float y, int ID) {
		this.TILESIZE = Constants.TILESIZE;
		this.x = x;
		this.y = y;
		this.ID = ID;

		activationCircle = new Circle(x, y, Constants.ACTIVATION_RANGE);
		try {
			interactIcon = new Image(Constants.PRESS_BUTTON_ICON);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void activate() {
		if (!activated)
			activated = true;
	}

	public void deactivate() {
		if (activated)
			activated = false;
	}

	public boolean isActivated() {
		return activated;
	}

	public Circle getActivationCircle() {
		return activationCircle;
	}

	public abstract void render(Graphics g) throws SlickException;

	public Vector2f getLocation() {
		return new Vector2f(x, y);
	}

	public int getID() {
		return ID;
	}

	public void isShowingButton(boolean button) {
		buttonShow = button;
	}
}