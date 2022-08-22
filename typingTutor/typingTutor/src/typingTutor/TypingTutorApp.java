package typingTutor;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
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
	static DuplicateRemover obj;
	static HungryWordMover hungry;
	static CountDownLatch startLatch; //so threads can start at once
	
	static AtomicBoolean started;  
	static AtomicBoolean pause;  
	static AtomicBoolean done;  
	static AtomicBoolean won; 
	
	static Score score = new Score();
	static GamePanel gameWindow;
	static ScoreUpdater scoreD ;
	
	static Thread gameWindowThread;
	static Thread scoreThread;
	
	/**
	 * setupGUI method 
	 * @param frameX the width of the frame
	 * @param frameY the height of the frame
	 * @param yLimit the y limit on the frame
	 */
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	JFrame frame = new JFrame("Typing Tutor"); 
		synchronized(frame){ // synchronizes the frame object
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
      	JPanel g = new JPanel(); 
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
		
		// set the game panel
		gameWindow = new GamePanel(words,yLimit,done,started,won); 
		gameWindow.setSize(frameX,yLimit+100);
	    g.add(gameWindow);
	    
		// set the panel that display the game results
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    JLabel caught =new JLabel("Caught: " + score.getCaught() + "    "); 
	    caught.setForeground(Color.blue);
	    JLabel missed =new JLabel("Missed:" + score.getMissed()+ "    ");
	    missed.setForeground(Color.red);
	    JLabel scr =new JLabel("Score:" + score.getScore()+ "    ");   
	    
		// put the labels in the text panel
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);
    
	    scoreD = new ScoreUpdater(caught, missed,scr,score,done,won,totalWords);      //thread to update score
        
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
		    public synchronized void actionPerformed(ActionEvent e) {
				
				
		    	won.set(false);
		    	done.set(false);
		    	started.set(true);
		    	if (pause.get()) { //this is a restart from pause
		    		pause.set(false);
		    	} else { //user quit last game
		    		score.reset();
					FallingWord.resetSpeed();
		    		done.set(false);
					startLatch = new CountDownLatch(1); //so threads can start at once
					createWordMoverThreads();   	 //create new threads for next game 
			    	startLatch.countDown(); //set wordMovers going - must have barrier[]
				
				}
		    	textEntry.requestFocus();
		      }
		});}//finish addActionListener
			
	   //the Pause Button
		JButton pauseB = new JButton("Pause");;
		// add the listener to the jbutton to handle the "pressed" event
		synchronized(pauseB){
		pauseB.addActionListener(new ActionListener(){
				   public void actionPerformed(ActionEvent e){
					   	pause.set(true);  // signal pause
					   	done.set(false); //double check for safety
				      }
	    });} //finish addActionListener
		
		//the QuitGameButton
	     JButton quitB = new JButton("Quit Game");;
	    // add the listener to the jbutton to handle the "pressed" event
		synchronized(quitB){
		quitB.addActionListener(new ActionListener() {
				  public void actionPerformed(ActionEvent e) {
					  done.set(true);  // signal stop
					  pause.set(false); //set for safety
					  gameWindow.repaint();
					 //word movers waiting on starting line
					   	for (int i=0;i<noWords;i++) {
					     		try {
					     			if (wrdShft[i].isAlive())	{
									wrdShft[i].join();}
								} catch (InterruptedException e1) {
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
		obj = new DuplicateRemover(words,wrdShft,score);
		obj.start();
		hungry = new HungryWordMover(words, wrdShft,score);
		hungry.start();
		
	}
	
	/**
	 * createWordMoverThreads creates the WordMoverThreads
	 */
	public static synchronized void createWordMoverThreads() {
		score.reset();
	  	//initialize shared array of current words with the words for this game
		for (int i=0;i<noWords;i++) {
			words[i]=new FallingWord(dict.getNewWord(),gameWindow.getValidXpos(),yLimit);
			//wordList.add(words[i].getWord());
		}
		//create threads to move them
	    for (int i=0;i<noWords;i++) {
	    		wrdShft[i] = new WordMover(words[i],dict,score,startLatch,done,pause);
	    }
        //word movers waiting on starting line
     	for (int i=0;i<noWords;i++) {
     		wrdShft[i] .start();
     	}

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
		started=new AtomicBoolean(false);
		done = new AtomicBoolean(false);
		pause = new AtomicBoolean(false);
		won = new AtomicBoolean(false);
		
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
		
		CatchWord.setWords(words);  //class setter - static method
		CatchWord.setScore(score);  //class setter - static method
		CatchWord.setFlags(done,pause); //class setter - static method
		
		//HungryWordMover.setFlags(done, pause);
		//HungryWordMover.setScore(score);

		setupGUI(frameX, frameY, yLimit);  
	
 		startLatch = new CountDownLatch(1); //REMOVE so threads can start at once
    	createThreads();
       	}
	}

