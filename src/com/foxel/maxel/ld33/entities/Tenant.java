package com.foxel.maxel.ld33.entities;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.map.Map;
import com.foxel.maxel.ld33.resources.Action;

public class Tenant extends Entity {
	/*
	 * Tenants of each house will use this class ### MACE ###
	 */

	private AStarPathFinder pathFinder;
	private Path path;
	private int pathIndex;
	private int tileSize;
	private SpriteSheet sprites;
	private Animation main, left, right, up, down;
	private Image mainIdle, leftIdle, rightIdle, upIdle, downIdle;
	private boolean idle = false;
	private String direction = "DOWN";
	private float movementSpeed = Constants.TENANT_MOVE_SPEED;
	private ArrayList<Action> schedule;
	private Action currentAction;
	private int currentActionIndex;
	private ArrayList<Action> overrideActions;
	private int actionTimer = 0;
	private int actionTime = 0;

	public Tenant(Map map, float x, float y) {
		super(map);
		tileSize = Constants.TILESIZE;
		this.x = x;
		this.y = y;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		sprites = new SpriteSheet(new Image(Constants.TENANT_SPRITESHEET_LOC), TILESIZE, 96);
		// Loading Tenant idle images
		leftIdle = sprites.getSubImage(10, 0);
		rightIdle = sprites.getSubImage(15, 0);
		upIdle = sprites.getSubImage(5, 0);
		downIdle = sprites.getSubImage(0, 0);

		mainIdle = downIdle;
		// Loading all animations
		left = new Animation(sprites, 11, 0, 14, 0, true, 180, false);
		right = new Animation(sprites, 16, 0, 19, 0, true, 180, false);
		up = new Animation(sprites, 6, 0, 9, 0, true, 180, false);
		down = new Animation(sprites, 1, 0, 4, 0, true, 180, false);

		main = down;

		collider = new Rectangle(x, y, 64, 96);

		pathFinder = new AStarPathFinder(map, 100, false);
		pathIndex = 0;

		schedule = new ArrayList<Action>();
		overrideActions = new ArrayList<Action>();
		schedule.add(new Action(2f, map.getSpot("fridge"), false));
		schedule.add(new Action(5f, map.getSpot("bed"), false));
		currentAction = schedule.get(0);
		actionTime = (int) (currentAction.time * 1000f);
		getActionPath();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (idle)
			g.drawImage(mainIdle, x * TILESIZE, y * TILESIZE - 32);
		else
			g.drawAnimation(main, x * TILESIZE, y * TILESIZE - 32);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (!idle) {
			Vector2f move = getPathVector();

			// System.out.println("X = " + move.x + " - Y = " + move.y);

			// Check if the tenant is idle
			if (move.x == 0 && move.y == 0)
				idle = true;
			else {
				idle = false;

				// Check which direction the tenant is moving and animate
				// correctly
				if (Math.abs(move.x * 0.003f * delta) > Math.abs(move.y * 0.003f * delta)) {
					if (move.x < 0) {
						main = left;
						mainIdle = leftIdle;
						direction = "left";
					} else {
						main = right;
						mainIdle = rightIdle;
						direction = "right";
					}
				} else {
					if (move.y < 0) {
						main = up;
						mainIdle = upIdle;
						direction = "up";
					} else {
						main = down;
						mainIdle = downIdle;
						direction = "down";
					}
				}
			}

			// Move entity by move vector
			moveTowards(move, new Vector2f(path.getX(pathIndex), path.getY(pathIndex)), delta);

			collider.setLocation(x * TILESIZE, y * TILESIZE);
		} else {
			actionTimer += delta;
			if (actionTimer > actionTime) {
				getNextAction();
			}
		}

		// Update main animation
		main.update(delta);
	}

	private void getNextAction() {
		currentActionIndex++;
		if (currentActionIndex >= schedule.size())
			currentActionIndex = 0;

		currentAction = schedule.get(currentActionIndex);
		actionTimer = 0;
		actionTime = (int) (currentAction.time * 1000f);
		idle = false;

		getActionPath();
	}

	private void getActionPath() {
		System.out.println("Moving from " + x + ", " + y + " to " + currentAction.position.x + ", "
				+ currentAction.position.y);
		path = pathFinder.findPath(null, (int) (x), (int) (y), (int) (currentAction.position.x),
				(int) (currentAction.position.y));
		if (path == null)
			getNextAction();
		pathIndex = 0;
	}

	private Vector2f getPathVector() {
		Vector2f entityLocation = new Vector2f(x, y);
		Vector2f pathLocation = new Vector2f(path.getX(pathIndex), path.getY(pathIndex));
		Vector2f pathVector = new Vector2f();

		if (pathIndex < (path.getLength() - 1) && pathLocation.distance(entityLocation) < 0.1f) {
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
	}

	private void moveTowards(Vector2f move, Vector2f dest, int delta) {
		float deltaX = Math.abs(dest.x - x);
		float deltaY = Math.abs(dest.y - y);

		float moveXBy = move.x * movementSpeed * delta / 1000f;
		float moveYBy = move.y * movementSpeed * delta / 1000f;

		if (moveXBy > deltaX)
			moveXBy = move.x * deltaX;
		if (moveYBy > deltaY)
			moveYBy = move.y * deltaY;

		x += moveXBy;
		y += moveYBy;
	}

	public void distract(Vector2f source) {
		if (source.x != currentAction.position.x && source.y != currentAction.position.y) {
			source.x /= tileSize;
			source.y /= tileSize;
			// overrideActions.add(new Action(5f, source, true));
			currentAction = new Action(5f, source, true);
			getActionPath();
		}
	}
}