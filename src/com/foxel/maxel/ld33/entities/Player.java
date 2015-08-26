package com.foxel.maxel.ld33.entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.map.Map;

public class Player extends Entity {
	/*
	 * Player Class -> Handles all player interactions with the game
	 */
	private final float MOVE_SPEED; // Players moveement speed
	private final String LEFT = "left", RIGHT = "right", UP = "up", DOWN = "down";
	private SpriteSheet sprites; // animation sprites
	private Animation main, left, right, up, down, leftIdle, rightIdle, upIdle, downIdle;
	private boolean spotted;
	private boolean isPlayerHidden;
	private String lastDirection;
	private boolean isIdle = true;
	private float colliderAdjustY;

	public Player(Map map, String ENTITTY_TYPE) {
		super(map, ENTITTY_TYPE);
		this.MOVE_SPEED = Constants.MOVE_SPEED;
	}

	// TODO Hidden boolean to prevent player from being spotted in a bin
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		if (sprites == null)
			sprites = new SpriteSheet(
					new Image(Constants.PLAYER_SPRITESHEET_LOC).getScaledCopy(64.f / 96.f),
					TILESIZE, TILESIZE);

		if (main == null)
			main = new Animation();

		// animation = new Animation(sprites, 0, 0, 3, 0, true, 180, false);
		if (left == null)
			left = new Animation(sprites, 14, 0, 17, 0, true, 250, false);

		if (right == null)
			right = new Animation(sprites, 20, 0, 23, 0, true, 250, false);

		if (up == null)
			up = new Animation(sprites, 8, 0, 11, 0, true, 250, false);

		if (down == null)
			down = new Animation(sprites, 2, 0, 5, 0, true, 250, false);

		if (leftIdle == null)
			leftIdle = new Animation(sprites, 12, 0, 13, 0, true, 500, false);

		if (rightIdle == null)
			rightIdle = new Animation(sprites, 18, 0, 19, 0, true, 500, false);

		if (upIdle == null)
			upIdle = new Animation(sprites, 6, 0, 7, 0, true, 500, false);

		if (downIdle == null)
			downIdle = new Animation(sprites, 0, 0, 1, 0, true, 500, false);

		main = downIdle;

		lastDirection = DOWN;

		x = map.getPlayerStart().x;
		y = map.getPlayerStart().y;

		// collider = new Rectangle(0, 0, 0, 0);

		colliderAdjustY = 10;

		collider = new Rectangle(0, 0, main.getCurrentFrame().getWidth() - 14, main
				.getCurrentFrame().getHeight() - 28);

		collider.setCenterX((x * TILESIZE) + main.getCurrentFrame().getWidth() / 2);
		collider.setCenterY(((y * TILESIZE) + main.getCurrentFrame().getHeight() / 2)
				+ colliderAdjustY);

		spotted = false;
		isPlayerHidden = false;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (!isPlayerHidden)
			g.drawAnimation(main, x * TILESIZE, y * TILESIZE);
//		g.fill(collider);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

		Input input = gc.getInput();
		Vector2f move = new Vector2f(); // Player moveement vector
		if (!isPlayerHidden) {
			// Player controls
			if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
				move.x = -MOVE_SPEED;
				lastDirection = LEFT;
			}

