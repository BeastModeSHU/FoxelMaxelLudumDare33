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
	private final int MAX_RANGE = 320;

	public NoiseMaker(float x, float y, int ID) {
		super(x, y, ID);
		distractionCircle = new Circle(x + Constants.TILESIZE, y, MAX_RANGE);
		try {
			image = new SpriteSheet(Constants.OBJECT_SPRITESHEET_LOC, TILESIZE, TILESIZE * 2)
					.getSubImage(ID, 0);
			this.activationCircle = new Circle(x + Constants.TILESIZE,y+Constants.TILESIZE/2,Constants.ACTIVATION_RANGE);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public Circle getDistractionCircle() {
		return distractionCircle;
	}

	@Override
	public void render(Graphics g) throws SlickException {
		g.drawImage(image, (x - image.getWidth() / 2) + Constants.TILESIZE, y - image.getHeight()
				/ 2.5f);
	}
}