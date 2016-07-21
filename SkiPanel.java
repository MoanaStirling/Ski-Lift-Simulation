package a3;

import java.awt.*;
import javax.swing.*;

public class SkiPanel extends JPanel {
	boolean stopped = false;
	float stopTime;
	boolean[] lift = {};
	int wait = 0;
	int onSlope = 0;
	
	public void updateSkiInfo(boolean[] newLift, int newWait, int newOnSlope){
		lift = newLift;
		wait = newWait;
		onSlope = newOnSlope;
		this.repaint();
	}
	
	public void changeLiftState(boolean stopped, float stopTime){
		this.stopped=stopped;
		this.stopTime =stopTime;
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        g.drawLine(500, 100, 800, 400);
        g.drawLine(200, 400, 500, 100);
        g.drawLine(200, 400, 800, 400);
        int x = 210;
        for(int i=0; i<wait; i++){
        	g.fillRect(x, 410, 10, 10);
        	x += 20;
        }
        x = 470;
        int y = 100;
        for(boolean i : lift){
        	g.fillRect(x, y, 10, 10);
        	if(i){
        		g.setColor(Color.WHITE);
        		g.fillRect(x+1, y+1, 8, 8);
        		g.setColor(Color.BLACK);
        	}
        	x-=20;
        	y+=20;
        }
        g.drawString(String.format("%s skier(s) on slope.", onSlope), 650, 200);
        if(stopped){
        	 g.drawString(String.format("Lift stops temporarily (for %f milliseconds).\n", stopTime), 50, 200);
        }
	}
}