			if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
				move.x = MOVE_SPEED;
				lastDirection = RIGHT;
			}

			if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
				move.y = -MOVE_SPEED;
				lastDirection = UP;
			}

			if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
				move.y = MOVE_SPEED;
				lastDirection = DOWN;
			}

			if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)
					|| input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)
					|| input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)
					|| input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
				isIdle = false;
			} else {
				isIdle = true;
			}

		}

		moveEntity(move, delta);
		updateAnimation(delta);

	}

	@Override
	protected void moveEntity(Vector2f move, int delta) {

		move = move.normalise();

		move.x *= (delta / 1000.f) * MOVE_SPEED;
		move.y *= (delta / 1000.f) * MOVE_SPEED;

		float newX = (x + move.x) * TILESIZE;
		float newY = (y + move.y) * TILESIZE + colliderAdjustY;

		collider.setCenterX(newX + main.getCurrentFrame().getWidth() / 2);
		collider.setCenterY((newY + main.getCurrentFrame().getHeight() / 2) + colliderAdjustY);
		if (map.isTileFree(collider)) {
			// If the location is free move onto it
			x += move.x;
			y += move.y;
			collider.setCenterX((x * TILESIZE) + main.getCurrentFrame().getWidth() / 2);
			collider.setCenterY(((y * TILESIZE) + main.getCurrentFrame().getHeight() / 2)
					+ colliderAdjustY);
		} else {
			// else wall slide

			Vector2f tempMove = moveBy(move);

			x += tempMove.x;
			y += tempMove.y;
			collider.setCenterX((x * TILESIZE) + main.getCurrentFrame().getWidth() / 2);
			collider.setCenterY(((y * TILESIZE) + main.getCurrentFrame().getHeight() / 2)
					+ colliderAdjustY);
		}
	}

	private Vector2f moveBy(Vector2f move) {

		// Vector to be returned at the end, initialised as (0,0)
		Vector2f moveByVector = new Vector2f();
		// Absolute values of the move vector
		Vector2f absMove = new Vector2f(Math.abs(move.x), Math.abs(move.y));

		Vector2f tempMove = new Vector2f(absMove.x * TILESIZE, absMove.y * TILESIZE); // Move
																						// vector
																						// scaled
																						// up
																						// to
																						// pixels

		boolean isLeft = false, isRight = false, isUp = false, isDown = false; // Booleans
																				// to
																				// check
																				// each
																				// direction
		float oldX = collider.getX(), oldY = collider.getY(); // Colliders
																// old
																// location
																// (in
																// the wall)
																// is
																// checked
		if (absMove.x > 0 && absMove.y > 0) {

			// Try left
			collider.setLocation((oldX - tempMove.x), oldY);
			if (map.isTileFree(collider))
				isLeft = true;

			// Try right
			collider.setLocation((oldX + tempMove.x), oldY);
			if (map.isTileFree(collider))
				isRight = true;

			// Try up
			collider.setLocation(oldX, (oldY - tempMove.y));
			if (map.isTileFree(collider))
				isUp = true;

			// Try down
			collider.setLocation(oldX, (oldY + tempMove.y));
			if (map.isTileFree(collider))
				isDown = true;

			if (isLeft)
				moveByVector.x = -absMove.x;

			if (isRight)
				moveByVector.x = absMove.x;

			if (isUp)
				moveByVector.y = -absMove.y;

			if (isDown)
				moveByVector.y = absMove.y;
		}
		return moveByVector;
	}

	public void spotted() {
		spotted = true;
	}

	public void resetSpotted() {
		spotted = false;
	}

	public boolean isSpotted() {
		return spotted;
	}

	@Override
	public Vector2f getEntityDimensions() {

		return new Vector2f(main.getCurrentFrame().getWidth(), main.getCurrentFrame().getHeight());
	}

	private void updateAnimation(int delta) {

		if (isIdle) {
			switch (lastDirection) {
			case LEFT:
				if (!main.equals(leftIdle))
					main = leftIdle;
				break;
			case RIGHT:
				if (!main.equals(rightIdle))
					main = rightIdle;
				break;
			case UP:
				if (!main.equals(upIdle))
					main = upIdle;
				break;
			case DOWN:
				if (!main.equals(downIdle))
					main = downIdle;
				break;

			}
		} else {
			switch (lastDirection) {
			case LEFT:
				if (!main.equals(left))
					main = left;
				break;
			case RIGHT:
				if (!main.equals(right))
					main = right;
				break;
			case UP:
				if (!main.equals(up))
					main = up;
				break;
			case DOWN:
				if (!main.equals(down))
					main = down;
				break;

			}

		}
		main.update(delta);
	}

	@Override
	public float getMaxY() {
		return ((y * TILESIZE) + main.getCurrentFrame().getHeight());

	}

	public void setPlayerLocation(float x, float y) {
		this.x = (x / TILESIZE) + 0.5f;
		this.y = y / TILESIZE + 0.2f;

	}

	public void setHidden(boolean isPlayerHidden) {
		this.isPlayerHidden = isPlayerHidden;
	}

	public boolean isPlayerHiding() {
		return isPlayerHidden;
	}
}
