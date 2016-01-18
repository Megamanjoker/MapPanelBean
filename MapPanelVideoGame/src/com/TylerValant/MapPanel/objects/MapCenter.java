package com.TylerValant.MapPanel.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import com.TylerValant.MapPanel.framework.Envelope;
import com.TylerValant.MapPanel.framework.MapObject;
import com.TylerValant.MapPanel.framework.ObjectID;

public class MapCenter extends MapObject
{
    /**
     * 
     */
    private static final long serialVersionUID = 7003532228198805203L;
    double Width=1,Height=1;
    private boolean DEBUG = true;
    private Envelope envlope;
    private boolean useEnvelope = true;
    
    public MapCenter(double x, double y, ObjectID id)
    {
	super(x, y, id);
	this.envlope = new Envelope(ObjectID.Envelope,  -96.728477, 32.966722,  -96.725505, 32.965182, Zoom);
    }

    @Override
    public void tick(List<MapObject> objects)
    {
	envlope.tick(objects);
	if(!this.getBound().intersects(envlope.getBound(this.Zoom)) && useEnvelope)
	{
	    this.x = envlope.getCenter().getX();// - this.Width/2;
	    this.y = envlope.getCenter().getY();// - this.Height/2;
	}
    }

    /**
     * Only renders in debug mode
     */
    @Override
    public void render(Graphics g)
    {
	if(DEBUG)
	{
	    g.setColor(Color.ORANGE);
	    g.fillRect((int)x - 4, (int)y - 4, 8, 8);
	    g.setColor(Color.RED);
	    g.drawRect(getTestPriority().x,getTestPriority().y,getTestPriority().width,getTestPriority().height);
	    g.setColor(Color.BLUE);
	    g.drawRect(getTestDeletePriority().x,getTestDeletePriority().y,getTestDeletePriority().width,getTestDeletePriority().height);
	    envlope.render(g);
	}
    }

    /**
     * Not used
     */
    @Override
    public Rectangle getBound()
    {
	// TODO Auto-generated method stub
	return new Rectangle((int)(x),(int)(y),(int)(1) ,(int)(1));
    }
    
    /**
     * Testing Priority
     * @return
     */
    public Rectangle getTestPriority()
    {
	return priorityBounds(0.325);
    }
    
    /**
     * Testing Delete Priority
     * @return
     */
    public Rectangle getTestDeletePriority()
    {
	return priorityBounds(0.35);
    }
    
    /**
     * Don't delete these tiles
     * @return - Collision box of don't delete box 
     */
    public Rectangle getPriority()
    {
	return priorityBounds(0.2);
    }
    
    /**
     * Anything outside this box should be deleted
     * @return - Collision box of do delete box
     */
    public Rectangle getDeletePriority()
    {
	return priorityBounds(0.3);
    }

    /**
     * This method is to minimize the number of times, I have to write the equation Rect = (x - w/2 - (w/2 * %) , y - h/2 - (h/2 * %), w + w * %, h + h * %)
     * @param percent - the percent of increase or decrease of the bounds
     * @return - the bound with the percent
     */
    private Rectangle priorityBounds(double percent)
    {
	return new Rectangle((int)((x - Width/2) -  ((Width/2) * percent)),(int)((y - Height/2) - ((Height/2) * percent)),(int)(Width + Width * percent) - 1 ,(int)(Height + Height * percent) -1) ;
    }
    
    public int getWidth()
    {
        return (int) Width;
    }

    public void setWidth(double width)
    {
        Width = width;
    }

    public int getHeight()
    {
        return (int) Height;
    }

    public void setHeight(double height)
    {
        Height = height;
    }

//    public Envelope getEnvlope()
//    {
//        return envlope;
//    }

    public void setEnvlope(Double startLon,Double startLat,Double endLon,Double endLat)
    {
        this.envlope = new Envelope(ObjectID.Envelope, startLon, startLat, endLon, endLat);
    }

    public boolean isUseEnvelope()
    {
        return useEnvelope;
    }

    public void setUseEnvelope(boolean useEnvelope)
    {
        this.useEnvelope = useEnvelope;
    }
    
    public void setZoom(int Zoom)
    {
	this.Zoom = Zoom;
	this.envlope.setZoom(Zoom);
    }
    
    
}
