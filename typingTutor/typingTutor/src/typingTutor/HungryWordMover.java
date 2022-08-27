package typingTutor;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HungryWordMover removes the words that are bumped by the Hungry Word
 * 
 * @author Shaun 
 * Date created 21/08/22
 * @version 1
 */
public class HungryWordMover extends Thread {

	private FallingWord myWord; // The falling hungry word
    private FallingWord[] wordList; // FallingWord array
	private AtomicBoolean done; // signal when the game is over

    static AtomicInteger lengthSum = new AtomicInteger(0); // the combination of the words lengths from their centers
    static AtomicInteger heightDiff = new AtomicInteger(0); // the difference of the words y co-ordinates
    static AtomicInteger lenghtDiff = new AtomicInteger(0); // the difference of the words x co-ordinates
    static AtomicInteger speedDiff = new AtomicInteger(1001); // the difference of the two words speed
    static AtomicInteger threshold = new AtomicInteger(25); // the threshold used as a guide if words intersect based on their speed

    static int size = TypingTutorApp.noWords; // get the size of the array of the words falling words to be used
	private AtomicBoolean pause; //singal to the program when the game is paused
	private Score score; // The Score class 
	CountDownLatch startLatch; //so all can start at once
    private Random rand = new Random(); // generate a random number so the hungry word may appear in the game randomly
    /**
     * the variable that store the random number from the random generator class
     */
    public int random; 
    WordDictionary dictionary; // dictionary of the word used

    //static AtomicInteger thresholdSpeed = new AtomicInteger(0); // the difference in speed of the words
     static AtomicInteger halfMaxHeight; // the GUI half size height
	
     /**
      * HungryWordMOver with one paramter
      * @param word is the falling word
      */
	HungryWordMover( FallingWord word) {
		myWord = word;
	}
	
    /**
     * The HungryWordMover constructor
     * @param f the FallingWord array passed by reference
     * @param w the WordMore array passed by reference
     * @param sco the score object passed by reference
     */
	HungryWordMover(FallingWord[] wordList, FallingWord word,WordDictionary dict, Score score,
			CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p) {
		this(word);
		this.startLatch = startLatch;
		this.score=score;
		this.done=d;
		this.pause=p;
        dictionary = dict;
        this.wordList = wordList;
	}
	

	public void run() {
		//System.out.println(myWord.getWord() + " falling speed = " + myWord.getSpeed());
		try {
			System.out.println(myWord.getWord() + " waiting to start " );
			startLatch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //wait for other threads to start
		System.out.println(myWord.getWord() + " started" );
        int half = myWord.getMaxHeight()/2;
        
		while (!done.get()) {
            random = rand.nextInt(dictionary.size*1000);	
			//animate the word
            try {
                sleep(random);
                myWord.setPos(0, half);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			while (myWord.getX()<900 && !done.get()) { //&&!myWord.droppedX()
                
				    myWord.moveRight(10);
                    synchronized(this){
                    for (int i = 0; i < size; i++) { // loops through the FallingWord created Threads
                      
                        for (int j = 0; j < size; j++) {
                            if(i==j) // passes the word itself not to be compared to
                                continue;

                            lengthSum.set(myWord.wordPixelLenght()+wordList[j].wordPixelLenght()); // the sum of the words distance from their centers in the horizonatal plane
                            lenghtDiff.set(Math.abs(myWord.getX()-wordList[j].getX())); // the absolute difference of the words y position
                            
                            if(lenghtDiff.get()<lengthSum.get()){ // check to see if the y co-ordinates of the words difference if is less than their words sum in length from their centers
                                heightDiff.set(Math.abs(myWord.getY()-wordList[j].getY()));  // the absolute difference of the y co-ordinates of the words
                                speedDiff.set(Math.abs(myWord.getSpeed()-wordList[j].getSpeed())); //the absolute differece of the words speed

                                if(heightDiff.get()<100){
                                if(speedDiff.get()<101) // when the speed is very fast there threshold to consider an intersection becomes high
                                    threshold.set(55);
                                else if(speedDiff.get()>100&&speedDiff.get()<301)
                                    threshold.set(35);
                                else                    // when the speed of the words is slow the threshold for intersection becomes smaller
                                    threshold.set(25);
                                
                                if(heightDiff.get()<threshold.get()){ // guard condition for the intersection threshold
                                    // resets all the values to default values
                                    score.missedWord();
                                    wordList[j].resetWord();
                                    lenghtDiff.set(0);
                                    lengthSum.set(0);
                                    heightDiff.set(0);
                                    speedDiff.set(1001);
                                    threshold.set(12);
                                }
                            }
                            }
                               
                        }	
                    }}          

					try {
						sleep(myWord.getSpeed());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};		
					while(pause.get()&&!done.get()) {};     
                
			}
			if (myWord.getX()>899 && !done.get()) { // increments number of missed words when the green word go pass the right end
				myWord.resetWord();
                score.missedWord();
			}
			myWord.resetWord();
            
		}
	}
	
}
