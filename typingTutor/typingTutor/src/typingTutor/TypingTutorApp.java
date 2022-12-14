package typingTutor;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TypingTutorApp class to run all the threads and also the setUp the GUI environment for the game
 * 
 * @author Shaun 
 * Date created 17/08/22
 * @version 1
 */

//model is separate from the view.
public class TypingTutorApp {
//shared class variables
	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static FallingWord[] words; 
	static WordMover[] wrdShft;
	static FallingWord hungryWordDict;
	static BackgroundSound audio;
	static HungryWordMover hungry;
	static CountDownLatch startLatch; //so threads can start at once
	static DuplicateRemover obj;

	static AtomicBoolean started;
	static AtomicBoolean pressedStart;
	static AtomicBoolean pause;  
	static AtomicBoolean done;  
	static AtomicBoolean won; 
	
	static Score score = new Score();
	static GamePanel gameWindow;
	static ScoreUpdater scoreD ;
	static Thread gameWindowThread;
	static Thread scoreThread;
	static AtomicInteger count = new AtomicInteger(0); // count the number of times the user presses the start button

	//the Pause Button
	static JButton pauseB = new JButton("Pause");

	//the QuitGameButton
	static JButton quitB = new JButton("Quit Game");;

	static String audioPath = ""; // the path of the audio file


	/**
	 * setupGUI method 
	 * @param frameX the width of the frame
	 * @param frameY the height of the frame
	 * @param yLimit the y limit on the frame
	 */
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	JFrame frame = new JFrame("Typing Tutor");
		synchronized(frame){
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);

		
    	
		// set the game panel
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 
		// intantiate the GamePanel object
		gameWindow = new GamePanel(words,hungryWordDict,score,yLimit,done,started,won,pressedStart, quitB, pauseB);
		gameWindow.setSize(frameX,yLimit+100);
	    g.add(gameWindow);
	    
		// set the panel for the text
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    JLabel caught =new JLabel("Caught: " + score.getCaught() + "    ");
	    caught.setForeground(Color.blue);
	    JLabel missed =new JLabel("Missed:" + score.getMissed()+ "    ");
	    missed.setForeground(Color.red);
	    JLabel scr =new JLabel("Score:" + score.getScore()+ "    ");   
	    
