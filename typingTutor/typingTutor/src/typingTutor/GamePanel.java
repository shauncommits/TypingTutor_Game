package typingTutor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import javax.swing.JLabel;

//import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
		private AtomicBoolean done ; //REMOVE
		private AtomicBoolean started ; //REMOVE
		private AtomicBoolean won ; //REMOVE

		// public static final String colReset = "\u001B[0m";
		// public static final String col = "\u001B[32m";
		JLabel greenWord = new JLabel("Hungry Word");

		private FallingWord[] words;
		private int noWords;
		private final static int borderWidth=25; //appearance - border

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
		    int height = getHeight()-borderWidth*2;
		    g.clearRect(0,0,width+50,height);//the active space
		    g.setColor(Color.pink); //change colour of pen
		    g.fillRect(borderWidth,height,width-50,borderWidth); //draw danger zone

		    g.setColor(Color.black);
		    g.setFont(new Font("Arial", Font.PLAIN, 26));
		   //draw the words
		    if (!started.get()) {
		    	g.setFont(new Font("Arial", Font.BOLD, 26));
				g.drawString("Type all the words before they hit the red zone,press enter after each one.",borderWidth*2,height/2);	
		    	
		    }
		    else if (!done.get()) {
		    	for (int i=0;i<noWords;i++){
					
					if(words[i].getWord().equals("Hungry Word")){
						synchronized(this){
						g.setColor(Color.GREEN);
						g.drawString(greenWord.getText(),words[i].getX()+borderWidth,words[i].getY());}
						g.setColor(Color.black);
					}
					else
						g.drawString(words[i].getWord(),words[i].getX()+borderWidth,words[i].getY());

		    			
		    	}
		    	g.setColor(Color.lightGray); //change colour of pen
		    	g.fillRect(borderWidth,0,width-50,borderWidth);
		   }
		   else { if (won.get()) {
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.drawString("Well done!",width/3,height/2);	
		   } else {
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.drawString("Game over!",width/2,height/2);	
		   }
		   }
		}
		
		public synchronized int getValidXpos() {
			int width = getWidth()-borderWidth*6;
			int x= (int)(Math.random() * width);

			return x;
		}
		
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(10); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
		}

	}


