package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Decrypter {
	
	
	
	
	private LetterFrequency[] letterFrequencies = {
	new LetterFrequency('e', 56.88f),
	new LetterFrequency('a', 43.31f),
	new LetterFrequency('r', 38.64f),
	new LetterFrequency('i', 38.45f),
	new LetterFrequency('o', 36.51f),
	new LetterFrequency('t', 35.43f),
	new LetterFrequency('n', 33.92f),
	new LetterFrequency('s', 29.23f),
	new LetterFrequency('l', 27.98f),
	new LetterFrequency('c', 23.13f),
	new LetterFrequency('u', 18.51f),
	new LetterFrequency('d', 17.25f),
	new LetterFrequency('p', 16.14f),
	new LetterFrequency('m', 15.36f),
	new LetterFrequency('h', 15.31f),
	new LetterFrequency('g', 12.59f),
	new LetterFrequency('b', 10.56f),
	new LetterFrequency('f', 9.24f),
	new LetterFrequency('y', 9.06f),
	new LetterFrequency('w', 6.57f),
	new LetterFrequency('k', 5.61f),
	new LetterFrequency('v', 5.13f),
	new LetterFrequency('x', 1.48f),
	new LetterFrequency('z', 1.39f),
	new LetterFrequency('j', 1.00f),
	new LetterFrequency('q', 1.00f),
	};
	
	public String GetXORCombination(Converter c, String first, String second)
	{
		String finalString = "";
		
		byte[] firstBytes = c.hexToBytes(first);
		byte[] secondBytes = c.hexToBytes(second);

		byte[] finalBytes = new byte[firstBytes.length];
		
		for(int i = 0; i < firstBytes.length; i++)
		{
			finalBytes[i] = (byte) (firstBytes[i] ^ secondBytes[i]);
		}
		
		finalString = c.bytesToHex(finalBytes);
		
		return finalString.toLowerCase();
	}
	
	public String GetXORCombinationWithKey(Converter c, String first, char key)
	{
		String finalString = "";
		
		byte[] firstBytes = c.hexToBytes(first);
		byte keyByte = (byte)key;

		byte[] finalBytes = new byte[firstBytes.length];
		
		for(int i = 0; i < firstBytes.length; i++)
		{
			finalBytes[i] = (byte) (firstBytes[i] ^ keyByte);
		}
		
		finalString = c.bytesToHex(finalBytes);
		finalString = c.hexToAscii(finalString);
		
		return finalString.toLowerCase();
	}
	
	public String GetXORCombinationWithKey(Converter c, byte[] bytes, char key)
	{
		String finalString = "";
		
		byte[] firstBytes = bytes;
		byte keyByte = (byte)key;

		byte[] finalBytes = new byte[firstBytes.length];
		
		for(int i = 0; i < firstBytes.length; i++)
		{
			finalBytes[i] = (byte) (firstBytes[i] ^ keyByte);
		}
		
		finalString = c.bytesToHex(finalBytes);
		finalString = c.hexToAscii(finalString);
		
		return finalString.toLowerCase();
	}
	
	public String GetXORCombinationWithKey(Converter c, String first, char[] key)
	{
		String finalString = "";
		
		byte[] firstBytes = c.hexToBytes(first);
		byte[] keyBytes = new byte[key.length];
		
		for(int i = 0; i < key.length; i++)
		{
			keyBytes[i] = (byte)key[i];
		}

		byte[] finalBytes = new byte[firstBytes.length];
		
		for(int i = 0; i < firstBytes.length; i++)
		{
			finalBytes[i] = (byte) (firstBytes[i] ^ keyBytes[i % keyBytes.length]);
		}
		
		finalString = c.bytesToHex(finalBytes);
		
		return finalString.toLowerCase();
	}
	
	public String GetXORCombinationWithKey(Converter c, byte[] firstBytes, char[] key)
	{
		String finalString = "";
		
		byte[] keyBytes = new byte[key.length];
		
		for(int i = 0; i < key.length; i++)
		{
			keyBytes[i] = (byte)key[i];
		}

		byte[] finalBytes = new byte[firstBytes.length];
		
		for(int i = 0; i < firstBytes.length; i++)
		{
			finalBytes[i] = (byte) (firstBytes[i] ^ keyBytes[i % keyBytes.length]);
		}
		
		finalString = c.hexToAscii(c.bytesToHex(finalBytes));
		
		return finalString.toLowerCase();
	}
	
	
	public String[] findEnglishKey(Converter c, String input)
	{
		ArrayList<Sentence> sentences = new ArrayList<Sentence>();
		
		for(int i = 0; i < 128; i++)
		{
			String result = GetXORCombinationWithKey(c, input, (char)i);
			
			int score = englishSentenceScore(result);
			
			sentences.add(new Sentence(result, score, (char)i));
		}
		
		Collections.sort(sentences, new Sentence());
		
		String[] returnSentences = new String[10];
		
		for(int i = 0; i < 10; i++)
		{
			returnSentences[i] = sentences.get(i).sentence;
		}
		
		return returnSentences;
	}
	
	public Sentence findEnglishKey(Converter c, File input)
	{
		ArrayList<Sentence> sentences = new ArrayList<Sentence>();
		ArrayList<Sentence> returnSentences = new ArrayList<Sentence>();
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			
			for(int i = 0; i < 128; i++)
			{
				String result = GetXORCombinationWithKey(c, line, (char)i);
				
				int score = englishSentenceScore(result);
				
				sentences.add(new Sentence(result, score, (char)i));
			}
			
			Collections.sort(sentences, new Sentence());
			
			for(int i = 0; i < 1; i++)
			{
				returnSentences.add(sentences.get(i));
			}
			sentences.clear();
		}
		
		Collections.sort(returnSentences, new Sentence());
		
		return returnSentences.get(0);
	}
	
	public Sentence[] findEnglishKey(Converter c, byte[] bytes)
	{
		ArrayList<Sentence> sentences = new ArrayList<Sentence>();
		
		for(int i = 0; i < 128; i++)
		{
			String result = GetXORCombinationWithKey(c, bytes, (char)i);
			
			int score = englishSentenceScore(result);
			
			sentences.add(new Sentence(result, score, (char)i));
		}
		
		Collections.sort(sentences, new Sentence());
		
		Sentence[] returnSentences = new Sentence[1];
		
		for(int i = 0; i < 1; i++)
		{
			returnSentences[i] = sentences.get(i);
		}
		
		return returnSentences;
	}
	
	int i = 0;
	
	public int englishSentenceScore(String s)
	{
		int total = 0;
		
		for(char c : s.toCharArray())
		{
			if((int)c > 96 && (int)c < 123)
			{
				for(LetterFrequency l : letterFrequencies)
				{
					if(l.letter == c)
					{
						total += l.index;
					}
				}
			}
			else
			{
				total -= 1;
			}
		}
		
		return total;
	}
	
	private final int KEY_SIZE_ARRAY_SIZE = 5;
	
	public int[] findBestKeySizes(Converter c, File input)
	{
		int[] bestKeys = new int[KEY_SIZE_ARRAY_SIZE];
		
		return bestKeys;
	}
	
	public byte[][] getByteBlocksBasedOnSize(byte[] bytes, int size)
	{
		byte[][] finalBytes = new byte[size][(int) Math.ceil((float)bytes.length/(float)size)];
		
		int i = 0;
		int currentLineIndex = 0;
		for(byte b : bytes)
		{
			if(i >= size)
			{
				i = 0;
				currentLineIndex++;
			}
			
			
			finalBytes[i][currentLineIndex] = b;
			i++;
		}
		
		
		
		return finalBytes;
	}
}

