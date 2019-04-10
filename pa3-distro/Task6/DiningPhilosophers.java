package Task6;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//to compile:
//cd pa3-distro
//javac Task4/DiningPhilosophers.java
//java Task4/DiningPhilosophers *numberOfPhilosopers
/** 
 * Class DiningPhilosophers
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class DiningPhilosophers
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;
	private static Random rng = new Random(); 
	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		try
		{
			/*
			 * TODO:
			 * Should be settable from the command line
			 * or the default if no arguments supplied.
			 */
			int iPhilosophers=DEFAULT_NUMBER_OF_PHILOSOPHERS;
			
			
			try {
			 iPhilosophers = Integer.parseInt(argv[0]);//take value from command line
			 if(iPhilosophers<1) {//check if it's a positive integer
				 throw new SmallerThan0Exception();
			 }
			}catch(InputMismatchException|NumberFormatException|SmallerThan0Exception e) {//if not a positive integer, exit
				System.out.println("\""+argv[0]+"\""+" is not a positive decimal integer");
				System.exit(1);
			}catch(ArrayIndexOutOfBoundsException e) {//if no argument is given, just take default value
			}

			
			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers];

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);//print this before the start() method is invoked

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}
			
			Philosopher[] newPhilArr=newPhil();//call the method to add new philosophers
			leaving(aoPhilosophers,iPhilosophers);//a random number of philosophers leaves
			
//			System.out.println
//			(
//				iPhilosophers +
//				" philosopher(s) came in for a dinner."
//			);

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			for(int j = 0; j < newPhilArr.length; j++)//main wait for newPhils to finish dinner
				newPhilArr[j].join();


			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
	
	public static void leaving(Philosopher aoPhilosophers[], int iPhilosophers) {//pick random philosophers to leave
		int leavingPhil;
		for(int i=0;i<(int)ThreadLocalRandom.current().nextInt(2, iPhilosophers);i++) {//generate a random number of philosophers to leave
		leavingPhil=rng.nextInt(iPhilosophers-1);//pick philosophers to leave at random
		aoPhilosophers[leavingPhil].leave();//make the philosopher leave
		}
	}
	
	public static Philosopher[] newPhil() {
		int numNew=(int)ThreadLocalRandom.current().nextInt(2, 6);//pick how many philosophers to add at random
		Philosopher newPhilArr[]=new Philosopher[numNew];//give space to new philosophers
		soMonitor.addPhil(numNew);//add philosophers to the monitor
		
		for(int i=0;i<newPhilArr.length;i++) {
			newPhilArr[i]=new Philosopher();//create new philosophers
			System.out.println("Philosopher "+newPhilArr[i].getTID()+" is added");
			newPhilArr[i].start();//make the new threads run
		}	
		return newPhilArr;
	}
	
}//class DiningPhilosophers

class SmallerThan0Exception extends Exception {
	
}

// EOF
