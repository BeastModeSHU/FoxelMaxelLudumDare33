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
	public Circle distractionCircle;
	private Image image;

	public NoiseMaker(int x, int y, float range) {
		super(x, y);
		distractionCircle = new Circle(x, y, range);
		id = "noisemaker";
		try {
			image = new SpriteSheet(Constants.TILESET_LOCATION, TILESIZE, TILESIZE).getSubImage(5,
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
		g.drawImage(image, x, y);
		g.draw(distractionCircle);

	}
}