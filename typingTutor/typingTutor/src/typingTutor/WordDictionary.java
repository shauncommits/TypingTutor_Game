package typingTutor;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;



public class WordDictionary {
	int size;
	static String [] theArr = {"litchi","banana","apple","mango","pear","orange","strawberry",
		"cherry","lemon","apricot","peach","guava","grape","kiwi","quince","plum","prune",
		"cranberry","blueberry","rhubarb","fruit","grapefruit","kumquat","tomato","berry",
		"boysenberry","loquat","avocado"}; //default dictionary

	List<String> theDict = new ArrayList<String>();
	public void add(){
		Collections.addAll(theDict, theArr);
		if(!theDict.contains("Hungry Word"))
			theDict.add("Hungry Word");
	}

	
	WordDictionary(String [] tmp) {
		add();
		size = tmp.length;
		theArr = new String[size];
		for (int i=0;i<size;i++) {
			theArr[i] = tmp[i];
		}
		
		
	}
	
	WordDictionary() {
		add();
		size=theDict.size();
	}
	
	public synchronized String getNewWord() {
		int wdPos= (int)(Math.random() * size);
		return theDict.get(wdPos);
	}
	
}
