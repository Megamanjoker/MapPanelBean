package mappanel.Listeners;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import mappanel.framework.Center;
import mappanel.framework.MapObject;
import mappanel.objects.MapPoint;
import mappanel.objects.MapShape;
import mappanel.window.Handler;
import mappanel.window.MapPanel;

/**
 * 
 *  Tyler Valant
 *  Listener
 *  1-19-2016
 *  1.0.0
 *
 * The Class is for listening to all things mouse 
 */

public class MouseInput extends MouseAdapter
{
    protected Point downCoords;
    protected Handler handle;
    private MapPanel map;
    private boolean mouseEntered;

    /**
     *  handle - the handler of the map
     *  map - the map
     * 
     * Constructor
     */
    public MouseInput(Handler handle, MapPanel map)
    {
	this.handle = handle;
	this.map = map;
	
    }
    
    public void mouseClicked(MouseEvent e)
    {
	checkObjects(e);
    }

    public void mouseDragged(MouseEvent e)
    {
	handlePosition(e);
	handleDrag(e);
    }

    /**
     * When dragging the mouse, the center moves around mouse cursor. 
     *  e - the mouse event
     */
    private void handleDrag(MouseEvent e)
    {
	if (downCoords != null)
	{
	    int tx = downCoords.x - e.getX();
	    int ty = downCoords.y - e.getY();
	    handle.getCenter().setX(handle.getCenter().getX() + tx);
	    handle.getCenter().setY(handle.getCenter().getY() + ty);
	    downCoords.x = e.getX();
	    downCoords.y = e.getY();
	}
    }

    private void handlePosition(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
	mouseEntered = true;
    }

    public void mouseExited(MouseEvent e)
    {
	mouseEntered = false;
    }

    public void mouseMoved(MouseEvent e)
    {
	if(mouseEntered)
	{
	    //This is for the Debug Panel
	    Point mapPoint = new Point(handle.getCenter().getX() - map.getWidth()/2 + e.getX(), handle.getCenter().getY() - map.getHeight()/2 + e.getY());
	    map.setMouseLat(MapPanel.position2lat((int) mapPoint.getY(), getMap().getZoom()));
	    map.setMouseLon(MapPanel.position2lon((int) mapPoint.getX(), getMap().getZoom()));
	    
	    //
	
	    for(MapObject point: handle.points)
	    {
		MapPoint t = (MapPoint) point;
		if(point.getBound().contains(mapPoint))
		{
		    if(!t.isEnter())
		    {
			t.setEnter(true);
			for(MouseListener ml : t.getMouseListeners())
			{
                            e.setSource(point);
			    ml.mouseEntered(e);
			}
		    }
		}
		else if(t.isEnter())
		{
		    t.setEnter(false);
		    for(MouseListener ml : t.getMouseListeners())
		    {
                        e.setSource(point);
			ml.mouseExited(e);
		    }
		}
	    }
	
	    for(MapObject shape: handle.shapes)
	    {
		MapShape t = (MapShape) shape;
		if(shape.getBound().contains(mapPoint))
		{
		    if(!t.isEnter())
		    {
			t.setEnter(true);
			for(MouseListener ml : t.getMouseListeners())
			{
                            e.setSource(shape);
			    ml.mouseEntered(e);
			}
		    }
		}
		else if(t.isEnter())
		{
		    t.setEnter(false);
		    for(MouseListener ml : t.getMouseListeners())
		    {
                        e.setSource(shape);
			ml.mouseExited(e);
		    }
	    	}
	    }
	}
    }

    public void mousePressed(MouseEvent e)
    {
	if (e.getButton() == MouseEvent.BUTTON1)
	{
	    downCoords = e.getPoint();
	}
	
	checkObjects(e);
    }

    public void mouseReleased(MouseEvent e)
    {
	checkObjects(e);
    }

    
    public void mouseWheelMoved(MouseWheelEvent e)
    {
	double wheel = e.getPreciseWheelRotation();
	if(wheel <= 0)
	{
	    handle.ZoomIn(e.getPoint());
	}
	else
	{
	    handle.ZoomOut(e.getPoint());
	}
	super.mouseWheelMoved(e);
    }

    public MapObject getCenter()
    {
        return handle.getCenter();
    }

    public void setCenter(MapObject center)
    {
        this.handle.setCenter( (Center) center);
    }
    
    /**
     *  e - the mouse event
     * 
     * Checks all the objects on the map to see if the event is on them, then dispatchs the event to the one's hit
     */
    protected void checkObjects(MouseEvent e)
    {
//	System.out.println("Checking objects");
	Point mapPoint = new Point(handle.getCenter().getX() - getMap().getWidth()/2 + e.getX(), handle.getCenter().getY() - getMap().getHeight()/2 + e.getY());
	for(MapObject point: handle.points)
	{
	    if(point.getBound().contains(mapPoint))
	    {
                e.setSource(point);
		point.dispatchEvent(e);
	    }
	}
	
	for(MapObject shape: handle.shapes)
	{
	    if(shape.getBound().contains(mapPoint))
	    {
                e.setSource(shape);
		shape.dispatchEvent(e);
	    }
	}
    }

    
    public MapPanel getMap()
    {
	return map;
    }

    public void setMap(MapPanel map)
    {
	this.map = map;
    }
    
}
