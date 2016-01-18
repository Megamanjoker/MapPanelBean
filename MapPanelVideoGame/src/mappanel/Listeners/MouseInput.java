package mappanel.Listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import mappanel.framework.MapObject;
import mappanel.objects.MapCenter;
import mappanel.objects.MapPoint;
import mappanel.objects.MapShape;
import mappanel.window.Handler;
import mappanel.window.MapPanel;

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener
{
    private Point downCoords;
    private Handler handle;
    private MapObject center;
    private MapPanel map;

    public MouseInput(Handler handle, MapCenter center,MapPanel map)
    {
	this.handle = handle;
	this.center = center;
	this.setMap(map);
	
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

    private void handleDrag(MouseEvent e)
    {
	//System.out.println("Drag");
	if (downCoords != null)
	{
	    int tx = downCoords.x - e.getX();
	    int ty = downCoords.y - e.getY();
//	    for(MapObject object : handle.objects)
//	    {
//		//System.out.println("Type " + object.id);
//		if(object.getId() == ObjectID.Center)
//		{
//		    System.out.print("From X: " + object.getX() + " Y: " + object.getY());
		    center.setX(center.getX() + tx);
		    center.setY(center.getY() + ty);
//		    System.out.println(" To X: " + object.getX() + " Y: " + object.getY());
//		}
//	    }
	    downCoords.x = e.getX();
	    downCoords.y = e.getY();
	}
    }

    private void handlePosition(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
	checkObjects(e);
    }

    public void mouseExited(MouseEvent e)
    {
	Point mapPoint = new Point(center.getX() - getMap().getWidth()/2 + e.getX(), center.getY() - getMap().getHeight()/2 + e.getY());
	checkObjects(e);
    }

    public void mouseMoved(MouseEvent e)
    {
	//This is for the Debug Panel
	Point mapPoint = new Point(center.getX() - map.getWidth()/2 + e.getX(), center.getY() - map.getHeight()/2 + e.getY());
	getMap().mouseLat = MapPanel.position2lat((int) mapPoint.getY(), getMap().getZoom());
	getMap().mouseLon = MapPanel.position2lon((int) mapPoint.getX(), getMap().getZoom());
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
			ml.mouseEntered(e);
		    }
		}
	    }
	    else if(t.isEnter())
	    {
		t.setEnter(false);
		for(MouseListener ml : t.getMouseListeners())
		{
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
			ml.mouseEntered(e);
		    }
		}
	    }
	    else if(t.isEnter())
	    {
		t.setEnter(false);
		for(MouseListener ml : t.getMouseListeners())
		{
		    ml.mouseExited(e);
		}
	    }
	}
	
	
	
	
	
    }

    public void mousePressed(MouseEvent e)
    {
	//System.out.println("Pressed");
	if (e.getButton() == MouseEvent.BUTTON1)
	{
	    downCoords = e.getPoint();
	    // downPosition = getMapPosition();
	}
	
	checkObjects(e);
    }

    public void mouseReleased(MouseEvent e)
    {
	checkObjects(e);
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
	int wheel = e.getWheelRotation();
	
	if(wheel < 0)
	{
	    handle.ZoomIn(e.getPoint());
	}
	else
	{
	    handle.ZoomOut(e.getPoint());
	}
    }

    public MapObject getCenter()
    {
        return center;
    }

    public void setCenter(MapObject center)
    {
        this.center = center;
    }
    
    protected void checkObjects(MouseEvent e)
    {
	Point mapPoint = new Point(center.getX() - getMap().getWidth()/2 + e.getX(), center.getY() - getMap().getHeight()/2 + e.getY());
	for(MapObject point: handle.points)
	{
	    if(point.getBound().contains(mapPoint))
	    {
		point.dispatchEvent(e);
	    }
	}
	
	for(MapObject shape: handle.shapes)
	{
	    if(shape.getBound().contains(mapPoint))
	    {
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
