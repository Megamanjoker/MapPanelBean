package com.TylerValant.MapPanel.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.TylerValant.MapPanel.framework.MapObject;
import com.TylerValant.MapPanel.framework.ObjectID;
import com.TylerValant.MapPanel.window.MapPanel;

public class MapShape extends MapObject
{

    /**
     * 
     */
    private static final long serialVersionUID = -367184054309016129L;
    
    private ArrayList<Double> ListOfLat = new ArrayList<Double>();
    private ArrayList<Double> ListOfLon = new ArrayList<Double>();
    private Color shapeColor = Color.BLACK;
    private Color tempColor = Color.BLACK;

    public MapShape(double x, double y, ObjectID id)
    {
	super(x, y, id);
	// TODO Auto-generated constructor stub
    }
    
    public MapShape(double x, double y, ObjectID id, ArrayList<Double> lons, ArrayList<Double> lats)
    {
	super(x, y, id);
	this.ListOfLon = lons;
	this.ListOfLat = lats;
    }

    @Override
    public void tick(List<MapObject> objects)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void render(Graphics g)
    {
	tempColor = g.getColor();
	g.setColor(shapeColor);
	if(ListOfLon.size() == ListOfLat.size())
	{
	    for(int i = 0; i < ListOfLon.size() - 1; i++)
	    {
		g.drawLine(MapPanel.lon2position(ListOfLon.get(i), this.getZoom()), MapPanel.lat2position(ListOfLat.get(i), Zoom), MapPanel.lon2position(ListOfLon.get(i+1), this.getZoom()) , MapPanel.lat2position(ListOfLat.get(i+1), Zoom));
	    }
	    g.drawLine(MapPanel.lon2position(ListOfLon.get(ListOfLon.size() - 1), this.getZoom()), MapPanel.lat2position(ListOfLat.get(ListOfLon.size() - 1), Zoom),MapPanel.lon2position(ListOfLon.get(0), this.getZoom()), MapPanel.lat2position(ListOfLat.get(0), Zoom));
	}
	g.setColor(tempColor);
    }

    @Override
    public Rectangle getBound()
    {
	// TODO Auto-generated method stub
	return null;
    }
}
