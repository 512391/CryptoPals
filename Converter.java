package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HexFormat;
import java.util.Scanner;

public class Converter {

	private static File inputFile4 = new File("C:\\Users\\512391\\eclipse-workspace\\CryptoPals1\\src\\Main\\input4.txt");
	private static File inputFile6 = new File("C:\\Users\\512391\\eclipse-workspace\\CryptoPals1\\src\\Main\\input6.txt");
	
	public static void main(String[] args) {
		Converter c = new Converter();
		Decrypter d = new Decrypter();
		
		//Challenge 1
		System.out.println("Challenge 1");
		System.out.println(c.hexToBase64("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"));
		
		//Challenge 2
		System.out.println("Challenge 2");
		System.out.println(d.GetXORCombination(c, "1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965"));
		
		//Challenge 3
		System.out.println("Challenge 3");
		//the 2 is pretty arbitraty it just happened to be the one that was the sentence but it is an array so you can see all of them
		System.out.println(d.findEnglishKey(c, "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")[2]);
		
		//Challenge 4
		System.out.println("Challenge 4");
		Sentence s = d.findEnglishKey(c, inputFile4);
		System.out.println(s.sentence + "key: " + s.key);
		
		//Challenge 5
		System.out.println("Challenge 5");
		String firstSentence = "Burning 'em, if you ain't quick and nimble ";
		String secondSentence = "I go crazy when I hear a cymbal";
		System.out.println(d.GetXORCombinationWithKey(c, asciiToHex(firstSentence + secondSentence), "ICE".toCharArray()));
		
		//Challenge 6
		System.out.println("Challenge 6");
		c.challengeSix(c, d);
	}
	
	private void challengeSix(Converter c, Decrypter d)
	{
		HammingDistance[] distances = c.findSmallestHammingDistances(c.getBytesFromFile(inputFile6, c.BYTE_RETURN_ARRAY_SIZE, false));
		String key = "";
		byte[] fileBytes = c.getBytesFromFile(inputFile6, 2876, true);
		for(HammingDistance dist : distances)
		{
			byte[][] bytes = d.getByteBlocksBasedOnSize(fileBytes, (int)dist.keyLength);
			
			for(byte[] b : bytes)
			{
				for(Sentence sen : d.findEnglishKey(c, b))
				{
					key+= sen.key;
				}
			}
			System.out.println("key: "+key);
		}
		
		System.out.println(d.GetXORCombinationWithKey(c, fileBytes, key.toCharArray()));
	}
	
	private final int BYTE_RETURN_ARRAY_SIZE = 160;
	
	private byte[] getBytesFromFile(File f, int returnArraySize, boolean getAllBytes)
	{
		byte[] returnArray = new byte[returnArraySize];
		
		String file = "";
		
		try {
			Scanner s = new Scanner(f);
			
			while(s.hasNextLine())
			{
				String line = s.nextLine();
				
				file += line;
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int j = 0;
		byte[] bytes = base64ToBytes(file);
		
		
		if(getAllBytes)
		{
			return bytes;
		}
		
		for(byte b : bytes)
		{
			if(j > returnArraySize-1)
			{
				return returnArray;
			}
			returnArray[j] = b;
			j++;
		}
		
		
		return returnArray;
	}

	public String hexToBase64(String hex)
	{
        byte[] bytes = HexFormat.of().parseHex(hex);
        String base64 = Base64.getEncoder().encodeToString(bytes);

        return base64;
	}
	
	private static String asciiToHex(String asciiStr) {
	    char[] chars = asciiStr.toCharArray();
	    StringBuilder hex = new StringBuilder();
	    for (char ch : chars) {
	        hex.append(Integer.toHexString((int) ch));
	    }

	    return hex.toString();
	}
	
	public byte[] hexToBytes(String hex)
	{
		return HexFormat.of().parseHex(hex);
	}
	
	public byte[] base64ToBytes(String base64)
	{
		return Base64.getDecoder().decode(base64);
	}
	
	public String bytesToHex(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		
		for (byte b : bytes) {
		    sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
	
	public String bytesToBase64(byte[] bytes)
	{
		return new String(Base64.getDecoder().decode(bytes));
	}
	
	public String hexToAscii(String hexStr) {
	    StringBuilder output = new StringBuilder("");
	    
	    for (int i = 0; i < hexStr.length(); i += 2) {
	        String str = hexStr.substring(i, i + 2);
	        output.append((char) Integer.parseInt(str, 16));
	    }
	    
	    return output.toString();
	}
	
	private final int MAX_KEY_LENGTH = 40;
	private final int MIN_KEY_LENGTH = 2;
	private final int HAMMING_RETURN_ARRAY_SIZE = 1;
	
	public HammingDistance[] findSmallestHammingDistances(byte[] bytes)
	{
		ArrayList<HammingDistance> hammingDistances = new ArrayList<>();
		
		for(int i = MIN_KEY_LENGTH; i <= MAX_KEY_LENGTH; i++)
		{
			byte[][] byteArrays = getSubByteArray(bytes, i);
			
			float distance = 0;
				distance += (getHammingDistance(byteArrays[0], byteArrays[1])/i);
				distance += (getHammingDistance(byteArrays[0], byteArrays[2])/i);
				distance += (getHammingDistance(byteArrays[0], byteArrays[3])/i);
				distance += (getHammingDistance(byteArrays[1], byteArrays[2])/i);
				distance += (getHammingDistance(byteArrays[1], byteArrays[3])/i);
				distance += (getHammingDistance(byteArrays[2], byteArrays[3])/i);
			
			hammingDistances.add(new HammingDistance(distance, i));
		}
		
		Collections.sort(hammingDistances, new HammingDistance());
		
		HammingDistance[] returnArray = new HammingDistance[HAMMING_RETURN_ARRAY_SIZE];
		
		for(int i = 0; i < HAMMING_RETURN_ARRAY_SIZE; i++)
		{
			returnArray[i] = hammingDistances.get(hammingDistances.size() - i - 1);
		}
		
		return returnArray;
	}
	
	private int getHammingDistance(byte[] first, byte[] second)
	{
		int hammingDistance = 0;
		
		char[] firstBinary = BytesToBinary(first).toCharArray();
		char[] secondBinary = BytesToBinary(second).toCharArray();
		for(int i = 0; i < secondBinary.length; i++)
		{
			try
			{
				if(firstBinary[i] != secondBinary[i])
				{
					hammingDistance++;
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				return hammingDistance;
			}
		}
		return hammingDistance;
	}
	
	private byte[][] getSubByteArray(byte[] array, int size)
	{
		byte[][] subArray = new byte[(int)Math.ceil((float)array.length/(float)size)][size];
		
		int j = 0;
		int x = 0;
		for(byte b : array)
		{
			if(j >= size)
			{
				j = 0;
				x++;
			}
			
			subArray[x][j] = b;
			
			j++;
		}
		
		return subArray;
	}
	
	private String BytesToBinary(byte[] bytes)
	{
		String binary = "";
		
		BigInteger binaryInt = new BigInteger(bytes);
		
		binary = binaryInt.toString(2);

		return binary;
	}
}
