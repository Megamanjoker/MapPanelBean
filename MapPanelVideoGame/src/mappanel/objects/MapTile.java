package mappanel.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;

import mappanel.framework.MapObject;
import mappanel.framework.ObjectID;

/**
 * 
 * @author Tyler Valant
 * @category Object
 * @since 1-19-2016
 * @version 1.0.0
 *
 */
public class MapTile extends MapObject implements ImageObserver, Runnable
{
    private static final long serialVersionUID = -7211837370532773221L;

    private boolean DEBUG = false;
    
    private int TileSize = 256;
    private int TestSize = 16;
    private int Zoom = 0;
    private Image image = null;
    private String TileServerURL = "";
    private Thread loadImageThread = new Thread(this);
    private volatile boolean running = false;
    private boolean dirty = true;
    private static final int MAX_AVAILABLE = 200;
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);
    private MapCenter center = null;
    private MapTile North,East,South,West;
    private Rectangle2D NorthBound,EastBound,SouthBound,WestBound;
    private boolean last = false;
    private boolean generateEnable = true;
    
    public MapTile(double x, double y, ObjectID id, String TileServerURL)
    {
	super(x, y, id);
	this.TileServerURL = TileServerURL;
	NorthBound = new Rectangle2D.Double(x + TileSize/2 ,y - TestSize,  TestSize - 1,TestSize - 1);
	EastBound  = new Rectangle2D.Double(x + TileSize,   y + TileSize/2,TestSize - 1,TestSize - 1);
	SouthBound = new Rectangle2D.Double(x + TileSize/2, y + TileSize,  TestSize - 1,TestSize - 1);
	WestBound  = new Rectangle2D.Double(x  - TestSize,  y + TileSize/2,TestSize - 1,TestSize - 1);
	last = true;
    }
    
    public MapTile(double x, double y, ObjectID id, String TileServerURL, int Zoom)
    {
	super(x, y, id);
	this.TileServerURL = TileServerURL;
	this.Zoom = Zoom;
	NorthBound = new Rectangle2D.Double(x + TileSize/2 ,y - TestSize,  TestSize - 1,TestSize - 1);
	EastBound  = new Rectangle2D.Double(x + TileSize,   y + TileSize/2,TestSize - 1,TestSize - 1);
	SouthBound = new Rectangle2D.Double(x + TileSize/2, y + TileSize,  TestSize - 1,TestSize - 1);
	WestBound  = new Rectangle2D.Double(x  - TestSize,  y + TileSize/2,TestSize - 1,TestSize - 1);
	
    }

    @Override
    public void tick(LinkedHashSet<MapObject> objects)
    {
	for(MapObject object : objects)
	{
	    if(object.getId() == ObjectID.Center && center == null)
	    {
		center = (MapCenter) object;
	    }
	    else if(object.getId() == ObjectID.Tile)
	    {
		getNeighbors(object);
	    }
	}
	
	checkBounds(objects);
    }

    private void checkBounds(LinkedHashSet<MapObject> objects)
    {
	boolean NorthEnabled = (0 <= y - TileSize);
	boolean EastEnabled  = (x < (Math.pow(2, Zoom) - 1) * TileSize );
	boolean SouthEnabled = (y < (Math.pow(2, Zoom) - 1) * TileSize );
	boolean WestEnabled  = (0 <= x - TileSize);
	
	if(North == null  && NorthEnabled && generateEnable && NorthBound.intersects(center.getPriority()))
	{
	    North = new MapTile(x,y - TileSize,ObjectID.Tile, this.TileServerURL, this.Zoom);
	    objects.add(North);
	    this.last = false;
	}
	else if(East == null  && EastEnabled && generateEnable && EastBound.intersects(center.getPriority()))
	{
	    East = new MapTile(x + TileSize, y,ObjectID.Tile, this.TileServerURL, this.Zoom);
	    objects.add(East);
	    this.last = false;
	}
	else if(South == null  && SouthEnabled && generateEnable && SouthBound.intersects(center.getPriority()))
	{
	    South = new MapTile(x, y + TileSize,ObjectID.Tile, this.TileServerURL, this.Zoom);
	    objects.add(South);
	    this.last = false;
	}
	else if(West == null  && WestEnabled && generateEnable && WestBound.intersects(center.getPriority()))
	{
	    West = new MapTile(x - TileSize,y,ObjectID.Tile, this.TileServerURL, this.Zoom);
	    objects.add(West);
	    this.last = false;
	}
	else if(!NorthBound.intersects(center.getPriority()))
	{
	    objects.remove(North);
	    North = null;
	}
	else if (!EastBound.intersects(center.getPriority()))
	{
	    objects.remove(East);
	    East = null;
	}
	else if (!SouthBound.intersects(center.getPriority()))
	{
	    objects.remove(South);
	    South = null;
	}
	else if (!WestBound.intersects(center.getPriority()))
	{
	    objects.remove(West);
	    West = null;
	}
    }

    private void getNeighbors(MapObject object)
    {
	if(object.getBound().intersects(NorthBound))
	{
	    North = (MapTile) object;
	}
	else if(object.getBound().intersects(EastBound))
	{
	    East = (MapTile) object;
	}
	else if(object.getBound().intersects(SouthBound))
	{
	    South = (MapTile) object;
	}
	else if(object.getBound().intersects(WestBound))
	{
	    West = (MapTile) object;
	}
    }

    
    @Override
    public void render(Graphics g)
    {
	if(center != null)
	{
	    Graphics2D g2d = (Graphics2D) g;
	    if(DEBUG)
	    {
		
		g2d.setColor(Color.CYAN);
		g2d.fillRect((int)x, (int)y, (int)TileSize, (int)TileSize);
		g2d.setColor(Color.black);
		g2d.drawRect((int)x, (int)y, (int)TileSize, (int)TileSize);
		g2d.drawString("X = " + (int)x + " Y = " + (int)y, (int)x + 10, (int)y + 15);
		g2d.setColor(new Color(33,237,94));
		g2d.draw(NorthBound);
		g2d.draw(EastBound);
		g2d.draw(SouthBound);
		g2d.draw(WestBound);    
	    }	
	    else
	    {
		if (dirty)
		{
		    this.start();
		}
		else if(!running)
		{
		    g2d.drawImage(image, (int) (x), (int) (y), (int) TileSize, (int) TileSize, this);
		}
	    }    
	}
    }

    //bounds for the tile
    @Override
    public Rectangle2D getBound()
    {
	return new Rectangle2D.Double(x,y,TileSize,TileSize);
    }

    public int getZoom()
    {
        return Zoom;
    }

    public void setZoom(int zoom)
    {
        Zoom = zoom;
        
    }

    public String getTileServerURL()
    {
        return TileServerURL;
    }

    public void setTileServerURL(String tileServerURL)
    {
        TileServerURL = tileServerURL;
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
    {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void run()
    {
	try
	{
	    available.acquire();
	    URL url;
	    try
	    {
		url = new URL( TileServerURL + Zoom + "/"+ (int)(y/256) + "/"+ (int)(x/256));
		image = ImageIO.read(url);
	    }
	    catch (MalformedURLException e)
	    {
		e.printStackTrace();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	}
	catch (InterruptedException e1)
	{
	    e1.printStackTrace();
	}
	
	running = false;
	dirty = false;
	available.release();
	
    }
    
    
    public void start()
    {
	if(running)
	    return;
	
	running = true;
	loadImageThread = new Thread(this);
	loadImageThread.start();
    }

    public void setDirty(boolean b)
    {
	dirty = b;
	
    }
    
    public boolean isDirty()
    {
	return dirty;
    }

    
    public boolean isLast()
    {
        return last;
    }

    public void setLast(boolean last)
    {
        this.last = last;
    }

    
    public boolean isGenerateEnable()
    {
        return generateEnable;
    }

    public void setGenerateEnable(boolean generateEnable)
    {
        this.generateEnable = generateEnable;
    }

    public Thread getLoadImageThread()
    {
        return loadImageThread;
    }
    
   

}