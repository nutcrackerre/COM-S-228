package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;


/**
 *  
 * @author Iteoluwakishi Osomo
 *
 */

/**
 * 
 * This class implements the version of the quicksort algorithm presented in the lecture.   
 *
 */

public class QuickSorter extends AbstractSorter
{
	
		
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *   
	 * @param pts   input array of integers
	 */
	public QuickSorter(Point[] pts)
	{
		super();
		
		//Deep copy for the array
		points = new Point[pts.length];
		for(int i = 0; i < pts.length; i++) {
			points[i] = pts[i];
		}		
		
		algorithm = "quicksort";
	}
		

	/**
	 * Carry out quicksort on the array points[] of the AbstractSorter class.  
	 * 
	 */
	@Override 
	public void sort()
	{
		quickSortRec(0, points.length - 1);
		coordinateTime = System.nanoTime();

	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last)
	{
		//Base case: If the partition size is 1 or 0 elements
		//then the partition is already sorted
		if(first >= last) {
			return;
		}
		
		//Partition the data within the array.
		//Value lowEndIndex returned from partitioning is the index of the low 
		//partition's last element.
		int lowEndIndex = partition(first, last);
		
		//Recursively sort low partition (first to lowEndIndex)
		//and high partition (lowEndIndex + 1 to last)
		quickSortRec(first, lowEndIndex);
		quickSortRec(lowEndIndex+1, last);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last)
	{
		//pick middle element as pivot
		int midpoint = first + (last - first) / 2;
		Point pivot = points[midpoint];
		
		boolean done = false;
		
		while(!done) {
			//Increment first while points[first] < pivot
			while(pointComparator.compare(points[first], pivot) < 0) {
				first++;
			}
			
			//Decrement last while pivot < points[last]
			while(pointComparator.compare(pivot, points[last]) < 0) {
				last--;
			}
			
			//If zero or one elements remain, then all numbers are partitioned
			//Return last
			if(first >= last) {
				done = true;
			}
			else {
				//Swap points[first] and points[last]
				swap(first, last);
				
				//Update first and last
				first++;
				last--;
			}
		}
		
		return last; 
	}	
		
}
