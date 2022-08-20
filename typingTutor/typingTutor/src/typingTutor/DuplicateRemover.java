package typingTutor;

public class DuplicateRemover extends Thread{
    
    static int size = TypingTutorApp.noWords;
    static FallingWord[] fall = new FallingWord[size];
	static WordMover[] wordsM = new WordMover[size];
    
	
    public DuplicateRemover(FallingWord[] f, WordMover[] w){
        fall = f;
        wordsM = w;
    }
	public void run() {
		while (true) {	
           for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(i==j)
                    continue;
                    fall[i].sameWord((fall[j]));    
            }	 
           }   
		}
	}	
}
