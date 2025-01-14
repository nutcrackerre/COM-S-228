package edu.iastate.cs228.hw3;

/*
 * @author Iteoluwakishi Osomo
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

/*
 * 
 * This class reconstructs a message archived within a binary-tree based algorithm
 * 
 */

public class MsgTree {

	/////////// Instance variables////////////
	/*
	 * Character stored in each node. Internal nodes store a null value while leaf
	 * nodes store various characters
	 */
	public char payLoadChar;

	/*
	 * Points to the root value of the Message Tree formed
	 */
	public TreeNode<Character> root;
	
	/*
	 * ArrayList storing characters in the encoding scheme. 
	 * Corresponds with codesList to access the codes for the individual characters
	 */
	private static ArrayList<Character> characterList = new ArrayList<>();
	
	/*
	 * ArrayList storing the codes of characters in the encoding scheme. 
	 * Corresponds with characterList to access the characters with the respective codes
	 */
	private static ArrayList<String> codesList = new ArrayList<>();
	
	

	/*
	 * Constructor building the tree from a string
	 * 
	 * @param encodingString
	 */
	public MsgTree(String encodingString) {
		// initializing the root node
		payLoadChar = encodingString.charAt(0);
		root = new TreeNode('^', null);

		// node value to point towards the parent of the current node
		TreeNode<Character> prevNode = root;

		// value represents if the current node is the left or right child of parent
		// node
		// 0 represents a left child and 1 represents a right child
		// initialized to 0 since this implements pre-order traversal
		int bitValue = 0;

		// building the rest of the tree
		//iterative solution
		for (int i = 1; i < encodingString.length(); i++) {
			payLoadChar = encodingString.charAt(i);
			TreeNode<Character> newNode = new TreeNode(payLoadChar, prevNode);
			newNode.parent = prevNode;
			newNode.setBit(bitValue);

			if (payLoadChar == '^') {
				prevNode = newNode;
				bitValue = 0;
				// bitValue stays as 0
			} else {
				if (bitValue == 0) {
					// newNode = newNode.parent.left
					while (prevNode.right != null) {
						prevNode = prevNode.parent;
					}
					if (prevNode.right == null) {
						bitValue = 1;
					}
					// bitValue stays the same otherwise
				} else {
					// newNode == newNode.parent.right
					while (prevNode.left != null && prevNode.right != null && prevNode != root) {
						prevNode = prevNode.parent;
					}
					if (prevNode.right == null) {
						bitValue = 1;
					}
					// bitValue stays the same otherwise

				}
			}
		}

	}

	/*
	 * Constructor for a single node with null children
	 * 
	 * @param payLoadChar
	 */
	public MsgTree(char payLoadChar) {
		root = new TreeNode(payLoadChar, null);
	}

	/*
	 * Performs a recursive pre-order traversal of the MsgTree and prints all the characters 
	 * and their bit codes
	 * 
	 * @param root
	 * @param code
	 */
	public static void printCodes(MsgTree root, String code) {
		System.out.println("Character     code");
		System.out.println("-------------------------");
		
		TreeNode<Character> R = root.root;
		preOrder(R, root.root);
		//how do I implement the string code to recursively return different codes?
		//don't know if i want to implement it that way
	}
	
	/*
	 * Helper method that carries out pre-order traversal on the MsgTree once the root is passed in
	 * 
	 * @param R
	 */
	private static void preOrder(TreeNode<Character> R, TreeNode<Character> root) {
		if(R == null) {
			return;
		}
		
		if(R.data != '^') {
			String path = String.valueOf(R.bit);
			TreeNode<Character> current = R;
			
			while(R != root) {
				current = current.parent;
				if(current == root) {
					break;
				}
				path += String.valueOf(current.bit);
			}
			
			String code = "";
			for(int i = path.length() - 1; i >= 0; i--) {
				code += path.charAt(i);
			}
			
			if(R.data == '\n') {
				//special case for newline characters
				System.out.println("  \\n" + "          " + code);
			}
			else {
				System.out.println("   " + R.data + "          " + code);
			}
			
			characterList.add(R.data);
			codesList.add(code);
		}
		
		preOrder(R.left, root);
		preOrder(R.right, root);
	}
	
