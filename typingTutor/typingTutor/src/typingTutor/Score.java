package typingTutor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Shaun 
 * Date created 17/08/22
 * @version 1
 */

public class Score {
	private AtomicInteger missedWords;
	private AtomicInteger caughtWords;
	private AtomicInteger gameScore;
	
	/**
	 * Default contructor of the Score class
	 */
	Score() {
		missedWords= new AtomicInteger(0);
		caughtWords=new AtomicInteger(0);
		gameScore=new AtomicInteger(0);
	}
		
	/**
	 * getMissed method
	 * @return the number of words missed
	 */
	public synchronized int getMissed() {
		return missedWords.get();
	}

	/**
	 * getCaught method
	 * @return the number of words that are caught
	 */
	public synchronized int getCaught() {
		return caughtWords.get();
	}
	
	/**
	 * getTotal method
 	 * @return the number of missed and caught caught words
	 */
	public synchronized int getTotal() {
		return (missedWords.get()+caughtWords.get());
	}

	/**
	 * getScore method
	 * @return the score of the player
	 */
	public synchronized int getScore() {
		return gameScore.get();
	}
	
	/**
	 * missedWord method, increments the number of missed words by the player
	 */
	public synchronized void missedWord() {
		missedWords.getAndIncrement();
	}
	
	/**
	 * caughtWord method
	 * @param length used to add to the score of the player
	 */
	public synchronized void caughtWord(int length) {
		caughtWords.getAndIncrement();
		gameScore.set(gameScore.get()+length);
	}

	/**
	 * reset method to reset the caught, missed and game score back to zero again
	 */
	public synchronized void reset() {
		caughtWords= new AtomicInteger(0);
		missedWords= new AtomicInteger(0);
		gameScore= new AtomicInteger(0);
	}
}
