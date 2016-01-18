package com.TylerValant.MapPanel.objects;

import com.TylerValant.MapPanel.framework.MapObject;
import com.TylerValant.MapPanel.window.MapPanel;

public class Camera
{
    private double x,y;
    private MapPanel map;
    
    public Camera(double x, double y, MapPanel map)
    {
	this.x = x;
	this.y = y;
	this.map = map;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    /**
     * re-centers the camera for drawing 
     * @param center - the center of the map
     */
    public void tick(MapObject center)
    {
	MapCenter newCenter = (MapCenter) center;
	x = map.getWidth()/2 - newCenter.getX(); // 
	y = map.getHeight()/2 - newCenter.getY(); //MapPanel.HEIGHT/2 - 
    }
    
    

}
