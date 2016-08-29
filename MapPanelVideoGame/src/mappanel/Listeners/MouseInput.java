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
 * Tyler Valant
 * Listener
 * 1-19-2016
 * 1.0.0
 * <p>
 * The Class is for listening to all things mouse
 */
public class MouseInput extends MouseAdapter
{
    /**
     * The Down coords.
     */
    protected Point downCoords;
    /**
     * The Handle.
     */
    protected Handler handle;
    private MapPanel map;
    private boolean mouseEntered;

    /**
     * handle - the handler of the map
     * map - the map
     * <p>
     * Constructor
     *
     * @param map the map
     */
    public MouseInput(MapPanel map)
    {
        this.handle = map.getHandler();
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
     * @param e - the mouse event
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

    /**
     * If the mouse moves into a MapObject, then fire a mouseEnter event on the object.
     * If the mouse has entered a MapObject and now is not in that MapObject, then fire a mouseExit event on that object
     * @param e event
     */
    public void mouseMoved(MouseEvent e)
    {
        if(mouseEntered)
        {
            //This is for the Debug Panel
            Point mapPoint = new Point(handle.getCenter().getX() - map.getWidth()/2 + e.getX(), handle.getCenter().getY() - map.getHeight()/2 + e.getY());
            map.setMouseLon(MapPanel.position2lon(mapPoint.x, getMap().getZoom()));
            map.setMouseLat(MapPanel.position2lat(mapPoint.y, getMap().getZoom()));
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

    /**
     * Some mouses are more precise than others. That is why I use the getPreciseWheelRotation and not the getWheelRotation
     * @param e event
     */
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

    /**
     * e - the mouse event
     * <p>
     * Checks all the objects on the map to see if the event is on them, then dispatches the event to the one's hit
     *
     * @param e the event
     */
    protected void checkObjects(MouseEvent e)
    {
//	System.out.println("Checking objects");
	Point mapPoint = new Point(handle.getCenter().getX() - getMap().getWidth()/2 + e.getX(), handle.getCenter().getY() - getMap().getHeight()/2 + e.getY());
        handle.points.stream().filter(point -> point.getBound().contains(mapPoint)).forEach(point -> {
            e.setSource(point);
            point.dispatchEvent(e);
        });

        handle.shapes.stream().filter(shape -> shape.getBound().contains(mapPoint)).forEach(shape -> {
            e.setSource(shape);
            shape.dispatchEvent(e);
        });
    }

    /**
     * Gets map.
     *
     * @return the map
     */
//Start of getters/setters
    public MapPanel getMap()
    {
	return map;
    }

    /**
     * Sets map.
     *
     * @param map the map
     */
    public void setMap(MapPanel map)
    {
	this.map = map;
    }

    /**
     * Gets center.
     *
     * @return the center
     */
    public MapObject getCenter()
    {
        return handle.getCenter();
    }

    /**
     * Sets center.
     *
     * @param center the center
     */
    public void setCenter(MapObject center)
    {
        this.handle.setCenter( (Center) center);
    }
    //End of getters/setters
}
