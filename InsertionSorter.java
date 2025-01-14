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
 * This class implements insertion sort.   
 *
 */

public class InsertionSorter extends AbstractSorter 
{
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts  
	 */
	public InsertionSorter(Point[] pts) 
	{
		super();
		
		//Deep copy for the array
		points = new Point[pts.length];
		for(int i = 0; i < pts.length; i++) {
			points[i] = pts[i];
		}
		
		algorithm = "insertion sort";
	}	

	
	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.  
	 */
	@Override 
	public void sort()
	{
		int i;
		int j;
		Point temp;
		
		for(i = 1; i < points.length; i++) {
			j = i;
			while(j > 0 && pointComparator.compare(points[j], points[j-1]) < 0) {
				temp = points[j];
				points[j] = points[j-1];
				points[j-1] = temp;
				
				--j;
			}
		}
		coordinateTime = System.nanoTime();
		
	}		
}
