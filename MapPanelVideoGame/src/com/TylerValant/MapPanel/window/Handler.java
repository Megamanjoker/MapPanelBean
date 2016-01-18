package com.TylerValant.MapPanel.window;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ConcurrentModificationException;
import java.lang.NullPointerException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.TylerValant.MapPanel.framework.MapObject;
import com.TylerValant.MapPanel.framework.ObjectID;
import com.TylerValant.MapPanel.objects.MapCenter;
import com.TylerValant.MapPanel.objects.MapPoint;
import com.TylerValant.MapPanel.objects.MapTile;

public class Handler 
{
    private static final int TileSize = 256;
    public List<MapObject> objects = new LinkedList<MapObject>();
    public List<MapObject> points = new LinkedList<MapObject>();
    public List<MapObject> shapes = new LinkedList<MapObject>();
    private String TileServerURL = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/";
    private MapCenter center;
    private int numberOfTiles = 1;
    private int MaxZoom = 19,MinZoom = 0, Zoom = 0;
    
    public void tick()
    {
	try
	{
	    for(MapObject object : objects)
	    {
		object.tick(objects);
		if(center == null && object.getId() == ObjectID.Center)
		{
		    center = (MapCenter) object;
		}
		else if( object.getId() == ObjectID.Tile)
		{
		    numberOfTiles++;
		}
	    }
	    if(numberOfTiles <= 1)
	    {
		for(MapObject object : objects)
		    {
		    	if( object.getId() == ObjectID.Tile)
			{
			    MapTile Map = (MapTile) object;
			    Map.setLast(true);
			}
		    }
	    }
	    numberOfTiles = 0;
	    removeLingeringTiles();
	}
	catch (ConcurrentModificationException e)
	{
	    
	}
	catch(NullPointerException e)
	{
	   
	}
    }
    
    public void render(Graphics g)
    {
	try
	{
	    for(MapObject object : objects)
	    {
		if(object.getId() != ObjectID.Center && object.getId() != ObjectID.Point && object.getId() != ObjectID.Shape)
		    object.render(g);
		else if(object.getId() == ObjectID.Point)
		    points.add(object);
		else if(object.getId() == ObjectID.Shape)
		    shapes.add(object);
	    }
	}
	catch (ConcurrentModificationException e)
	{
	    
	}
    }
    
    public void renderCenter(Graphics g)
    {
	if(center != null)
	{
	    center.render(g);
	}
	
    }
    
    public void renderShapes(Graphics g)
    {
	for(MapObject object : shapes)
	{
		object.render(g);
	}
    }
    
    public void renderPoints(Graphics g)
    {
	for(MapObject object : points)
	{
	    object.render(g);
	 
	}
    }
    
    public void addMapObject(MapObject object)
    {
	//System.out.println("MapObject Added");
	this.objects.add(object);
    }
    
    public void removeMapObject(MapObject object)
    {
	this.objects.remove(object);
    }
    
    /**
     * Creates the start of the map
     */
    public void CreateMap()
    {
	addMapObject(new MapTile(0,0,ObjectID.Tile,TileServerURL));
    }
    
    /**
     * Destroys previous map and Creates the map at some point 
     */
    public void CreateMap(int x,int y)
    {
//	System.out.println("X = " + (x - x % TileSize) + " Y = " + (y - y % TileSize));
	MapTile StartingTile = new MapTile(x - x % TileSize,y - y % TileSize,ObjectID.Tile,TileServerURL,Zoom);
	StartingTile.setLast(true);
	addMapObject(StartingTile);
	if(center != null)
	{
	    center.setY(y);
	    center.setX(x);
	}
	
    }
    
    
    /**
     * Creates the map at some point 
     */
    public void CreateMap(Point point)
    {
	clearMapTile();
	addMapObject(new MapTile(point.getX(),point.getY(),ObjectID.Tile,TileServerURL));
    }

