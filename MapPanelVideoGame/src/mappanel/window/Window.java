package mappanel.window;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window 
{

    public Window(int width, int height, String title, MapPanel map)
    {
	map.setPreferredSize(new Dimension(width,height));
	map.setMaximumSize(new Dimension(width,height));
	map.setMinimumSize(new Dimension(width,height));
	
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
