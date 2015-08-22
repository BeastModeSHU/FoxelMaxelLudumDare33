package com.foxel.maxel.ld33.resources;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.entities.Player;
import com.foxel.maxel.ld33.map.Map;

public class SortZAxis {
	/*
	 * ### MACE ###
	 */
	private Player player;
	private Map map;
	private Camera camera;
	private int aboveY; 
	
	
	public SortZAxis(Player player, Map map, Camera camera) {
		this.player = player;
		this.map = map;
		this.camera = camera;
	}
	
	public void renameMethod(){ 
	
	
			
	}
	public int[] getBelowPlayer() {
		
		/* 
		int x = 0;
		int y = 0;
		int startX = x;
		int startY = y;
		int width = 15;
		int height = (int) (player.getTileLocation().y);

		return new int[] { x, y, startX, startY, width, height };*/
		int[] tempData;
		
		float playerY = player.getPixelLocation().y + player.getEntityDimensions().y;
//		float playerY = player.getPixelLocation().y;
		boolean stopLoop = false;
		int counter = 0;
		
		while(!stopLoop && counter < map.getHeightInTiles()){ 
			float tileY = ( counter * Constants.TILESIZE) + Constants.TILESIZE; 
			
			if(playerY < tileY){
				stopLoop = true;
			}else
				System.out.println(tileY + "  -  " + counter + " - " + playerY);
			++counter;
		}
		
		int x = 0;
		int y = 0; 
		int startX = x; 
		int startY = y; 
		int width = 15; 
		int height = 0 + (counter);
		
		
		return new int[] {x,y, startX, startY, width, height } ;
	}

	public int[] getAbovePlayer() {
		/*
		int startX = 0;// x;
		int startY = (int) player.getTileLocation().y;
		int width = 15;
		int height = (int) ((15 - player.getTileLocation().y));
		*/ 
		float playerY = player.getPixelLocation().y + player.getEntityDimensions().y;
		boolean stopLoop = false; 
		int counter = 0; 
		
		while(!stopLoop && counter < map.getHeightInTiles()){ 
			float tileY = (counter * Constants.TILESIZE)  + Constants.TILESIZE;
			if(playerY < tileY){ 
				stopLoop = true;
			}
			++counter;
		}
		int x = 0; 
		int y = (counter-2); 
		int width = 15;
		int height = 15 - (counter);
		
		/*
		 * for (int i = 0; i < temp.length; ++i) { System.out.print(temp[i] +
		 * "  "); } System.out.println();
		 */
		return new int[] { x, y * Constants.TILESIZE, x, y, width, height };
	}
}
