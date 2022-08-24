
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

                                if(fall[i].getY()>50){ // ensure the Hungry Word gets to eat other words when it appears on the active panel

                                    heightDiff = Math.abs(fall[i].getY()-fall[j].getY()); // get the words height difference 
                                    thresholdSpeed = Math.abs(fall[i].getSpeed()-fall[j].getSpeed()); // get the difference in the words falling speed
                                    
                                    if(fall[i].getY()<halfMaxHeight){ // use this condition to deal with two cases, when the word is falling vertically and horinzontally
                                    // cater for all the words falling speed so the word can dissapear when bumped based on relative speed
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
                                    else{ // this is the case when the word is moving horizontally, the relative speed becomes higher. Hence the threshold increases to deem a bump has happened
                                        if(fall[j].getSpeed()<200)
                                        threshold = 66;
                                    else if(fall[j].getSpeed()>199&&fall[j].getSpeed()<400)
                                        threshold = 54;
                                    else if(fall[j].getSpeed()>399&&fall[j].getSpeed()<600)
                                        threshold = 44;
                                        else if(fall[j].getSpeed()>599&&fall[j].getSpeed()<900)
                                        threshold = 34;
                                    else
                                        threshold = 24;
                                    } 
                                      
                                    if(heightDiff<threshold){  // execute this code when the threshold has been met
                                        lengthSum = fall[i].wordPixelLenght()+fall[j].wordPixelLenght(); //sum of the word's pixel length from the center
                                        pixelPosition = Math.abs(fall[i].getX()-fall[j].getX()); // difference in their x position
                                        if(pixelPosition<lengthSum){ // only when this final condition has been met we consider that there has been a bump. When the sum of the words length from center
                                                                     // is more than the pixel's x position relativity
                                            fall[j].resetWord();
                                            score.missedWord();
                                            pixelPosition = 10000; //reset the pixels positions to arbitary large numbers out of the game panel lenght
                                            lengthSum = 10000; //reset the word's sum lengths to arbitary large numbers out of the game panel lenght
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
