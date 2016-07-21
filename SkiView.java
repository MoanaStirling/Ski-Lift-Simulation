package a3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class SkiView extends JFrame {
	
	ModelSwingWorker worker;
	
	private int totalSkiers=30;
	private int liftSeats=10;
	private int liftSpeed=1000;
	private double stopProbability=0.05;
	private int maxSkiTime=12000;
	
    public SkiView() {
        setTitle("Ski Field");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container cp = getContentPane();
        
        final SkiPanel skiPanel = new SkiPanel();
        skiPanel.setPreferredSize(new Dimension(1000, 500));
        cp.add(skiPanel, BorderLayout.CENTER);
		
        
        JPanel topPanel = new JPanel();

        final JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                worker = new ModelSwingWorker(totalSkiers, liftSeats, stopProbability, liftSpeed, maxSkiTime, skiPanel);
                worker.execute();
            }
        });
        topPanel.add(startButton);
        
        final JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(!startButton.isEnabled()){
	            	worker.cancel(true);
	                startButton.setEnabled(true);
            	}
            }
        });
        topPanel.add(stopButton);

        JTextField skiersTxt = new JTextField("   30");
		skiersTxt.setToolTipText("Number of Skiers");
		skiersTxt.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField tf = (JTextField)e.getSource();
				try {
					int newValue = Integer.parseInt(tf.getText());
					if (newValue > 0) // if the value is valid, then change the current height
					 	totalSkiers=newValue;
					else
						tf.setText(totalSkiers+"");
				} catch (Exception ex) {
					tf.setText(totalSkiers+""); //if the number entered is invalid, reset it
				}
			}
		});
		topPanel.add(skiersTxt);
		
		JTextField liftSeatsTxt = new JTextField("   10");
		liftSeatsTxt.setToolTipText("Lift Seats");
		liftSeatsTxt.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField tf = (JTextField)e.getSource();
				try {
					int newValue = Integer.parseInt(tf.getText());
					if (newValue > 0) // if the value is valid, then change the current height
					 	liftSeats=newValue;
					else
						tf.setText(liftSeats+"");
				} catch (Exception ex) {
					tf.setText(liftSeats+""); //if the number entered is invalid, reset it
				}
			}
		});
		topPanel.add(liftSeatsTxt);
		
		JTextField liftSpeedTxt = new JTextField("    1");
		liftSpeedTxt.setToolTipText("Lift Speed: Skiers/Second");
		liftSpeedTxt.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField tf = (JTextField)e.getSource();
				try {
					int newValue = Integer.parseInt(tf.getText());
					if (newValue > 0) // if the value is valid, then change the current height
					 	liftSpeed= (int) ((Math.pow(newValue,-1))*1000);
					else
						tf.setText(liftSpeed+"");
				} catch (Exception ex) {
					tf.setText(liftSpeed+""); //if the number entered is invalid, reset it
				}
			}
		});
		topPanel.add(liftSpeedTxt);
		
		JTextField skiTimeTxt = new JTextField("12000");
		skiTimeTxt.setToolTipText("Max Time Down Slope: mS");
		skiTimeTxt.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField tf = (JTextField)e.getSource();
				try {
					int newValue = Integer.parseInt(tf.getText());
					if (newValue > 0) // if the value is valid, then change the current height
					 	maxSkiTime=	newValue;
					else
						tf.setText(maxSkiTime+"");
				} catch (Exception ex) {
					tf.setText(maxSkiTime+""); //if the number entered is invalid, reset it
				}
			}
		});
		topPanel.add(skiTimeTxt);
		
		JTextField breakdownTxt = new JTextField("0.05");
		breakdownTxt.setToolTipText("Probability of Lift Breakdown");
		breakdownTxt.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField tf = (JTextField)e.getSource();
				try {
					double newValue = Double.parseDouble(tf.getText());
					if (newValue > 0) // if the value is valid, then change the current height
					 	stopProbability = newValue;
					else
						tf.setText(stopProbability+"");
				} catch (Exception ex) {
					tf.setText(stopProbability+""); //if the number entered is invalid, reset it
				}
			}
		});
		topPanel.add(breakdownTxt);

        cp.add(topPanel, BorderLayout.NORTH);

        pack();
    }


}
