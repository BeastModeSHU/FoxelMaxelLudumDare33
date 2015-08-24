package com.foxel.maxel.ld33.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;

import com.foxel.maxel.ld33.constants.Constants;

public class NoiseMaker extends Interactable {
	/*
	 * Interactable object in the game which will distract enemies
	 */
	private Circle distractionCircle;
	private Image image;
	private final int MAX_RANGE = 200;

	public NoiseMaker(float x, float y, int ID) {
		super(x, y, ID);
		distractionCircle = new Circle(x, y, MAX_RANGE);
		try {
			image = new SpriteSheet(Constants.OBJECT_SPRITESEET_LOC, TILESIZE, TILESIZE * 2).getSubImage(ID,
					0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public Circle getDistractionCircle() {
		return distractionCircle;
	}

	@Override
	public void render(Graphics g) throws SlickException {
		g.drawImage(image, x, y); // Render the radio centre at the point
		g.draw(distractionCircle);
//		g.draw(this.activationCircle);

	}
}