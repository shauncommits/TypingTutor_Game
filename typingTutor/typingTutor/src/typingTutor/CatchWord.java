package typingTutor;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CatchWord Thread to monitor the word that has been typed.
 * 
 * @author Shaun 
 * Date created 17/08/22
 * @version 1
 */
public class CatchWord extends Thread {
	String target; //word that is a duplicate
	static AtomicBoolean done ;
	static AtomicBoolean pause; 
	
	private static  FallingWord[] words; //list of words
	private static int noWords; //how many
	private static Score score; //user score
	
	/**
	 * CacthWord constructor
	 * @param typedWord word that is typed by the user during the game
	 */
	CatchWord(String typedWord) {
		target=typedWord;
	}
	
	/**
	 * setWords method to set the pass by reference the FallingWords array
	 * @param wordList the array reference
	 */
	public static synchronized void setWords(FallingWord[] wordList) {
		words=wordList;	
		noWords = words.length;
	}
	/**
	 * sharedScore method to share the Score object by reference
	 * @param sharedScore the score object
	 */
	public static synchronized void setScore(Score sharedScore) {
		score=sharedScore;
	}
	
	/**
	 * setFLags method to signal when the game is paused or ended
	 * @param d to signal done
	 * @param p to signal paused
	 */
	public static synchronized void setFlags(AtomicBoolean d, AtomicBoolean p) {
		done=d;
		pause=p;
	}
	
	/**
	 * Run method to be executed when the Thread is started
	 */
	public void run() {
		int i=0;
		
		while (i<noWords) {	//loops while the number of words are not reached
			while(pause.get()) {};
			if (words[i].matchWord(target)) { //checks if the words are the same 
				System.out.println( " score! '" + target); //for checking
				score.caughtWord(target.length());	// computes the length of the word that is catched and adds it to the total score of the player
				//FallingWord.increaseSpeed();
				break;
			}
		   i++;
		}
		
	}	
}
