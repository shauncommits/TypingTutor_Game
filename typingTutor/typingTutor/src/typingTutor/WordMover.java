package typingTutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * WordMover class to move the falling words
 * 
 * @author Shaun 
 * Date created 17/08/22
 * @version 1
 */

public class WordMover extends Thread {
	private FallingWord myWord;
	private AtomicBoolean done;
	private AtomicBoolean pause; 
	private Score score;
	CountDownLatch startLatch; //so all can start at once
	
	
	/**
	 * WordMover constructor
	 * @param word is the FallingWord object passed by reference
	 */
	WordMover( FallingWord word) {
		myWord = word;
	}
	
	/**
	 * WordMover constructor 
	 * 
	 * @param word is the FallingWord 
	 * @param dict the dictionary with the words
	 * @param score passed by value Score
	 * @param startLatch the latch to start when count down method is called with specific threshold value
	 * @param d signal when the game has ended
	 * @param p signal when the game is paused
	 */
	WordMover( FallingWord word,WordDictionary dict, Score score,
			CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p) {
		this(word);
		this.startLatch = startLatch;
		this.score=score;
		this.done=d;
		this.pause=p;
	}
	
	public void run() {
		
		try {
			System.out.println(myWord.getWord() + " waiting to start " );
			startLatch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //wait for other threads to start
		System.out.println(myWord.getWord() + " started" );
		while (!done.get()) {				
			//animate the word
			while (!myWord.dropped() && !done.get()) {
				    myWord.drop(10);
					try {
						sleep(myWord.getSpeed());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};		
					while(pause.get()&&!done.get()) {


						int treshold = 180; // seconds
					    long start = System.currentTimeMillis()/1000; // converting machine time to seconds
						
						// time the game to be pause not more than a minute
					    while((System.currentTimeMillis()/1000)-start<treshold&&pause.get()&&!done.get()){ 
							if((System.currentTimeMillis()/1000)-start>=treshold){
								System.exit(0);
							}
					   } 

					};
			}
			if (!done.get() && myWord.dropped()) {
				score.missedWord();
				myWord.resetWord();
			}
			myWord.resetWord();
		}
	}
	
}