	/*
	 * Decodes and prints the message to the console
	 * 
	 * @param codes
	 * @param msg
	 */
	public static void decode(MsgTree codes, String msg) {
		
		//finding the length of the longest code
		int longestCode = 0;
		for(int i = 0; i < codesList.size(); i++) {
			if(codesList.get(i).length() >= longestCode) {
				longestCode = codesList.get(i).length();
			}
		}
		
		//integer to keep track of msg scanned in
		int j = 0;
		while(j != msg.length()) {
			//create empty string
			String scanned = "";
			//scanned = msg.substring(j, longestCode + j);
			int a;
			for(a = j; a < longestCode + j; a++) {
				scanned+= msg.charAt(a);
				if(a + 1 >= msg.length()) {
					break;
				}
			}
			
			int k;
			int x;
			for(k = scanned.length(); k >= 0; k--) {
				String temp = scanned.substring(0, k);
				
				for(x = 0; x < codesList.size(); x++) {
					if(temp.compareTo(codesList.get(x)) == 0) {
						break;
					}
				}
				
				if(x == codesList.size()) {
					x = codesList.size() - 1;
				}
				
				if(temp.compareTo(codesList.get(x)) == 0) {
					System.out.print(characterList.get(x));
					break;
				}
				
			}
			
			j += k;
		}
		
	}

	/*
	 * Private inner class for each tree node in the message tree
	 */
	private class TreeNode<Character> {

		///////// Instance Variables///////////////
		/*
		 * Left child of this node
		 */
		private TreeNode<Character> left = null;

		/*
		 * Right child of this node
		 */
		private TreeNode<Character> right = null;

		/*
		 * Value denoting if this node is a left or right child to its parent node Value
		 * is null for root node
		 */
		private int bit;

		/*
		 * Parent node of this node
		 */
		private TreeNode<Character> parent;

		/*
		 * Data stored in this node
		 */
		private Character data;

		/*
		 * Constructor takes in the character to be stored and link to parent node
		 * 
		 * @param data
		 * @param parent
		 */
		public TreeNode(Character data, TreeNode<Character> parent) {
			this.data = data;
			this.parent = parent;
		}

		/*
		 * Mutator method to set the bit value as 0 or 1 for this node and links its
		 * parent either as the left or right child respectively
		 * 
		 * @param bit
		 */
		public void setBit(int bit) {
			this.bit = bit;

			if (bit == 0) {
				parent.left = this;
			} else {
				parent.right = this;
			}
		}
	}
	
	
	
	public static void main(String[]args) throws FileNotFoundException {
		
		System.out.print("Please enter filename to decode: ");
		Scanner s = new Scanner (System.in);
		String fileName = s.next();
		File file = new File(fileName);
		
		//counting how many lines are in the file
		Scanner lineCounter = new Scanner(file);
		int count = 0;
		while(lineCounter.hasNextLine()) {
			count++;
			lineCounter.nextLine();
		}
		lineCounter.close();
		
		//scanning in all lines containing encoding scheme
		Scanner scnr = new Scanner(new FileReader(file));
		
		String line = "";
		
		if(count == 2) {
			line += scnr.nextLine();
		}
		else if(count == 3) {
			line += scnr.nextLine();
			line += '\n';
			line += scnr.nextLine();
		}
		
		//creating new Message tree node
		MsgTree T = new MsgTree(line);
		
		//printing codes
		System.out.println();
		printCodes(T, "");
		
		//scanning final line with message
		String msg = "";
		msg += scnr.nextLine();
		
		
		System.out.println();
		//printing message
		System.out.println("MESSAGE:");
		decode(T, msg);
		
	}

}
