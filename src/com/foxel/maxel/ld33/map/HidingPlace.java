package com.foxel.maxel.ld33.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;

import com.foxel.maxel.ld33.constants.Constants;

public class HidingPlace extends Interactable {
	private Image hiding;

	public HidingPlace(float x, float y, int ID) {
		super(x, y, ID);
		activationCircle = new Circle(x, y, 0);
		try {
			image = new SpriteSheet(Constants.OBJECT_SPRITESHEET_LOC, TILESIZE, TILESIZE * 2)
					.getSubImage(ID, 0);
			hiding = new Image(Constants.HIDING_ICON);
			activationCircle.setRadius(image.getWidth());
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void render(Graphics g) throws SlickException {
		g.drawImage(image, x + image.getWidth() / 2, y - image.getHeight() / 2.5f);
		if (this.activated) {
			g.drawImage(hiding, x + hiding.getWidth() / 2, y - image.getHeight() / 3);
		}
//		g.draw(this.activationCircle);
	}

}
