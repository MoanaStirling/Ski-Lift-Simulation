package a3;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Model 
	{
	private int totalSkiers;
	private int liftSeats;
	private int liftSpeed;
	private double stopProbability;
	private int maxSkiTime;
	private BlockingQueue<Skier> liftQueue;
	private BlockingQueue<Skier> waitingQueue;
	
	private boolean finished = false;
	
	public Model()
	{
		this(30, 10, 0.05, 1000, 12000);
	}
	
	public Model(int skiers, int seats, double probability, int initialLiftSpeed, int initialMaxSkiTime){
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
	}
	
	public void start() throws IOException
	{
		LiftThread lift = new LiftThread("Lift");
		lift.start();
		try {
			Thread.sleep(50000); // change runtime here;
		} catch (InterruptedException e) {}
		finished = true;
	}
	
	public void display()
	{
		
	}
	
	public class Skier implements Runnable 
		{
		
		private int id = -1;
		private boolean empty;
		
		Skier(){
			empty = true;
		}
		
		Skier(int initialId){
			id = initialId;
		}
		
		public boolean isEmpty(){
			if (id==-1){
				return true;
			}
			else{
				return false;
			}
		}

		public void run() 
		{
			try {
				Thread.sleep(2000+(long)(Math.random()*(maxSkiTime-2000)));
				synchronized(waitingQueue){
					waitingQueue.put(this);
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
	
	public class LiftThread extends Thread 
		{

		public LiftThread(String name) {
			super(name);
		}

		public void run(){
			System.out.println(String.format("Waiting: %s", waitingQueue.toString()));
			System.out.println(String.format("On Lift: %s", liftQueue.toString()));
			try{
				while(!finished){
					if (Math.random()<=stopProbability){
						long time = (long)(Math.random()*8000);
						System.out.println(String.format("Lift stops temporarily (for %f milliseconds).", (float)time));
						Thread.sleep(time);
						System.out.println("Lift continues operation");
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
						synchronized(waitingQueue){
							System.out.println(String.format("Waiting: %s", waitingQueue.toString()));
						}
						synchronized(liftQueue){
							System.out.println(String.format("On Lift: %s", liftQueue.toString()));
						}
						Thread.sleep(liftSpeed);
					}
				}
			} catch (InterruptedException e) {
			}
		}
	}
}

