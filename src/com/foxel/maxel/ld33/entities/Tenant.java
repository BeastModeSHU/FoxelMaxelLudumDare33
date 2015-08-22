package com.foxel.maxel.ld33.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

import com.foxel.maxel.ld33.map.Map;

public class Tenant extends Entity {

	private AStarPathFinder pathFinder;
	private Path path;

	public Tenant(Map map) {
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
	public Vector2f getEntityDimensions() {

		return null;
	}

	@Override
	protected void moveEntity(Vector2f move, int delta) {

	}

}
