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
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{ 
		super();
		
		//Deep copy for the array
		points = new Point[pts.length];
		for(int i = 0; i < pts.length; i++) {
			points[i] = pts[i];
		}
		
		algorithm = "mergesort";
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		mergeSortRec(points);
		coordinateTime = System.nanoTime();
		
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		//Base or termination case
		if(pts.length <= 1) {
			return;
		}
		
		int i = 0;
		int k = pts.length - 1;
		int j = (i + k) / 2;           //Finding the midpoint for the partition
		
		Point[] leftPart = new Point[j - i + 1];       //Initializing and creating temporary array for left partition
		for(int a = 0; a < leftPart.length; ++a) {
			leftPart[a] = pts[a+i];
		}
		
		Point[] rightPart = new Point[k - j];      //Initializing and creating temporary array for right partition
		for(int a = 0; a < rightPart.length; ++a) {
			rightPart[a] = pts[j+1+a];
		}
		
		//Recursively sorting left and right partitions
		mergeSortRec(leftPart);
		mergeSortRec(rightPart);
		
		//Merge left and right partition in sorted order
		merge(pts, i, j, k);
		
	}

	
	// Other private methods if needed ...
	
	/**
	 * Helper method for merge sort procedure
	 * @param arr  given array
	 * @param i   start index of the left partition
	 * @param j   end index of the left partition
	 * @param k   end index of the right partition
	 */
	private void merge(Point[] arr, int i, int j, int k) {
		
		int mergedSize = k - i + 1;                          //Size of merged partition
		int mergedPos = 0;                                   //Position to insert merged number
		int leftPos = 0;                                     //Position of elements in left partition
		int leftSize = j - i + 1;                            //Length of left partition
		int rightPos = 0;                                    //Position of elements in right partition
		int rightSize = k - j;                               //Length of right partition
		Point[] merged = new Point[mergedSize];              //Temporary array for merged numbers
		
		//Add smallest element from left or right partition to temporary array
		while(leftPos < leftSize && rightPos < rightSize) {
			if(pointComparator.compare(arr[leftPos], arr[rightPos]) <= 0) {
				merged[mergedPos] = arr[leftPos];
				++leftPos;
			}
			else {
				merged[mergedPos] = arr[rightPos];
				++rightPos;
			}
			++mergedPos;
		}
		
		//If left partition is not empty, add remaining elements to temporary array
		while(leftPos < leftSize) {
			merged[mergedPos] = arr[leftPos];
			++leftPos;
			++mergedPos;
		}
		
		//If right partition is not empty, add remaining elements to temporary array
		while(rightPos < rightSize) {
			merged[mergedPos] = arr[rightPos];
			++rightPos;
			++mergedPos;
		}
	
		
		//Deep Copy temporary array back into given array
		for(mergedPos = 0; mergedPos < mergedSize; mergedPos++) {
			arr[i + mergedPos] = merged[mergedPos];
		}
	
	}

}
