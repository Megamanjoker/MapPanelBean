package mappanel.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.util.*;
import java.lang.NullPointerException;

import mappanel.framework.Camera;
import mappanel.framework.Center;
import mappanel.framework.MapObject;
import mappanel.framework.ObjectID;
import mappanel.objects.MapPoint;
import mappanel.objects.MapShape;
import mappanel.objects.MapTile;

/**
 * 
 *  Tyler Valant
 *  Window
 *  1-19-2016
 *  1.0.0
 *
 */
public class Handler 
{
    private static final int TileSize = 256;
    public LinkedHashSet<MapObject> objects = new LinkedHashSet<>();
    public LinkedHashSet<MapObject> points = new LinkedHashSet<>();
    public LinkedHashSet<MapObject> shapes = new LinkedHashSet<>();
    public LinkedHashSet<String> listOfTileServerURL = new LinkedHashSet<>();
	private Center center;
    private int numberOfTiles = 1;
    private int MaxZoom = 19,MinZoom = 0, Zoom = 0;
    private LinkedHashSet<ObjectID> idBlackList = new LinkedHashSet<ObjectID>(Arrays.asList(ObjectID.Center,ObjectID.Tile,ObjectID.Envelope));
    private Camera camera;
    
    
    public Handler(double initLon, double initLat, int initZoom, MapPanel mapPanel)
    {
		String tileServerURL = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/";
		this.listOfTileServerURL.add(tileServerURL);
        this.Zoom = initZoom;
        center = new Center(MapPanel.lon2position(initLon,initZoom),MapPanel.lat2position(initLat,initZoom));
        camera = new Camera(MapPanel.lon2position(initLon,initZoom), MapPanel.lat2position(initLat,initZoom),mapPanel);
        objects.add(center);
    }

    /**
     * tick all the MapObjects
     */
    public void tick()
    {
        try
        {
            for(MapObject object : objects)
            {
                object.tick(objects);
                if( object.getId() == ObjectID.Tile)
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
                        Map.setLast();
                    }
                }
            }
            numberOfTiles = 0;

