package typingTutor;


/**
 * WordDictionary class that contains the falling words to be used during the game
 * 
 * @author Shaun 
 * Date created 17/08/22
 * @version 1
 */
public class WordDictionary {
	int size;
	static String [] theDict= {"litchi","banana","apple","mango","pear","orange","strawberry",
		"cherry","lemon","apricot","peach","guava","grape","kiwi","quince","plum","prune",
		"cranberry","blueberry","rhubarb","fruit","grapefruit","kumquat","tomato","berry",
		"boysenberry","loquat","avocado"}; //default dictionary
	
	/**
	 * WordDictionary constructor with one parameter
	 * @param tmp
	 */	
	WordDictionary(String [] tmp) {
		size = tmp.length;
		theDict = new String[size];
		for (int i=0;i<size;i++) {
			theDict[i] = tmp[i];
		}
		
	}
	
	/**
	 * WordDictionary constructor with no parameters, to add the Hungry Word and the initialize the
	 * size of the array when called
	 */
	WordDictionary() {
		size=theDict.length;
	}
	
	/**
	 * getNewWord method to return a new word that is generated through the random method 
	 * by using the number as the index value of the word to be used
	 * @return the new word
	 */
	public synchronized String getNewWord() {
		int wdPos= (int)(Math.random() * size);
		return theDict[wdPos];
	}
	
}
