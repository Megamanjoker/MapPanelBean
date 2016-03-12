package mappanel.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;

import mappanel.Listeners.MouseInput;
import mappanel.framework.Camera;
import mappanel.framework.Center;
import mappanel.framework.Envelope;
import mappanel.framework.MapObject;
import mappanel.framework.ObjectID;
import mappanel.objects.MapPoint;
import mappanel.objects.MapShape;

/**
 * 
 * @author Tyler Valant
 * @category Window
 * @since 1-19-2016
 * @version 1.0.0
 *
 */
public class MapPanel extends Canvas implements Runnable
{
    private static final long serialVersionUID = 5968719154189097854L;
    private static final int TILE_SIZE = 256;
    private int maxZoom = 15;
    private int minZoom = 0;
    public int WIDTH,HEIGHT;
    private volatile boolean running = false;
    private Thread thread;
    private int initZoom = 15;
    private double initLat = 32.966199,initLon = -96.726889;
    private ArrayList<Double> testLon = new ArrayList<Double>();
    private ArrayList<Double> testLat = new ArrayList<Double>();
    private String tileServerURL = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/";
    private int zoom = 0;
    private Color envelopeColor = Color.BLACK;
    public double mouseLon = 0;
    public double mouseLat = 0;
    
    //Object Handler
    Handler handler;
    private LinkedHashSet<MapPoint> listOfPoints = new LinkedHashSet<MapPoint>();
    private LinkedHashSet<MapShape> listOfShapes = new LinkedHashSet<MapShape>();
    private double endLat =  32.965182;
    private double endLon = -96.725505d;
    private double startLat = 32.966722d;
    private double startLon = -96.728477d;
    private boolean draw = false;
    private boolean useEnvelope = false;
    private Envelope envelope;
    private double lowerLeftCornerLat;
    private double lowerLeftCornerLon;
    private double upperLeftCornerLat;
    private double upperLeftCornerLon;
    private double upperRightCornerLon;
    private double upperRightCornerLat;
    private double lowerRightCornerLon;
    private double lowerRightCornerLat;
    
    public MapPanel()
    {
	
    }
    
    private void initialize()
    {
	handler = new Handler(initLon,initLat,initZoom,this);
	
	this.setBackground(new Color(54,69,79));
	this.setIgnoreRepaint(true);
	preinitializeHandler();
	initializeMap();
	initializeListeners();
	
    }
    
    private void preinitializeHandler()
    {
	handler.setMaxZoom(maxZoom);
	handler.setMinZoom(minZoom);
	handler.setEnvelope(startLon, startLat, endLon, endLat);
	handler.setDrawingEnvelope(draw);
	handler.setEnvelopeColor(envelopeColor);
	handler.setEnvelopeUsed(useEnvelope);
	
	
	if(tileServerURL != null)
	    handler.addURL(tileServerURL);
	else
	    handler.addURL("http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/");
	
	for(MapPoint point : listOfPoints)
	{
	    handler.addPoint(point);
	}
	for(MapShape shape : listOfShapes)
	{
	    handler.addShape(shape);
	}
	listOfPoints.clear();
	listOfShapes.clear();
	
    }

    private void initializeListeners()
    {
	MouseInput mouse = new MouseInput(handler,this);
	this.addMouseListener(mouse);
	this.addMouseMotionListener(mouse);
	this.addMouseWheelListener(mouse);
	
    }

    public void initializeMap()
    {
	// Debug Test Data for a point and shape
//	testLon.add(10d);
//	testLat.add(10d);
//	testLon.add(-10d);
//	testLat.add(10d);
//	testLon.add(-10d);
//	testLat.add(-10d);
//	testLon.add(10d);
//	testLat.add(-10d);
//	addShape(new MapShape(testLon,testLat,Color.BLACK,initZoom));
//	addPoint(new MapPoint(endLat,endLon,initZoom));
	// end of Debug
	handler.CreateMap(lon2position(initLon,initZoom),lat2position(initLat,initZoom));
	
    }
    
    public synchronized void start()
    {
	if(running)
	    return;
	
	running = true;
	thread = new Thread(this);
	thread.start();
    }
    
    public void stop()
    {
	running = false;
    }
    
    @Override
    public void run()
    {
	initialize();
	long lastTime = System.nanoTime();
	double ammountOfTicks = 60.0;
	double ns = 1000000000 / ammountOfTicks;
	double delta = 0;
	long timer = System.currentTimeMillis();
	int updates = 0;
	int frames = 0;
	while(running)
	{
	    long now = System.nanoTime();
	    delta += (now - lastTime) / ns;
	    lastTime = now;
	    while(delta >= 1)
	    {
		tick();
		updates++;
		delta--;
	    }
	    
	    render();
	    frames++;
	    
	    if(System.currentTimeMillis() - timer > 1000)
	    {
		timer += 1000;
//		System.out.println("FPS: " + frames + ", Tick: " + updates + ", Number of Objects: " + handler.objects.size());
		updates = 0;
		frames = 0;
	    }
	}
    }
    
