package jaccard;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Shingling {

	public static HashSet<String> S1 = new HashSet<String>();
	public static HashSet<String> S2 = new HashSet<String>();

	public static void main(String[] args) {

		String str1 = "IELTS (International English Language Testing System) conducted "
				+ "by the British Council, University of Cambridge Local "
				+ "Examinations Syndicate and International Development Program "
				+ "of Australian Universities and College: providing grade 6.5 or "
				+ "higher (i.e. 7, 8, 9) overall has been obtained with a breakdown of "
				+ "6.0 in reading and writing and 5.5 in listening and speaking";

		String str2 = "IELTS / UKVI-CIELTS 6.5 overall with 6.0 in reading and writing, "
				+ "5.5 in listening and speaking for Law, Psychology, Architecture, "
				+ "English, Accounting and Finance";

		System.out.println("k值");
		Scanner scanner = new Scanner(System.in);
		int k = scanner.nextInt();
		scanner.close();

		
		Shingling.getK(str1, str2, k);
		
		System.out.println("shingling S1" + S1.size());
		System.out.println("shingling S2" + S2.size());

		HashSet<String> hashSet = new HashSet<String>();
		hashSet.addAll(S1);
		hashSet.addAll(S2);

		System.out.println("总长度" + hashSet.size());
		
		System.out.println("Jaccard" + getJaccard(S1,S2,hashSet));

	}
	
	public static void getK(String str1, String str2, int k) {
		
		for (int i = 0; i <= str1.length() - k; i++) {
			String temp = str1.substring(i, i + k);
			S1.add(temp);
		}
		
		for (int i = 0; i <= str2.length() - k; i++) {
			String temp = str2.substring(i, i + k);
			S2.add(temp);
		}
	}

	public static float getJaccard(HashSet<String> hashSet1, HashSet<String> hashSet2, HashSet<String> hashSet) {

		int[] h1 = new int[] { 6000, 6000 };
		int[] h2 = new int[] { 6000, 6000 };

		Iterator<String> iterator = hashSet.iterator();

		for (int i = 0; iterator.hasNext(); i++) {

			String next = (String) iterator.next();
			if (hashSet1.contains(next)) {
				if ((3 * i + 1) % 7 < h1[0]) {
					h1[0] = (3 * i + 1) % 7;
				}
				if ((5 * i + 1) % 7 < h1[1]) {
					h1[1] = (5 * i + 1) % 7;
				}
			}
			
			if (hashSet2.contains(next)) {
				if ((3 * i + 1) % 7 < h2[0]) {
					h2[0] = (3 * i + 1) % 7;
				}
				if ((5 * i + 1) % 7 < h2[1]) {
					h2[1] = (5 * i + 1) % 7;
				}
			}

		}
		
		System.out.println("sign1: [" + h1[0] + "," + h1[1] + "]");
		System.out.println("sign2: [" + h2[0] + "," + h2[1] + "]");

		float same = 0;
		if(h1[0] == h2[0]) same++;
		if(h1[1] == h2[1]) same++;
		
		return same/2;
	}

}
