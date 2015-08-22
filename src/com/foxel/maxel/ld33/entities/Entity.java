package com.foxel.maxel.ld33.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.map.Map;

public abstract class Entity {

	/*
	 * ###MACE### Default entity which all entities will extend from
	 */
	protected final int TILESIZE;
	protected float x, y;
	protected Map map;

	public Entity(Map map) {
		this.map = map;
		this.TILESIZE = Constants.TILESIZE;
	}

	public abstract void init(GameContainer gc, StateBasedGame sbg) throws SlickException;

	public abstract void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException;

	public abstract void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException;

	public abstract Vector2f getEntityDimensions();
	
	protected abstract void moveEntity(Vector2f move); 
	
	public Vector2f getPixelLocation() {
		return new Vector2f(x * TILESIZE, y * TILESIZE);
	}

	public Vector2f getTileLocation() {
		return new Vector2f(x, y);
	}
	
}
