package com.TylerValant.MapPanel.Listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.TylerValant.MapPanel.framework.MapObject;
import com.TylerValant.MapPanel.framework.ObjectID;
import com.TylerValant.MapPanel.window.Handler;

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener
{
    private Point downCoords;
    private Handler handle;
    private MapObject center;

    public MouseInput(Handler handle)
    {
	this.handle = handle;
    }
    
    public void mouseClicked(MouseEvent e)
    {
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
	e.getPoint();
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseMoved(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
	//System.out.println("Pressed");
	if (e.getButton() == MouseEvent.BUTTON1)
	{
	    downCoords = e.getPoint();
	    // downPosition = getMapPosition();
	}
    }

    public void mouseReleased(MouseEvent e)
    {
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
    
    
}
