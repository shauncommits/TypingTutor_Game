package typingTutor;

import java.util.concurrent.atomic.AtomicInteger;

public class Score {
	private AtomicInteger missedWords;
	private AtomicInteger caughtWords;
	private AtomicInteger gameScore;
	
	Score() {
		missedWords= new AtomicInteger(0);
		caughtWords=new AtomicInteger(0);
		gameScore=new AtomicInteger(0);
	}
		
	// all getters and setters must be synchronized
	
	synchronized public int getMissed() {
		return missedWords.get();
	}

	synchronized public int getCaught() {
		return caughtWords.get();
	}
	
	synchronized public int getTotal() {
		return (missedWords.get()+caughtWords.get());
	}

	synchronized public int getScore() {
		return gameScore.get();
	}
	
	synchronized public void missedWord() {
		missedWords.getAndIncrement();
	}
	synchronized public void caughtWord(int length) {
		caughtWords.getAndIncrement();
		gameScore.set(gameScore.get()+length);
	}

	synchronized public void reset() {
		caughtWords= new AtomicInteger(0);
		missedWords= new AtomicInteger(0);
		gameScore= new AtomicInteger(0);
	}
}
