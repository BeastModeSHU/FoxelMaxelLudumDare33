package com.foxel.maxel.ld33.entities;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.map.Map;
import com.foxel.maxel.ld33.resources.Action;
import com.foxel.maxel.ld33.resources.Camera;
import com.foxel.maxel.ld33.resources.VisionCone;
import com.foxel.maxel.ld33.resources.XMLData;

public class Tenant extends Entity {
	/*
	 * Tenants of each house will use this class ### MACE ###
	 */
	private final float startX, startY;
	private final int TILESIZE;
	private double turnSpeed;
	private double PI = Math.PI;
	private double R2D = 180d / PI;
	private double D2R = PI / 180d;
	private double degAngle = 0d;
	public double angle = 0d;
	private float movementSpeed = Constants.TENANT_MOVE_SPEED;
	private int pathIndex;
	private int actionTimer = 0;
	private int actionTime = 0;
	private int currentActionIndex;
	private AStarPathFinder pathFinder;
	private Path path;
	private SpriteSheet sprites;
	private Animation main, left, right, up, down;
	private Image mainIdle, leftIdle, rightIdle, upIdle, downIdle;
	private Vector2f move = new Vector2f();
	private VisionCone vision;
	public Polygon[] polys;
	private ArrayList<Action> schedule;
	private Action currentAction;
	private ArrayList<Action> overrideActions;
	private Camera camera;
	private String name;
	private String[] spriteSheets;
	private boolean idle = false;
	private boolean turning = false;
	private boolean overrideTrigger = false;

	public Tenant(Map map, String ENTITY_TYPE, float x, float y, String name, Camera camera) {
		super(map, ENTITY_TYPE);

		TILESIZE = Constants.TILESIZE;
		this.startX = x;
		this.startY = y;
		this.name = name;
		this.camera = camera;
		turnSpeed = Constants.TENANT_TURN_SPEED;
		spriteSheets = new String[] { Constants.TENANT01_SPRITESHEET_LOC,
				Constants.TENANT02_SPRITESHEET_LOC };
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		if (sprites == null) {
			int spriteNum = XMLData.getSpriteSheetIndex(name);
			sprites = new SpriteSheet(new Image(spriteSheets[spriteNum - 1]), TILESIZE, 96);
		}
		x = startX;
		y = startY;

		// Loading Tenant idle images
		if (leftIdle == null)
			leftIdle = sprites.getSubImage(10, 0);
		if (rightIdle == null)
			rightIdle = sprites.getSubImage(15, 0);
		if (upIdle == null)
			upIdle = sprites.getSubImage(5, 0);
		if (downIdle == null)
			downIdle = sprites.getSubImage(0, 0);

		mainIdle = downIdle;
		// Loading all animations
		if (left == null)
			left = new Animation(sprites, 11, 0, 14, 0, true, 240, false);
		if (right == null)
			right = new Animation(sprites, 16, 0, 19, 0, true, 240, false);
		if (up == null)
			up = new Animation(sprites, 6, 0, 9, 0, true, 240, false);
		if (down == null)
			down = new Animation(sprites, 1, 0, 4, 0, true, 240, false);

		main = down;

		collider = new Rectangle(x, y, 64, 96);
		if (pathFinder == null)
			pathFinder = new AStarPathFinder(map, 100, false);
		pathIndex = 0;

		schedule = XMLData.getSchedule(name);

		// schedule = new ArrayList<Action>();
		overrideActions = new ArrayList<Action>();
		// schedule.add(new Action(2f, map.getSpot("fridge"), false));
		// schedule.add(new Action(5f, map.getSpot("bed"), false));
		currentAction = schedule.get(0);
		actionTime = (int) (currentAction.time * 1000f);

		path = new Path();
		getActionPath();

		vision = new VisionCone(x, y, (float) (degAngle * D2R), (float) (PI * 0.5d), 16, 32f, 16f,
				map);
		polys = vision.updateCone(x, y, (float) (degAngle * D2R));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		if (idle)
			g.drawImage(mainIdle, x * TILESIZE, y * TILESIZE - 32);
		else
			g.drawAnimation(main, x * TILESIZE, y * TILESIZE - 32);

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (!idle) {
			move = getPathVector();

			// Check if the tenant is idle
			if (move.x == 0 && move.y == 0)
				idle = true;
			else {
				idle = false;

				// Turn entity towards move vector
				turnTowards(move, delta);

				// Check which direction the tenant is moving and animate
				// correctly
				int animAngle = flattenAngle(degAngle);
				switch (animAngle) {
				case 0:
					main = right;
					mainIdle = rightIdle;
					break;
				case 90:
					main = down;
					mainIdle = downIdle;
					break;
				case 180:
					main = left;
					mainIdle = leftIdle;
					break;
				case -90:
					main = up;
					mainIdle = upIdle;
					break;
				}
			}

			// Move entity by move vector
			if (!turning) {
				moveTowards(move, new Vector2f(path.getX(pathIndex), path.getY(pathIndex)), delta);
			}

			collider.setLocation(x * TILESIZE, y * TILESIZE);
		} else {
			actionTimer += delta;
			if (actionTimer > actionTime) {
				if (overrideActions.size() > 0)
					getNextOverride();

				else {
					if (currentAction.override)
						currentActionIndex--;
					getNextAction();
				}
			}
		}

		if (overrideTrigger) {
			getNextOverride();
		}

		// Update main animation
		main.update(delta);

		if (degAngle < -180d)
			degAngle += 360d;
		else if (degAngle > 180d)
			degAngle -= 360d;
		// System.out.println(degAngle);
		angle = degAngle * D2R;
		if (camera.isInLargeView(this.getPixelLocation()))
			polys = vision.updateCone(x * TILESIZE + main.getCurrentFrame().getWidth() / 2, y
					* TILESIZE + main.getCurrentFrame().getHeight() * .5f, (float) (angle));
	}

