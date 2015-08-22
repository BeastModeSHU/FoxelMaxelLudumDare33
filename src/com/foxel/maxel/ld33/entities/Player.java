package com.foxel.maxel.ld33.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.foxel.maxel.ld33.map.Map;

public class Player extends Entity {

	private Image image;

	public Player(Map map) {
		super(map);

	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

	}

	@Override
	protected void moveEntity(Vector2f move) {

	}

	@Override
	public Vector2f getEntityDimensions() {

		return new Vector2f(image.getWidth(), image.getHeight());
	}

}
