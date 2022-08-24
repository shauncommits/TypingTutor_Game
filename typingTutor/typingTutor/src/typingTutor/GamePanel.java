package typingTutor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import javax.swing.JLabel;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JPanel;

/**
 * GamePanel Thread to draw the GUI panel
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
		 * signal when the game has started
		 */
		private AtomicBoolean started ;
		/**
		 * signal when the game has been won
		 */
		private AtomicBoolean won ;

		/**
		 * the JLable Hungry Word to color it green later 
		 */
		JLabel greenWord = new JLabel("Hungry Word");
		/**
		 * Falling words array
		 */
		private FallingWord[] words;
		/**
		 * Number of words
		 */
		private int noWords;
		private final static int borderWidth=25; //appearance - border

		/**
		 * GamePanel constructor
		 * @param words // FallingWords object
		 * @param maxY // max height of the falling word to be used
		 * @param d // signal if the game is done
		 * @param s // signal if the game has started
		 * @param w // signal if the game has been won
		 */
		GamePanel(FallingWord[] words, int maxY,	
				 AtomicBoolean d, AtomicBoolean s, AtomicBoolean w) {
			this.words=words; //shared word list
			noWords = words.length; //only need to do this once
			done=d; //REMOVE
			started=s; //REMOVE
			won=w; //REMOVE
		}
		
	
		public synchronized void paintComponent(Graphics g) {
		    int width = getWidth(); // fix the width of the drawing board
		    int height = getHeight()-borderWidth*2; // height for the lower bar 
		    g.clearRect(0,0,width+50,height+50);//the active space
		    g.setColor(Color.pink); //change colour of pen
		    g.fillRect(borderWidth,height,width-50,borderWidth); //draw danger zone

		    g.setColor(Color.black);
		    g.setFont(new Font("Arial", Font.PLAIN, 26));
		   //draw the words with the following style
		    if (!started.get()) {
		    	g.setFont(new Font("Arial", Font.BOLD, 26));
				g.drawString("Type all the words before they hit the red zone,press enter after each one.",borderWidth*2,height/2);	
		    	
		    }
		    else if (!done.get()) { // run this section of code if the game has not finished
		    	for (int i=0;i<noWords;i++){
					
					if(words[i].getWord().equals("Hungry Word")){ // check to see the generated word is Hungry Word
						synchronized(this){
						g.setColor(Color.GREEN); // changes the word to green
						g.drawString(greenWord.getText(),words[i].getX()+borderWidth,words[i].getY());} // draw the green word
						g.setColor(Color.black); // changes the graphic pen to black again
					}
					else
						g.drawString(words[i].getWord(),words[i].getX()+borderWidth,words[i].getY()); // draw the word if is not Hungry Word as it is

		    			
		    	}
		    	g.setColor(Color.lightGray); //change colour of pen
		    	g.fillRect(borderWidth,0,width-50,borderWidth);
		   }
		   else { if (won.get()) { // what to be displayed if the game is won
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.drawString("Well done!",width/3,height/2);	
		   } else { // what to be displayed if the game is over or the user when presses quit
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.drawString("Game over!",width/2,height/2);	
		   }
		   }
		}
		
		/**
		 * getValidXpos method 
		 * @return the valid x posotion for the word to fit inside the drawind pane
		 */
		public synchronized int getValidXpos() {
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


