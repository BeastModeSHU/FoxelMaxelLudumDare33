package com.foxel.maxel.ld33.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.foxel.maxel.ld33.constants.Constants;

public class HidingPlace extends Interactable {
	private Image image;

	public HidingPlace(float x, float y) {
		super(x, y);
		try {
			image = new SpriteSheet(Constants.TILESET_LOCATION, TILESIZE, TILESIZE).getSubImage(6,
					0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(Graphics g) throws SlickException {
		g.drawImage(image, x, y);

	}

}
