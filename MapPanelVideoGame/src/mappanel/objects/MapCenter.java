package mappanel.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashSet;
import java.util.List;

import mappanel.framework.Envelope;
import mappanel.framework.MapObject;
import mappanel.framework.ObjectID;

public class MapCenter extends MapObject
{
    /**
     * 
     */
    private static final long serialVersionUID = 7003532228198805203L;
    double Width=1,Height=1;
    private boolean DEBUG = false;
    private Envelope envelope;
    private boolean useEnvelope = true;
    
    public MapCenter(double x, double y, ObjectID id)
    {
	super(x, y, id);
	this.envelope = new Envelope(ObjectID.Envelope,  -96.728477, 32.966722,  -96.725505, 32.965182, Zoom);
    }

    @Override
    public void tick(LinkedHashSet<MapObject> objects)
    {
	envelope.tick(objects);
	if(!this.getBound().intersects(envelope.getBound(this.Zoom)) && useEnvelope)
	{
	    this.x = envelope.getCenter().getX();// - this.Width/2;
	    this.y = envelope.getCenter().getY();// - this.Height/2;
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
	}
	envelope.render(g);
    }

    /**
     * Not used
     */
    @Override
    public Rectangle2D getBound()
    {
	// TODO Auto-generated method stub
	return new Rectangle2D.Double(x,y,1,1);
    }
    
    /**
     * Testing Priority
     * @return
     */
    public Rectangle getTestPriority()
    {
	return priorityBounds(1);
    }
    
    /**
     * Testing Delete Priority
     * @return
     */
    public Rectangle getTestDeletePriority()
    {
	return priorityBounds(1.05);
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

    public Envelope getEnvelope()
    {
        return envelope;
    }

    public void setEnvelope(Double startLon,Double startLat,Double endLon,Double endLat)
    {
        this.envelope = new Envelope(ObjectID.Envelope, startLon, startLat, endLon, endLat, this.Zoom);
    }

    public boolean isEnvelopeUsed()
    {
        return useEnvelope;
    }

    public void setEnvelopeUsed(boolean useEnvelope)
    {
        this.useEnvelope = useEnvelope;
    }
    
    public void setZoom(int Zoom)
    {
	this.Zoom = Zoom;
	this.envelope.setZoom(Zoom);
    }
    
    public boolean isDrawingEnvelope()
    {
        return envelope.isDraw();
    }

    public void setDrawingEnvelope(boolean draw)
    {
        this.envelope.setDraw(draw);
    }

    public Color getEnvelopeColor()
    {
        return this.envelope.getColor();
    }

    public void setEnvelopeColor(Color color)
    {
        this.envelope.setColor(color);
    }
    
    
}
