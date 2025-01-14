package edu.iastate.cs228.hw2;

import java.lang.reflect.Array;
import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the list interface based on linked nodes that store
 * multiple items per node. Rules for adding and removing elements ensure that
 * each node (except possibly the last one) is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * 
	 * @param nodeSize number of elements that may be stored in each node, must be
	 *                 an even number
	 */
	public StoutList(int nodeSize) {
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) {
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean add(E item) {
		if(item == null) {
			throw new NullPointerException();
		}
		if(isEmpty()) {
			Node newNode = new Node();
			link(head, newNode);
			newNode.data[0] = item;
			newNode.count += 1;
			//Updating this node's info each time a new element is added to the node
			newNode.thisNodeInfo.setCount(newNode.count);
		}
		else {
			// Find last node in the list and check to see if its predecessor is full
			Node current = tail.previous;
			int count = current.count;
			
			if(count == 4) { //if the predecessor is full
				//create a new node and put the item at offset 0 of that node
				Node newNode = new Node();
				link(current, newNode);
				newNode.data[0] = item;
				++newNode.count;
				//Updating this node's info each time a new element is added to the node
				newNode.thisNodeInfo.setCount(newNode.count);
			}
			else {
				current.data[count] = item;
				++current.count;
				//Updating this node's info each time a new element is added to the node
				current.thisNodeInfo.setCount(current.count);
			}
		}
		size += 1;
		return true;
	}

	@Override
	public void add(int pos, E item) {
		if(item == null) {
			throw new NullPointerException();
		}
		//if the given list is empty, add a new node and put the item at offset 0
		if(isEmpty()) {
			Node newNode = new Node();
			link(head, newNode);
			newNode.data[0] = item;
			++newNode.count;
			//Updating this node's info each time a new element is added to the node
			newNode.thisNodeInfo.setCount(newNode.count);
		}
		else {
			Node current = find(pos).node;
			int givenIndex = pos - findLastIndex(current);
			
			//if the specified position is empty 
			if(current.data[givenIndex] == null) {
				current.data[givenIndex] = item;
			}
			else {
				//check if the specified position is at the end of the data array
				if(givenIndex == 3) {
					//if yes, make a new successive node
					Node newNode = new Node();
					link(current, newNode);
					//move the element occupying the specified position to offset 0 of the new node
					newNode.data[0] = current.data[givenIndex];
					++newNode.count;
					//Updating this node's info each time a new element is added to the node
					newNode.thisNodeInfo.setCount(newNode.count);
					//change the value of the element in the specified position to the given element
					current.data[givenIndex] = item;
				}
				
				//if not, check if the next position in the array is full as well
				else if(current.data[givenIndex + 1] != null) {
					//check if the array is full
					//due to the successive design of the array, if the 3rd index is occupied then all those before it are occupied as well
					//thus the array would be full and a new node will need to be made
					if(current.data[3] != null) {
						Node newNode = new Node();
						link(current, newNode);
						//move each successive element into the new node
						int nodeIndex = 0;
						for(int i = givenIndex; i < 4; i++) {
							newNode.data[nodeIndex] = current.data[i];
							++newNode.count;
							//Updating this node's info each time a new element is added to the node
							newNode.thisNodeInfo.setCount(newNode.count);
							
							current.data[i] = null;
							--current.count;
							//Updating this node's info each time an element is removed from the node
							current.thisNodeInfo.setCount(current.count);
							
							nodeIndex++;
						}
						current.data[givenIndex] = item;
					}
					else { 
						//this means that array is not completely full but the specified position and the position after it are both occupied
					
						if(givenIndex == 0) {
							//index 0 of the data array, 1st element
							//since index 0 and 1 are both occupied, check if index 2 is as well
							if(current.data[2] != null) {
								current.data[3] = current.data[2];
								current.data[2] = current.data[1];
								current.data[1] = current.data[0];
								current.data[0] = item;
							}
							else {
								current.data[2] = current.data[1];
								current.data[1] = current.data[0];
								current.data[0] = item;
							}
							
						}
						else if(givenIndex == 1) {
							//index 1 of the data array, 2nd element
							//since index 1 and 2 are both occupied but index 3 is not, move elements accordingly
							current.data[3] = current.data[2];
							current.data[2] = current.data[1];
							current.data[1] = item;
							
						}
						//not checking for givenIndex == 2 since it satisfies the next condition at this point
					}
				}
				
				//if the specified position is occupied but the one after it is not then 
				//move the element already in the specified position to the next one and 
				//update the specified position with the given element
				else {
					current.data[givenIndex + 1] = current.data[givenIndex];
					current.data[givenIndex] = item;
				}
			}
			++current.count;
			//Updating this node's info each time a new element is added to the node
			current.thisNodeInfo.setCount(current.count);
		}
		++size;
	}

	@Override
	public E remove(int pos) {

		Node current = find(pos).node;
		int offset = pos - findLastIndex(current);
		E temp = current.data[offset];
		current.data[offset] = null;
		--current.count;
		//Updating this node's info each time a new element is added to the node
		current.thisNodeInfo.setCount(current.count);
		--size;
		
		if(current.count == 0) {
			unlink(current);
		}
		else if(current == tail.previous) {
			//close whatever gap was created and do nothing else
			for(int i = offset + 1; i < nodeSize; i++) {
				current.data[i - 1] = current.data[i];
			}
		}
		else if(current.data[offset + 1] != null) {
			if(current.count == 1) {
				//the element at index 0 was removed and index 1 is still occupied, there is a gap
				if(current.data[3] != null) {
					//if the array was fully occupied before remove() was called on the first index
					//close the gap
					current.data[0] = current.data[1];
					current.data[1] = current.data[2];
					current.data[2] = current.data[3];
					current.data[3] = null;
				}
				else if(current.data[2] != null) {
					//if the array was not full but the first three index were occupied before remove was called 
					//on the first index
					//close the gap
					current.data[0] = current.data[1];
					current.data[1] = current.data[2];
					current.data[2] = null;
				}
				else {
					//if the array was not full but the first two index were occupied before remove was called
					//on the first index
					//close the gap
					current.data[0] = current.data[1];
					current.data[1] = null;
					
					//mini-merge with next node since there are now < M/2 elements in a node that is not tail.previous
					Node next = current.next;
					current.data[1] = next.data[0];
					++current.count;
					//Updating this node's info each time a new element is added to the node
					current.thisNodeInfo.setCount(current.count);
					int position = findLastIndex(next);
					remove(position); 
				}
			}
			else if(current.count == 2) {
				//the element at index 1 was removed and index 2 is still occupied
				if(current.data[3] != null) {
					//if the array was fully occupied before remove() was called on the second index
					current.data[1] = current.data[2];
					current.data[2] = current.data[3];
					current.data[3] = null;
				}
				else {
					//if the array was not fully occupied but the first three index were occupied before remove() was called
					//on the second index
					//close the gap
					current.data[1] = current.data[2];
					current.data[2] = null;
				}	
			}
			else if(current.count == 3) {
				//the element at index 2 was removed and index 3 is still occupied
				//close the gap
				current.data[2] = current.data[3];
				current.data[3] = null;  
			}
		}
		else if(current.count == 1) {
			//mini-merge with next node since there are now < M/2 elements in a node that is not tail.previous
			Node next = current.next;
			current.data[1] = next.data[0];
			++current.count;
			//Updating this node's info each time a new element is added to the node
			current.thisNodeInfo.setCount(current.count);
			int position = findLastIndex(next);
			remove(position);
		}
		return temp;
	}
	
	/**
	 * Helper method to link a new node within the list
	 * @param current - previous node to new node
	 * @param n - new node being added
	 */
	private void link(Node current, Node n) {
		n.previous = current;
		n.next = current.next;
		current.next.previous = n;
		current.next = n;
	}
	
	/**
	 * Helper method to unlink a node within the list for deletion
	 * @param n - node being unlinked for deletion
	 */
	private void unlink(Node n) {
		n.previous.next = n.next;
		n.next.previous = n.previous;
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do
	 * the following. Traverse the list and copy its elements into an array,
	 * deleting every visited node along the way. Then, sort the array by calling
	 * the insertionSort() method. (Note that sorting efficiency is not a concern
	 * for this project.) Finally, copy all elements from the array back to the
	 * stout list, creating new nodes for storage. After sorting, all nodes but
	 * (possibly) the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {
		//creating a new array
		E[] arr = (E[]) new Object[size];
		
		//creating a list iterator to iterate through the node and copy elements into the array
		ListIterator<E> iter = listIterator();
		for(int i = 0; i < size; i++) {
			arr[i] = iter.next();
		}
		
		//creating a new comparator for call to insertionSort
		Comparator<E> objectComparator = new Comparator<E>() {
			@Override
			public int compare(E value1, E value2) {
				if(value1.equals(value2)){
					return 0;
				}
				else if(value1.compareTo(value2) > 0){ 
					return 1;
				}
				else{
					return -1;
				}
			}
			
		};
		
		//destroying current nodes in the list
		for(int j = size; j <= 0; --j) {
			remove(j);
		}
		
		//call to insertionSort
		insertionSort(arr, objectComparator);
		
		//creating new nodes in the list and adding elements back from the sorted array
		for(int k = 0; k < arr.length; k++) {
			add(k, arr[k]);
		}
	}

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the
	 * bubbleSort() method. After sorting, all but (possibly) the last nodes must be
	 * filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		
		E[] arr = (E[]) new Object[size];
		ListIterator<E> iter = listIterator();
		
		//copying elements stored in the Stout List into the array
		for(int i = 0; i < size; i++) {
			arr[i] = iter.next();
		}
		
		//destroying current nodes in the list
		for(int j = size; j <= 0; --j) {
			remove(j);
		}
		
		bubbleSort(arr);
		
		//creating new nodes in the list and adding elements back from the sorted array
		for(int k = 0; k < arr.length; k++) {
			add(k, arr[k]);
		}
		
	}

	@Override
	public ListIterator<E> listIterator() {

		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {

		return new StoutListIterator(index);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes and the position of the iterator.
	 *
	 * @param iter an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Node type for this list. Each node holds a maximum of nodeSize elements in an
	 * array. Empty slots are null.
	 */
	private class Node {
		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the number of
		 * elements in this node.
		 */
		public int count;
		
		/**
		 * The NodeInfo object for this node. It stores the node's count and is called by various methods
		 */
		public NodeInfo thisNodeInfo = new NodeInfo(this);

		/**
		 * Adds an item to this node at the first available offset. Precondition: count
		 * < nodeSize
		 * 
		 * @param item element to be added
		 */
		void addItem(E item) {
			if (count >= nodeSize) {
				return;
			}
			data[count++] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to
			// node " + Arrays.toString(data));
			
			//NOTE: Modified by author
			//Updating this node's info each time a new element is added to the node
			thisNodeInfo.setCount(count);
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the
		 * right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * 
		 * @param offset array index at which to put the new element
		 * @param item   element to be added
		 */
		void addItem(int offset, E item) {
			if (count >= nodeSize) {
				return;
			}
			for (int i = count - 1; i >= offset; --i) {
				data[i + 1] = data[i];
			}
			++count;
			data[offset] = item;
			// useful for debugging
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
			
			//NOTE: Modified by author
			//Updating this node's info each time a new element is added to the node
			thisNodeInfo.setCount(count);
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting elements
		 * left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void removeItem(int offset) {
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}
	}
	
	/**
	 * Helper class to store the offsets of the nodes in the StoutList
	 */
	public class NodeInfo{
		public Node node;
		public int count;
		
		public NodeInfo(Node node) {
			this.node = node;
			this.count = node.count;
		}
		
		public void setCount(int count) {
			this.count = count;
		}
	}
	
	/**
	 * Helper method that finds a node based on its offset
	 * @param pos
	 * @return
	 */
	public NodeInfo find(int pos) {
		if(!isEmpty()) {
			Node current = head.next;
			if(pos >= 0 && pos <= 3) {
				
			}
			else {
				int prevSum = current.count - 1;
				int newSum = prevSum;
				while(current != null) {
					if(current.next == tail) {
						break;
					}
					current = current.next;
					newSum += current.count;
					if(pos > prevSum && pos <= newSum) {
						break;
					}
					prevSum = newSum;
				}
			}
			
			return current.thisNodeInfo;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Helper method that returns the index of the last element in the node 
	 * previous to the current one
	 * @param node
	 * @return
	 */
	public int findLastIndex(Node node) {
		Node current = head;
		int tempIndex = current.thisNodeInfo.count;
		
		while(current != node.previous) {
			current = current.next;
			tempIndex += current.thisNodeInfo.count;
		}
		
		return tempIndex;
	}
	

	private class StoutListIterator implements ListIterator<E> {
		// constants you possibly use ...
		
		/**
		 * Direction for remove() and set after previous() has been called
		 */
		private static final int AHEAD = 1;
		
		/**
		 * Direction for remove() and set() after next() has been called
		 */
		private static final int BEHIND = -1;
		
		/**
		 * Direction for remove() and set() when neither previous() nor next() have been called
		 */
		private static final int NONE = 0;
		

		// instance variables ...
		
		/**
		 * Current node we are iterating through
		 */
		private Node thisNode;
		
		/**
		 * Direction that was iterated in depending on whether next() or previous() was called
		 */
		public int direction;
		
		/**
		 * Index of the next element in the list to be returned by the next call to next()
		 */
		public int indexPosition;
		
		/**
		 * Value from 0 - 4(nodeSize) showing the cursor's position within the current node's individual array of elements
		 */
		public int logicalIndexPosition;
		
		/**
		 * Value representing the logicalIndexPosition as an offset in thisNode
		 */
		private int offset;
		
		

		/**
		 * Default constructor
		 */
		public StoutListIterator() {

			this(0);
		}

		/**
		 * Constructor finds node at a given position.
		 * 
		 * @param pos
		 */
		public StoutListIterator(int pos) {

			thisNode = find(pos).node;
			direction = NONE;
			
			logicalIndexPosition = pos;
			if(logicalIndexPosition == size) {
				indexPosition = logicalIndexPosition - 1;
			}
			else {
				indexPosition = logicalIndexPosition;
			}
			offset = pos - findLastIndex(thisNode);
		}

		@Override
		public void remove() {
			
			if(direction == NONE) {
				throw new IllegalStateException();
			}
			else {
				if(direction == AHEAD) {
					thisNode.data[offset] = null;
					--thisNode.count;
					//Updating this node's info each time a new element is added to the node
					thisNode.thisNodeInfo.setCount(thisNode.count);
					--size;
				}
				else { //if direction == BEHIND
					thisNode.data[offset - 1] = null;
					--thisNode.count;
					//Updating this node's info each time a new element is added to the node
					thisNode.thisNodeInfo.setCount(thisNode.count);
					--size;
					--logicalIndexPosition;
					--indexPosition;
				}
				
				//shifts and merges
				if(thisNode.count == 0) {
					logicalIndexPosition = findLastIndex(thisNode);
					if(logicalIndexPosition == size) {
						indexPosition = logicalIndexPosition - 1;
					}
					else {
						indexPosition = logicalIndexPosition;
					}
					unlink(thisNode);
				}
				else if(thisNode == tail.previous) {
					//close whatever gap was created and do nothing else
					for(int i = offset + 1; i < nodeSize; i++) {
						thisNode.data[i - 1] = thisNode.data[i];
					}
				}
				else if(thisNode.data[offset + 1] != null || thisNode.data[offset] != null) { //depends on if direction was AHEAD or BEHIND
					if(thisNode.count == 1) {
						if(thisNode.data[3] != null) {
							//if the array was fully occupied before remove() was called on the first index
							//close the gap
							thisNode.data[0] = thisNode.data[1];
							thisNode.data[1] = thisNode.data[2];
							thisNode.data[2] = thisNode.data[3];
							thisNode.data[3] = null;
						}
						else if(thisNode.data[2] != null) {
							//if the array was not full but the first three index were occupied before remove was called 
							//on the first index
							//close the gap
							thisNode.data[0] = thisNode.data[1];
							thisNode.data[1] = thisNode.data[2];
							thisNode.data[2] = null;
						}
						else {
							//if the array was not full but the first two index were occupied before remove was called
							//on the first index or second index
							//close the gap
							thisNode.data[0] = thisNode.data[1];
							thisNode.data[1] = null;
							
							//mini-merge with next node since there are now < M/2 elements in a node that is not tail.previous
							Node next = thisNode.next;
							thisNode.data[1] = next.data[0];
							++thisNode.count;
							++size;
							//Updating this node's info each time a new element is added to the node
							thisNode.thisNodeInfo.setCount(thisNode.count);
							direction = AHEAD;
							logicalIndexPosition = findLastIndex(next) + 1;
							if(logicalIndexPosition == size) {
								indexPosition = logicalIndexPosition - 1;
							}
							else {
								indexPosition = logicalIndexPosition;
							}
							thisNode = next;
							offset = 0;
							if(thisNode != tail) {
								remove(); 
							}
						}
					}
					else if(thisNode.count == 2) {
						//the element at index 1 was removed and index 2 is still occupied
						if(thisNode.data[3] != null) {
							//if the array was fully occupied before remove() was called on the second index
							thisNode.data[1] = thisNode.data[2];
							thisNode.data[2] = thisNode.data[3];
							thisNode.data[3] = null;
						}
						else {
							//if the array was not fully occupied but the first three index were occupied before remove() was called
							//on the second index
							//close the gap
							thisNode.data[1] = thisNode.data[2];
							thisNode.data[2] = null;
						}	
					}
					else if(thisNode.count == 3) {
						//the element at index 2 was removed and index 3 is still occupied
						//close the gap
						thisNode.data[2] = thisNode.data[3];
						thisNode.data[3] = null;  
					}
				}
				else if(thisNode.count == 1) {
					//mini-merge with next node since there are now < M/2 elements in a node that is not tail.previous
					Node next = thisNode.next;
					thisNode.data[1] = next.data[0];
					++thisNode.count;
					//Updating this node's info each time a new element is added to the node
					thisNode.thisNodeInfo.setCount(thisNode.count);
					direction = AHEAD;
					logicalIndexPosition = findLastIndex(next);
					if(logicalIndexPosition == size) {
						indexPosition = logicalIndexPosition - 1;
					}
					else {
						indexPosition = logicalIndexPosition;
					}
				}
			}
			direction = NONE;
		}
			

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//
		
		@Override
		public void add(E item) {
			if(item == null) {
				throw new NullPointerException();
			}
			if(isEmpty()) {
				Node newNode = new Node();
				link(head, newNode);
				newNode.data[0] = item;
				++newNode.count;
				//Updating this node's info each time a new element is added to the node
				newNode.thisNodeInfo.setCount(newNode.count);
				thisNode = newNode;
				logicalIndexPosition = 0;
				indexPosition = 0;
			}
			else {
				//Find the cursor position and add the element behind it
				//if the specified position is empty (the logicalIndexPosition is at the end of the list == size)
				if(thisNode.data[offset] == null) {
					thisNode.data[offset] = item;
					++logicalIndexPosition;
				}
				else {
					//check if the specified position is at the end of the data array
					if(offset == 3) {
						//if yes, make a new successive node
						Node newNode = new Node();
						link(thisNode, newNode);
						//move the element occupying the specified position to offset 0 of the new node
						newNode.data[0] = thisNode.data[offset];
						++newNode.count;
						//Updating this node's info each time a new element is added to the node
						newNode.thisNodeInfo.setCount(newNode.count);
						//change the value of the element in the specified position to the given element
						thisNode.data[offset] = item;
						//update values
						thisNode = newNode;
						logicalIndexPosition += thisNode.thisNodeInfo.count;
						if(logicalIndexPosition == size) {
							indexPosition = logicalIndexPosition - 1;
						}
						else {
							indexPosition = logicalIndexPosition;
						}
						offset = logicalIndexPosition - findLastIndex(thisNode);
						++thisNode.count;
					}
					
					//if not, check if the next position in the array is full as well
					else if(thisNode.data[offset + 1] != null) {
						//check if the array is full
						//due to the successive design of the array, if the 3rd index is occupied then all those before it are occupied as well
						//thus the array would be full and a new node will need to be made
						if(thisNode.data[3] != null) {
							Node newNode = new Node();
							link(thisNode, newNode);
							//move each successive element into the new node
							int nodeIndex = 0;
							for(int i = offset; i < 4; i++) {
								newNode.data[nodeIndex] = thisNode.data[i];
								++newNode.count;
								//Updating this node's info each time a new element is added to the node
								newNode.thisNodeInfo.setCount(newNode.count);
								
								thisNode.data[i] = null;
								--thisNode.count;
								//Updating this node's info each time an element is removed from the node
								thisNode.thisNodeInfo.setCount(thisNode.count);
								
								nodeIndex++;
							}
							thisNode.data[offset] = item;
							thisNode = newNode;
							offset = 0;
						}
						else { 
							//this means that array is not completely full but the specified position and the position after it are both occupied
						
							if(offset == 0) {
								//index 0 of the data array, 1st element
								//since index 0 and 1 are both occupied, check if index 2 is as well
								if(thisNode.data[2] != null) {
									thisNode.data[3] = thisNode.data[2];
									thisNode.data[2] = thisNode.data[1];
									thisNode.data[1] = thisNode.data[0];
									thisNode.data[0] = item;
								}
								else {
									thisNode.data[2] = thisNode.data[1];
									thisNode.data[1] = thisNode.data[0];
									thisNode.data[0] = item;
								}
								++thisNode.count;
							}
							else if(offset == 1) {
								//index 1 of the data array, 2nd element
								//since index 1 and 2 are both occupied but index 3 is not, move elements accordingly
								thisNode.data[3] = thisNode.data[2];
								thisNode.data[2] = thisNode.data[1];
								thisNode.data[1] = item;
								++thisNode.count;
							}
							//not checking for givenIndex == 2 since it satisfies the next condition at this point
						}
					}
					
					//if the specified position is occupied but the one after it is not then 
					//move the element already in the specified position to the next one and 
					//update the specified position with the given element
					else {
						thisNode.data[offset + 1] = thisNode.data[offset];
						thisNode.data[offset] = item;
						++thisNode.count;
					}
					
					//updating logicalIndexPosition and indexPosition accordingly
					++logicalIndexPosition;
					if(logicalIndexPosition == size) {
						indexPosition = logicalIndexPosition - 1;
					}
					else {
						indexPosition = logicalIndexPosition;
					}
				}
				//Updating this node's info each time a new element is added to the node
				thisNode.thisNodeInfo.setCount(thisNode.count);
			}
			++size;
		}
		
		@Override
		public boolean hasPrevious() {
			return indexPosition > 0;
		}
		
		@Override
		public boolean hasNext() {
			return indexPosition < size;
		}
		
		@Override
		public int nextIndex() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			return indexPosition;
		}
		
		@Override 
		public int previousIndex() {
			if(!hasPrevious()) {
				throw new NoSuchElementException();
			}
			return indexPosition - 1;
		}
		
		@Override
		public E previous() {
			
			if(!hasPrevious()) {
				throw new NoSuchElementException();
			}
			
			//if offsetCursor is 0, then we need to move to the previous node to move backward
			//values of thisNode, index and offsetCursosr will change
			if(offset == 0) {
				thisNode = thisNode.previous;
				logicalIndexPosition = thisNode.thisNodeInfo.count - 1;
				//reset offset for new node
				offset = logicalIndexPosition - findLastIndex(thisNode);
			}
			else {  //else just move backward by 1
				--logicalIndexPosition;
				--offset;
			}
			
			//updating indexPosition correspondingly
			if(logicalIndexPosition == size) {
				indexPosition = logicalIndexPosition - 1;
			}
			else {
				indexPosition = logicalIndexPosition;
			}
			
			direction = AHEAD;
			return thisNode.data[offset]; 
		}
		
		@Override
		public E next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			//if offsetCursor is 3, then we need to mode to the next node to move forward
			if(offset == 3) {
				Node tempCurrent = thisNode;
				thisNode = thisNode.next;
				int tempOffset = offset;
				++logicalIndexPosition;
				//reset offset for new node
				offset = indexPosition - findLastIndex(thisNode);
				++offset;
				
				//updating indexPosition correspondingly
				if(logicalIndexPosition == size) {
					indexPosition = logicalIndexPosition - 1;
				}
				else {
					indexPosition = logicalIndexPosition;
				}
				
				direction = BEHIND;
				return tempCurrent.data[tempOffset];
			}
			else {
				++logicalIndexPosition;
				++offset;

				//updating indexPosition correspondingly
				if(logicalIndexPosition == size) {
					indexPosition = logicalIndexPosition - 1;
				}
				else {
					indexPosition = logicalIndexPosition;
				}
				
				direction = BEHIND;
				return thisNode.data[offset - 1]; 
			}
		}
		
		@Override
		public void set(E item) {
			
			if(direction == NONE) {
				throw new IllegalStateException();
			}
			else {
				if(direction == AHEAD) {
					thisNode.data[offset] = item;
				}
				else {
					//if direction == BEHIND
					if(offset == 0) {
						Node tempNode = thisNode;
						tempNode = tempNode.previous;
						
						int tempCursor = tempNode.thisNodeInfo.count;
						tempNode.data[tempCursor] = item; 
					}
					else {
						int tempCursor = offset - 1;
						thisNode.data[tempCursor] = item; 
					}
				}
			}
	
		}

		
	}

	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING
	 * order.
	 * 
	 * @param arr  array storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {
		int i;
		int j;
		E temp;
		
		for(i = 1; i < arr.length; i++) {
			j = i;
			while(j > 0 && comp.compare(arr[j], arr[j-1]) < 0) {
				//swap
				temp = arr[j];
				arr[j] = arr[j-1];
				arr[j-1] = temp;
				
				--j;
			}
		}
	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a
	 * description of bubble sort please refer to Section 6.1 in the project
	 * description. You must use the compareTo() method from an implementation of
	 * the Comparable interface by the class E or ? super E.
	 * 
	 * @param arr array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {
		boolean notSwapped;
		for(int i = 0; i < arr.length - 1; i++) {
			notSwapped = true;
			for(int j = 0; j < arr.length - i - 1; j++) {
				if(arr[j].compareTo(arr[j+1]) <= 0) { //non-increasing order
					//swap
					E temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
					notSwapped = false;
				}
			}
			if(notSwapped) {
				break;
			}
		}
	}

}