	// end
	private void getNextAction() {

		currentActionIndex++;
		if (currentActionIndex >= schedule.size())
			currentActionIndex = 0;
		currentAction = schedule.get(currentActionIndex);
		idle = false;
		getActionPath();
		resetActionTimer();
	}

	private void getNextOverride() {

		overrideTrigger = false;
		currentAction = overrideActions.get(0);
		overrideActions.remove(0);
		idle = false;
		getActionPath();
		resetActionTimer();
	}

	private void getActionPath() {

		if (Math.abs(x - currentAction.position.x) <= 0.5f
				&& Math.abs(y - currentAction.position.y) <= 0.5f) {
			idle = true;
		} else {
			path = pathFinder.findPath(null, (int) (x), (int) (y),
					(int) (currentAction.position.x), (int) (currentAction.position.y));
			pathIndex = 0;
		}
		
		if(path == null) {
			
		}
	}

	private void resetActionTimer() {

		actionTimer = 0;
		actionTime = (int) (currentAction.time * 1000f);
	}

	private Vector2f getPathVector() {
		int happy = 0;
		Vector2f entityLocation = new Vector2f(x + happy, y);
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
		// /XXX Redundant
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

	private boolean turnTowards(Vector2f move, int delta) {

		boolean turning = false;

		double tgtAngle = Math.atan2(move.y, move.x) * R2D;
		double deltaAngle = Math.abs(degAngle - tgtAngle);

		if (deltaAngle > 0.05d) {
			turning = true;
			float dir = getAngleDir((float) (degAngle), (float) (tgtAngle));
			double toTurn = turnSpeed * (delta / 1000f);
			if (deltaAngle < toTurn) {
				// System.out.println("flatten angle");
				degAngle = flattenAngle(degAngle);
				turning = false;
			}
			degAngle += toTurn * dir;
		}

		return turning;
	}

	private float getAngleDir(float start, float target) {

		float deltaAngle = Math.abs(start - target);

		if (start < target) {
			if (deltaAngle < 180d)
				return 1f;
			else
				return -1f;
		}
		if (deltaAngle < 180d)
			return -1f;
		else
			return 1f;
	}

	private int flattenAngle(double ang) {

		if (ang > 0d) {
			if (ang < 45d)
				return 0;
			else if (ang < 135d)
				return 90;
			else
				return 180;
		} else {
			if (ang > -45d)
				return 0;
			else if (ang > -135d)
				return -90;
			else
				return -180;
		}
	}

	public void distract(Vector2f source) {

		if (source.x != currentAction.position.x && source.y != currentAction.position.y) {

			if (source.x / 64.f >= 1.f && source.y / 64.f >= 1.f) {
				source.x = (float) Math.ceil(source.x / 64.f); 
				source.y = (float) Math.ceil(source.y / 64.f);
				overrideActions.clear();
				overrideTrigger = true;
				overrideActions.add(new Action(0.5f, new Vector2f(x, y), true));
				overrideActions.add(new Action(4f, source, true));
			}
		}
	}

	@Override
	public float getMaxY() {
		return ((y * TILESIZE) - 48 + main.getCurrentFrame().getHeight());

	}
}