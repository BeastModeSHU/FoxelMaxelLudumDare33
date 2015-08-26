package com.foxel.maxel.ld33.resources;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;

import com.foxel.maxel.ld33.map.Map;
import com.foxel.maxel.ld33.constants.Constants;

public class VisionCone
{
	public float x, y, angle, angleRange;
	private int rays;
	private float resolution;
	private float secondaryResolution = 2f;
	private float range;
	private float sqrRange;
	private Map map;
	private Point[] hits;
	private Polygon[] polys;
	
	public VisionCone(float x, float y, float angle, float angleRange, int rays, float res1, float res2, Map map)
	{
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.angleRange = angleRange;
		this.rays = rays;
		this.resolution = res1;
		this.secondaryResolution = res2;
		range = Constants.TENANT_VISION_RANGE;
		sqrRange = range * range;
		this.map = map;
		
		hits = new Point[rays];
		polys = new Polygon[rays - 1];
		for (int i = 0; i < polys.length; i++)
		{
			polys[i] = new Polygon(new float[]{0f, 0f, 0f, 0f, 0f, 0f});
		}
	}
	
	public Polygon[] updateCone(float x, float y, float angle)
	{
		this.x = x;
		this.y = y;
		this.angle = angle;
		
		float minAngle = angle - angleRange / 2f;
		float angleOffset = angleRange / (float)(rays);
		
		for (int i = 0; i < rays; i++)
		{
			float[] hitPoint = shootRay(new float[]{x, y}, minAngle + i * angleOffset);
			Point hit = new Point(hitPoint[0], hitPoint[1]);
			if (hit != null)
				hits[i] = hit;
		}
		int polyLen = polys.length;
		for (int i = 0; i < polyLen; i++)
		{
			polys[i] = new Polygon(new float[]{x, y, hits[i].getX(), hits[i].getY(), hits[i+1].getX(), hits[i+1].getY()});
		}
		
		return polys;
	}
	
	private float[] shootRay(float[] start, float angle)
	{
		float[] hit = null;
		
		float[] cur = new float[] {0, 0};
		float dist = cur[0] * cur[0] + cur[1] * cur[1];
		float addX = (float) (Math.cos(angle) * resolution);
		float addY = (float) (Math.sin(angle) * resolution);
		float secAddX = (float) (Math.cos(angle) * secondaryResolution);
		float secAddY = (float) (Math.sin(angle) * secondaryResolution);
		
		
		float[] newPoint = new float[] {0, 0};
		while (dist < sqrRange && hit == null) {
			cur[0] += addX;
			cur[1] += addY;
			dist = cur[0] * cur[0] + cur[1] * cur[1];
			
			newPoint[0] = start[0] + cur[0];
			newPoint[1] = start[1] + cur[1];
			if(!map.isPointFree(newPoint))
			{
				cur[0] -= addX;
				cur[1] -= addY;
				
				
				while (hit == null)
				{
					cur[0] += secAddX;
					cur[1] += secAddY;
					newPoint[0] = start[0] + cur[0];
					newPoint[1] = start[1] + cur[1];
					
					if (!map.isPointFree(newPoint))
						hit = newPoint;
				}
			}
		}
		if (dist > sqrRange)
			hit = newPoint;
		
		return hit;
	}
}