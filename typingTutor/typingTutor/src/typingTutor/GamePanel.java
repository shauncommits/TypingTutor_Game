package typingTutor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * GamePanel Thread to draw the GUI panel
 * 
 * @author Shaun 
 * Date created 17/08/22
 * @version 1
 */
public class GamePanel extends JPanel implements Runnable {
		/**
		 * signal when the game is finished
		 */
		private AtomicBoolean done ;
		/**
		 * signal when the game is pressed again after the press start was pressed before
		 */
		private AtomicBoolean pressedStart;
		/**
		 * signal when the game has started
		 */
		private AtomicBoolean started ;
		/**
		 * signal when the game has been won
		 */
		private AtomicBoolean won ;
		/**
		 * Falling words array
		 */
		private FallingWord[] words;
		/**
		 * Falling hungry word object
		 */
		private FallingWord hungryWord;
		/**
		 * Number of words
		 */
		private int noWords;
		private final static int borderWidth=25; //appearance - border
		/**
		 * The score boolean
		 */
		private Score score;
		/**
		 * The pause button
		 */
		private JButton paus;
		/**
		 * the quit button
		 */
		private JButton quit;

		/**
		 * GamePanel constructor
		 * @param words FallingWords array object
		 * @param hung FallingWord object for the hungry word
		 * @param maxY max height of the falling word to be used
		 * @param d signal if the game is done
		 * @param s signal if the game has started
		 * @param w signal if the game has been won
		 */
		GamePanel(FallingWord[] words, FallingWord hung, Score score, int maxY,	
			AtomicBoolean d, AtomicBoolean s, AtomicBoolean w,AtomicBoolean ps, JButton p, JButton q) {
			this.words=words; //shared word list
			hungryWord = hung;
			noWords = words.length; //only need to do this once
			done=d; 
			started=s; 
			won=w; 
			this.score = score;
			pressedStart = ps;
			paus = p;
			quit = q;
		}
		
		public void paintComponent(Graphics g) {
		    int width = getWidth(); 
		    int height = getHeight()-borderWidth*2;
		    g.clearRect(0,0,width+50,height+50);//the active space
		    g.setColor(Color.pink); //change colour of pen
		    g.fillRect(borderWidth,height,width-50,borderWidth); //draw danger zone

		    g.setColor(Color.black);
		    g.setFont(new Font("Arial", Font.PLAIN, 26));
			
		   //draw the words
		    if (!started.get()) {
		    	g.setFont(new Font("Arial", Font.BOLD, 26));
				g.drawString("Type all the words before they hit the red zone,press enter after each one.",borderWidth*2,height/2);	
		    	paus.setEnabled(false);
				quit.setEnabled(false);
		    }
		    else if (!done.get()) {
		    	for (int i=0;i<noWords;i++){	    	
		    		g.drawString(words[i].getWord(),words[i].getX()+borderWidth,words[i].getY());	
					
		    	}
				
				g.setColor(Color.green); //change colour of pen
				g.drawString(hungryWord.getWord(), hungryWord.getX()+borderWidth,hungryWord.getY());
				
				
				//g.setColor(Color.);
				if(score.getMissed()==2){
					g.setColor(Color.red); //change colour of pen
		    		g.fillRect(borderWidth,0,width-50,borderWidth);
				}
				else{
					g.setColor(Color.lightGray); //change colour of pen
		    		g.fillRect(borderWidth,0,width-50,borderWidth);
				}
				
				
				
		   }
		   else { if (won.get()) {
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.drawString("Well done!",width/3,height/2);	
			
		   } 
		   else if(pressedStart.get()){
				g.setFont(new Font("Arial", Font.BOLD, 24));
				g.drawString("You pressed the start button again while the game already started!",borderWidth,height/4);
				g.drawString("Next time if you want to quit the game, just press the Quit Game button!",borderWidth,height/3);
				g.drawString("You can press the start button again if you want to restart the game or",borderWidth,height/2);
				g.drawString("the exit button to exit the Game!",borderWidth,height/2+40);
		   }
		   else {
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.drawString("Game over!",width/2,height/2);	
		   }
		   }
		}
		
		/**
		 * getValidXpos method 
		 * @return the valid x posotion for the word to fit inside the drawind pane
		 */
		public int getValidXpos() {
			int width = getWidth()-borderWidth*10;
			int x= (int)(Math.random() * width);
			return x;
		}
		
		public void run() {
			while (true) { //does the pianting as long as the Thread hasn't terminated yet
				repaint();
				try {
					Thread.sleep(10); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
		}

	}


