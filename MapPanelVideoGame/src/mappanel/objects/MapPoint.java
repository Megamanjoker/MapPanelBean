package mappanel.objects;

import java.awt.*;
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

    /**
     *
     * @param lon
     * @param lat
     * @param Zoom
     * @param image
     * @param mouseListener
     * @param name
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

//        System.out.println(this.name + "'s visibility is " + this.labelText);
    }

    /**
     *
     * @param g
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
     *
     * @return
     */
    public Rectangle2D getBound()
    {
	return new Rectangle2D.Double(x,y, width,height);
    }

    /**
     *
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     * @param arg5
     * @return
     */
    public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5)
    {
	return false;
    }

    /**
     *
     * @param Zoom
     */
    public void setZoom(int Zoom)
    {
        this.Zoom = Zoom;
        this.x = MapPanel.lon2position(lon, Zoom) - this.image.getWidth(this)/2;
        this.y = MapPanel.lat2position(lat, Zoom) - this.image.getHeight(this)/2;
        
    }

    /**
     *
     * @return
     */
    public Image getImage() {
        return image;
    }

    /**
     *
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * 
     * @param objects
     */
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

    public Boolean getLabelVisible() {
        return labelVisible;
    }

    public void setLabelVisible(Boolean labelVisible) {
        this.labelVisible = labelVisible;
    }

    public Color getLabelForegroundColor() {
        return labelForegroundColor;
    }

    public void setLabelForegroundColor(Color labelForegroundColor) {
        this.labelForegroundColor = labelForegroundColor;
    }

    public Color getLabelBackgroundColor() {
        return labelBackgroundColor;
    }

    public void setLabelBackgroundColor(Color labelBackgroundColor) {
        this.labelBackgroundColor = labelBackgroundColor;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }
}
