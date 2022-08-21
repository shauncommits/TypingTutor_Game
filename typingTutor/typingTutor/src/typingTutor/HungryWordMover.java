
package typingTutor;
import java.util.concurrent.atomic.AtomicBoolean;

    

public class HungryWordMover extends Thread {

    public String target = "Hungry Word"; // target word
	static AtomicBoolean done ; //REMOVE
	static AtomicBoolean pause; //REMOVE
    static int thislen1,ylen1,ylen1ad,ythislen1; 
    static int thislen2,ylen2,ylen2ad,ythislen2;
    static int bothLen;
    static int diffPos;
    static int size = TypingTutorApp.noWords;
    static FallingWord[] fall = new FallingWord[size];
	static WordMover[] wordsM = new WordMover[size];
    static Score score;

    public HungryWordMover(FallingWord[] f, WordMover[] w,Score sco){
        fall = f;
        wordsM = w;
        score = sco;
    }
	
	public synchronized void run() {
		while(true){
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if(i==j)
                        continue;
                            if(fall[i].getWord().equals("Hungry Word")){
                                ylen1 = -3 + fall[i].getY();
                                ylen1ad = ylen1-6;
                                ylen2 = 3 + fall[i].getY();

                                ythislen1 = -6 + fall[j].getY();
                                ylen2ad = ylen2 + 6;
                                ythislen2 = 6 + fall[j].getY();
                                if(fall[i].getX()>940){
                                    System.out.println("Was greater than eight hundred!");
                                    fall[i].resetWord();
                                    score.missedWord();
                                }
                                if(fall[i].getY()>25){
                                    if((ythislen1>=ylen2&&ythislen1>=ylen2ad)||(ythislen2>=ylen1&&ythislen2>=ylen1ad)){
                                        bothLen = (target.length()+fall[j].getWord().length())/2;
                                        diffPos = Math.abs(fall[i].getX()-fall[j].getX());
                                        if(bothLen>=diffPos){
                                            score.missedWord();
                                            fall[j].resetWord();
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
