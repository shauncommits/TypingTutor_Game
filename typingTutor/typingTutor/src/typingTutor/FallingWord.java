package typingTutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.*;

/**
 * FallingWord blueprint for the falling words, has all the methods and attribute to find, modify and signal
 * an event based on the word position
 * 
 * @author Shaun 
 * Date created 17/08/22
 * @version 1
 */

public class FallingWord {
	private String word; // the word
	private AtomicInteger x; //position - width
	private AtomicInteger y; // postion - height
	private AtomicInteger maxY; //maximum height
	private AtomicBoolean dropped; //flag for if user does not manage to catch word in time
	
	private AtomicInteger fallingSpeed; //how fast this word is
	private static AtomicInteger maxWait= new AtomicInteger(1000); // initialize the maximum time for waiting to 1000 to alter the speed by which the falling word falls
	private static AtomicInteger minWait= new AtomicInteger(100); // initialize the manimmum time for waiting to 1000 to alter the speed by which the falling word falls
	/**
	 * dictionary of the words
	 */
	public static WordDictionary dict; // the object with the words used for the game
	
	FallingWord() { //constructor with defaults
		word="computer"; // a default - not used
		x= new AtomicInteger(0); // initialize the x posistion of the word to zero
		y= new AtomicInteger(0); // initialize the y position of the word to zero
		maxY=new AtomicInteger(300); // initialize the max height to signal when a word is dropped
		dropped= new AtomicBoolean(false); // signal when a word is dropped
		fallingSpeed= new AtomicInteger((int)(Math.random() * (maxWait.get()-minWait.get())+minWait.get())); // initialize the falling speed
	}
	
	/**
	 * FallingWord constructor with a single parameter
	 * @param text
	 */
	FallingWord(String text) { 
		this();
		this.word=text;  // specify the chosen word
	}
	
	/**
	 * FallingWord constructor with 3 parameters
	 * @param text the word
	 * @param x its x co-ordinate
	 * @param maxY its maximum y to be used
	 */
	FallingWord(String text,int x, int maxY) { //most commonly used constructor - sets it all.
		this(text);
		this.x= new AtomicInteger(x); //only need to set x, word is at top of screen at start
		this.maxY= new AtomicInteger(maxY); // set the maximum height
	}
	
	/**
	 * increaseSpeed method to increase the speed of the word by which it is falling
	 */
	public synchronized static void increaseSpeed( ) {
		minWait.set(minWait.intValue()+50); // increment the minimum speed by 50 
		maxWait.set(maxWait.get()+50); // increment the maximu speed by 50
	}
	
	/**
	 * resetSpeed method to reset the speed of the falling word to default speed threshold values
	 */
	public synchronized static void resetSpeed( ) {
		maxWait.set(1000);
		minWait.set(100);
	}
	
	
	/**
	 * setY method to set the y position of the falling word
	 * @param y the new position for the falling word
	 */
	public synchronized  void setY(int y) {
		if (y>maxY.get()) {
			y=maxY.get();
			dropped.set(true); //user did not manage to catch this word
		}
		this.y.set(y);
	}
	
	/**
	 * setX method to set the position of the falling word on the x-axis to the new parameter value
	 * @param x the new word position on the x-axis
	 */ 
	public synchronized  void setX(int x) {
		this.x.set(x);
	}
	
	/**
	 * setWord method to set the falling word
	 * @param text the word to set the falling word to
	 */
	public synchronized  void setWord(String text) {
		this.word=text;
	}

	/**
	 * getWord method
	 * @return the falling word
	 */
	public synchronized  String getWord() {
		return word;
	}
	
	/**
	 * getX method 
	 * @return the x position value of the word
	 */
	public synchronized  int getX() {
		return x.get();
	}	
	
	/**
	 * getY method
	 * @return the y position of the falling word
	 */
	public synchronized  int getY() {
		return y.get();
	}

	/**
	 * getMaxHeight method
	 * @return the maximum height of the word to reach when falling
	 */
	public synchronized int getMaxHeight(){
		return maxY.get();
	}

	/**
	 * getSpeed method
	 * @return the speed by which the word is falling by
	 */
	public synchronized  int getSpeed() {
		return fallingSpeed.get();
	}

	/**
	 * setPos method to set the position of the falling word 
	 * @param x position
	 * @param y position
	 */
	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}
	/**
	 * resetPos method resets the default position of the falling word
	 */
	public synchronized void resetPos() {
		setY(0);
	}

	/**
	 * resetWord method, changes the fallig word and also resets position it with the default position and flag that hasn't been caught
	 */
	public synchronized void resetWord() {
		resetPos(); // call resetPos method
		word=dict.getNewWord(); // reset word
		dropped.set(false); // reset it that hasn't been caught 
		fallingSpeed.set((int)(Math.random() * (maxWait.get()-minWait.get())+minWait.get())); // word falling speed
	}
	
	/**
	 * matchWord method to chech dupicate words
	 * @param typedText matches this object word to a word received through the parameter
	 * @return the true or false if typed word matches with any word on the screen panel
	 */
	public synchronized boolean matchWord(String typedText) {
		if (typedText.equals(getWord())) {
			resetWord();
			return true;
		}
		else
			return false;
	}

	/**
	 * sameWord method to check the words if they are the same
	 * @param fallW is a FallingWord object
	 * @return true or false if the words are the same
	 */
	public synchronized boolean sameWord(FallingWord fallW){
		if(fallW.getWord().equals(this.getWord())){
			if(fallW.getY()>this.getY()){
			    fallW.resetWord();
				return true;
			}
			this.resetWord();
			return true;
		}
		return false;
	}

	
	/**
	 * drop method
	 * @param inc the height by which the falling word to be dropped by
	 */
	public synchronized  void drop(int inc) {
		setY(y.get()+inc);
	}

	/**
	 * left method
	 * @param inc the widht by which the x position of the hungry word to be incremented by
	 */
	public synchronized  void left(int inc) {
		setX(x.get()+inc);
	}	
	
	/**
	 * dropped method
	 * @return true of false if the word has been dropped
	 */
	public synchronized  boolean dropped() {
		return dropped.get();
	}

	/**
	 * wordPixelLenght method
	 * @return half the word pixel length
	 */
	public synchronized int wordPixelLenght(){
		Font font = new Font("Arial", Font.PLAIN, 12); // use the same font used in the GUI
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		
		return (int)(font.getStringBounds(word, frc).getWidth())/2;
	}

}
