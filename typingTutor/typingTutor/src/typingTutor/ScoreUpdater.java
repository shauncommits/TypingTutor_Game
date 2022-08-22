package typingTutor;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JLabel;

/**
 * @author Shaun 
 * Date created 17/08/22
 * @version 1
 */

public class ScoreUpdater  implements Runnable {
	private Score score;
	JLabel caught;
	JLabel missed;
	JLabel scoreView;
	private AtomicBoolean done;
	private AtomicBoolean won;
	private int maxWords;

	/**
	 * ScoreUpdate constructor
	 * @param c the JLable of the number of words caught for updating the GUI
	 * @param m the JLable of the number of words missed for updating the GUI
	 * @param s the JLable of the number of the score accumulated to update the score on the GUI
	 * @param score the Score object
	 * @param d done signal 
	 * @param w won game signal
	 * @param max maximum number of words 
	 */
	ScoreUpdater(JLabel c, JLabel m, JLabel s, Score score, 
			AtomicBoolean d, AtomicBoolean w, int max) {
        this.caught=c;
        missed = m;
        scoreView = s;
        this.score=score;
        done=d;
         won=w;
        maxWords=max;
    }
	

	public synchronized void run() {
        while (true) {  
				synchronized(caught){ // display the number of caught words
                caught.setText("Caught: " + score.getCaught() + "    ");}
				synchronized(missed){ // display the number of missed words
                missed.setText("Missed:" +  score.getMissed()+ "    " );}
				synchronized(scoreView){ // display the score update on the GUI
                scoreView.setText("Score:" + score.getScore()+ "    " );  //setText is thread safe (I think)
				}
				if ((score.getMissed())>=3) {
					synchronized(caught){  	
					   caught.setText("Caught: " + score.getCaught() + "    ");}
					   synchronized(missed){
					   missed.setText("Missed:" +  score.getMissed()+ "    " );}
					   synchronized(scoreView){
					   scoreView.setText("Score:" + score.getScore()+ "    " );} //setText is thread safe (I think)
					   done.set(true); //game ends when missed 3
					   won.set(false);
				} else if (score.getCaught()>=maxWords) {
					   done.set(true); //game ends when missed 3
					   won.set(true);
					   synchronized(caught){
		               caught.setText("Caught: " + score.getCaught() + "    ");}
					   synchronized(missed){
		               missed.setText("Missed:" +  score.getMissed()+ "    " );}
					   synchronized(scoreView){
		               scoreView.setText("Score:" + score.getScore()+ "    " );}
				}

        }
    }
}
