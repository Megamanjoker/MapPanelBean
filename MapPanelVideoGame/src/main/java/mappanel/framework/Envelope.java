package mappanel.framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Tyler Valant
 * Framework
 * 1-19-2016
 * 1.0.0
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

    /**
     * Instantiates a new Envelope.
     *
     * @param startLon the start lon
     * @param startLat the start lat
     * @param endLon   the end lon
     * @param endLat   the end lat
     * @param Zoom     the zoom
     */
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
     * @param g graphic to be render on
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
     * Gets bound.
     *
     * @param zoom the zoom
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
     *
     * @param lon the lon
     * @param z   the z
     * @return returns the X-coordinated of the given Longitude and Zoom
     * @see <a href="http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Lon..2Flat._to_tile_numbers">Source</a>
     */
    public static int lon2position(double lon, int z)
    {
        double xmax = TILE_SIZE * Math.pow(2, z);
        return (int) Math.floor((lon + 180) / 360 * xmax);
    }

    /**
     * This is a conversion of Latitude and Zoom level to screen X-coordinates.
     *
     * @param lat the lat
     * @param z   the z
     * @return returns the Y-coordinated of the given Latitude and Zoom
     * @see <a href="http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Lon..2Flat._to_tile_numbers">Source</a>
     */
    public static int lat2position(double lat, int z)
    {
        double ymax = TILE_SIZE * Math.pow(2, z);
        return (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * ymax);
    }

    /**
     * Gets center.
     *
     * @return returns the center point of the envelope
     */
    public Point getCenter()
    {
        return center;
    }

    /**
     * Is draw boolean.
     *
     * @return the boolean
     */
//Start of getters/setters
    public boolean isDraw()
    {
        return draw;
    }

    /**
     * Sets draw.
     *
     * @param draw the draw
     */
    public void setDraw(boolean draw)
    {
        this.draw = draw;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(Color color)
    {
        this.color = color;
    }

    /**
     * Sets start lon.
     *
     * @param startLon the start lon
     */
    public void setStartLon(double startLon)
    {
        this.startLon = startLon;
    }

    /**
     * Gets start lon.
     *
     * @return the start lon
     */
    public double getStartLon()
    {
        return startLon;
    }

    /**
     * Sets start lat.
     *
     * @param startLat the start lat
     */
    public void setStartLat(double startLat)
    {
        this.startLat = startLat;
    }

    /**
     * Gets start lat.
     *
     * @return the start lat
     */
    public double getStartLat()
    {
        return startLat;
    }

    /**
     * Sets end lon.
     *
     * @param endLon the end lon
     */
    public void setEndLon(double endLon)
    {
        this.endLon = endLon;
    }

    /**
     * Gets end lon.
     *
     * @return the end lon
     */
    public double getEndLon()
    {
        return endLon;
    }

    /**
     * Sets end lat.
     *
     * @param endLat the end lat
     */
    public void setEndLat(double endLat)
    {
        this.endLat = endLat;
    }

    /**
     * Gets end lat.
     *
     * @return the end lat
     */
    public double getEndLat()
    {
        return endLat;
    }
    //end of getters/setters
}
