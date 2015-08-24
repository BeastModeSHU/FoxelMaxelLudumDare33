package com.foxel.maxel.ld33.resources;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.map.Map;

public class XMLData {
	
	private static XMLParser tenantParser;
	private static XMLElement tenantRoot;
	private static Map map;
	
	public static void init(Map _map) throws SlickException {
		
		tenantParser = new XMLParser();
		tenantRoot = tenantParser.parse(Constants.TENANT_BEHAVIOURS_LOC);
		
		map = _map;
	}
	
	public static int getSpriteSheetIndex(String name) {
		
		int index = 1;
		
		XMLElementList behaviours = tenantRoot.getChildren();
		for (int i = 0; i < behaviours.size(); i++) {
			
			XMLElement behaviour = behaviours.get(i);
			String ident = behaviour.getAttribute("id");
			if (ident.equals(name)) {
				
				XMLElementList actions = behaviour.getChildren();
				for (int j = 0; j < actions.size(); j++) {
					
					XMLElement action = actions.get(j);
					if (action.getName().equals("graphic"))
						index = Integer.parseInt(action.getContent());
				}
			}
		}
		return index;
	}
	
	public static ArrayList<Action> getSchedule(String name) {
		
		ArrayList<Action> schedule = new ArrayList<Action>();
		
		XMLElementList behaviours = tenantRoot.getChildren();
		for (int i = 0; i < behaviours.size(); i++)
		{
			XMLElement behaviour = behaviours.get(i);
			String ident = behaviour.getAttribute("id");
			if (ident.equals(name)) {
				
				XMLElementList actions = behaviour.getChildren();
				for (int k = 0; k < actions.size(); k++) {
					
					float time = 0f;
					Vector2f spot = null;
					
					XMLElementList parameters = actions.get(k).getChildren();
					for (int j = 0; j < parameters.size(); j++) {
						
						XMLElement parameter = parameters.get(j);
						if (parameter.getName().equals("spot"))
							spot = map.getSpot(parameter.getContent());
						if (parameter.getName().equals("time"))
							time = Float.parseFloat(parameter.getContent());
					}
					
					if (time != 0f && spot != null) {
						
						Action act = new Action(time, spot, false);
						schedule.add(act);
					}
				}
			}
		}
		
		return schedule;
	}
}