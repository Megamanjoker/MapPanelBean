package com.tv.map.mappanel.objects;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.LinkedHashSet;

import javax.imageio.ImageIO;

import com.tv.map.mappanel.window.MapPanel;
import com.tv.map.mappanel.framework.ObjectID;
import com.tv.map.mappanel.framework.MapObject;

/**
 * The type Map point.
 */
public class MapPoint extends MapObject implements ImageObserver
{
    private static final long serialVersionUID = 3169731756082120901L;
    private Boolean labelVisible = false;
    private Color labelForegroundColor = Color.black;
    private Color labelBackgroundColor = Color.white;
    private String labelText = "";
    private Image image;
    private int width,height;
    private double lat,lon;
    private boolean enter = false;
    private int uniqueID = 0;
    private MapPanel map;

    /**
     * Instantiates a new Map point.
     *
     * @param lon                  the lon
     * @param lat                  the lat
     * @param Zoom                 the zoom
     * @param image                the image
     * @param name                 the name
     * @param labelText            the label text
     * @param labelBackgroundColor the label background color
     * @param labelForegroundColor the label foreground color
     * @param labelVisible         the label visible
     * @param mouseListener        the mouse listener
     */
    public MapPoint(double lon, double lat, int Zoom, Image image,  String name, String labelText, Color labelBackgroundColor, Color labelForegroundColor, Boolean labelVisible, MouseListener mouseListener)
    {
        super(0, 0, ObjectID.Point,2);
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
        this.name = name;

        if(labelText != null)
            this.labelText = labelText;
        if(labelBackgroundColor != null)
            this.labelBackgroundColor = labelBackgroundColor;
        if (labelForegroundColor != null)
            this.labelForegroundColor = labelForegroundColor;
        if (labelVisible != null)
            this.labelVisible = labelVisible;

    }

    /**
     * Draws the Image on the map
     * @param g graphic to render on
     */
    public void render(Graphics g)
    {
	    g.drawImage(image, x, y, this);
        if(labelVisible)
        {
            FontMetrics fm = g.getFontMetrics();
            int fontWidth = fm.stringWidth(this.labelText);
            int fontHeight = fm.getAscent();
            g.setColor(labelBackgroundColor);
            g.fillRect((int) this.getBound().getCenterX() - (fontWidth / 2) - 5 ,(int) this.getBound().getCenterY() - fontHeight - image.getHeight(this)/2 - 15 , fontWidth + 10, fontHeight + 10);
            g.setColor(labelForegroundColor);
            g.drawString(this.labelText,(int) this.getBound().getCenterX() - (fontWidth / 2),(int) this.getBound().getCenterY() - (fontHeight / 4) - image.getHeight(this)/2 - 10);
        }
    }

    /**
     * @return returns the boundary of the image
     */
    public Rectangle2D getBound()
    {
	    return new Rectangle2D.Double(x,y, width,height);
    }

    /**
     * Used for the ImageObserver interface
     * @return returns false
     */
    public boolean imageUpdate(Image image, int arg1, int arg2, int arg3, int arg4, int arg5)
    {
	    return false;
    }

    /**
     * move the icon every tick
     * @param objects object to be ticked
     */
    public void tick(LinkedHashSet<MapObject> objects)
    {
	    this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
        this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
    }

    //Start of getters/setters
    public void setZoom(int Zoom)
    {
        this.Zoom = Zoom;
        //Change the X and Y to fit the zoom level
        this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
        this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Is enter boolean.
     *
     * @return the boolean
     */
    public boolean isEnter()
    {
        return enter;
    }

    /**
     * Sets enter.
     *
     * @param enter the enter
     */
    public void setEnter(boolean enter)
    {
        this.enter = enter;
    }

    /**
     * Gets label visible.
     *
     * @return the label visible
     */
    public Boolean getLabelVisible() {
        return labelVisible;
    }

    /**
     * Sets label visible.
     *
     * @param labelVisible the label visible
     */
    public void setLabelVisible(Boolean labelVisible) {
        this.labelVisible = labelVisible;
    }

    /**
     * Gets label foreground color.
     *
     * @return the label foreground color
     */
    public Color getLabelForegroundColor() {
        return labelForegroundColor;
    }

    /**
     * Sets label foreground color.
     *
     * @param labelForegroundColor the label foreground color
     */
    public void setLabelForegroundColor(Color labelForegroundColor) {
        this.labelForegroundColor = labelForegroundColor;
    }

    /**
     * Gets label background color.
     *
     * @return the label background color
     */
    public Color getLabelBackgroundColor() {
        return labelBackgroundColor;
    }

    /**
     * Sets label background color.
     *
     * @param labelBackgroundColor the label background color
     */
    public void setLabelBackgroundColor(Color labelBackgroundColor) {
        this.labelBackgroundColor = labelBackgroundColor;
    }

    /**
     * Gets label text.
     *
     * @return the label text
     */
    public String getLabelText() {
        return labelText;
    }

    /**
     * Sets label text.
     *
     * @param labelText the label text
     */
    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    /**
     * Gets unique id.
     *
     * @return the unique id
     */
    public int getUniqueID() {
        return uniqueID;
    }

    /**
     * Sets unique id.
     *
     * @param uniqueID the unique id
     */
    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    /**
     * Gets map.
     *
     * @return the map
     */
    public MapPanel getMap()
    {
        return map;
    }

    /**
     * Sets map.
     *
     * @param map the map
     */
    public void setMap(MapPanel map)
    {
        this.map = map;
    }
    //End of getters/setters
}
