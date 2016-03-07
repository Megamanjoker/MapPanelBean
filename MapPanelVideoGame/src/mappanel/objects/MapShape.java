package mappanel.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

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
public class MapShape extends MapObject
{

    /**
     * 
     */
    private static final long serialVersionUID = -367184054309016129L;
    
    private ArrayList<Double> ListOfLat = new ArrayList<Double>();
    private ArrayList<Double> ListOfLon = new ArrayList<Double>();
    private String name;
    private Color shapeStrokeColor = Color.BLACK;
    private Color shapeFillColor = null;
    private Color tempColor = Color.BLACK;
    private BasicStroke strokeThickness = new BasicStroke(1);
    private Boolean line = false;
    private GeneralPath bounds = new GeneralPath();
    private boolean enter = false;

    public MapShape(double x, double y)
    {
	super(x, y, ObjectID.Shape);
    }
    
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats)
    {
	this(lons,lats,null,null,null);
    }
    
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats, Color shapeStrokeColor)
    {
	this(lons,lats,null,shapeStrokeColor,null);
    }
    
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats, Color shapeStrokeColor, int zoom)
    {
	this(lons, lats, null , shapeStrokeColor, null, zoom, null, null);
    }
    
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats, String name , Color shapeStrokeColor)
    {
	this(lons,lats,name,shapeStrokeColor,null);
    }
    
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats, Color shapeStrokeColor,Color shapeFillColor)
    {
	this(lons,lats,null,shapeStrokeColor,shapeFillColor);
    }
    
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats, String name , Color shapeStrokeColor, Color shapeFillColor)
    {
	this(lons,lats,name,shapeStrokeColor,shapeFillColor,null);
    }
    
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats, String name , Color shapeStrokeColor, Color shapeFillColor, BasicStroke strokeThickness)
    {
	this(lons, lats, name , shapeStrokeColor, shapeFillColor, strokeThickness,false);
    }
    
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats, String name , Color shapeStrokeColor, Color shapeFillColor, BasicStroke strokeThickness, Boolean Line)
    {
	this(lons, lats, name , shapeStrokeColor, shapeFillColor, null, strokeThickness, Line);
    }
    
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats, String name , Color shapeStrokeColor, Color shapeFillColor, Integer Zoom, BasicStroke strokeThickness, Boolean Line)
    {
	this(lons, lats, name , shapeStrokeColor, shapeFillColor, null, strokeThickness, Line, null);
    }
    
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats, String name , Color shapeStrokeColor, Color shapeFillColor, Integer Zoom, BasicStroke strokeThickness, Boolean Line, MouseListener mouse)
    {
	super(0, 0, ObjectID.Shape);
	this.ListOfLon = lons;
	this.ListOfLat = lats;
	this.name = name;
	
	if(Line != null)
	    this.line = Line;
	if(shapeStrokeColor != null)
	    this.shapeStrokeColor = shapeStrokeColor;
	if(shapeFillColor != null)
	    this.shapeFillColor = shapeFillColor;
	if(strokeThickness != null)
	    this.strokeThickness = strokeThickness;
	if(mouse != null)
	    this.addMouseListener(mouse);
	
    }

    @Override
    public void tick(LinkedHashSet<MapObject> objects)
    {
	
    }

    @Override
    public void render(Graphics g)
    {
	tempColor = g.getColor();
	
	Graphics2D g2d = (Graphics2D) g;
	g2d.setColor(shapeStrokeColor);
	Stroke tempStroke = g2d.getStroke();
	GeneralPath shape = new GeneralPath();
	g2d.setStroke(strokeThickness);
	if(ListOfLon.size() == ListOfLat.size())
	{
	    shape.moveTo(MapPanel.lon2position(ListOfLon.get(0), Zoom), MapPanel.lat2position(ListOfLat.get(0), Zoom));
	    for(int i = 1; i < ListOfLon.size(); i++)
	    {
		shape.lineTo(MapPanel.lon2position(ListOfLon.get(i), Zoom), MapPanel.lat2position(ListOfLat.get(i), Zoom));
	    }
	    if(!line)
		shape.closePath();
	    
	    if(shapeFillColor != null)
	    {
		g2d.setColor(shapeFillColor);
		g2d.fill(shape);
	    }
	    g2d.draw(shape);
	    bounds = shape;
	}
	g2d.setStroke(tempStroke);
	g.setColor(tempColor);
    }

    @Override
    public Rectangle2D getBound()
    {
	return bounds.getBounds2D();
    }

    public Color getShapeStrokeColor()
    {
        return shapeStrokeColor;
    }

    public void setShapeStrokeColor(Color shapeStrokeColor)
    {
        this.shapeStrokeColor = shapeStrokeColor;
    }
    
    public String toString()
    {
	String toString = "";
	toString += "Name = " + name + "\n";
	toString += "Bounding Box = " + bounds.getBounds() + "\n";
	toString += "ListOfLon = " + ListOfLon + " ListOfLat = " + ListOfLat + "\n";
	toString += "shapeStrokeColor = " + shapeStrokeColor + " shapeFillColor = " + shapeFillColor + "\n";
	toString += "Zoom = " + Zoom + "\n";
	toString += "Lat and Lon sizes equal = " ;
	toString += (ListOfLon.size() == ListOfLat.size()) ;
	toString += "\n";
	toString += "Lon list size = " + ListOfLon.size() + " Lat list size = " + ListOfLat.size() + "\n";
	
	return toString;
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