    /**
     *  Graph Rendering
     */
    private void render()
    {
	BufferStrategy bs = this.getBufferStrategy();
	if(bs == null)
	{
	    try
	    {
		this.createBufferStrategy(2);
		return;
	    }
	    catch (Exception e)
	    {
//		System.out.println("Creating Double Buffer Strategy Failed");
//		e.printStackTrace();
//		System.out.println("Trying again...");
		return;
	    }
	}
	Graphics g = bs.getDrawGraphics();
	Graphics2D g2d = (Graphics2D) g;
	//Clear the Screen
	g.clearRect(0, 0, this.getWidth(), this.getHeight());
	
	/////////////////////////////////////////////
	//Start Of Camera
	g2d.translate(handler.getCamera().getX(), handler.getCamera().getY());
	handler.ClearScreen(g);
	//Drawing the gameObjects
	try
	{
	    handler.render(g);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	
	
	//End Of Camera
	g2d.translate(-handler.getCamera().getX(), -handler.getCamera().getY());
	/////////////////////////////////////////////
	g.dispose();
	bs.show();

//	System.gc();
    }

    
    /**
     * 
     */
    private void tick()
    {
	try
	{
	    handler.tick();
	    handler.getCenter().setWidth(this.getWidth());
	    handler.getCenter().setHeight(this.getHeight());
	}
	catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

    public static void main(String args[])
    {
	new Window(1280,800,"Map Panel Prototype", new MapPanel());
    }

    public static double position2lon(int x, int z)
    {
	double xmax = TILE_SIZE * Math.pow(2, z);
	return x / xmax * 360.0 - 180;
    }
    
    public static double position2lat(int y, int z)
    {
	double ymax = TILE_SIZE * Math.pow(2, z);
	return Math.toDegrees(Math.atan(Math.sinh(Math.PI - (2.0 * Math.PI * y) / ymax)));
    }

    public static double tile2lon(int x, int z)
    {
	return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    public static double tile2lat(int y, int z)
    {
	return Math.toDegrees(Math.atan(Math.sinh(Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z))));
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
    
    public void addPoint(MapPoint point)
    {
	if (handler != null)
	{
	    handler.addPoint(point);
	}
	else
	{
	    listOfPoints.add(point);
	}
    }
    
    public void clearPoints()
    {
	if (handler != null)
	{
	    handler.clearPoints();
	}
    }
    
    public void addShape(MapShape shape)
    {
	if (handler != null)
	{
	    handler.addShape(shape);
	}
	else
	{
	    listOfShapes.add(shape);
	}
    }
    
    public void clearShapes()
    {
	if (handler != null)
	{
	    handler.clearShapes();
	}
    }
    
    

    public LinkedHashSet<MapObject> collisionCheckAt(int x, int y)
    {
	// TODO Auto-generated method stub
	return handler.collisionCheckAt(x,y);
    }
    
    
    //start of setters/getters
    public void setMouseListener()
    {
	
    }

    public void setCenterLocation(double lon, double lat)
    {
	handler.setCenterLocation(lon,lat);
    }
    
    public boolean isEnvelopeUsed()
    {
	if(handler.getCenter() != null)
	    return handler.getCenter().isEnvelopeUsed();
	else
	    return useEnvelope;
    }

    public void setEnvelopeUsed(boolean useEnvelope)
    {
	if(handler.getCenter() != null)
	{    
	    this.handler.getCenter().setEnvelopeUsed(useEnvelope);
	    this.useEnvelope = useEnvelope;
	}
	else
	    this.useEnvelope = useEnvelope;
    }
    
    public boolean isDrawingEnvolpe()
    {
	if(handler.getCenter() != null)
	    return handler.getCenter().getEnvelope().isDraw();
	else
	    return draw;
    }

    public void setDrawingEnvolpe(boolean draw)
    {
	if(handler.getCenter() != null)
	{
	    this.draw = draw;
	    this.handler.getCenter().getEnvelope().setDraw(draw);
	}
	else
	    this.draw = draw;
    }

    public Color getEnvelopeColor()
    {
	if(handler.getCenter() != null)
	    return this.handler.getCenter().getEnvelope().getColor();
	else
	    return envelopeColor;
    }

    public void setEnvelopeColor(Color color)
    {
	if(handler.getCenter() != null)
	{
	    this.handler.getCenter().getEnvelope().setColor(color);
	    this.envelopeColor = color;
	}
	else
	    this.envelopeColor = color;
    }
    
    public void setEnvelope(Double startLon, Double startLat, Double endLon, Double endLat)
    {
	if(handler.getCenter() != null)
	{
	    this.handler.getCenter().setEnvelope(startLon, startLat, endLon, endLat);
	}
	else
	{
	    this.startLon = startLon;
	    this.startLat = startLat;
	    this.endLon = endLon;
	    this.endLat = endLat;
	}
	this.envelope = new Envelope(startLon, startLat, endLon, endLat, this.zoom);
    }
    
    public Envelope getEnvelope()
    {
	if(handler.getCenter() != null)
	    return this.handler.getCenter().getEnvelope();
	else
	    return this.envelope;
    }
    
    public Handler getHandler()
    {
        return handler;
    }

    public Center getCenter()
    {
        return handler.getCenter();
    }
    
    public int getInitZoom()
    {
        return initZoom;
    }

    public void setInitZoom(int initZoom)
    {
        this.initZoom = initZoom;
    }

//    public void setTileServer(String tileURL)
//    {
//	if (handler != null)
//	{
//	    this.firePropertyChange("tileServer", this.tileServerURL, tileServerURL);
//	    handler.setTileServerURL(tileURL);
//	    this.tileServerURL = tileURL;
//	}
//	else
//	{
//	    this.tileServerURL = tileURL;
//	}
//    }
//    
//    public String getTileServer()
//    {
//	if (handler != null)
//	    return handler.getTileServerURL();
//	else
//	    return tileServerURL;
//    }
    
    public void addURL(String url)
    {
	if (handler != null)
	    handler.addURL(url);
    }
    
    public void clearURLs()
    {
	if (handler != null)
	    handler.clearURLs();
    }
    

    public void setMinZoom(int minZoom)
    {
	if (handler != null)
	{
	    this.firePropertyChange("minZoom", this.minZoom, minZoom);
	    handler.setMinZoom(minZoom);
	    this.minZoom = minZoom;
	}
	else
	{
	    this.minZoom = minZoom;
	}
    }
    
    public int getMinZoom()
    {
	if(handler != null)
	    return handler.getMinZoom();
	else
	    return this.minZoom;
    }
    
    public void setMaxZoom(int maxZoom)
    {
	if (handler != null)
	{
	    this.firePropertyChange("maxZoom", this.maxZoom, maxZoom);
	    handler.setMaxZoom(maxZoom);
	    this.maxZoom = maxZoom;
	}
	else
	{
	    this.maxZoom = maxZoom;
	}
    }
    
    public int getMaxZoom()
    {
	if(handler == null)
	{
	    return maxZoom;
	}
	else
	{
	    return handler.getMaxZoom();
	}
    }
    
    public int getZoom()
    {
	if(handler == null)
	{
	    return initZoom;
	}
	else
	{
	    return handler.getZoom();
	}
    }
    
    public void setZoom(int zoom)
    {
	if (handler != null)
	{
	    this.firePropertyChange("zoom", this.zoom, zoom);
	    handler.setZoom(zoom);
	    this.zoom = zoom;
	}
	else
	{
	    this.zoom = zoom;
	}
    }

    public double getMouseLon()
    {
        return mouseLon;
    }

    public void setMouseLon(double mouseLon)
    {
	this.firePropertyChange("mouseLon", this.mouseLon, mouseLon);
        this.mouseLon = mouseLon;
    }

    public double getMouseLat()
    {
        return mouseLat;
    }

    public void setMouseLat(double mouseLat)
    {
	this.firePropertyChange("mouseLat", this.mouseLat, mouseLat);
        this.mouseLat = mouseLat;
    }

    public double getUpperRightCornerLon()
    {
	upperRightCornerLon = MapPanel.position2lon(this.getCenter().getX() + this.getWidth()/2, this.getZoom());
        return upperRightCornerLon;
    }

    public double getUpperRightCornerLat()
    {
	upperRightCornerLat = MapPanel.position2lat(this.getCenter().getY() - this.getHeight()/2, this.getZoom());
        return upperRightCornerLat;
    }

    public double getLowerRightCornerLon()
    {
	lowerRightCornerLon = MapPanel.position2lon(this.getCenter().getX() + this.getWidth()/2, this.getZoom());
        return lowerRightCornerLon;
    }

    public double getLowerRightCornerLat()
    {
	lowerRightCornerLat = MapPanel.position2lat(this.getCenter().getY() + this.getHeight()/2, this.getZoom());
        return lowerRightCornerLat;
    }

    public double getUpperLeftCornerLon()
    {
	upperLeftCornerLon = MapPanel.position2lon(this.getCenter().getX() - this.getWidth()/2, this.getZoom());
        return upperLeftCornerLon;
    }

    public double getUpperLeftCornerLat()
    {
	upperLeftCornerLat = MapPanel.position2lat(this.getCenter().getY() - this.getHeight()/2, this.getZoom());
        return upperLeftCornerLat;
    }

    public double getLowerLeftCornerLon()
    {
	lowerLeftCornerLon = MapPanel.position2lon(this.getCenter().getX() - this.getWidth()/2, this.getZoom());
        return lowerLeftCornerLon;
    }

    public double getLowerLeftCornerLat()
    {
	lowerLeftCornerLat = MapPanel.position2lat(this.getCenter().getY() + this.getHeight()/2, this.getZoom());
        return lowerLeftCornerLat;
    }
    
    
}
