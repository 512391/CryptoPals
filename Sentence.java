package Main;

import java.util.Comparator;

public class Sentence implements Comparator<Sentence>{
	public String sentence;
	public int score;
	public char key;
	
	public Sentence(String sen, int sco, char key)
	{
		sentence = sen;
		score = sco;
		this.key = key;
	}
	
	public void printSentence()
	{
		//System.out.println(sentence);
		System.out.println("Score: " + Integer.toString(score) +  " Key: " + Character.toString(key));
	}
	
	public Sentence()
	{
		
	}

	@Override
	public int compare(Sentence o1, Sentence o2) {
		return o2.score-o1.score;
	}
}
