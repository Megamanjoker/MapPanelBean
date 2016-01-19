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
    
    public MapPoint(double x, double y, ObjectID id)
    {
	super( x,  y,  id);
    }
    
    public MapPoint(ObjectID id, double lat, double lon)
    {
	this(id,  lat,  lon,  0,  null,  null,  null);
	
    }
    
    public MapPoint(ObjectID id, double lat, double lon, int Zoom)
    {
	this( id,  lat,  lon,  Zoom,  null,  null,  null);
    }
    
    public MapPoint(ObjectID id, double lat, double lon, Image image)
    {
	this(id,  lat,  lon,  0,  image,  null,  null);
    }  
    
    public MapPoint(ObjectID id, double lat, double lon, int Zoom, Image image)
    {
	this( id,  lat,  lon,  Zoom,  image,  null,  null);
    }
    
    public MapPoint(ObjectID id, double lat, double lon, int Zoom, Image image, String Name)
    {
	this( id,  lat,  lon,  Zoom,  image,  null,  Name);
    }
    
    public MapPoint( ObjectID id, double lat, double lon, int Zoom, Image image, MouseListener mouseListener)
    {
	this( id,  lat,  lon,  Zoom,  image,  mouseListener,  null);
    }
    
    public MapPoint(ObjectID id, double lat, double lon, int Zoom, Image image, MouseListener mouseListener, String Name)
    {
	super(0, 0, id);
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
    
    
        

    @Override
    public void render(Graphics g)
    {
	g.drawImage(image, (int)x, (int)y, this);
    }

    @Override
    public Rectangle2D getBound()
    {
	// TODO Auto-generated method stub
//	System.out.println(new Rectangle((int)x,(int)y, width,height));
	return new Rectangle2D.Double(x,y, width,height);
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

    @Override
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
