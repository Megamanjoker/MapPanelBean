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
import mappanel.framework.MapObject;
import mappanel.framework.ObjectID;
import mappanel.objects.MapEnvelope;
import mappanel.objects.MapCenter;
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
    private MapCenter center;
    private ArrayList<Double> testLon = new ArrayList<Double>();
    private ArrayList<Double> testLat = new ArrayList<Double>();
    private String tileServerURL = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/";
    private int zoom = 0;
    private Color envelopeColor = Color.BLACK;
    public double mouseLon = 0;
    public double mouseLat = 0;
    
    //Object Handler
    Handler handler;
    Camera camera;
    private LinkedHashSet<MapPoint> listOfPoints = new LinkedHashSet<MapPoint>();
    private LinkedHashSet<MapShape> listOfShapes = new LinkedHashSet<MapShape>();
    private double endLat =  32.965182;
    private double endLon = -96.725505d;
    private double startLat = 32.966722d;
    private double startLon = -96.728477d;
    private boolean draw;
    private boolean useEnvelope;
    private MapEnvelope envelope;
    
    public MapPanel()
    {
	
    }
    
    private void initialize()
    {
	handler = new Handler();
	center = new MapCenter(lon2position(initLon,initZoom),lat2position(initLat,initZoom), ObjectID.Center);
	handler.setCenter(center);
	camera = new Camera(lon2position(initLon,initZoom),lat2position(initLat,initZoom),this);
	this.setBackground(new Color(54,69,79));
	this.setIgnoreRepaint(true);
	initializeValues();
	initializeObjects();
	initializeListeners();
	
    }
    
    private void initializeValues()
    {
	handler.setMaxZoom(maxZoom);
	handler.setMinZoom(minZoom);
	handler.setZoom(initZoom);
	
	center.setEnvelope(startLon, startLat, endLon, endLat);
	center.setDrawingEnvelope(draw);
	center.setEnvelopeColor(envelopeColor);
	
	if(tileServerURL != null)
	    handler.setTileServerURL(tileServerURL);
	else
	    handler.setTileServerURL("http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/");
	
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
	MouseInput mouse = new MouseInput(handler,center,this);
	mouse.setCenter(center);
	this.addMouseListener(mouse);
	this.addMouseMotionListener(mouse);
	this.addMouseWheelListener(mouse);
	
    }

    public void initializeObjects()
    {
	
	testLon.add(10d);
	testLat.add(10d);
	testLon.add(-10d);
	testLat.add(10d);
	testLon.add(-10d);
	testLat.add(-10d);
	testLon.add(10d);
	testLat.add(-10d);
	addShape(new MapShape(testLon,testLat,Color.BLACK,initZoom)); //,Color.BLACK));
	addPoint(new MapPoint(endLat,endLon,initZoom));
	handler.CreateMap(lon2position(initLon,initZoom),lat2position(initLat,initZoom));
	center.setEnvelopeUsed(false);
	handler.addMapObject(center);
	
    }
    
    public synchronized void start()
    {
	if(running)
	    return;
	
//	System.out.println("Start");
	running = true;
	thread = new Thread(this);
	thread.start();
    }
    
    public void stop()
    {
//	this.getBufferStrategy().dispose();
//	handler.clearMapTile();
//	System.out.println("Stop");
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
//	    tick();
//	    updates++;
	    
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
//	System.out.println("Rendering");
	if(bs == null)
	{
	    try
	    {
		this.createBufferStrategy(2);
//		System.out.println("Creating BufferStrategy");
		return;
	    }
	    catch (Exception e)
	    {
		// TODO Auto-generated catch block
//		e.printStackTrace();
		return;
	    }
	}
	Graphics g = bs.getDrawGraphics();
	Graphics2D g2d = (Graphics2D) g;
	//Clear the Screen
	g.clearRect(0, 0, this.getWidth(), this.getHeight());
	
	/////////////////////////////////////////////
	//Start Of Camera
	g2d.translate(camera.getX(), camera.getY());
	handler.ClearScreen(g);
	//Drawing the gameObjects
	try
	{
	    handler.render(g);
//	    handler.renderShapes(g);
//	    handler.renderPoints(g);
//	    handler.renderCenter(g);
	}
	catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	
	//End Of Camera
	g2d.translate(-camera.getX(), -camera.getY());
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
//	if(count > Math.pow(2, 2))
//	{
//	    System.out.println("Number of Object = " + handler.objects.size());
//	    count = 0;
//	}
//	else
//	{
//	    count++;
//	}
	
	try
	{
	    handler.tick();
//	    
//	    try
//	    {
//		for(MapObject object: handler.objects)
//		{
//		    if(object.getId() == ObjectID.Center)
//		    {
			camera.tick(center);
			center.setWidth(this.getWidth());
			center.setHeight(this.getHeight());

//			System.gc();
//			MapCenter temp = (MapCenter) object;
//			temp.setWidth(this.getWidth());
//			temp.setHeight(this.getHeight());
//			temp = null;
//		    }
//		}
//	    }
//	    catch (ConcurrentModificationException e)
//	    {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
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

    public int getInitZoom()
    {
        return initZoom;
    }

    public void setInitZoom(int initZoom)
    {
        this.initZoom = initZoom;
    }

    public void setTileServer(String tileURL)
    {
	if (handler != null)
	{
	    handler.setTileServerURL(tileURL);
	    this.tileServerURL = tileURL;
	}
	else
	{
	    this.tileServerURL = tileURL;
	}
    }
    
    public String getTileServer()
    {
	if (handler != null)
	    return handler.getTileServerURL();
	else
	    return tileServerURL;
    }

    public void setMinZoom(int minZoom)
    {
	if (handler != null)
	{
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
	    handler.setZoom(zoom);
	    this.zoom = zoom;
	}
	else
	{
	    this.zoom = zoom;
	}
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
    
    public boolean isEnvelopeUsed()
    {
	if(center != null)
	    return center.isEnvelopeUsed();
	else
	    return useEnvelope;
    }

    public void setEnvelopeUsed(boolean useEnvelope)
    {
	if(center != null)
	{    
	    this.center.setEnvelopeUsed(useEnvelope);
	    this.useEnvelope = useEnvelope;
	}
	else
	    this.useEnvelope = useEnvelope;
    }
    
    public boolean isDrawingEnvolpe()
    {
	if(center != null)
	    return center.getEnvelope().isDraw();
	else
	    return draw;
    }

    public void setDrawingEnvolpe(boolean draw)
    {
	if(center != null)
	{
	    this.draw = draw;
	    this.center.getEnvelope().setDraw(draw);
	}
	else
	    this.draw = draw;
    }

    public Color getEnvelopeColor()
    {
	if(center != null)
	    return this.center.getEnvelope().getColor();
	else
	    return envelopeColor;
    }

    public void setEnvelopeColor(Color color)
    {
	if(center != null)
	{
	    this.center.getEnvelope().setColor(color);
	    this.envelopeColor = color;
	}
	else
	    this.envelopeColor = color;
    }
    
    public void setEnvelope(Double startLon, Double startLat, Double endLon, Double endLat)
    {
	if(center != null)
	{
	    this.center.setEnvelope(startLon, startLat, endLon, endLat);
	}
	else
	{
	    this.startLon = startLon;
	    this.startLat = startLat;
	    this.endLon = endLon;
	    this.endLat = endLat;
	}
	this.envelope = new MapEnvelope(ObjectID.Envelope, startLon, startLat, endLon, endLat, this.zoom);
    }
    
    public MapEnvelope getEnvelope()
    {
	if(center != null)
	    return this.center.getEnvelope();
	else
	    return this.envelope;
    }

    
    
    public Handler getHandler()
    {
        return handler;
    }
    
    

    public MapCenter getCenter()
    {
        return center;
    }

    public LinkedHashSet<MapObject> collisionCheckAt(int x, int y)
    {
	// TODO Auto-generated method stub
	return handler.collisionCheckAt(x,y);
    }

//    @Override
//    public void paint(Graphics g)
//    {
//        // TODO Auto-generated method stub
//        super.paint(this.getBufferStrategy().getDrawGraphics());
//    }
}
