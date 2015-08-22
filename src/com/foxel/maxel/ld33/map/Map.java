package com.foxel.maxel.ld33.map;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

import com.foxel.maxel.ld33.constants.Constants;

public class Map {

//	private ArrayList<Rectangle> mapColliders; 
	private int blockedMap[][]; /// 0 = not blocked, 1 = blocked
	private final int TILESIZE;
	private TiledMap map;

	public Map() {
		this.TILESIZE = Constants.TILESIZE;
	}

	public void init() throws SlickException {
		map = new TiledMap(Constants.TEST_MAP_LOC);
		blockedMap = new int[map.getHeight()][map.getWidth()];
		/*
		for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
			for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
				int tileId = map.getTileId(xAxis, yAxis, 0);
				String value = map.getTileProperty(tileId, "blocked", "false");
				if ("true".equals(value)) {
					blocked[xAxis][yAxis] = true;
					Map.add(new Rectangle(xAxis * 64, yAxis * 64, 64, 64));
				}

			}
		}
		 */
		
		for(int x = 0; x < map.getWidth(); ++x){ 
			for(int y = 0; y < map.getWidth(); ++y){ 
				int tileID = map.getTileId(x, y,0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if("true".equals(value)){ 
					blockedMap[x][y] = 1;
				}else{ 
					blockedMap[x][y]= 0;
				}
			}
		}
		
		printBlocked();
	
	}
	private void printBlocked(){ 
		for(int i = 0; i < map.getHeight(); ++i){ 
			for(int j = 0; j < map.getWidth(); ++j){ 
				System.out.print(blockedMap[j][i]);
			}
			System.out.println();
		}
	}
	public void render() throws SlickException {
		map.render(0, 0);
	}

	public boolean isTileFree(Rectangle collider) {
		boolean isFree = true;
		for(int i =0; i < map.getHeight(); ++i){ 
			for(int j =0; j < map.getWidth(); ++j){ 
				if(blockedMap[j][i] == 1){ 
					if(new Rectangle(j * TILESIZE, i * TILESIZE, TILESIZE, TILESIZE).intersects(collider))
						isFree = false;
				}
			}
		}
		return isFree;
	}

	public int getTileWidth() {
		return map.getWidth();
	}

	public int getTileHeight() {
		return map.getHeight();
	}

	public int getWidth() {
		return map.getWidth() * TILESIZE;
	}

	public int getHeight() {
		return map.getHeight() * TILESIZE;
	}

}
