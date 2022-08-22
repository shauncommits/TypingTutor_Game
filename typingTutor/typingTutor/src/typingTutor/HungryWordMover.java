
package typingTutor;

/**
 * @author Shaun 
 * Date created 21/08/22
 * @version 1
 */
    

public class HungryWordMover extends Thread {
    /**
     * Center of attention word is the Hungry Word
     */
    public String target = "Hungry Word"; // use this not simplify the calling of the word's height 
    static int thislen1,ylen1,ylen1ad,ythislen1; // the height position of the Hungry Word
    static int thislen2,ylen2,ylen2ad,ythislen2; // the heigth position of the other words 
    static int bothLen; // the combination of the lenght of the words from thier centers
    static int diffPos; // the difference in the words x posisition
    static int size = TypingTutorApp.noWords; // get the size of the array of the words falling words to be used
    static FallingWord[] fall = new FallingWord[size]; // FallingWord array
	static WordMover[] wordsM = new WordMover[size]; // WordMover array
    static Score score; // The Score object

    /**
     * The HungryWordMover constructor
     * @param f the FallingWord array passed by reference
     * @param w the WordMore array passed by reference
     * @param sco the score object passed by reference
     */
    public HungryWordMover(FallingWord[] f, WordMover[] w,Score sco){
        fall = f;
        wordsM = w;
        score = sco;
    }
	
	public synchronized void run() {
		while(true){ // Execution that is executed while the Main Thread has not ended
            for (int i = 0; i < size; i++) { // loops through the FallingWord created Threads
                for (int j = 0; j < size; j++) {
                    if(i==j)
                        continue;
                            if(fall[i].getWord().equals("Hungry Word")){ // code to be implement when the Hungry Word is on the screen
                                // Get the surrrounding y values of the Hungry Word in the screen as it is moving
                                ylen1 = -8 + fall[i].getY();
                                ylen1ad = ylen1-14;
                                ylen2 = 8 + fall[i].getY();

                                // Get the surrrounding y values of the other words in the screen as they are moving
                                ythislen1 = -8 + fall[j].getY();
                                ylen2ad = ylen2 + 14;
                                ythislen2 = 8 + fall[j].getY();

                                if(fall[i].getX()>940){ // When the Hungry Word not scored and manages to go out screen the word is reseted and number of missed words are incremented
                                    fall[i].resetWord();
                                    score.missedWord();
                                }
                                if(fall[i].getY()>45){ // The Hungry Word start to eat other words when it is visible on the screen
                                    
                                    if((ythislen1>=ylen2&&ythislen1>=ylen2ad)||(ythislen2>=ylen1&&ythislen2>=ylen1ad)){ // Check if the y values of the Hungry Word and the values of other word are overlapping 
                                        bothLen = (target.length()+fall[j].getWord().length())/2; // Add the lengths of both half the hungry word and other words
                                        diffPos = Math.abs(fall[i].getX()-fall[j].getX()); // get the difference between the x postions of the words that are almost overlap in y values with the Hungry Word
                                        if(bothLen>=diffPos){ // check if the difference in the x values of the words is less than the sum of their half lengths and if so the words are touching eac other
                                            fall[j].resetWord();
                                            score.missedWord();
                                            bothLen = 1000;
                                        }
                                    } 
                                }
                            } 
                }	 
            }
        }
	}	
    
}
