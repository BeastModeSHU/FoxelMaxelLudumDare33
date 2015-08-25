package com.foxel.maxel.ld33.resources;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.foxel.maxel.ld33.entities.Entity;

public class Camera {

	private int x, y; // The X & Y position of the viewport
	private int mapWidth, mapHeight; // The width & height of the map
	private Rectangle viewPort; // viewport rectangle
	private Rectangle largeViewPort;

	public Camera(int mapWidth, int mapHeight) {
		x = 0;
		y = 0;
		// initialise viewport at 0,0 with dimensions of the screen
		viewPort = new Rectangle(0, 0, Display.getWidth(), Display.getHeight());
		largeViewPort = new Rectangle(viewPort.getCenterX(),viewPort.getCenterY() , Display.getWidth() + 100, Display.getHeight() + 100);
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;

	}

	public void translate(Graphics g, Entity player) {
		// if the players x position (in the world) minus the screen size over 2
		// + half the size of the player
		// is lees than 0, set the viewport x to 0

		if (player.getPixelLocation().x - Display.getWidth() / 2 + player.getEntityDimensions().x
				/ 2 < 0) {
			x = 0;
		} else if (player.getPixelLocation().x + Display.getWidth() / 2
				+ player.getEntityDimensions().x / 2 > mapWidth) {
			x = -mapWidth + Display.getWidth();
		} else {
			x = (int) (-player.getPixelLocation().x + Display.getWidth() / 2 - player
					.getEntityDimensions().x / 2);
		}

		if (player.getPixelLocation().y - Display.getHeight() / 2 + player.getEntityDimensions().x
				/ 2 < 0) {
			y = 0;
		} else if (player.getPixelLocation().y + Display.getHeight() / 2
				+ player.getEntityDimensions().y / 2 > mapHeight) {
			y = -mapHeight + Display.getHeight();
		} else {
			y = (int) (-player.getPixelLocation().y + Display.getHeight() / 2 - player
					.getEntityDimensions().y / 2);
		}

		g.translate(x, y);
		g.fill(largeViewPort);
		viewPort.setX(-x);
		viewPort.setY(-y);
		largeViewPort.setCenterX(viewPort.getCenterX());
		largeViewPort.setCenterY(viewPort.getCenterY());

	}

	public Vector2f getTranslation() {
		return new Vector2f(viewPort.getX(), viewPort.getY());
	}

	public boolean isInLargeView(Vector2f point) {
		if (largeViewPort.contains(point.x, point.y)) {
			return true;
		}
		return false;
	}

}