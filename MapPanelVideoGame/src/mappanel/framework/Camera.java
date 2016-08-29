package mappanel.framework;

import mappanel.window.MapPanel;

/**
 * Tyler Valant
 * Framework
 * 1-19-2016
 * 1.0.0
 * <p>
 * The Camera class is used to keep track of where the windows is relative to the map
 * The Camera class is attached to MapCenter Class
 */
public class Camera
{
    private double x,y;
    private MapPanel map;

    /**
     * Instantiates a new Camera.
     *
     * @param x   the x
     * @param y   the y
     * @param map the map
     */
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
     * re-centers the camera to the MapCenter for drawing 
     * @param center - the center of the map
     */
    public void tick(MapObject center)
    {
	    x = map.getWidth()/2 - center.getX();
	    y = map.getHeight()/2 - center.getY();
    }
    
    

}
