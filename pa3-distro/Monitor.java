
/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	private int chopstickNum,sleepingPhilosopher=0;
	private boolean eating[];//indicates if a given philosopher is eating or not
	private boolean silent=true;//indicates if anyone is talking
	
	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		chopstickNum=piNumberOfPhilosophers;
		eating=new boolean[piNumberOfPhilosophers];//each philosopher has a boolean that indicates whether they are eating
		
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */
	public synchronized void startSleep() {
		sleepingPhilosopher++;//indicates that there is one more philosopher sleeping
	}
	
	public synchronized void endSleep() {
		sleepingPhilosopher--;//signal that the philosopher has ended sleeping 
		//System.out.println(sleepingPhilosopher);
	}
	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{	
		while(true)//while the philosopher haven't eaten
			//if neighbors are not eating, take the chopsticks and eat
		if(eating[(piTID+chopstickNum)%chopstickNum]!=true && eating[(piTID+chopstickNum-2)%chopstickNum]!= true) {
			//change status to eating
			eating[piTID-1]=true;
			//break out of the loop when the philosopher is eating
			break;
		}else {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{	
		eating[piTID-1]=false;//set the eating status of the philosopher to false
		notifyAll();//let others check if they can eat
	}

	/**
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		
		while(!silent||sleepingPhilosopher>0) {//while someone is talking
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		silent=false;//else indicate that silent is false and talk
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		silent=true;//it's silent again and someone can talk
		notifyAll();//signal that someone can talk
	}
}

// EOF
