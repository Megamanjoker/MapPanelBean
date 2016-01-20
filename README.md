---
<h1><a href = "http://megamanjoker.github.io/MapPanelBean">Map Panel Bean<a></h1>
---
<h2>The main goals of this Map Panel Bean:</h2>

1. <b> Be easy to integrate in to any java application </b>
2. <b> Be orginized and will structured </b>
3. <b> Be Well Commented and Documentated</b>

---
<h1>How do I implement this MapPanel?</h1>
1. Download and import the zip file
2. Do something like this

```java
import mappanel.window.MapPanel;
import javax.swing.JFrame;

public class someClass
{
    String title = "Some Title";
    MapPanel map = new MapPanel();
    public static void main()
    {
        JFrame frame = new JFrame(title);
        frame.add(map);
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(true);
    	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	map.start();
    }
}

```
---
<h1>Tutorial</h1>
<h3>This is the part where you learn</h3>

<h2>How do I add a point to the Map?</h2>
After starting the map, do something like this
```java
public void initPoint()
{
    double someLon = 10;
    double someLat = 10;
    MapPoint point = MapPoint(ObjectID.Point,someLon,someLat);
    map.addPoint(point);
}
```
This will create an X on to that lon/lat. the X is the default picture

<h2>How do I add a Shape to the Map?</h2>

```java
public void initShape()
{
    ArrayList<Double> Lons = new ArrayList<Double>();
    ArrayList<Double> Lats = new ArrayList<Double>();
    Lons.add(10d);
    Lats.add(10d);
    Lons.add(-10d);
    Lats.add(10d);
    Lons.add(-10d);
    Lats.add(-10d);
    Lons.add(10d);
    Lats.add(-10d);
    map.addShape(new MapShape(ObjectID.Shape,Lons,Lats,Color.BLACK));
}
```


---
<h1>Some of the Class explained</h1>
<dl>
    <dt><h2>Map Objects</h2></dt>
    <dd>The Base of all the Objects on the map</dd>
    <dd><h5>Methods proved by this abstart class</h5></dd>
    <dd>
        <ul>
            <li>set/get name - the name of the object</li>
            <li>set/get zoom - the current zoom level of the object</li>
            <li>set/get x and y - the x and y of that object in the map panel</li>
            <li>set/get id - this is a way to tell the differents between tiles and everything else</li>
            </ul>
    </dd>
    
    <dt><h3>MapPoint</h3></dt>
    <dd>The Base for all the points</dd>
    
    <dt><h3>MapShape</h3></dt>
    <dd>The Base for all the shapes</dd>

</dl>


---
<dl>
    <dt><h2>Screen Shots</h2></dt>  
    
    <dt><h3>this is the Map Panel</h3></dt>
    <dd><img src="https://github.com/Megamanjoker/MapPanelBean/blob/master/Pictures%20of%20the%20MapPanel/MapPanelScreenShot.png?raw=true" alt="Map Panel Screen Shot with no shapes or points" height="250" width="400"></dd>
    
    <dt><h3>You can add points!</h3></dt>
    <dd><img src="https://github.com/Megamanjoker/MapPanelBean/blob/master/Pictures%20of%20the%20MapPanel/MapPanelScreenShotWithPoint.png?raw=true" alt="Map Panel Screen Shot with a point" height="250" width="400"></dd>
    
    <dt><h3>You can add shapes!</h3></dt>
    <dd><img src="https://github.com/Megamanjoker/MapPanelBean/blob/master/Pictures%20of%20the%20MapPanel/MapPanelScreenShotWithAShape.png?raw=true" alt="Map Panel Screen Shot with a shape" height="250" width="400"></dd>
    
    <dt><h3>You want both, go right a head!</h3></dt>
    <dd><img src="https://github.com/Megamanjoker/MapPanelBean/blob/master/Pictures%20of%20the%20MapPanel/MapPanelScreenShotWithPointAndShape.png?raw=true" alt="Map Panel Screen Shot with a shape and point" height="250" width="400"></dd>

</dl>
