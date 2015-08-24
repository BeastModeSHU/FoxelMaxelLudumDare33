package com.foxel.maxel.ld33.entities;

import java.util.ArrayList;

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
import com.foxel.maxel.ld33.map.Interactable;

public class Player extends Entity {
	/*
	 * Player Class -> Handles all player interactions with the game
	 */
	private final float MOVE_SPEED; // Players moveement speed
	private SpriteSheet sprites; // animation sprites
	private Animation animation;
	private boolean spotted = false;
	private boolean isPlayerHidden = false;

	public Player(Map map, String ENTITTY_TYPE) {
		super(map, ENTITTY_TYPE);
		this.MOVE_SPEED = Constants.MOVE_SPEED;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		sprites = new SpriteSheet(
				new Image(Constants.PLAYER_SPRITESHEET_LOC).getScaledCopy(64.f / 96.f), TILESIZE,
				TILESIZE);

		animation = new Animation(sprites, 0, 0, 3, 0, true, 180, false);

		x = map.getPlayerStart().x;
		y = map.getPlayerStart().y;

		collider = new Rectangle((x * TILESIZE), (y * TILESIZE), animation.getCurrentFrame()
				.getWidth(), animation.getCurrentFrame().getHeight());
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (!isPlayerHidden)
			g.drawAnimation(animation, x * TILESIZE, y * TILESIZE);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

		Input input = gc.getInput();
		Vector2f move = new Vector2f(); // Player moveement vector
		if (!isPlayerHidden) {
			// Player controls
			if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
				move.x = -MOVE_SPEED;
			}

			if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
				move.x = MOVE_SPEED;
			}

			if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
				move.y = -MOVE_SPEED;
			}

			if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
				move.y = MOVE_SPEED;
			}
		}

		moveEntity(move, delta);
		
		spotted = false;
	}

	@Override
	protected void moveEntity(Vector2f move, int delta) {

		move = move.normalise();

		move.x *= (delta / 1000.f) * MOVE_SPEED;
		move.y *= (delta / 1000.f) * MOVE_SPEED;

		if (move.x != 0 || move.y != 0)
			updateAnimation(delta);
		else
			animation.setCurrentFrame(0);

		float newX = (x + move.x) * TILESIZE;
		float newY = (y + move.y) * TILESIZE;

		collider.setLocation(newX, newY);

		if (map.isTileFree(collider)) {
			// If the location is free move onto it
			x += move.x;
			y += move.y;
			collider.setLocation((x * TILESIZE), (y * TILESIZE));
		} else {
			// else wall slide
			Vector2f tempMove = moveBy(move);
			x += tempMove.x;
			y += tempMove.y;

		}
	}

	private Vector2f moveBy(Vector2f move) {

		Vector2f moveByVector = new Vector2f(); // Vector to be returned at the
												// end, initialised as (0,0)
		Vector2f absMove = new Vector2f(Math.abs(move.x), Math.abs(move.y)); // Absolute
																				// values
																				// of
																				// the
																				// move
																				// vector
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
		float oldX = collider.getX(), oldY = collider.getY(); // Colliders old
																// location (in
																// the wall) is
																// checked

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

		return moveByVector;
	}

	public void spotted() {
		spotted = true;
	}

	@Override
	public Vector2f getEntityDimensions() {

		return new Vector2f(animation.getCurrentFrame().getWidth(), animation.getCurrentFrame()
				.getHeight());
	}

	private void updateAnimation(int delta) {
		animation.update(delta);
	}

	@Override
	public float getMaxY() {
		return ((y * TILESIZE) + animation.getCurrentFrame().getHeight());

	}

	public void setHidden(boolean isPlayerHidden) {
		this.isPlayerHidden = isPlayerHidden;
	}

	public boolean isPlayerHiding() {
		return isPlayerHidden;
	}
}
