package mappanel.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import javax.swing.JPanel;

import mappanel.Listeners.MouseInput;
import mappanel.framework.Center;
import mappanel.framework.Envelope;
import mappanel.framework.MapObject;
import mappanel.objects.MapPoint;
import mappanel.objects.MapShape;

/**
 * 
 * @author Tyler Valant
 * @since 1-19-2016
 * @version 1.0.6.7-SNAPSHOT
 *
 */
public class MapPanel extends Canvas implements Runnable
{
    private static final long serialVersionUID = 5968719154189097854L;
    private static final int TILE_SIZE = 256;
    private int maxZoom = 15;
    private int minZoom = 0;
    private volatile boolean running = false;
	private int initZoom = 15;
    private double initLat = 32.966199,initLon = -96.726889;
	private int zoom = 0;
    private Color envelopeColor = Color.BLACK;
    public double mouseLon = 0;
    public double mouseLat = 0;
    
    //Object Handler
    Handler handler;
    DebugConsole debugConsole;
    private LinkedHashSet<MapPoint> listOfPoints = new LinkedHashSet<MapPoint>();
    private LinkedHashSet<MapShape> listOfShapes = new LinkedHashSet<MapShape>();
    private double endLat =  32.965182;
    private double endLon = -96.725505d;
    private double startLat = 32.966722d;
    private double startLon = -96.728477d;
    private boolean draw = false;
    private boolean useEnvelope = false;
    private Envelope envelope;

	public MapPanel()
    {
	    debugConsole = new DebugConsole(this);
        this.debugConsole.setVisible(false);
    }

    /**
     * Initialize the Handler and background color
     */
    private void initialize()
    {
        handler = new Handler(initLon,initLat,initZoom,this);
        this.setBackground(new Color(54,69,79));
        this.setIgnoreRepaint(true);
        preInitializeHandler();
        initializeMap();
        initializeListeners();
    }

    /**
     * We need to give the handler default data to start with
     */
    private void preInitializeHandler()
    {
        handler.setMaxZoom(maxZoom);
        handler.setMinZoom(minZoom);
        handler.setEnvelope(startLon, startLat, endLon, endLat);
        handler.setDrawingEnvelope(draw);
        handler.setEnvelopeColor(envelopeColor);
        handler.setEnvelopeUsed(useEnvelope);
    
    
        String tileServerURL = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/";
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
        MouseInput mouse = new MouseInput(this);
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        this.addMouseWheelListener(mouse);
    }

    public void initializeMap()
    {
	    handler.CreateMap(lon2position(initLon,initZoom),lat2position(initLat,initZoom));
    }

    /**
     * start the map thread, return if all ready running
     */
    public synchronized void start()
    {
        if(running)
            return;

        running = true;
        Thread thread = new Thread(this);
        thread.start();
    }
    
    public void stop()
    {
	    running = false;
    }

    /**
     * This will run in the thread
     */
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
     *  Graphics Rendering
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
    }

    /**
     * tells the handler to tick
     */
    private void tick()
    {
        try
        {
            handler.tick();
            //Reset the center
            handler.getCenter().setWidth(this.getWidth());
            handler.getCenter().setHeight(this.getHeight());
            debugConsole.tick();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        new Window(new MapPanel());
    }

    /**
     * This is a conversion of screen X-coordinates and Zoom level to Longitude.
     * @See <a href="http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Lon..2Flat._to_tile_numbers">Source</>
     * @param x
     * @param z
     * @return returns the Longitude of the given X-coordinated  and Zoom
     */
    public static double position2lon(int x, int z)
    {
        double xmax = TILE_SIZE * Math.pow(2, z);
        return x / xmax * 360.0 - 180;
    }

    /**
     * This is a conversion of screen Y-coordinates and Zoom level to Latitude.
     * @See <a href="http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Lon..2Flat._to_tile_numbers">Source</>
     * @param y
     * @param z
     * @return returns the Latitude of the given screen Y-coordinates and Zoom
     */
    public static double position2lat(int y, int z)
    {
        double ymax = TILE_SIZE * Math.pow(2, z);
        return Math.toDegrees(Math.atan(Math.sinh(Math.PI - (2.0 * Math.PI * y) / ymax)));
    }

    /**
     * This is a conversion of Longitude and Zoom level to screen X-coordinates.
     * @See <a href="http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Lon..2Flat._to_tile_numbers">Source</>
     * @param lon
     * @param z
     * @return returns the X-coordinated of the given Longitude and Zoom
     */
    public static int lon2position(double lon, int z)
    {
        double xmax = TILE_SIZE * Math.pow(2, z);
        return (int) Math.floor((lon + 180) / 360 * xmax);
    }

    /**
     * This is a conversion of Latitude and Zoom level to screen Y-coordinates.
     * @See <a href="http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Lon..2Flat._to_tile_numbers">Source</>
     * @param lat
     * @param z
     * @return returns the Y-coordinated of the given Latitude and Zoom
     */
    public static int lat2position(double lat, int z)
    {
        double ymax = TILE_SIZE * Math.pow(2, z);
        return (int) Math.floor(
            (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * ymax);
    }

    //start of setters/getters
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
        return handler.collisionCheckAt(x,y);
    }

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
    
    public boolean isDrawingEnvelope()
    {
        if(handler.getCenter() != null)
            return handler.getCenter().getEnvelope().isDraw();
        else
            return draw;
    }

    public void setDrawingEnvelope(boolean draw)
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
		double upperRightCornerLon = MapPanel.position2lon(this.getCenter().getX() + this.getWidth() / 2, this.getZoom());
        return upperRightCornerLon;
    }

    public double getUpperRightCornerLat()
    {
		double upperRightCornerLat = MapPanel.position2lat(this.getCenter().getY() - this.getHeight() / 2, this.getZoom());
        return upperRightCornerLat;
    }

    public double getLowerRightCornerLon()
    {
		double lowerRightCornerLon = MapPanel.position2lon(this.getCenter().getX() + this.getWidth() / 2, this.getZoom());
        return lowerRightCornerLon;
    }

    public double getLowerRightCornerLat()
    {
		double lowerRightCornerLat = MapPanel.position2lat(this.getCenter().getY() + this.getHeight() / 2, this.getZoom());
        return lowerRightCornerLat;
    }

    public double getUpperLeftCornerLon()
    {
		double upperLeftCornerLon = MapPanel.position2lon(this.getCenter().getX() - this.getWidth() / 2, this.getZoom());
        return upperLeftCornerLon;
    }

    public double getUpperLeftCornerLat()
    {
		double upperLeftCornerLat = MapPanel.position2lat(this.getCenter().getY() - this.getHeight() / 2, this.getZoom());
        return upperLeftCornerLat;
    }

    public double getLowerLeftCornerLon()
    {
		double lowerLeftCornerLon = MapPanel.position2lon(this.getCenter().getX() - this.getWidth() / 2, this.getZoom());
        return lowerLeftCornerLon;
    }

    public double getLowerLeftCornerLat()
    {
		double lowerLeftCornerLat = MapPanel.position2lat(this.getCenter().getY() + this.getHeight() / 2, this.getZoom());
        return lowerLeftCornerLat;
    }
    
    public JPanel getDebugConsole()
    {
        return debugConsole;
    }
    //end of getters/setters
}