            center.tick(objects);
            camera.tick(center);
            removeLingeringTiles();
        }
        catch (ConcurrentModificationException e)
        {

        }
        catch(NullPointerException e)
        {

        }
    }

    /**
     * render all the MapObjects
     * @param g
     */
    public void render(Graphics g)
    {
        try
        {
            ArrayList<MapObject> renderingList = new ArrayList<>(objects);
            renderingList.sort((o1, o2) -> o1.getRenderingPriority() - o2.getRenderingPriority());
            for(MapObject object : renderingList)
                object.render(g);
        }
        catch (ConcurrentModificationException e)
        {

        }
    }

    /**
     * Creates the start of the map
     */
    public void CreateMap()
    {
        if(center != null)
            addMapObject(new MapTile(center.getX() - center.getX()%256,center.getY() - center.getY()%256,listOfTileServerURL));
    }
    
    /**
     * Destroys previous map and Creates the map at some point 
     */
    public void CreateMap(int x,int y)
    {
        MapTile StartingTile = new MapTile(x - x % TileSize,y - y % TileSize,listOfTileServerURL, this.center,Zoom);
        StartingTile.setLast();
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
        addMapObject(new MapTile(point.x,point.y,listOfTileServerURL));
    }

    /**
     * Zoom in
     *  point - zoom in on this point
     */
    public void ZoomIn(Point point)
    {
	if(Zoom <  MaxZoom)
	{
	    double lat = MapPanel.position2lat(center.getY() + point.y - center.getHeight()/2, Zoom);
	    double lon = MapPanel.position2lon(center.getX() + point.x - center.getWidth()/2, Zoom);
	    double envelopeLon = MapPanel.position2lon((int)(center.getEnvelope().getCenter().getX()), Zoom);
	    double envelopeLat = MapPanel.position2lat((int)(center.getEnvelope().getCenter().getY()), Zoom);
	    Zoom++;
	    clearMapTile();
	    
	    int x = MapPanel.lon2position(lon, Zoom);
	    int y = MapPanel.lat2position(lat, Zoom);
	    if(center.getEnvelope().getBound().intersects(new Rectangle(x,y,1,1)))
	    {
		CreateMap(x, y);
	    }
	    else if(!center.isEnvelopeUsed())
	    {
		CreateMap(x, y);
	    }
	    else 
	    {
		CreateMap(MapPanel.lon2position(envelopeLon, Zoom), MapPanel.lat2position(envelopeLat, Zoom));
	    }
	    updateObjectZoom();
	}
    }

    /**
     * Zoom out 
     *  point - zoom out from this point
     */
    public void ZoomOut(Point point)
    {
	if(MinZoom < Zoom)
	{
	    
	    double lat = MapPanel.position2lat(center.getY() + point.y - center.getHeight()/2, Zoom);
	    double lon = MapPanel.position2lon(center.getX() + point.x - center.getWidth()/2, Zoom);

	    double envelopeLon = MapPanel.position2lon((int)(center.getEnvelope().getCenter().getX()), Zoom);
	    double envelopeLat = MapPanel.position2lat((int)(center.getEnvelope().getCenter().getY()), Zoom);
	    Zoom--;
	    clearMapTile();
	    int x = MapPanel.lon2position(lon, Zoom);
	    int y = MapPanel.lat2position(lat, Zoom);
	    if(center.getEnvelope().getBound().intersects(new Rectangle(x,y,1,1)))
	    {
		CreateMap(x, y);
	    }
	    else if(!center.isEnvelopeUsed())
	    {
		CreateMap(x, y);
	    }
	    else 
	    {
		CreateMap(MapPanel.lon2position(envelopeLon, Zoom), MapPanel.lat2position(envelopeLat, Zoom));
	    }
	    
	    updateObjectZoom();
	}
    }
    
    /**
     * Clear out all the tiles
     */
    protected void clearMapTile()
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
	            tile.setGenerateEnable();
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
	    clearMapTile();
	}
	
	
    }

    /**
     * Updates the zoom level on all the MapObjects
     */
    public void updateObjectZoom()
    {
//	this.center.setZoom(Zoom);

        try
        {
            for(MapObject object : objects)
            {
                object.setZoom(Zoom);
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Updates all the tiles urls
     */
    public void updateTileUrl()
    {
        objects.stream().filter(object -> object.getId() == ObjectID.Tile).forEach(object -> {
            MapTile tile = (MapTile) object;
            tile.setListOfTileServerURL(listOfTileServerURL);
            tile.setDirty();
        });
    }


    /**
     * Removes any lingering tiles
     */
    protected void removeLingeringTiles()
    {
        objects.stream().filter(object -> object.getId() == ObjectID.Tile).forEach(object -> {
            MapTile tile = (MapTile) object;
            if (!center.getDeletePriority().intersects(tile.getBound()) && !tile.isLast()) {
                if (tile.getLoadImageThread().isAlive()) {
                    tile.getLoadImageThread().interrupt();
                }
                objects.remove(tile);
            }
        });


    }

    //Start of getters/setters
    public void addMapObject(MapObject object)
    {
        this.objects.add(object);
    }

    public void removeMapObject(MapObject object)
    {
        this.objects.remove(object);
    }

    public int getMaxZoom()
    {
        return MaxZoom;
    }

    public void setMaxZoom(int maxZoom)
    {
        MaxZoom = maxZoom;
    }

    public int getMinZoom()
    {
        return MinZoom;
    }

    public void setMinZoom(int minZoom)
    {
        MinZoom = minZoom;
    }

    public void setUseEnvelope(boolean useEnvelope)
    {
	center.setEnvelopeUsed(useEnvelope);
    }
    
    public boolean getUseEnvelope()
    {
	return center.isEnvelopeUsed();
    }
    

    public int getZoom()
    {
        return Zoom;
    }

    public void setZoom(int zoom)
    {
        double lat = MapPanel.position2lat((int)(center.getY()), Zoom);
        double lon = MapPanel.position2lon((int)(center.getX()), Zoom);

        double envolpeLon = MapPanel.position2lon((int)(center.getEnvelope().getCenter().getX()), Zoom);
        double envolpeLat = MapPanel.position2lat((int)(center.getEnvelope().getCenter().getY()), Zoom);
        clearMapTile();
        int x = MapPanel.lon2position(lon, zoom);
        int y = MapPanel.lat2position(lat, zoom);
        if(center.getEnvelope().getBound().intersects(new Rectangle(x,y,1,1)))
        {
            CreateMap(x, y);
        }
        else if(!center.isEnvelopeUsed())
        {
            CreateMap(x, y);
        }
        else
        {
            CreateMap(MapPanel.lon2position(envolpeLon, Zoom), MapPanel.lat2position(envolpeLat, Zoom));
        }

        this.Zoom = zoom;
        updateObjectZoom();
    }
    
    public void setInitZoom(int zoom)
    {
        this.Zoom = zoom;
        updateObjectZoom();
    }
    
    public void ClearScreen(Graphics g)
    {
	    g.clearRect((int) - (Math.pow(2,  Zoom + 1 * 2) * TileSize)/2, (int)-(Math.pow(2, Zoom + 1 * 2) * TileSize)/2, (int)Math.pow(2,  Zoom + 1 * 2) * TileSize, (int)Math.pow(2,  Zoom + 1 * 2) * TileSize);
    }
    
    public void addPoint(MapPoint point)
    {
        objects.add(point);
        points.add(point);
    }
    
    public void clearPoints()
    {
        if (points != null && !points.isEmpty())
        {
            for (MapObject object : points)
            {
                for(MouseListener l :object.getMouseListeners())
                    object.removeMouseListener(l);

                objects.remove(object);
            }
            points.clear();
        }
    }
    
    public void addShape(MapShape shape)
    {
	    objects.add(shape);
	    shapes.add(shape);
    }
    
    public void clearShapes()
    {
	if (shapes != null && !shapes.isEmpty())
	{
	    for (MapObject object : shapes)
	    {
		for(MouseListener l :object.getMouseListeners())
		    object.removeMouseListener(l);
		objects.remove(object);
	    }
	    shapes.clear();
	}
    }

    public Center getCenter()
    {
        return center;
    }

    public void setCenter(Center center)
    {
        this.center = center;
    }

    public LinkedHashSet<MapObject> collisionCheckAt(int x, int y)
    {
        LinkedHashSet<MapObject> collidedObjects = new LinkedHashSet<MapObject>();
        for(MapObject object: objects)
        {
            if(object.getBound().contains(center.getX() - center.getWidth()/2 + x, center.getY() - center.getHeight()/2 + y) && !isIdBlackListed(object))
            {
            collidedObjects.add(object);
            }
        }

        return collidedObjects;
    }

    private boolean isIdBlackListed(MapObject object)
    {
		return idBlackList.contains(object.getId());
    }

    public void setEnvelope(double startLon, double startLat, double endLon, double endLat)
    {
	    center.setEnvelope(startLon, startLat, endLon, endLat);
    }

    public void setDrawingEnvelope(boolean draw)
    {
	center.setDrawingEnvelope(draw);
    }

    public void setEnvelopeColor(Color envelopeColor)
    {
	center.setEnvelopeColor(envelopeColor);
    }

    public void setEnvelopeUsed(boolean useEnvelope)
    {
	center.setEnvelopeUsed(useEnvelope);
    }

    public Camera getCamera()
    {
        return camera;
    }

    public void setCenterLocation(double lon, double lat)
    {
        center.setX(MapPanel.lon2position(lon, Zoom));
        center.setY(MapPanel.lat2position(lat, Zoom));
        CreateMap(MapPanel.lon2position(lon, Zoom),MapPanel.lat2position(lat, Zoom));
	
    }
    
    public void addURL(String url)
    {
        this.listOfTileServerURL.add(url);
        updateTileUrl();
    }
    
    public void clearURLs()
    {
        this.listOfTileServerURL.clear();
        updateTileUrl();
    }

    //End of getters/setters
}