    /**
     * Zoom in
     * @param point - zoom in on this point
     */
    public void ZoomIn(Point point)
    {
	if(Zoom <  MaxZoom)
	{
//	    System.out.println("Center x = " + center.x + " Center y = " + center.y + " ");
//	    System.out.println("old x = " + point.x + " old y = " + point.y + " ");
	    double lat = MapPanel.position2lat((int)(center.getY() + point.y - center.getHeight()/2), Zoom);
	    double lon = MapPanel.position2lon((int)(center.getX() + point.x - center.getWidth()/2), Zoom);
//	    System.out.print("Lon = " + centerLon + " Lat = " + centerLat + " ");
	    Zoom++;
	    clearMapTile();
//	    System.out.println("New x = " + MapPanel.lon2position(centerLon,Zoom) + " New Y = " + MapPanel.lat2position(centerLat, Zoom) + " ");
	    CreateMap(MapPanel.lon2position(lon,Zoom) ,MapPanel.lat2position(lat, Zoom));
	    updateObjectZoom();
	}
	System.out.println("Zoom In =  " + Zoom);
    }

    /**
     * Zoom out 
     * @param point - zoom out from this point
     */
    public void ZoomOut(Point point)
    {
	if(MinZoom < Zoom)
	{
	    
	    double lat = MapPanel.position2lat((int)(center.getY() + point.y - center.getHeight()/2), Zoom);
	    double lon = MapPanel.position2lon((int)(center.getX() + point.x - center.getWidth()/2), Zoom);
	    Zoom--;
	    clearMapTile();
	    CreateMap(MapPanel.lon2position(lon, Zoom), MapPanel.lat2position(lat, Zoom));
	    updateObjectZoom();
	}
	
	System.out.println("Zoom Out =  " + Zoom);
    }
    
    /**
     * Clear out all the tiles
     */
    private void clearMapTile()
    {
	try
	{
	    Iterator<MapObject> i = objects.iterator();
	    while(i.hasNext())
	    {
	        MapObject object = i.next();
	        if(object.getId() == ObjectID.Tile)
	        {
	            MapTile tile = (MapTile) object;
	            if(tile.getLoadImageThread().isAlive())
		    {
			tile.getLoadImageThread().interrupt();
		    }
	            tile.setGenerateEnable(false);
	        }
	    }
	    
	    i = objects.iterator();
	    while(i.hasNext())
	    {
	        MapObject object = i.next();
	        if(object.getId() == ObjectID.Tile)
	        {
	            objects.remove(object);
	        }
	    }
	}
	catch (ConcurrentModificationException e)
	{
	    // TODO Auto-generated catch block
	    //e.printStackTrace();
	    clearMapTile();
	    return;
	}
	
	
    }
    
    public String getTileServerURL()
    {
        return TileServerURL;
    }

    public void setTileServerURL(String tileServerURL)
    {
        TileServerURL = tileServerURL;
    }
    
    
    public void updateObjectZoom()
    {
	for(MapObject object : objects)
	{
	    object.setZoom(Zoom);
	}
    }
    
    
    /**
     * Removes any lingering tiles
     */
    private void removeLingeringTiles()
    {
	for(MapObject object : objects)
	{
	    if(object.getId() == ObjectID.Tile)
	    {
		MapTile tile = (MapTile) object;
		if(!center.getTestDeletePriority().intersects(tile.getBound()) && !tile.isLast())
		{
//		    System.out.println("Deleting X = " + tile.getX() + ", Y =" + tile.getY());
		    if(tile.getLoadImageThread().isAlive())
		    {
			tile.getLoadImageThread().interrupt();
		    }
		    objects.remove(tile);
		    
		}
	    }
	}
	
	
    }

    public int getZoom()
    {
        return Zoom;
    }

    public void setZoom(int zoom)
    {
        Zoom = zoom;
        updateObjectZoom();
    }
    
    public void ClearScreen(Graphics g)
    {
	g.clearRect((int) -(Math.pow(2,  Zoom + 1 * 2) * TileSize)/2, (int)-(Math.pow(2, Zoom + 1 * 2) * TileSize)/2, (int)Math.pow(2,  Zoom + 1 * 2) * TileSize, (int)Math.pow(2,  Zoom + 1 * 2) * TileSize);
//	g.get
    }
    
}
