package mappanel.window;

import java.awt.Dimension;

import javax.swing.*;

/**
 * 
 *  Tyler Valant
 *  Window
 *  1-19-2016
 *  1.0.0
 *
 */
public class Window 
{

    public Window(MapPanel map)
    {
        map.setPreferredSize(new Dimension(1280, 800));
        map.setMaximumSize(new Dimension(1280, 800));
        map.setMinimumSize(new Dimension(1280, 800));

        JFrame frame = new JFrame("Map Panel Prototype");
        frame.add(map);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        map.start();
    } 
}
