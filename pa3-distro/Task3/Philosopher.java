package Task3;
import common.BaseThread;
import java.util.concurrent.ThreadLocalRandom;
/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	public void Pslpeep() {
		try
		{
			System.out.println("Phil "+getTID()+" has started sleeping");//phil started to sleep
			yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Phil "+getTID()+" is done sleeping");//phil is done eating
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}
	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			System.out.println("Phil "+getTID()+" has started eating");//phil started to eat
			yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Phil "+getTID()+" is done eating");//phil is done eating
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try
		{
			System.out.println("Phil "+getTID()+" has started thinking");//phil started to think
			yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Phil "+getTID()+" is done thinking");//phil is done eating
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		System.out.println("Phil "+getTID()+" has started talking");//phil started to talk
		yield();
		saySomething();//say something brilliant at random
		yield();
		System.out.println("Phil "+getTID()+" is done talking");//phil is done talking
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(getTID());
			eat();
			DiningPhilosophers.soMonitor.putDown(getTID());
			think();
			DiningPhilosophers.soMonitor.startSleep();
			Pslpeep();
			DiningPhilosophers.soMonitor.endSleep();
			
			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			if(ThreadLocalRandom.current().nextInt(0, 5)==0)//25% chance for a philosopher to say something terribly useful
			{
				DiningPhilosophers.soMonitor.requestTalk();//request to talk
				talk();
				DiningPhilosophers.soMonitor.endTalk();//end talking
			}

			yield();
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
		
		try {
			sleep((long)(Math.random() * 1000)+6000);//talk for at least 6 seconds
		} catch (InterruptedException e) {			//this might make the executions stop in the middle 
			e.printStackTrace();					//but the goal is accomplished:other philosophers' states can be updated 
													//while the philosopher is talking
													//if this executes right after the sentence is print, then the other philosophers' states cannot be updated,
													//meaning the other philosophers cannot update their states while the philosopher is talking
		}
	
		
	}
}

// EOF
