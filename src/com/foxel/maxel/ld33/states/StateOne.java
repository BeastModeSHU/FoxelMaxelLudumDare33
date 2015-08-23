package com.foxel.maxel.ld33.states;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.entities.Entity;
import com.foxel.maxel.ld33.entities.Player;
import com.foxel.maxel.ld33.entities.Tenant;
import com.foxel.maxel.ld33.map.Interactable;
import com.foxel.maxel.ld33.map.NoiseMaker;
import com.foxel.maxel.ld33.map.Map;
import com.foxel.maxel.ld33.resources.Camera;
import com.foxel.maxel.ld33.resources.VisionCone;
import com.foxel.maxel.ld33.resources.XMLData;
import com.foxel.maxel.ld33.rendering.Renderer;


public class StateOne extends BasicGameState {

	private final int STATE_ID;
	private ArrayList<Entity> renderable;
	private Map map;
	private Camera camera;
	private Player player;
	private ArrayList<Interactable> interactables;
	private ArrayList<Interactable> playerInteractables;
	private Tenant tenant;	
	private VisionCone vis;
	private Polygon[] polys;
	private Image tex;
	private Renderer renderer;


	public StateOne(int STATE_ID) {
		this.STATE_ID = STATE_ID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		renderable = new ArrayList<Entity>();

		map = new Map();
		map.init();
		XMLData.init(map);
		camera = new Camera(map.getWidth(), map.getHeight());

		player = new Player(map);
		player.init(gc, sbg);

		tenant = new Tenant(map, 2, 2);
		tenant.init(gc, sbg);

		Tenant snep = new Tenant(map, 10, 10);
		snep.init(gc, sbg);


		renderable.add(player);
		renderable.add(tenant);
		renderable.add(snep);

		//zSort = new SortZAxis(player, map);

		interactables = new ArrayList<Interactable>();
		
		vis = new VisionCone(tenant.getPixelLocation().x, tenant.getPixelLocation().y, tenant.angle, (float)(Math.PI / 2), 30, 32f, 4f, map);
		polys = vis.updateCone(tenant.getPixelLocation().x, tenant.getPixelLocation().y, tenant.angle);
		tex = new Image(Constants.VISIONCONE_LOC, false, Image.FILTER_NEAREST);

		interactables = map.getInteractables();
		
		renderer = new Renderer(player, map, renderable);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		camera.translate(g, player);
		renderer.render(gc,sbg, g);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE))
			gc.exit();


		for (int i = 0; i < renderable.size(); ++i) {
				renderable.get(i).update(gc, sbg, delta);
		}
		checkInteractables();
		
		polys = vis.updateCone(tenant.getPixelLocation().x + 32f, tenant.getPixelLocation().y + 32f, tenant.angle);
	}

	private void checkInteractables() {
		player.interactables.clear();
		for (int i = 0; i < interactables.size(); i++) {
			Interactable boop = interactables.get(i);
			if (boop.getActivationCircle().intersects(player.collider))
				player.interactables.add(boop);
			if (boop.activated) {
				boop.confirmActivation();
				if (boop.id.equals("noisemaker")) {
					distractTenants(new Vector2f(boop.x, boop.y),
							((NoiseMaker) (boop)).distractionCircle);
				}
			}
		}
	}

	private void distractTenants(Vector2f source, Circle collider) {
		// TODO Fix function to cehck if an entity is a tenant, then to check
		// for colliders
		for (int i = 0; i < renderable.size(); i++) {

			/*
			 * if (collider.intersects(tenants.get(i).collider))
			 * tenants.get(i).distract(source);
			 */
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return STATE_ID;
	}
}