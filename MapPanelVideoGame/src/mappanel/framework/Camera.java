package mappanel.framework;

import mappanel.window.MapPanel;

/**
 * 
 * @author Tyler Valant
 * @category Framework
 * @since 1-19-2016
 * @version 1.0.0
 *
 * The Camera class is used to keep track of where the windows is relative to the map
 * The Camera class is attached to MapCenter Class
 */
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
     * re-centers the camera to the MapCenter for drawing 
     * @param center - the center of the map
     */
    public void tick(MapObject center)
    {
	x = map.getWidth()/2 - center.getX(); 
	y = map.getHeight()/2 - center.getY(); 
    }
    
    

}
