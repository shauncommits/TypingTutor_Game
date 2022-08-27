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
	String target;
	static AtomicBoolean done ;
	static AtomicBoolean pause; 
	
	private static  FallingWord[] words; //list of words
	private static FallingWord hungryWord;
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
	 * @param hung the FallingWord object for the Hungry Word
	 */
	public static void setWords(FallingWord[] wordList, FallingWord hung) {
		words=wordList;	
		noWords = words.length;
		hungryWord = hung;
	}
	
	/**
	 * sharedScore method to share the Score object by reference
	 * @param sharedScore the score object
	 */
	public static void setScore(Score sharedScore) {
		score=sharedScore;
	}
	
	/**
	 * setFLags method to signal when the game is paused or ended
	 * @param d to signal done
	 * @param p to signal paused
	 */
	public static void setFlags(AtomicBoolean d, AtomicBoolean p) {
		done=d;
		pause=p;
	}
	
	/**
	 * Run method to be executed when the Thread is started
	 */
	public void run() {
		int i=0;
		while (i<noWords) {		
			while(pause.get()) {};
			if (words[i].matchWord(target)||hungryWord.matchWord(target)) { //
				System.out.println( " score! '" + target); //for checking
				score.caughtWord(target.length());	
				//FallingWord.increaseSpeed();
				break;
			}
		   i++;
		}
		
	}	
}
