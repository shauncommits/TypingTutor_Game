package typingTutor;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * WordDictionary class that contains the falling words to be used during the game
 * 
 * @author Shaun 
 * Date created 17/08/22
 * @version 1
 */

public class WordDictionary {
	int size; // the size of the dictionary
	static String [] theArr = {"litchi","banana","apple","mango","pear","orange","strawberry",
		"cherry","lemon","apricot","peach","guava","grape","kiwi","quince","plum","prune",
		"cranberry","blueberry","rhubarb","fruit","grapefruit","kumquat","tomato","berry",
		"boysenberry","loquat","avocado"}; //default dictionary

	List<String> theDict = new ArrayList<String>(); // an array to transfer the dictionary elements to
	
	/**
	 * add method to add the words from the array to the array list theDict
	 */
	public void add(){ 
		Collections.addAll(theDict, theArr); // Collections class to add the array to the array list 
		// adds the Hungry Word if is not in the dictionary
		if(!theDict.contains("Hungry Word")) 
			theDict.add("Hungry Word");
	}

	/**
	 * WordDictionary constructor with one parameter
	 * @param tmp
	 */	
	WordDictionary(String [] tmp) {
		add(); // call the method
		size = tmp.length; // lenght of the array
		theArr = new String[size];
		for (int i=0;i<size;i++) { // copy the reference of the parameter array
			theArr[i] = tmp[i];
		}
		
		
	}
	/**
	 * WordDictionary constructor with no parameters, to add the Hungry Word and the initialize the
	 * size of the array when called
	 */
	WordDictionary() {
		add();
		size=theDict.size();
	}
	
	/**
	 * getNewWord method to return a new word that is generated through the random method 
	 * by using the number as the index value of the word to be used
	 * @return the new word
	 */
	public synchronized String getNewWord() {
		int wdPos= (int)(Math.random() * size);
		return theDict.get(wdPos);
	}
	
}
