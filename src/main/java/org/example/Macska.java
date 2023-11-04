package main.java.org.example;

import javax.swing.*;
import java.awt.*;

public class Macska extends JPanel {

    protected void paintComponent(Graphics g){
        g.setColor(Color.red);
        g.drawPolygon(new int[]{50,75,100}, new int[]{100,200,100},3);
    }
}
