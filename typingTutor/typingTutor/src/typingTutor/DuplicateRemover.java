package typingTutor;

public class DuplicateRemover extends Thread{
    
    static int size = TypingTutorApp.noWords;
    static FallingWord[] fall = new FallingWord[size];
	static WordMover[] wordsM = new WordMover[size];
    Score sco;
    
	
    public DuplicateRemover(FallingWord[] f, WordMover[] w,Score score){
        fall = f;
        wordsM = w;
        sco = score;
    }
	public void run() {
		while (true) {	
           for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(i==j)
                    continue;
                    if(fall[i].sameWord((fall[j])));
            }	 
           }   
		}
	}	
}
