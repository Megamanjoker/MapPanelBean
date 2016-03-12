package mappanel.objects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;

import javax.imageio.ImageIO;

import mappanel.framework.MapObject;
import mappanel.framework.ObjectID;
import mappanel.window.MapPanel;

/**
 * 
 * @author Tyler Valant
 * @category Object
 * @since 1-19-2016
 * @version 1.0.0
 *
 */
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
    private boolean enter = false;
    
    public MapPoint(double lon, double lat)
    {
	this(lon,  lat,  0,  null,  null,  null);
	
    }
    
    public MapPoint(double lon, double lat, int Zoom)
    {
	this(lon,  lat,  0,  null,  null,  null);
    }
    
    public MapPoint(double lon, double lat, Image image)
    {
	this(lon,  lat,  0,  image,  null,  null);
    }  
    
    public MapPoint(double lon, double lat, int Zoom, Image image)
    {
	this(lon,  lat,  Zoom,  image,  null,  null);
    }
    
    public MapPoint(double lon, double lat, int Zoom, Image image, String Name)
    {
	this(lon,  lat,  Zoom,  image,  null,  Name);
    }
    
    public MapPoint(double lon, double lat, int Zoom, Image image, MouseListener mouseListener)
    {
	this(lon,  lat,  Zoom,  image,  mouseListener,  null);
    }
    
    public MapPoint(double lon, double lat, int Zoom, Image image, MouseListener mouseListener, String Name)
    {
	super(0, 0, ObjectID.Point);
	if(image == null)
	{
	    try
	    {
		this.image = ImageIO.read(this.getClass().getResource("/image/X.png"));
		this.width = this.image.getWidth(this);
		this.height = this.image.getHeight(this);
		this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
		this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
	    }
	    catch (IOException e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	else
	{
	    this.image = image;
	    this.width = this.image.getWidth(this);
	    this.height = this.image.getHeight(this);
	    this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
	    this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
	}
	
	this.lat = lat;
	this.lon = lon;
	this.Zoom = Zoom;
	if(mouseListener != null)
	{
		this.addMouseListener(mouseListener);
	}
	this.Name = Name;
    }
    
    
        

    
    public void render(Graphics g)
    {
	g.drawImage(image, (int)x, (int)y, this);
    }

    
    public Rectangle2D getBound()
    {
	return new Rectangle2D.Double(x,y, width,height);
    }

    
    public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5)
    {
	return false;
    }

    
    public void setZoom(int Zoom)
    {
        this.Zoom = Zoom;
        this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
        this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
        
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    
    public void tick(LinkedHashSet<MapObject> objects)
    {
	this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
        this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
    }

    public boolean isEnter()
    {
        return enter;
    }

    public void setEnter(boolean enter)
    {
        this.enter = enter;
    }
    
    
    
    
    
    
}
