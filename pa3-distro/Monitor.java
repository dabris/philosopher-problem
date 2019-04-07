
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
	//did not use a array to indicate the state of each philosopher because the requestTalk and endTalk methods don't have any parameter, so that I cannot associate the state of talking with any id
	private int chopstickNum,sleepingPhil=0;//sleepingPhil contains the number of philosophers who are sleeping
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
		while(silent==false)//wait until nobody is talking to sleep	
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		sleepingPhil++;
	}
	public synchronized void endSleep() {
		sleepingPhil--;
		notifyAll();
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
	public synchronized void requestTalk(){
	while(silent==false || sleepingPhil>0) {//while someone is talking or already started to sleep
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
