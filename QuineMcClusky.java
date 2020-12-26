import java.util.*;
import java.io.*;

class MinTerms {
	private String BinaryNum;				//	private variables
	private ArrayList<Integer> coveredNums;
	
	/**
	 * creates the MinTerm with the Binary String
	 * @param binaryString String of Binary Number
	 */
	MinTerms (String binaryString) {
		BinaryNum = binaryString;
		coveredNums = new ArrayList<>();
	}
	
	/**
	 * return the BinaryNum String
	 * @return BinaryNum String
	 */
	String getBinaryNum() {
		return BinaryNum;
	}
	
	/**
	 * returns the ArrayList of the MinTerm
	 * @return ArrayList of Covered Numbers
	 */
	ArrayList<Integer> getCoveredNums() {
		return coveredNums;
	}
	
	/**
	 * adds covered number to the arrayList
	 * @param a covered number
	 */
	void addNum(int a) {
		coveredNums.add(a);
	}
	
	/**
	 * removes the number from the ArrayList
	 * @param coveredNum number to be removed
	 */
	void removeNum(int coveredNum) {
		if(coveredNums.contains(coveredNum))
			coveredNums.remove(coveredNums.indexOf(coveredNum));
	}
	
	/**
	 * determines if the object is equal to the MinTerm object
	 * @param a object to compare
	 * @return if the object is equal to the MinTerm object
	 */
	@Override
	public boolean equals(Object a) {
		if(this == a)
			return true;
		if(!(a instanceof MinTerms))
			return false;
		MinTerms obj = (MinTerms) a;
		return obj.getBinaryNum().equals(BinaryNum);
	}
	
	/**
	 * prints the MinTerm Object
	 * @return String that represents the minTerm Object
	 */
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("");
		str.append(BinaryNum + "\t");
		for(int a: coveredNums) 
			str.append(a + " ");
		return str.toString();
	}
}


public class QuineMcClusky {
	
	/**
	 * Determines if the array of String contains the "d" for don't cares
	 * @param tokens array of Strings separated by spaces
	 * @return the index of the "d" if it exists
	 */
	public static int findD(String[] tokens) {
		for(int lcv = 0; lcv < tokens.length; lcv++)
			if(tokens[lcv].equals("d"))
				return lcv;
		return -1;
	}
	
	/**
	 * Converts the integer into Binary form with a certain number of bits
	 * @param num integer to convert to Binary
	 * @param bits number of bits to represent the Binary
	 * @return Binary form of the number
	 */
	public static String getBinary(int num, int bits) {
		String binary = Integer.toBinaryString(num);
		while(binary.length() < bits)
			binary = "0" + binary;
		return binary;
	}
	
	/**
	 * determines if the Binary Numbers are adjacent to each other
	 * @param binary1 binary String of num1
	 * @param binary2 binary String of num2
	 * @return if the 2 numbers are adjacent
	 */
	public static boolean ifAdjacent(String binary1,String binary2) {
		int numDiff = 0;
		char [] b1 = binary1.toCharArray();
		char [] b2 = binary2.toCharArray();
		for(int lcv = 0; lcv < b1.length; lcv++)
			if(b1[lcv] != b2[lcv])
				numDiff++;	
		return numDiff == 1;
	}
	
	/**
	 * combines the two Strings
	 * @param binary1 String of the first number
	 * @param binary2 String of the second number
	 * @return the combined String
	 */
	public static String makeCombineString(String binary1,String binary2) {
		String newString = "";
		char [] b1 = binary1.toCharArray();
		char [] b2 = binary2.toCharArray();
		for(int lcv = 0; lcv < b1.length; lcv++)
			if(b1[lcv] == b2[lcv])
				newString += b1[lcv];
			else
				newString += "-";
		return newString;
	}
	
	/**
	 * generates all the Prime Implicants
	 * @param list ArrayList of MinTerms to generated Prime Implicants into
	 */
	public static void getPrimeImplicants(ArrayList<MinTerms> list) {
		for(int lcv = 0; lcv < list.size(); lcv++) {
			boolean grouped = false;
			for(int lcv1 = lcv + 1; lcv1 < list.size(); lcv1++) {
				if(ifAdjacent(list.get(lcv).getBinaryNum(),list.get(lcv1).getBinaryNum())) {
					grouped = true;
					MinTerms newBinary = new MinTerms(makeCombineString(list.get(lcv).getBinaryNum(),list.get(lcv1).getBinaryNum()));
					for(int a: list.get(lcv).getCoveredNums())
						newBinary.addNum(a);
					for(int a: list.get(lcv1).getCoveredNums())
						newBinary.addNum(a);
					if(!list.contains(newBinary)) 
						list.add(newBinary);
				}
			}
			if(grouped) {
				list.remove(lcv);
				lcv--;
			}
		}
	}
	
