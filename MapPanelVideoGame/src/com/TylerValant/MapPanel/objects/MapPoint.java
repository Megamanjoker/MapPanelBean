package com.TylerValant.MapPanel.objects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.TylerValant.MapPanel.framework.MapObject;
import com.TylerValant.MapPanel.framework.ObjectID;
import com.TylerValant.MapPanel.window.MapPanel;

public class MapPoint extends MapObject implements ImageObserver
{
    /**
     * 
     */
    private static final long serialVersionUID = 3169731756082120901L;
    private Image image;
    private int width,height;
    private double lat,lon;
    private String Name;
    
    public MapPoint(double x, double y, ObjectID id)
    {
	super(x, y, id);
	try
	{
	    image = ImageIO.read(this.getClass().getResource("/image/X.png"));
	    this.width = image.getWidth(this);
	    this.height = image.getHeight(this);
	    
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public MapPoint(double x, double y, ObjectID id, double lat, double lon)
    {
	super(x, y, id);
	try
	{
	    image = ImageIO.read(this.getClass().getResource("/image/X.png"));
	    this.width = image.getWidth(this);
	    this.height = image.getHeight(this);
	    this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
	    this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	this.lat = lat;
	this.lon = lon;
	
    }
    
    public MapPoint(double x, double y, ObjectID id, double lat, double lon, int Zoom)
    {
	super(x, y, id);
	try
	{
	    image = ImageIO.read(this.getClass().getResource("/image/X.png"));
	    this.width = image.getWidth(this);
	    this.height = image.getHeight(this);
	    this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
	    this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	this.lat = lat;
	this.lon = lon;
	this.Zoom = Zoom;
    }
    
    public MapPoint(double x, double y, ObjectID id, double lat, double lon, Image image)
    {
	super(x, y, id);
	this.image = image;
	this.width = image.getWidth(this);
	this.height = image.getHeight(this);
	this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
	this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
	this.lat = lat;
	this.lon = lon;
    }  
    
    public MapPoint(double x, double y, ObjectID id, double lat, double lon, int Zoom, Image image)
    {
	super(x, y, id);
	this.image = image;
	this.width = image.getWidth(this);
	this.height = image.getHeight(this);
	this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
	this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
	this.lat = lat;
	this.lon = lon;
	this.Zoom = Zoom;
    }
    
    public MapPoint(double x, double y, ObjectID id, double lat, double lon, int Zoom, Image image, MouseListener mouseListener)
    {
	super(x, y, id);
	this.image = image;
	this.width = image.getWidth(this);
	this.height = image.getHeight(this);
	this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
	this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
	this.lat = lat;
	this.lon = lon;
	this.Zoom = Zoom;
	this.addMouseListener(mouseListener);
    }
    
    public MapPoint(double x, double y, ObjectID id, double lat, double lon, int Zoom, Image image, MouseListener mouseListener, String Name)
    {
	super(x, y, id);
	this.image = image;
	this.width = image.getWidth(this);
	this.height = image.getHeight(this);
	this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
	this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
	this.lat = lat;
	this.lon = lon;
	this.Zoom = Zoom;
	this.addMouseListener(mouseListener);
	this.Name = Name;
    }
        

    @Override
    public void tick(List<MapObject> objects)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void render(Graphics g)
    {
	g.drawImage(image, (int)x, (int)y, this);
    }

    @Override
    public Rectangle getBound()
    {
	// TODO Auto-generated method stub
	return new Rectangle((int)x,(int)y , width,height);
    }

    @Override
    public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5)
    {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void setZoom(int Zoom)
    {
        this.Zoom = Zoom;
        x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
        y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
        
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }
    
    
    
    
    
}
