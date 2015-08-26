package com.foxel.maxel.ld33.rendering;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.entities.Entity;
import com.foxel.maxel.ld33.entities.Player;
import com.foxel.maxel.ld33.map.Interactable;
import com.foxel.maxel.ld33.map.Map;
import com.foxel.maxel.ld33.resources.Camera;

public class Renderer {
	/*
	 * ### MACE ### Will z-sort the map & entity list and render each thing at
	 * the right location
	 */
	private final int MAP_SECTION_WIDTH = 15;
	private final int MAP_SECTION_HEIGHT = 1;
	private final int TILESIZE;
	private final int CEILING_LAYER;
	private Player player;
	private Map map;
	private ArrayList<Entity> renderable;
	private ArrayList<Polygon> cones;
	private Image tex;
	private ArrayList<Interactable> interactables;
	private Camera camera;

	public Renderer(Camera camera, Player player, Map map, ArrayList<Entity> renderable,
			ArrayList<Interactable> interactables, ArrayList<Polygon> cones) {
		this.player = player;
		this.map = map;
		this.renderable = renderable;
		this.TILESIZE = Constants.TILESIZE;
		this.CEILING_LAYER = Constants.CEILING_LAYER_ID;
		this.camera = camera;

		try {
			tex = new Image(Constants.VISIONCONE_LOC, false, Image.FILTER_NEAREST);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.interactables = interactables;

	}

	public void sortRenderableByZ() {
		int j = 0;
		boolean swapped = true; // set flag to true to begin first pass

		while (swapped) {
			swapped = false; // set flag to false awaiting a possible swap
			for (j = 0; j < renderable.size() - 1; j++) {
				float num1 = renderable.get(j).getMaxY(), num2 = renderable.get(j + 1).getMaxY();

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

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g, ArrayList<Polygon> cones)
			throws SlickException {
		this.cones = cones;

		// Will handle map rendering & renderable rendering
		map.renderFloorLayer();

		for (int i = 0; i < cones.size(); i++) {
			
			if (camera.isInLargeView((new Vector2f(cones.get(i).getCenterX(), cones.get(i)
					.getCenterY()))))
				g.texture(cones.get(i), tex, true);
		}

		map.renderWallLayer();

		for (Interactable i : interactables) {
			i.render(g);
		}
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
	}

	public void setInteractables(ArrayList<Interactable> list) {
		this.interactables = list;
	}
}