	/**
	 * generates the 2d boolean array to contain the all covered numbers for each MinTerm
	 * @param list ArrayList of the MinTerms
	 * @param mintermsNum ArrayList of the covered numbers (not including DC)
	 * @return 2d boolean array
	 */
	public static boolean [][] makeCoverageTable(ArrayList<MinTerms> list, ArrayList<Integer> mintermsNum) {
		boolean [][] coverageTable = new boolean[list.size()][mintermsNum.size()];
		for(int i = 0; i < coverageTable.length; i++) {
			ArrayList<Integer> nums = list.get(i).getCoveredNums();
				for(int j = 0; j < coverageTable[i].length; j++) 
					if(nums.contains(mintermsNum.get(j)))
						coverageTable[i][j] = true;
		}
		return coverageTable;
	}
	
	/**
	 * removes all the don't cares from the MinTerms
	 * @param list ArrayList of MinTerms
	 * @param DC ArrayList of don't cares
	 */
	public static void removeDontCares(ArrayList<MinTerms> list, ArrayList<Integer> DC) {
		if(DC.isEmpty())
			return;
		for(int lcv = 0; lcv < list.size(); lcv++) 
			for (int i = 0; i < DC.size(); i++) 
				if(list.get(lcv).getCoveredNums().contains(DC.get(i)))
					list.get(lcv).getCoveredNums().remove(DC.get(i));
	}
	
	/**
	 * determines if the boolean array is "empty"
	 * @param coverageTable 2d boolean array to check
	 * @return if the boolean Array is "empty"
	 */
	public static boolean isBoardEmpty(boolean[][] coverageTable) {
		for(int row = 0; row < coverageTable.length; row++)
			for(int col = 0; col < coverageTable[row].length; col++)
				if(coverageTable[row][col])
					return false;
		return true;
	}
	
	/**
	 * removes the covered numbers from the row of the boolean array
	 * @param coverageTable 2d boolean array
	 * @param row row to remove
	 * @param list ArrayList of the MinTerms
	 * @param mintermsNum ArrayList of the MintermsNums
	 */
	public static void removeRow(boolean[][] coverageTable, int row, ArrayList<MinTerms> list, ArrayList<Integer> mintermsNum) {
		for(int lcv = 0; lcv < coverageTable[row].length; lcv++)
			if(coverageTable[row][lcv]) {
				coverageTable[row][lcv] = false;
				list.get(row).removeNum(mintermsNum.get(lcv));
			}
	}
	
	/**
	 * removes the covered numbers from the column of the boolean array
	 * @param coverageTable 2d boolean array
	 * @param col column to remove
	 * @param list ArrayList of the MinTerms
	 * @param mintermsNum ArrayList of the MintermsNums
	 */
	public static void removeCol(boolean[][] coverageTable, int col, ArrayList<MinTerms> list, ArrayList<Integer> mintermsNum) {
		for(int lcv = 0; lcv < coverageTable.length;lcv++) 
			if(coverageTable[lcv][col]) {
				coverageTable[lcv][col] = false;
				list.get(lcv).removeNum(mintermsNum.get(col));
			}
	}
	
	/**
	 * determines if the MinTerms is a "less-than"
	 * @param c1 ArrayList of covered numbers (assumed to be smaller but not empty)
	 * @param c2 ArrayList of covered numbers
	 * @return if the c1 is a less-than to c2
	 */
	public static boolean ifLessThan(ArrayList<Integer> c1, ArrayList<Integer> c2) {
		if( (c2.size() < c1.size()) || (c1.isEmpty() || c2.isEmpty()))
			return false;
		for(int a: c1)
			if(!c2.contains(a))
				return false;
		return true;
	}
	
	/**
	 * removes the less-than from the boolean array
	 * @param coverageTable 2d boolean array
	 * @param list ArrayList of MinTerms
	 * @param mintermsNum ArrayList of numbers covered
	 */
	public static void removeLessThans(boolean[][] coverageTable, ArrayList<MinTerms> list, ArrayList<Integer> mintermsNum) {
		for(int lcv = 0 ; lcv < coverageTable.length; lcv++) 
			for(int lcv1 = lcv + 1; lcv1 < coverageTable.length; lcv1++) 
				if(ifLessThan(list.get(lcv).getCoveredNums(),list.get(lcv1).getCoveredNums())) 
					removeRow(coverageTable,lcv,list,mintermsNum);
				else if(ifLessThan(list.get(lcv1).getCoveredNums(),list.get(lcv).getCoveredNums()))
					removeRow(coverageTable,lcv1,list,mintermsNum);
			
	}
	
	/**
	 * determines if the column is a Essential Prime Implicant
	 * @param coverageTable 2d boolean array
	 * @param col column of the array
	 * @return if the column only contains one number covered
	 */
	public static boolean ifEPI(boolean[][] coverageTable, int col) {
		int count = 0;
		for (int lcv = 0; lcv < coverageTable.length; lcv++) 
			if(coverageTable[lcv][col])
				count++;
		return count == 1;
	}
	
	/**
	 * grabs the row number of the EPI
	 * @param coverageTable 2d boolean array
	 * @param col column of the array
	 * @return
	 */
	public static int grabEPIRow(boolean[][]coverageTable, int col) {
		for(int lcv = 0; lcv < coverageTable.length; lcv++)
			if(coverageTable[lcv][col])
				return lcv;
		return -1;
	}
	
