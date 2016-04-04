package mappanel.framework;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashSet;

/**
 * This is the base of almost the objects in the map
 * This excludes the Camera
 */
public abstract class MapObject extends JComponent
{
    private static final long serialVersionUID = 3769380260168628120L;
    protected int x,y;
    protected ObjectID id;
    protected float velX = 0, velY = 0;
    protected int Zoom = 0;
    protected String name;
    protected int renderingPriority;
    

    public MapObject(int x,int y, ObjectID id, int renderingPriority)
    {
        this.x = x;
        this.y = y;
        this.id = id;
        this.renderingPriority = renderingPriority;
    }
    
    public abstract void tick(LinkedHashSet<MapObject> objects);
    
    public abstract void render(Graphics g);
    
    public abstract Rectangle2D getBound(); 

    //Start of getters/setters
    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
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

    public int getRenderingPriority() {
        return renderingPriority;
    }

    public void setRenderingPriority(int renderingPriority) {
        this.renderingPriority = renderingPriority;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    //End of getters/setters
}
