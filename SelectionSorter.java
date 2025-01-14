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
 * This class implements selection sort.   
 *
 */

public class SelectionSorter extends AbstractSorter
{
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts  
	 */
	public SelectionSorter(Point[] pts)  
	{
		super();
		
		//Deep copy for the array
		points = new Point[pts.length];
		for(int i = 0; i < pts.length; i++) {
			points[i] = pts[i];
		}
		
		algorithm = "selection sort";
	}	

	
	/** 
	 * Apply selection sort on the array points[] of the parent class AbstractSorter.  
	 * 
	 */
	@Override 
	public void sort()
	{
		int i;
		int j;
		int indexSmallest;
		Point temp;
		
		for(i = 0; i < points.length - 1; i++) {
			indexSmallest = i;
			for(j = i+1; j < points.length; j++) {
				if(pointComparator.compare(points[j], points[indexSmallest]) < 0) {
					indexSmallest = j;
				}
			}
			
			temp = points[i];
			points[i] = points[indexSmallest];
			points[indexSmallest] = temp;
		}
		
		coordinateTime = System.nanoTime();
	
	}	
}
