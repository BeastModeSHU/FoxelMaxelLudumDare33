package com.foxel.maxel.ld33.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.map.Map;

public class Tenant extends Entity {
	/*
	 * Tenants of each house will use this class ### MACE ###
	 */

	private Image image;
	private AStarPathFinder pathFinder;
	private Path path;
	private int pathIndex;

	public Tenant(Map map) {
		super(map);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		image = new Image(Constants.TEMP_TENANT_LOC);

		x = map.getTenantStart().x;
		y = map.getTenantStart().y;

		pathFinder = new AStarPathFinder(map, 100, false);
		path = pathFinder.findPath(null, (int) (x), (int) (y), (int) (map.getPlayerStart().x),
				(int) (map.getPlayerStart().y));

		pathIndex = 0;

		for (int i = 0; i < path.getLength(); ++i) {
			System.out.println("X = " + path.getX(i) + " - Y = " + path.getY(i));
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(image, x * TILESIZE, y * TILESIZE);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Vector2f move = getPathVector();

		moveEntity(move, delta);
	}

	private Vector2f getPathVector() {

		Vector2f entityLocation = new Vector2f(x, y);
		Vector2f pathLocation = new Vector2f(path.getX(pathIndex), path.getY(pathIndex));
		Vector2f pathVector = new Vector2f();
		
		if (pathIndex < (path.getLength() - 1) && pathLocation.distance(entityLocation) < 0.004f) {
			++pathIndex;
			pathLocation.x = path.getX(pathIndex);
			pathLocation.y = path.getY(pathIndex);
		}


		pathVector.x = pathLocation.x - entityLocation.x;
		pathVector.y = pathLocation.y - entityLocation.y;

		if (pathVector.x > 0.f || pathVector.x < 0.f)
			pathVector.x = pathVector.x / Math.abs(pathVector.x);
		if (pathVector.y > 0.f || pathVector.y < 0.f)
			pathVector.y = pathVector.y / Math.abs(pathVector.y);

		return new Vector2f(pathVector);
	}

	@Override
	public Vector2f getEntityDimensions() {
		return new Vector2f(image.getWidth(), image.getHeight());
	}

	@Override
	protected void moveEntity(Vector2f move, int delta) {
		x += move.x * 0.003f * delta;
		y += move.y * 0.003f * delta;
	}

}
