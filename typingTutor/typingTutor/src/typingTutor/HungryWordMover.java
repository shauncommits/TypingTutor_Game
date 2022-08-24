
package typingTutor;

/**
 * HungryWordMover removes the words that are bumped by the Hungry Word
 * 
 * @author Shaun 
 * Date created 21/08/22
 * @version 1
 */
    

public class HungryWordMover extends Thread {
    /**
     * Center of attention word is the Hungry Word
     */
    public String target = "Hungry Word"; // use this not simplify the calling of the word's height 
    static int lengthSum; // the combination of the words lengths from their centers
    static int pixelPosition; // the difference in the words pixel positions
    static int heightDiff; // pixel height difference
    static int size = TypingTutorApp.noWords; // get the size of the array of the words falling words to be used
    static FallingWord[] fall = new FallingWord[size]; // FallingWord array
	static WordMover[] wordsM = new WordMover[size]; // WordMover array
    static Score score; // The Score object
    static int thresholdSpeed = 0; // the difference in speed of the words
    static int threshold;
     static int halfMaxHeight; // the GUI half size height

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
        halfMaxHeight = fall[0].getMaxHeight()/2; // get half of the GUI height
    }


	
	public synchronized void run() {
		while(true){ // Execution that is executed while the Main Thread has not ended
            for (int i = 0; i < size; i++) { // loops through the FallingWord created Threads
                for (int j = 0; j < size; j++) {
                    if(i==j)
                        continue;
                            if(fall[i].getWord().equals("Hungry Word")){ // code to be implement when the Hungry Word is on the screen

                                if(fall[i].getY()>50){
                                    heightDiff = Math.abs(fall[i].getY()-fall[j].getY());
                                    thresholdSpeed = Math.abs(fall[i].getSpeed()-fall[j].getSpeed());
                                    if(fall[i].getY()<halfMaxHeight){
                                    if(thresholdSpeed<200)
                                        threshold = 15;
                                    else if(thresholdSpeed>199&&thresholdSpeed<400)
                                        threshold = 25;
                                    else if(thresholdSpeed>399&&thresholdSpeed<600)
                                        threshold = 35;
                                        else if(thresholdSpeed>599&&thresholdSpeed<900)
                                        threshold = 45;
                                    else
                                        threshold = 55;
                                    } 
                                    else{
                                        if(fall[j].getSpeed()<200)
                                        threshold = 66;
                                    else if(fall[j].getSpeed()>199&&fall[j].getSpeed()<400)
                                        threshold = 56;
                                    else if(fall[j].getSpeed()>399&&fall[j].getSpeed()<600)
                                        threshold = 46;
                                        else if(fall[j].getSpeed()>599&&fall[j].getSpeed()<900)
                                        threshold = 36;
                                    else
                                        threshold = 26;
                                    } 
                                      
                                    if(heightDiff<threshold){  // if(fall[i].getSpeed())
                                        lengthSum = fall[i].wordPixelLenght()+fall[j].wordPixelLenght();
                                        pixelPosition = Math.abs(fall[i].getX()-fall[j].getX());
                                        if(pixelPosition<lengthSum){
                                            fall[j].resetWord();
                                            score.missedWord();
                                            System.out.println("Diff "+pixelPosition+" both "+lengthSum);
                                            pixelPosition = 10000;
                                            lengthSum = 10000;
                                        }
                                    }
                                }

                                if(fall[i].getX()>940){ // When the Hungry Word not scored and manages to go out screen the word is reseted and number of missed words are incremented
                                    fall[i].resetWord();
                                    score.missedWord();
                                }
                            } 
                }	 
            }
        }
	}	
    
}
