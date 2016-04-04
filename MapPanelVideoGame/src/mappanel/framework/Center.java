package mappanel.framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashSet;
import java.util.List;

import mappanel.window.MapPanel;

/**
 * 
 *  Tyler Valant
 *  Framework
 *  1-19-2016
 *  1.0.0
 *
 */
public class Center extends MapObject
{
    /**
     * 
     */
    private static final long serialVersionUID = 7003532228198805203L;
    private double Width=1;
    private double Height=1;
    double lon,lat;
    private Envelope envelope;
    private boolean useEnvelope = false;
    private int oldX = 0,oldY = 0;
    
    
    public Center(int x, int y)
    {
        super(x, y, ObjectID.Center,2);
        this.envelope = new Envelope(-96.728477, 32.966722,  -96.725505, 32.965182, Zoom);
    }

    
    
    public void tick(LinkedHashSet<MapObject> objects)
    {
        if(oldX != x || oldY != y)
    {
        firePropertyChange("center", new Point(oldX, oldY), new Point((int)x, (int)y));
        this.oldX = x;
        this.oldY = y;
    }

        envelope.tick(objects);
        if(!this.getBound().intersects(envelope.getBound(this.Zoom)) && useEnvelope)
        {
            this.x = envelope.getCenter().x;
            this.y = envelope.getCenter().y;
        }
    }

    /**
     * Only renders in debug mode
     */
    
    public void render(Graphics g)
    {
        boolean DEBUG = false;
        if(DEBUG)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.ORANGE);
            g2d.fillRect((int)x - 4, (int)y - 4, 8, 8);
            g2d.setColor(Color.RED);
            g2d.draw(getPriority());
            g2d.setColor(Color.BLUE);
            g2d.draw(getDeletePriority());
        }
        envelope.render(g);
    }

    /**
     * Not used
     */
    public Rectangle2D getBound()
    {
	    // TODO Auto-generated method stub
	    return new Rectangle2D.Double(x,y,1,1);
    }
    
    /**
     * the tiles in this 
     *  - Collision box of don't delete box
     */
    public Rectangle2D getPriority()
    {
	return priorityBounds(.3);
    }
    
    /**
     * Anything outside this box should be deleted
     *  - Collision box of do delete box
     */
    public Rectangle2D getDeletePriority()
    {
	return priorityBounds(.4);
    }

    /**
     * This method is to minimize the number of times, I have to write the equation Rect = (x - w/2 - (w/2 * %) , y - h/2 - (h/2 * %), w + w * %, h + h * %)
     *  percent - the percent of increase or decrease of the bounds
     *  - the bound with the percent
     */
    private Rectangle2D priorityBounds(double percent)
    {
	    return new Rectangle2D.Double((x - Width/2) -  ((Width/2) * percent),(y - Height/2) - ((Height/2) * percent),(Width + Width * percent) - 1 ,(Height + Height * percent) - 1) ;
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
        this.envelope.setStartLon(startLon);
        this.envelope.setStartLat(startLat);
        this.envelope.setEndLon(endLon);
        this.envelope.setEndLat(endLat);
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
    //	this.lon = MapPanel.position2lon((int) this.x, this.Zoom);
    //	this.lat = MapPanel.position2lat((int) this.y, this.Zoom);
        this.Zoom = Zoom;
    //	this.x = MapPanel.lon2position(this.lon, this.Zoom);
    //	this.y = MapPanel.lat2position(this.y, this.Zoom);
    //	System.out.println("Setting Zoom");
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
