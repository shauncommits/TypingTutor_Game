package typingTutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class FallingWord {
	private String word; // the word
	private AtomicInteger x; //position - width
	private AtomicInteger y; // postion - height
	private AtomicInteger maxY; //maximum height
	private AtomicBoolean dropped; //flag for if user does not manage to catch word in time
	
	private AtomicInteger fallingSpeed; //how fast this word is
	private static AtomicInteger maxWait= new AtomicInteger(1000);
	private static AtomicInteger minWait= new AtomicInteger(100);

	public static WordDictionary dict;
	
	FallingWord() { //constructor with defaults
		word="computer"; // a default - not used
		x= new AtomicInteger(0);
		y= new AtomicInteger(0);	
		maxY=new AtomicInteger(300);
		dropped= new AtomicBoolean(false);
		fallingSpeed= new AtomicInteger((int)(Math.random() * (maxWait.get()-minWait.get())+minWait.get())); 
	}
	
	FallingWord(String text) { 
		this();
		this.word=text;
	}
	
	FallingWord(String text,int x, int maxY) { //most commonly used constructor - sets it all.
		this(text);
		this.x= new AtomicInteger(x); //only need to set x, word is at top of screen at start
		this.maxY= new AtomicInteger(maxY);
	}
	
	public synchronized static void increaseSpeed( ) {
		minWait.set(minWait.intValue()+50);
		maxWait.set(maxWait.get()+50);
	}
	
	public synchronized static void resetSpeed( ) {
		maxWait.set(1000);
		minWait.set(100);
	}
	
	
// all getters and setters must be synchronized
	public synchronized  void setY(int y) {
		if (y>maxY.get()) {
			y=maxY.get();
			dropped.set(true); //user did not manage to catch this word
		}
		this.y.set(y);
	}
	

	public synchronized  void setX(int x) {
		this.x.set(x);
	}
	
	public synchronized  void setWord(String text) {
		this.word=text;
	}

	public synchronized  String getWord() {
		return word;
	}
	
	public synchronized  int getX() {
		return x.get();
	}	
	
	public synchronized  int getY() {
		return y.get();
	}

	public synchronized int getMaxHeight(){
		return maxY.get();
	}

	
	public synchronized  int getSpeed() {
		return fallingSpeed.get();
	}

	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}
	public synchronized void resetPos() {
		setY(0);
	}

	public synchronized void resetWord() {
		resetPos();
		word=dict.getNewWord();
		dropped.set(false);
		fallingSpeed.set((int)(Math.random() * (maxWait.get()-minWait.get())+minWait.get())); 
		//System.out.println(getWord() + " falling speed = " + getSpeed());
	}
	
	public synchronized boolean matchWord(String typedText) {
		//System.out.println("Matching against: "+text);
		if (typedText.equals(getWord())) {
			resetWord();
			return true;
		}
		else
			return false;
	}

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

	

	public synchronized  void drop(int inc) {
		setY(y.get()+inc);
	}

	public synchronized  void left(int inc) {
		setX(x.get()+inc);
	}	
	
	public synchronized  boolean dropped() {
		return dropped.get();
	}


}
