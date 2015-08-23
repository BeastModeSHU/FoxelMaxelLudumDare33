package com.foxel.maxel.ld33.rendering;

import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.entities.Entity;
import com.foxel.maxel.ld33.entities.Player;
import com.foxel.maxel.ld33.map.Map;

public class Renderer {
	/*
	 * ### MACE ###
	 * 
	 * XXX DO NOT FORMAT CLASS XXX
	 */
	private Player player;
	private Map map;
	private ArrayList<Entity> renderable;

	public Renderer(Player player, Map map, ArrayList<Entity> renderable) {
		this.player = player;
		this.map = map;
		this.renderable = renderable;

	
	}

	// TODO rename to getBelowEntities();
	public int[] getBelowPlayer() {
		/*
		float playerY = player.getPixelLocation().y + player.getEntityDimensions().y;

		boolean stopLoop = false;
		int counter = 0;

		while (!stopLoop && counter < map.getHeightInTiles()) {
			float tileY = (counter * Constants.TILESIZE) + Constants.TILESIZE;

			if (playerY < tileY) {
				stopLoop = true;
			}
			++counter;
		}

		int x = 0;
		int y = 0;
		int startX = x;
		int startY = y;
		int width = 15;
		int height = 0 + (counter);

		return new int[] { x, y, startX, startY, width, height };
		*/
		return null;
	}

	// TODO rename to getAboveEntities();
	public int[] getAbovePlayer() {

		float playerY = player.getPixelLocation().y + player.getEntityDimensions().y;
		boolean stopLoop = false;
		int counter = 0;

		while (!stopLoop && counter < map.getHeightInTiles()) {
			float tileY = (counter * Constants.TILESIZE) + Constants.TILESIZE;
			if (playerY < tileY) {
				stopLoop = true;
			}
			++counter;
		}
		int x = 0;
		int y = (counter - 1);
		int width = 15;
		int height = 16 - (counter);

		return new int[] { x, y * Constants.TILESIZE, x, y, width, height };
	}

	public void sortRenderableByZ() {

		/* 
		 * TODO sort entities via sorting algorithm
		 */
	}


	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Will handle map rendering & renderable rendering
		map.renderFloorLayer();
		map.renderWallLayer();
		//sortRenderableByZ();
		for (int i = 0; i < renderable.size(); ++i){
			renderable.get(i).render(gc, sbg, g);
		}
		

		
		// player.render(gc, sbg, g);

		/*
		 * Render entities & map areas here.
		 */
	}

	private void renderMapLayers() {

	}

	private void splitMap() {
		float[] yValues = new float[renderable.size()];

		for (int i = 0; i < renderable.size(); ++i) {
			yValues[i] = renderable.get(i).getPixelLocation().y
					+ renderable.get(i).getEntityDimensions().y;

		}
	}

}