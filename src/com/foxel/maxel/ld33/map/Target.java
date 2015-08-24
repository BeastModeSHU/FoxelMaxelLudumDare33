package com.foxel.maxel.ld33.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.foxel.maxel.ld33.constants.Constants;

public class Target extends Interactable {
	private Image alive, dead;

	public Target(float x, float y, int ID) {
		super(x, y, ID);
		try {
			SpriteSheet temp = new SpriteSheet(Constants.TARGET_SPRITESHEET_LOC, TILESIZE, TILESIZE);
			alive = temp.getSubImage(0, 0);
			dead = temp.getSubImage(0, 1);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(Graphics g) throws SlickException {
		if (!activated)
			g.drawImage(alive, x + alive.getWidth() / 2, y + alive.getHeight() / 2);
		else
			g.drawImage(dead, x + dead.getWidth() / 2, y + dead.getHeight() / 2);
	}

}
