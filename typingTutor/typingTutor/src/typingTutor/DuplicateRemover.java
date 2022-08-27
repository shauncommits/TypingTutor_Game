package typingTutor;

/**
 * DuplicateRemover Thread to remove duplicate words from the game active environment
 * 
 * @author Shaun 
 * Date created 20/08/22
 * @version 1
 */

public class DuplicateRemover extends Thread{
    
    static int size = TypingTutorApp.noWords; // the number of words specified in TypingTutor by the user he wants to fall when playing the game
    static FallingWord[] fall = new FallingWord[size]; // Array of the FalligWord 
	static WordMover[] wordsM = new WordMover[size]; // Array of the WordMover Thread
    static FallingWord hungWord = new FallingWord();
    Score sco; // Score declaration
    
	/**
     * Constructor of the class
     * @param f array of the FallingWord
     * @param w array of the WordMover
     * @param score Score reference
     * @param d the FallingWord object for the Hungry Word
     */
    public DuplicateRemover(FallingWord[] f,FallingWord d, WordMover[] w,Score score){
        fall = f;
        wordsM = w;
        sco = score;
        hungWord = d;
    }

    /**
     * loops through all the Falling words and compare one words to all other falling words to remove duplicates
     */
	public void run() {
		while (true) {// loops throughout the game as long as it has been started and remove all the duplicate words from the game
           for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(i==j)
                    continue;
                    if(fall[i].getY()>50&fall[j].getY()>50){// remove the duplicates when the word appear on the screen
                        if(fall[i].sameWord((fall[j]))); // method to remove the duplicate words
                        if(fall[i].sameWord(hungWord));}
            }	 
           }   
		}
	}	
}
