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
    /**
     * The X.
     */
    protected int x, /**
 * The Y.
 */
y;
    /**
     * The Id.
     */
    protected ObjectID id;
    /**
     * The Vel x.
     */
    protected float velX = 0, /**
 * The Vel y.
 */
velY = 0;
    /**
     * The Zoom.
     */
    protected int Zoom = 0;
    /**
     * The Name.
     */
    protected String name;
    /**
     * The Rendering priority.
     */
    protected int renderingPriority;


    /**
     * Instantiates a new Map object.
     *
     * @param x                 the x
     * @param y                 the y
     * @param id                the id
     * @param renderingPriority the rendering priority
     */
    public MapObject(int x,int y, ObjectID id, int renderingPriority)
    {
        this.x = x;
        this.y = y;
        this.id = id;
        this.renderingPriority = renderingPriority;
    }

    /**
     * Tick.
     *
     * @param objects the objects
     */
    public abstract void tick(LinkedHashSet<MapObject> objects);

    /**
     * Render.
     *
     * @param g the g
     */
    public abstract void render(Graphics g);

    /**
     * Gets bound.
     *
     * @return the bound
     */
    public abstract Rectangle2D getBound();

    //Start of getters/setters
    public int getX()
    {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(int y)
    {
        this.y = y;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public ObjectID getId()
    {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(ObjectID id)
    {
        this.id = id;
    }

    /**
     * Gets vel x.
     *
     * @return the vel x
     */
    public float getVelX()
    {
        return velX;
    }

    /**
     * Sets vel x.
     *
     * @param velX the vel x
     */
    public void setVelX(float velX)
    {
        this.velX = velX;
    }

    /**
     * Gets vel y.
     *
     * @return the vel y
     */
    public float getVelY()
    {
        return velY;
    }

    /**
     * Sets vel y.
     *
     * @param velY the vel y
     */
    public void setVelY(float velY)
    {
        this.velY = velY;
    }


    /**
     * Gets zoom.
     *
     * @return the zoom
     */
    public int getZoom()
    {
        return Zoom;
    }


    /**
     * Sets zoom.
     *
     * @param zoom the zoom
     */
    public void setZoom(int zoom)
    {
        Zoom = zoom;
    }

    /**
     * Gets rendering priority.
     *
     * @return the rendering priority
     */
    public int getRenderingPriority() {
        return renderingPriority;
    }

    /**
     * Sets rendering priority.
     *
     * @param renderingPriority the rendering priority
     */
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
