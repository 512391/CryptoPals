package Main;

import java.util.Comparator;

public class HammingDistance implements Comparator<HammingDistance>{
	public float distance;
	public int keyLength;
	
	public HammingDistance(float dist, int keySize)
	{
		distance = dist;
		keyLength = keySize;
	}
	
	public HammingDistance()
	{
		
	}
	
	@Override
	public int compare(HammingDistance o1, HammingDistance o2) {
		return (int)((o2.distance-o1.distance)*10000);
	}
}
