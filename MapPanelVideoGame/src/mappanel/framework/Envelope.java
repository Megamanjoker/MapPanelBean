package mappanel.framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 
 *  Tyler Valant
 *  Framework
 *  1-19-2016
 *  1.0.0
 *
 */
public class Envelope extends MapObject
{
    /**
     * 
     */
    private static final long serialVersionUID = 8514287961487124557L;
    private static final double TILE_SIZE = 256;
    private static final boolean DEBUG = false;
    private double startLon, startLat, endLon, endLat;
    private Point center;
    private boolean draw = true;
    private Color color = Color.CYAN;

    public Envelope(double startLon,double startLat,double endLon,double endLat, Integer Zoom)
    {
        super(0, 0, ObjectID.Envelope,3);
        if(Zoom != null)
            this.Zoom = Zoom;

        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;

        int newX = (lon2position(startLon, this.Zoom) + lon2position(endLon, this.Zoom)) /2;
        int newY = (lat2position(startLat, this.Zoom) + lat2position(endLat, this.Zoom)) /2;
        center = new Point( newX, newY);
        this.x = newX;
        this.y = newY;
    }

    /**
     * Every Tick, the envelope's center is going to be moved to the Center class's position
     * @param objects - all the object on the map
     */
    public void tick(LinkedHashSet<MapObject> objects)
    {
	
        int newX = (lon2position(startLon, Zoom) + lon2position(endLon, Zoom)) /2;
        int newY = (lat2position(startLat, Zoom) + lat2position(endLat, Zoom)) /2;

        center = new Point( newX, newY);
    }

    /**
     * Renders the envelope, if enable to
     * @param g
     */
    public void render(Graphics g)
    {
	    if (draw)
	    {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            g2d.draw(getBound());
	    }
    }

    /**
     * @return - the boundary line of the envelope
     */
    public Rectangle getBound()
    {
        int StartX = lon2position(startLon,Zoom);
        int StartY = lat2position(startLat, Zoom);
        int Width = lon2position(endLon,Zoom) - StartX;
        int Height = lat2position(endLat, Zoom) - StartY;
        return new Rectangle(StartX, StartY, Width, Height);
    }

    /**
     * @param zoom
     * @return - the boundary line of the envelope at a given zoom level
     */
    public Rectangle getBound(int zoom)
    {
        this.Zoom = zoom;
        int StartX = lon2position(startLon,zoom);
        int StartY = lat2position(startLat, zoom);
        int Width = lon2position(endLon,zoom) - StartX;
        int Height = lat2position(endLat, zoom) - StartY;
        return new Rectangle(StartX, StartY, Width, Height);
    }

    /**
     * This is a conversion of Longitude and Zoom level to screen X-coordinates.
     * @See <a href="http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Lon..2Flat._to_tile_numbers">Source</>
     * @param lon
     * @param z
     * @return returns the X-coordinated of the given Longitude and Zoom
     */
    public static int lon2position(double lon, int z)
    {
        double xmax = TILE_SIZE * Math.pow(2, z);
        return (int) Math.floor((lon + 180) / 360 * xmax);
    }

    /**
     * This is a conversion of Latitude and Zoom level to screen X-coordinates.
     * @See <a href="http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Lon..2Flat._to_tile_numbers">Source</>
     * @param lat
     * @param z
     * @return returns the Y-coordinated of the given Latitude and Zoom
     */
    public static int lat2position(double lat, int z)
    {
        double ymax = TILE_SIZE * Math.pow(2, z);
        return (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * ymax);
    }

    /**
     *
     * @return returns the center point of the envelope
     */
    public Point getCenter()
    {
        return center;
    }

    //Start of getters/setters
    public boolean isDraw()
    {
        return draw;
    }

    public void setDraw(boolean draw)
    {
        this.draw = draw;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void setStartLon(double startLon)
    {
        this.startLon = startLon;
    } 
    
    public double getStartLon()
    {
        return startLon;
    }

    public void setStartLat(double startLat)
    {
        this.startLat = startLat;
    }
    
    public double getStartLat()
    {
        return startLat;
    }

    public void setEndLon(double endLon)
    {
        this.endLon = endLon;
    }
    
    public double getEndLon()
    {
        return endLon;
    }   

    public void setEndLat(double endLat)
    {
        this.endLat = endLat;
    }

    public double getEndLat()
    {
        return endLat;
    }
    //end of getters/setters
}
