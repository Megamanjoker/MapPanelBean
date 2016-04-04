package mappanel.objects;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import javax.swing.JLabel;

import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import mappanel.framework.MapObject;
import mappanel.framework.ObjectID;
import mappanel.window.MapPanel;

/**
 * class for drawing shapes on the map.
 * @author Tyler Valant
 * @since 1-19-2016
 * @version 1.0.5.12-SNAPSHOT
 *
 */
public class MapShape extends MapObject
{
    private static final long serialVersionUID = -367184054309016129L;
    
    private ArrayList<Double> ListOfLat = new ArrayList<Double>();
    private ArrayList<Double> ListOfLon = new ArrayList<Double>();
    private Color shapeStrokeColor = Color.BLACK;
    private Color shapeFillColor = null;
    private Color tempColor = Color.BLACK;
    private BasicStroke strokeThickness = new BasicStroke(1);
    private Boolean line = false;
    private GeneralPath bounds = new GeneralPath();
    private boolean enter = false;
    private boolean labelVisible = false;
    private String labelText = "";
    private Color labelBackgroundColor;
    private Color labelForegroundColor;
    private int uniqueID = 0;
    private MapPanel map;

    /**
     * the main constructor
     * @param lons - {@link ArrayList<> ArrayList} of Doubles that are the longitudes
     * @param lats - {@link ArrayList<> ArrayList} of Doubles that are the latitudes
     * @param name - name of the shape
     * @param shapeStrokeColor - color of the stroke
     * @param shapeFillColor - color of the fill
     * @param Zoom - the current zoom
     * @param strokeThickness - the thickness of the stroke
     * @param isItALine - is the shape closed or not?
     * @param labelText - the text of the label
     * @param labelBackgroundColor - label's background color
     * @param labelForegroundColor - label's foreground color
     * @param labelVisible -  is the label visible?
     * @param mouse -  the mouse listener for the shape
     */
    public MapShape(ArrayList<Double> lons, ArrayList<Double> lats, String name , Color shapeStrokeColor, Color shapeFillColor, Integer Zoom, BasicStroke strokeThickness, Boolean isItALine, String labelText, Color labelBackgroundColor, Color labelForegroundColor, Boolean labelVisible, MouseListener mouse)
    {
        super(0, 0, ObjectID.Shape,1);
        this.ListOfLon = lons;
        this.ListOfLat = lats;
        this.name = name;
        this.Zoom = Zoom;

        if(isItALine != null)
            this.line = isItALine;
        if(shapeStrokeColor != null)
            this.shapeStrokeColor = shapeStrokeColor;
        if(shapeFillColor != null)
            this.shapeFillColor = shapeFillColor;
        if(strokeThickness != null)
            this.strokeThickness = strokeThickness;
        if(mouse != null)
            this.addMouseListener(mouse);
        if(labelText != null)
            this.labelText = labelText;
        if(labelBackgroundColor != null)
            this.labelBackgroundColor = labelBackgroundColor;
        if (labelForegroundColor != null)
            this.labelForegroundColor = labelForegroundColor;
        if (labelVisible != null)
            this.labelVisible = labelVisible;
	
    }


    public void tick(LinkedHashSet<MapObject> objects)
    {
	
    }

    /**
     * renders the shape and label
     * @param g
     * @See Graphics
     */
    public void render(Graphics g)
    {
        tempColor = g.getColor();

        Graphics2D g2d = (Graphics2D) g;
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

            if(!line)
            {
                g2d.setColor(shapeFillColor);
                g2d.fill(shape);
            }
            g2d.setColor(shapeStrokeColor);
            g2d.draw(shape);
            bounds = shape;
        }
        if(labelVisible)
        {
            FontMetrics fm = g.getFontMetrics();
            int fontWidth = fm.stringWidth(this.labelText);
            int fontHeight = fm.getAscent();
            g.setColor(labelBackgroundColor);
            g.fillRect((int) this.getBound().getCenterX() - (fontWidth / 2) - 10,(int) this.getBound().getCenterY() - (fontHeight / 2) - 10,fontWidth + 20 , fontHeight + 20);
            g.setColor(labelForegroundColor);
            g.drawString(this.labelText,(int) this.getBound().getCenterX() - (fontWidth / 2),(int) this.getBound().getCenterY() + (fontHeight / 4));
        }

        g2d.setStroke(tempStroke);
        g.setColor(tempColor);
    }

    /**
     * the bounding box of the shape
     * @return the bounding box of the shape
     * @See Rectangle2D
     */
    public Rectangle2D getBound()
    {
	return bounds.getBounds2D();
    }


    /**
     * returns the information of the shape
     * @return toString is a {@link java.lang.String String}
     * @See String
     */
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

    //Start of getters/setters
    public boolean isEnter()
    {
        return enter;
    }

    public void setEnter(boolean enter)
    {
        this.enter = enter;
    }

    public BasicStroke getStrokeThickness() {
        return strokeThickness;
    }

    public void setStrokeThickness(BasicStroke strokeThickness) {
        this.strokeThickness = strokeThickness;
    }

    public Boolean getLine() {
        return line;
    }

    public void setLine(Boolean line) {
        this.line = line;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public Color getLabelBackgroundColor() {
        return labelBackgroundColor;
    }

    public void setLabelBackgroundColor(Color labelBackgroundColor) {
        this.labelBackgroundColor = labelBackgroundColor;
    }

    public Color getLabelForegroundColor() {
        return labelForegroundColor;
    }

    public void setLabelForegroundColor(Color labelForegroundColor) {
        this.labelForegroundColor = labelForegroundColor;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    public MapPanel getMap() {
        return map;
    }

    public void setMap(MapPanel map) {
        this.map = map;
    }

    public boolean isLabelVisible() {
        return labelVisible;
    }

    public void setLabelVisible(boolean labelVisible) {
        this.labelVisible = labelVisible;
    }

    public Color getShapeStrokeColor()
    {
        return shapeStrokeColor;
    }

    public void setShapeStrokeColor(Color shapeStrokeColor)
    {
        this.shapeStrokeColor = shapeStrokeColor;
    }

    public Color getShapeFillColor()
    {
        return shapeFillColor;
    }

    public void setShapeFillColor(Color shapeFillColor)
    {
        this.shapeFillColor = shapeFillColor;
    }

    //End of getters/setters
}
