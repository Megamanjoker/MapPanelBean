package com.TylerValant.MapPanel.framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import com.TylerValant.MapPanel.window.MapPanel;

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
    
    
    public Envelope(double x, double y, ObjectID id)
    {
	super(x, y, id);
	// TODO Auto-generated constructor stub
    }
    
    public Envelope(ObjectID id, Double startLon,Double startLat,Double endLon,Double endLat)
    {
	super(0, 0, id);
	this.startLat = startLat;
	this.startLon = startLon;
	this.endLat = endLat;
	this.endLon = endLon;
	int newX = (lon2position(startLon, Zoom) + lon2position(endLon, Zoom)) /2;
	int newY = (lat2position(startLat, Zoom) + lat2position(endLat, Zoom)) /2;
	center = new Point( newX, newY);
    }
    
    public Envelope(ObjectID id, Double startLon,Double startLat,Double endLon,Double endLat, int Zoom)
    {
	super(0, 0, id);
	this.startLat = startLat;
	this.startLon = startLon;
	this.endLat = endLat;
	this.endLon = endLon;
	this.Zoom = Zoom;
	int newX = (lon2position(startLon, Zoom) + lon2position(endLon, Zoom)) /2;
	int newY = (lat2position(startLat, Zoom) + lat2position(endLat, Zoom)) /2;
	center = new Point( newX, newY);
	this.x = center.getX();
	this.y = center.getY();
    }

    @Override
    public void tick(List<MapObject> objects)
    {
	
	int newX = (lon2position(startLon, Zoom) + lon2position(endLon, Zoom)) /2;
	int newY = (lat2position(startLat, Zoom) + lat2position(endLat, Zoom)) /2;
	center = new Point( newX, newY);
    }

    @Override
    public void render(Graphics g)
    {
	    g.setColor(Color.CYAN);
	    int startX = lon2position(startLon,this.getZoom());
	    int startY = lat2position(startLat, this.getZoom());
	    int dx =  lon2position(endLon, this.getZoom()) - startX;
	    int dy =  lat2position(endLat, this.getZoom()) - startY;
	    g.drawRect(startX,startY, dx, dy);
//	    System.out.println("Lon = " + startLon + " ,Lat = " + startLat);
//	    System.out.println("startX = "+ startX + " ,startY = "+ startY + ", dx = "+ dx + ", dy = "+ dy + " ,Zoom = " + Zoom);
    }

    @Override
    public Rectangle getBound()
    {
	int StartX = lon2position(startLon,Zoom);
	int StartY = lat2position(startLat, Zoom);
	int Width = lon2position(endLon,Zoom) - StartX;
	int Height = lat2position(endLat, Zoom) - StartY;
	return new Rectangle(StartX, StartY, Width, Height);
    }
    
    public Rectangle getBound(int zoom)
    {
	this.Zoom = zoom;
	int StartX = lon2position(startLon,zoom);
	int StartY = lat2position(startLat, zoom);
	int Width = lon2position(endLon,zoom) - StartX;
	int Height = lat2position(endLat, zoom) - StartY;
	return new Rectangle(StartX, StartY, Width, Height);
    }
    
    public static int lon2position(double lon, int z)
    {
	double xmax = TILE_SIZE * Math.pow(2, z);
	return (int) Math.floor((lon + 180) / 360 * xmax);
    }

    public static int lat2position(double lat, int z)
    {
	double ymax = TILE_SIZE * Math.pow(2, z);
	return (int) Math.floor(
		(1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * ymax);
    }

    public Point getCenter()
    {
        return center;
    }

//    public void setCenter(Point center)
//    {
//        this.center = center;
//    }
    
    
}
