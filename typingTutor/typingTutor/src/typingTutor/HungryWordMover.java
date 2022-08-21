
package typingTutor;
import java.util.concurrent.atomic.AtomicBoolean;

    

public class HungryWordMover extends Thread {
    public String target = "Hungry Word";
	static AtomicBoolean done ; //REMOVE
	static AtomicBoolean pause; //REMOVE
    static int len1;
    static int len2;
    static int thislen1;
    static int thislen2; 
    static int ylen1;
    static int ylen2;
    static int ylen1ad;
    static int ylen2ad;
    static int ythislen1;
    static int ythislen2; 
    static int bothLen;
    static int diffPos;
    static int size = TypingTutorApp.noWords;
    static FallingWord[] fall = new FallingWord[size];
	static WordMover[] wordsM = new WordMover[size];
    static Score sco;

    public HungryWordMover(FallingWord[] f, WordMover[] w){
        fall = f;
        wordsM = w;
        ///sco = score;
    }
	
	// public static void setFlags(AtomicBoolean d, AtomicBoolean p) {
	// 	done=d;
	// 	pause=p;
	// }
	
	public void run() {
		while(true){
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if(i==j)
                        continue;
                            if(fall[i].getWord().equals("Hungry Word")){
                                ylen1 = -3 + fall[i].getY();
                                ylen1ad = ylen1-6;
                                ylen2 = 3 + fall[i].getY();
                                ylen2ad = ylen2 + 6;
                                ythislen1 = -6 + fall[j].getY();
                                ythislen2 = 6 + fall[j].getY();
                               // System.out.println("HUngry word");
                                if(fall[i].getY()>12){
                                if((ythislen1>=ylen2&&ythislen1>=ylen2ad)||(ythislen2>=ylen1&&ythislen2>=ylen1ad)){
                                    //System.out.println("It is hear kodwa");
                                    // sco.missedWord(); 
                                    // fall[j].resetWord();
                                    // break;
                                   
                                    bothLen = (target.length()+fall[j].getWord().length());
                                    diffPos = Math.abs(fall[i].getX()-fall[j].getX());
                                        // len1 = -target.length()/2 + fall[i].getX();
                                        // len2 = len1 + fall[i].getWord().length();
                                        // thislen1 = -fall[j].getWord().length()/2 + fall[j].getX();
                                        // thislen2 = thislen1 + fall[j].getWord().length();
                                        if(bothLen<diffPos){
                                             
                                    System.out.println("Green Word width "+fall[i].getX());
                                    System.out.println(fall[j].getWord()+" Word width "+fall[j].getX());
                                    fall[j].resetWord();
                                    bothLen = 1000;

                                            // System.out.println("Green Word height "+fall[i].getY()+" width "+fall[i].getX());
                                            // System.out.println(fall[j].getWord()+" Word height "+fall[j].getY()+" width "+fall[j].getX());

                                            //fall[j].drop(300);
                                        }
                                        
                                     
                                        

                                } }
                            } 
                }	 
               }
        }
	}	
    
}