	/**
	 * grabs the EPI and removes the row and columns associated with the row
	 * @param coverageTable 2d boolean Array
	 * @param list ArayList of MinTerms
	 * @param row row number of the array
	 * @param mintermsNum ArrayList of the coveredNumbers
	 * @return
	 */
	public static MinTerms grabEPI(boolean[][]coverageTable, ArrayList<MinTerms> list, int row, ArrayList<Integer> mintermsNum) {
		MinTerms EPI = list.get(row);
		for(int lcv = 0; lcv < coverageTable[row].length; lcv++) 
			if(coverageTable[row][lcv])
				removeCol(coverageTable,lcv,list,mintermsNum);
		return EPI;
	}
	
	/**
	 * finds the row with the most covered numbers
	 * @param list ArrayList of MinTerms
	 * @return the MinTerm with the largest size of covered numbers
	 */
	public static int findBiggestPI(ArrayList<MinTerms> list) {
		int row = -1;
		int size = 0;
		for(int lcv = 0; lcv < list.size(); lcv++) {
			if(list.get(lcv).getCoveredNums().size() >= size) {
				row = lcv;
				size = list.get(lcv).getCoveredNums().size();
			}
		}
		return row;
	}
	
	/**
	 * outputs the minimal Sum of Products of the covered numbers
	 * @param answer ArrayList of the Solution MinTerms
	 * @param vars String array of the literals
	 * @return minimal Sum of Products of the covered numbers
	 */
	public static String ouputAnswer(ArrayList<MinTerms> answer, String[] vars) {
		StringBuffer str = new StringBuffer("");
		for(int lcv = 0; lcv < answer.size(); lcv++) {
			char[] minterm = answer.get(lcv).getBinaryNum().toCharArray();
			for(int index = 0; index < minterm.length; index++) {
				if(minterm[index] == '0') 
					str.append(vars[index] + "'");
				else if(minterm[index] == '1')
					str.append(vars[index]);
			}
			if(lcv != answer.size() - 1)
				str.append(" + ");
		}
		return str.toString();
	}
	
	public static void main(String[] args) throws IOException{
	//	Scanner in = new Scanner(new File("minterms1"));	//	Testing purposes 
		Scanner in = new Scanner(System.in);				//	Submission
		String line = in.nextLine();
		String[] tokens = line.split(" ");
		in.close();											//	Closes the File
		
		int numLiterals = Integer.parseInt(tokens[0]);		//	Grabs the Number of Variables
		String[] vars = new String[numLiterals];
		for(int lcv = 0; lcv < vars.length; lcv++)
			vars[lcv] = Character.toString((char)(65 + lcv));
		
		ArrayList<String> minTerms = new ArrayList<>();		//	Creates ArrayLists to grab the information
		ArrayList<Integer> allNums = new ArrayList<>();
		ArrayList<Integer> mintermsNum = new ArrayList<>();
		ArrayList<Integer> DC = new ArrayList<>();	
		
		for(int lcv = 1; lcv < tokens.length; lcv++) 		//	Grabs the ON MinTerms and DC (if any)
			if(findD(tokens) == -1) {
				allNums.add(Integer.parseInt(tokens[lcv]));
				mintermsNum.add(Integer.parseInt(tokens[lcv]));
			}
			else {
				if(lcv != findD(tokens))
					allNums.add(Integer.parseInt(tokens[lcv]));
				if((findD(tokens) != -1) && (lcv < findD(tokens))) 			
					mintermsNum.add(Integer.parseInt(tokens[lcv]));
				if(lcv > findD(tokens))
					DC.add(Integer.parseInt(tokens[lcv]));
			}			
		Collections.sort(allNums);
		
		for(int a: allNums)									//	Adds the Binary form of the minTerms and DC
			minTerms.add(getBinary(a,numLiterals));		
		
		ArrayList<MinTerms> list = new ArrayList<>();		//	Creates and add the ArrayList with MinTerms and covered Numbers for each minterm
		for(int lcv = 0; lcv < minTerms.size(); lcv++) {
			list.add(new MinTerms(minTerms.get(lcv)));
			list.get(lcv).addNum(allNums.get(lcv));
		}
		
		getPrimeImplicants(list);							//	generates the Prime Implicants
		
		boolean [][] coverageTable = makeCoverageTable(list,mintermsNum);	//	creates the 2D boolean Array
		
		removeDontCares(list,DC);							//	removes the DC from the MinTerms
		
		ArrayList<MinTerms> answer = new ArrayList<>(); 	//	ArrayList to hold the solution
		
		while(!isBoardEmpty(coverageTable)) {				//	The Quine McCluskey
			boolean foundEPI = false;
			removeLessThans(coverageTable,list,mintermsNum);
			for(int col = 0; col < coverageTable[0].length; col++) 
				if(ifEPI(coverageTable,col)) {
					foundEPI = true;
					answer.add(grabEPI(coverageTable,list,grabEPIRow(coverageTable,col),mintermsNum));
				}
			if(!foundEPI)
				answer.add(grabEPI(coverageTable,list,findBiggestPI(list),mintermsNum));
		}
		
		System.out.println(ouputAnswer(answer,vars));		//	Output the Solution
	}
}