package mappanel.framework;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.JComponent;

/**
 * 
 * @author Tyler Valant
 * @category Framework
 * @since 1-19-2016
 * @version 1.0.0
 *
 * The MapObject class is the base component for all objects in the Map.
 */
public abstract class MapObject extends JComponent
{
    /**
     * 
     */
    private static final long serialVersionUID = 3769380260168628120L;
    protected double x,y;
    protected ObjectID id;
    protected float velX = 0, velY = 0;
    protected int Zoom = 0;
    protected String name;
    
    /**
     * Constructor
     * @param x - x coordinate
     * @param y - y coordinate
     * @param id - the id of the object
     */
    public MapObject(double x,double y, ObjectID id)
    {
	this.x = x;
	this.y = y;
	this.id = id;
    }
    
    
    /**
     * Does some function each tick
     * Examples - Collision checking, Movement tracking.
     * @param objects - All the objects in the scene
     */
    public abstract void tick(LinkedHashSet<MapObject> objects);
    
    /**
     * Draws the object.
     * @param g - graphic to be drawn on.
     */
    public abstract void render(Graphics g);
    
    /**
     * The bounds of this object
     * @return - the Bounding box of this object
     */
    public abstract Rectangle2D getBound(); 

    public int getX()
    {
        return (int) x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public int getY()
    {
        return (int) y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public ObjectID getId()
    {
        return id;
    }

    public void setId(ObjectID id)
    {
        this.id = id;
    }

    public float getVelX()
    {
        return velX;
    }

    public void setVelX(float velX)
    {
        this.velX = velX;
    }

    public float getVelY()
    {
        return velY;
    }

    public void setVelY(float velY)
    {
        this.velY = velY;
    }


    public int getZoom()
    {
        return Zoom;
    }


    public void setZoom(int zoom)
    {
        Zoom = zoom;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }

    
    
    
    
}