		// put the text in the text panel
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);
    
	    scoreD = new ScoreUpdater(caught, missed,scr,score,done,won,totalWords);  //thread to update score
       
		// the textField area to reach the word typed by the user
	   final JTextField textEntry = new JTextField("",20);
	   textEntry.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent evt) { 
	    	  //what happens when user pressed enter
	    	  if (!pause.get()) {
	    		  String text = textEntry.getText();
	    		  CatchWord catchThread = new CatchWord(text);
	    		  catchThread.start(); //set 'm running
	    		  textEntry.setText("");
	    		  textEntry.requestFocus();
	    	  }
	    	  else textEntry.setText("");
	      }
	    });
	   
	   // add the text entry to the text panel
	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);
	    
	    JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
        
        //create all the buttons
        //The Start Button
	   	JButton startB = new JButton("Start");;
	    // add the listener to the jbutton to handle the "pressed" event
		synchronized(startB){
		startB.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
				pressedStart.set(false);
				
				textEntry.setEnabled(true); //enables text to be inserted in the text entry
		    	won.set(false); 
		    	done.set(false);
		    	started.set(true);
		    	if (pause.get()) { //this is a restart from pause
		    		pause.set(false);
				}
				else if(count.get()>0){ // checks how many times the start button is pressed during the game, this is an error from the user though. However, it is gracefully handled
					done.set(true); // ends the threads
					pressedStart.set(true); // gracefully inform the user of the error
					count.set(0); // reset count
					pauseB.setEnabled(false);
					quitB.setEnabled(false);
					textEntry.setEnabled(false);
				}
				else { //user quit last game
					pauseB.setEnabled(true);
					quitB.setEnabled(true);
		    		score.reset();
					FallingWord.resetSpeed();
		    		done.set(false);
					startLatch = new CountDownLatch(1); //so threads can start at once
					createWordMoverThreads();   	 //create new threads for next game 
			    	startLatch.countDown(); //set wordMovers going - must have barrier[]
					count.getAndIncrement();
		    	}
		    	textEntry.requestFocus();
				//done.set(true);
				
		      }
			  
		});
	}//finish addActionListener
			
	   
		// add the listener to the jbutton to handle the "pressed" event
		synchronized(pauseB){
		pauseB.addActionListener(new ActionListener(){
				   public void actionPerformed(ActionEvent e){
					   	pause.set(true);  // signal pause

						textEntry.setEnabled(false); //disables the text entry from being used when the game is paused

					   	done.set(false); //double check for safety
				      }
	    });} //finish addActionListener
		
		
	    // add the listener to the jbutton to handle the "pressed" event
		synchronized(quitB){
		quitB.addActionListener(new ActionListener() {
				  public void actionPerformed(ActionEvent e) {
					  done.set(true);  // signal stop
					  pause.set(false); //set for safety
					  gameWindow.repaint();
					
					 	pauseB.setEnabled(false);
						textEntry.setEnabled(false);
						count.set(0);

						 //word movers waiting on starting line
					   	for (int i=0;i<noWords;i++) {
					     		try {
					     			if (wrdShft[i].isAlive())	{
									wrdShft[i].join();}
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
					    }
				}
		});}  //finish addActionListener
					
		//the Exit Button
		JButton endB = new JButton("Exit");;
				// add the listener to the jbutton to handle the "pressed" event
				synchronized(endB){
				endB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
			    	  System.exit(0);
			      }
			    });}
	    
       //add all the buttons
		b.add(startB);
		b.add(pauseB);
		b.add(quitB);
		b.add(endB);
		g.add(b);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
       	//frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);
	}}
	
	/**
	 * createThreads method to create and start the Threads
	 */
	public static synchronized void createThreads() {
		score.reset();
        //main Display Thread 
      	gameWindowThread = new Thread(gameWindow);  //updating panel
        //scoreThread - for updating score
      	scoreThread = new Thread(scoreD);  
       	scoreThread.start();
    	gameWindowThread.start();
		
    	createWordMoverThreads();

		obj = new DuplicateRemover(words,hungryWordDict,wrdShft,score);
		obj.start();

		// get the absolute path for the audio file from any machine
		File file = new File("typingTutor/typingTutor/src/typingTutor/both-of-us-14037.wav");
		audioPath = file.getAbsolutePath();

		// Instantiate and start the BackgroundSound Thread
		audio = new BackgroundSound(audioPath);
		audio.start();
	}
	
	/**
	 * createWordMoverThreads creates the WordMoverThreads
	 */
	public static synchronized void createWordMoverThreads() {
		score.reset();
	  	//initialize shared array of current words with the words for this game
		for (int i=0;i<noWords;i++) {
			words[i]=new FallingWord(dict.getNewWord(),gameWindow.getValidXpos(),yLimit);
		}
		//create threads to move them
	    for (int i=0;i<noWords;i++) {
	    		wrdShft[i] = new WordMover(words[i],dict,score,startLatch,done,pause);
	    }
        //word movers waiting on starting line
     	for (int i=0;i<noWords;i++) {
     		wrdShft[i] .start();
     	}

		 hungry = new HungryWordMover(words,hungryWordDict,dict,score,startLatch,done,pause);
		 hungry.start();
	}
	
/**
 * getDictFromFile
 * @param filename the file name containing the dictionay
 * @return the dictionary of the file
 */
public static synchronized String[] getDictFromFile(String filename) {
	//read in the list of words.
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt(); //file starts with number of words in file
			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;
	}

/**
 * Main method to run the programs 
 * @param args take any number of arguments
 */
public static void main(String[] args) {
		// initialize the atomic variables
		started=new AtomicBoolean(false);
		done = new AtomicBoolean(false);
		pause = new AtomicBoolean(false);
		won = new AtomicBoolean(false);
		pressedStart = new AtomicBoolean(false);
		
		totalWords=24;
		noWords=6;
		dict= new WordDictionary();
		
		//deal with command line arguments
		if (args.length==2) {
					totalWords=Integer.parseInt(args[0]);  //total words to fall
					noWords=Integer.parseInt(args[1]); // total words falling at any point
					assert(totalWords>=noWords); // 
		} else if (args.length==3) {
					totalWords=Integer.parseInt(args[0]);  //total words to fall
					noWords=Integer.parseInt(args[1]); // total words falling at any point
					assert(totalWords>=noWords); // 
					String[] tmpDict=getDictFromFile(args[2]); //file of words
					if (tmpDict!=null)
						dict= new WordDictionary(tmpDict);
		}
				
		FallingWord.dict=dict; //set the class dictionary for the words.
		
		words = new FallingWord[noWords];  //array for the  current chosen words from dict
		wrdShft = new WordMover[noWords]; //array for the threads that animate the words
		hungryWordDict = new FallingWord(dict.getNewWord(),0,yLimit);
		
		CatchWord.setWords(words,hungryWordDict);  //class setter - static method
		CatchWord.setScore(score);  //class setter - static method
		CatchWord.setFlags(done,pause); //class setter - static method

		setupGUI(frameX, frameY, yLimit);  
	
 		startLatch = new CountDownLatch(1); //REMOVE so threads can start at once
    	createThreads();
       	}
	}

