package com.foxel.maxel.ld33.entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
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

	private AStarPathFinder pathFinder;
	private Path path;
	private int pathIndex;
	private SpriteSheet sprites;
	private Animation main, left, right, up, down;
	private Image mainIdle, leftIdle, rightIdle, upIdle, downIdle;
	private boolean idle = true;
	private String direction = "DOWN";

	public Tenant(Map map) {
		super(map);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		sprites = new SpriteSheet(new Image(Constants.TENANT_SPRITESHEET_LOC), TILESIZE, 96);

		leftIdle = sprites.getSubImage(10, 0);
		rightIdle = sprites.getSubImage(15, 0);
		upIdle = sprites.getSubImage(5, 0);
		downIdle = sprites.getSubImage(0, 0);

		mainIdle = downIdle;

		left = new Animation(sprites, 11, 0, 14, 0, true, 180, false);
		right = new Animation(sprites, 16, 0, 19, 0, true, 180, false);
		up = new Animation(sprites, 6, 0, 9, 0, true, 180, false);
		down = new Animation(sprites, 1, 0, 4, 0, true, 180, false);

		main = down;

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
		if (idle)
			g.drawImage(mainIdle, (x * TILESIZE) - mainIdle.getWidth()/2 , (y * TILESIZE) + mainIdle.getHeight()/2);
		else
			g.drawAnimation(main, (x * TILESIZE) - main.getCurrentFrame().getWidth()/2, 
					(y * TILESIZE) + main.getCurrentFrame().getHeight()/2);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Vector2f move = getPathVector();

		// System.out.println("X = " + move.x + " - Y = " + move.y);

		if (move.x == 0 && move.y == 0)
			idle = true;
		else
			idle = false;

		if (Math.abs(move.x * 0.003f * delta) > Math.abs(move.y * 0.003f * delta)) {

			if (move.x < 0)
				main = left;
			else
				main = right;
		} else {

			if (move.y < 0)
				main = up;
			else
				main = down;
		}
		main.update(delta);
		moveEntity(move, delta);
	}

	private Vector2f getPathVector() {

		Vector2f entityLocation = new Vector2f(Math.round(x), Math.round(y));
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

		Vector2f dimensions = new Vector2f(main.getCurrentFrame().getWidth(), main
				.getCurrentFrame().getHeight());

		if (idle) {
			dimensions.x = mainIdle.getWidth();
			dimensions.y = mainIdle.getHeight();
		}

		return dimensions;
	}

	@Override
	protected void moveEntity(Vector2f move, int delta) {

		// System.out.println("X = " + move.x * 0.003f * delta + " - Y = " +
		// move.y * 0.003f * delta);
		x += move.x * 0.003f * delta;
		y += move.y * 0.003f * delta;
	}

}
