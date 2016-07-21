package a3;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;

public class ModelSwingWorker extends SwingWorker<String, String> {
	
	private int totalSkiers;
	private int liftSeats;
	private int liftSpeed;
	private double stopProbability;
	private int maxSkiTime;
	private BlockingQueue<Skier> liftQueue;
	private BlockingQueue<Skier> waitingQueue;
	private SkiPanel skiPanel;
	
	private int waitingSize;
	private boolean[] liftOutput;
	private AtomicInteger skiersOnSlope = new AtomicInteger(0);
	
	public ModelSwingWorker(SkiPanel skiPanel){
		this(30, 10, 0.05, 1000, 12000, skiPanel);
	}
	
	public ModelSwingWorker(int skiers, int seats, double probability, int initialLiftSpeed, int initialMaxSkiTime, SkiPanel skiPanel){
		
		totalSkiers = skiers;
		liftSeats = seats;
		stopProbability = probability;
		liftSpeed = initialLiftSpeed;
		maxSkiTime = initialMaxSkiTime;
		waitingQueue = new LinkedBlockingQueue<Skier>(totalSkiers);
		liftQueue = new LinkedBlockingQueue<Skier>(liftSeats);
		for (int i=1; i<=totalSkiers; i++){
			waitingQueue.add(new Skier(i));
		}
		for (int i=0; i<liftSeats; i++){
			liftQueue.add(new Skier());
		}
		this.skiPanel = skiPanel;
		
		liftOutput = new boolean[seats];
	}
	
	private void generateOutput(){
		int i=0;
		for (Skier skier: liftQueue){
			liftOutput[i] = skier.isEmpty();
			i++;
		}
		synchronized(waitingQueue){
			waitingSize = waitingQueue.size();
		}
	}
	
	@Override
	protected String doInBackground() {
		System.out.print(String.format("Waiting: %s\n", waitingQueue.toString()));
		generateOutput();
		skiPanel.updateSkiInfo(liftOutput, waitingSize, skiersOnSlope.intValue());
		System.out.print(String.format("On Lift: %s\n", liftQueue.toString()));
		try{
			while(true){
				if (Math.random()<=stopProbability){
					long time = (long)(Math.random()*8000);
					skiPanel.changeLiftState(true, (float)time);
					System.out.print(String.format("Lift stops temporarily (for %f milliseconds).\n", (float)time));
					Thread.sleep(time);
					skiPanel.changeLiftState(false, 0);
					System.out.print("Lift continues operation\n");
				}
				else{
					Skier leavingLiftSkier = liftQueue.take();
					synchronized(waitingQueue){
						if (!waitingQueue.isEmpty()){
							liftQueue.add(waitingQueue.take());
						}
					}
					if (!leavingLiftSkier.isEmpty()){
						new Thread(leavingLiftSkier, "Skier: " + leavingLiftSkier.toString()).start();
					}
					if (liftQueue.size() != liftSeats){
						liftQueue.add(new Skier());
					}
					generateOutput();
					skiPanel.updateSkiInfo(liftOutput, waitingSize, skiersOnSlope.intValue());
					synchronized(waitingQueue){
						System.out.print(String.format("Waiting: %s\n", waitingQueue.toString()));
					}
					System.out.print(String.format("On Lift: %s\n", liftQueue.toString()));
					Thread.sleep(liftSpeed);
				}
			}
		} catch (InterruptedException e) {
		}
		return null;
	}
	
	protected void process() {
		// TODO Auto-generated method stub

	}
	
	public class Skier implements Runnable 
	{
	
		private int id;
		private boolean empty=false;
		
		Skier(){
			empty = true;
		}
		
		Skier(int initialId){
			id = initialId;
		}
		
		public boolean isEmpty(){
			return empty;
		}
	
		public void run() 
		{
			try {
				skiersOnSlope.incrementAndGet();
				Thread.sleep(2000+(long)(Math.random()*(maxSkiTime-2000)));
				synchronized(waitingQueue){
					waitingQueue.put(this);
				skiersOnSlope.decrementAndGet();
				}
			} catch (InterruptedException e) {
			}
		}
		
		public String toString()
		{
			if (empty) {
				return "EMPTY";
			}
			return Integer.toString(id);
		}
	
	}
	
}
