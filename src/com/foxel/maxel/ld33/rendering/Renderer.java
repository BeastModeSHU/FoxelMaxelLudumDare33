package com.foxel.maxel.ld33.rendering;

import java.util.ArrayList;

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
	 * Will z-sort the map & entity list and render each thing at the right location
	 */
	private Player player;
	private Map map;
	private ArrayList<Entity> renderable;
	private final int TILESIZE;
	private final int CEILING_LAYER;
	private final int MAP_SECTION_WIDTH = 15;
	private final int MAP_SECTION_HEIGHT = 1;

	public Renderer(Player player, Map map, ArrayList<Entity> renderable) {
		this.player = player;
		this.map = map;
		this.renderable = renderable;
		this.TILESIZE = Constants.TILESIZE;
		this.CEILING_LAYER = Constants.CEILING_LAYER_ID;
	}

	public void sortRenderableByZ() {
		int j = 0;
		boolean swapped = true; // set flag to true to begin first pass

		while (swapped) {
			swapped = false; // set flag to false awaiting a possible swap
			for (j = 0; j < renderable.size() - 1; j++) {
				float num1, num2;

				if (renderable.get(j).equals(player)) {
					num1 = renderable.get(j).getPixelLocation().y
							+ renderable.get(j).getEntityDimensions().y;
				} else {
					num1 = renderable.get(j).getPixelLocation().y
							+ renderable.get(j).getEntityDimensions().y - 48.f;
				}
				if (renderable.get(j + 1).equals(player)) {
					num2 = renderable.get(j + 1).getPixelLocation().y
							+ renderable.get(j + 1).getEntityDimensions().y;
				}else{ 
					num2 = renderable.get(j + 1).getPixelLocation().y
							+ renderable.get(j + 1).getEntityDimensions().y - 48.f;
				}
				if (num1 > num2) // change to > for ascending sort
				{
					swapEntities(j, (j + 1));
					swapped = true; // shows a swap occurred
				}
			}
		}

	}

	private void swapEntities(int index1, int index2) {
		Entity temp = renderable.get(index1);
		renderable.set(index1, renderable.get(index2));
		renderable.set(index2, temp);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Will handle map rendering & renderable rendering
		map.renderFloorLayer();
		map.renderWallLayer();
		sortRenderableByZ();
		renderMapLayers(gc, sbg, g);
	}

	private void renderMapLayers(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		int lastIndex = 0;
		int height = map.getHeightInTiles();

		for (int i = 0; i < height; ++i) {

			int currentLayerY = (i * TILESIZE) + TILESIZE;
			int counter = lastIndex;
			boolean found = true;
			while (found && counter < renderable.size()) {

				if (renderable.get(counter).getMaxY() < currentLayerY) {
					renderable.get(counter).render(gc, sbg, g);
					++lastIndex;
				} else {
					found = false;
				}
				++counter;
			}

			map.renderLayerSection(0, currentLayerY - TILESIZE, 0, i, MAP_SECTION_WIDTH,
					MAP_SECTION_HEIGHT, CEILING_LAYER);

		}
		System.out.println();
	}

}