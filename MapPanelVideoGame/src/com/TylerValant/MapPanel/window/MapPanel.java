package com.TylerValant.MapPanel.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.TylerValant.MapPanel.Listeners.MouseInput;
import com.TylerValant.MapPanel.framework.MapObject;
import com.TylerValant.MapPanel.framework.ObjectID;
import com.TylerValant.MapPanel.objects.Camera;
import com.TylerValant.MapPanel.objects.MapCenter;
import com.TylerValant.MapPanel.objects.MapPoint;
import com.TylerValant.MapPanel.objects.MapShape;

public class MapPanel extends Canvas implements Runnable
{
    private static final long serialVersionUID = 5968719154189097854L;
    private static final int TILE_SIZE = 256;
    public int WIDTH,HEIGHT;
    private volatile boolean running = false;
    private Thread thread;
    private int initZoom = 15;
    private double initLat = 32.966199,initLon = -96.726889;
    private MapCenter center;
    private ArrayList<Double> testLon = new ArrayList<Double>();
    private ArrayList<Double> testLat = new ArrayList<Double>();
    
    //Object Handler
    Handler handler;
    Camera camera;
    
    public MapPanel()
    {
	this.start();
    }
    
    private void initialize()
    {
	WIDTH = getWidth();
	HEIGHT = getHeight();
	handler = new Handler();
	camera = new Camera(0,0,this);
	this.setBackground(new Color(54,69,79));
	initializeObjects();
	initializeListeners();
    }
    
    private void initializeListeners()
    {
	MouseInput mouse = new MouseInput(handler);
	mouse.setCenter(center);
	this.addMouseListener(mouse);
	this.addMouseMotionListener(mouse);
	this.addMouseWheelListener(mouse);
	
    }

    public void initializeObjects()
    {
	center = new MapCenter(lon2position(initLon,initZoom),lat2position(initLat,initZoom), ObjectID.Center);
	handler.addMapObject(center);
	handler.addMapObject(new MapPoint(0,0,ObjectID.Point,32.966199,-96.726889,0));
//	testLon.add(10d);
//	testLat.add(10d);
//	testLon.add(-10d);
//	testLat.add(10d);
//	testLon.add(-10d);
//	testLat.add(-10d);
//	testLon.add(10d);
//	testLat.add(-10d);
//	handler.addMapObject(new MapShape(0,0,ObjectID.Shape,testLon,testLat));
	handler.CreateMap(lon2position(initLon,initZoom),lat2position(initLat,initZoom));
	handler.setZoom(initZoom);
    }
    
    public synchronized void start()
    {
	if(running)
	    return;
	
	running = true;
	thread = new Thread(this);
	thread.start();
    }
    
    @Override
    public void run()
    {
	initialize();
	long lastTime = System.nanoTime();
	double ammountOfTicks = 30.0;
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
		System.out.println("FPS: " + frames + ", Tick: " + updates + ", Number of Objects: " + handler.objects.size());
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
	    this.createBufferStrategy(3);
	    return;
	}
	Graphics g = bs.getDrawGraphics();
	Graphics2D g2d = (Graphics2D) g;
	//Clear the Screen
	g.clearRect(0, 0, this.getWidth(), this.getHeight());
	handler.ClearScreen(g);
	/////////////////////////////////////////////
	//Start Of Camera
	g2d.translate(camera.getX(), camera.getY());
	
	//Drawing the gameObjects
	try
	{
	    handler.render(g);
	    handler.renderShapes(g);
	    handler.renderPoints(g);
	    handler.renderCenter(g);
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

    
//    public int count = 0;
    
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



}
