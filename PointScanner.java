package edu.iastate.cs228.hw1;

/**
 * 
 * @author  Iteoluwakishi Osomo
 *
 */

import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;


/**
 * 
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class PointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;    
	
		
	protected long scanTime; 	       // sum of execution time in nanoseconds. 
	
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		sortingAlgorithm = algo;
		
		if(pts == null || pts.length == 0) {
			throw new IllegalArgumentException();
		}
		
		//Deep copy for the array
		points = new Point[pts.length];
		for(int i = 0; i < pts.length; i++) {
			points[i] = pts[i];
		}
		
	}

	
	/**
	 * This constructor reads points from a file. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{

		sortingAlgorithm = algo;
		ArrayList<Integer> list = new ArrayList<>();
		
		File file = new File(inputFileName);
		Scanner scnr = new Scanner(file);
		
		while(scnr.hasNextLine()) {
			String str = scnr.nextLine();
			
			//Using multiple delimiters to parse input
			String[] strArr = str.split("[() ,]");
			String[] intArr = new String[2];
			//Still parsing input
			int b = 0;
			for(int a = 0; a < strArr.length; a++) {
				if(strArr[a].equals("")) {
					continue;
				}
				else {
					intArr[b] = strArr[a];
					b++;
				}
			} 
			
			//Adding to a temporary array list
			for(int x = 0; x < intArr.length; x++) {
				list.add(Integer.parseInt(intArr[x]));
			}
		}
		
		//closing...
		scnr.close();
		
		//If the file contains an odd number of integers
		if(list.size() % 2 != 0) {
			throw new InputMismatchException();
		}
		
		//Make new array list of points that takes in points from the array list of integers
		ArrayList<Point> pts = new ArrayList<>();
		for(int i = 0; i < list.size(); i+=2) {
			int x = list.get(i);
			int y = list.get(i + 1);
			Point p = new Point(x, y);
			pts.add(p);
		}
		
		//Make points the right size and deep copy
		points = new Point[pts.size()];
		for(int j = 0; j < pts.size(); j++) {
			points[j] = pts.get(j);
		}
		
	}

	
	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.     
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.       
	 * @param algo
	 * @return
	 */
	public void scan()
	{
		AbstractSorter aSorter; 
		
		// create an object to be referenced by aSorter according to sortingAlgorithm.
		if(sortingAlgorithm.equals(Algorithm.QuickSort)) {
			aSorter = new QuickSorter(points);
		}
		else if(sortingAlgorithm.equals(Algorithm.MergeSort)) {
			aSorter = new MergeSorter(points);
		}
		else if(sortingAlgorithm.equals(Algorithm.InsertionSort)) {
			aSorter = new InsertionSorter(points);
		}
		else {
			//if(sortingAlgorithm.equals(Algorithm.SelectionSort))
			aSorter = new SelectionSorter(points);
		}
		
		
		/////////////First sorting round/////////////////
		
		//a) call setComparator() with an argument 0
		aSorter.setComparator(0);    //sorting by x-coordinate
		
		// b) call sort(). 
		aSorter.sort();
		
		//local variable keeping track of time, used later for step (e)
		long xTime = aSorter.coordinateTime;
		
		// c) use a new Point object to store the coordinates of the medianCoordinatePoint
		// d) set the medianCoordinatePoint reference to the object with the correct coordinates.
		
		medianCoordinatePoint = aSorter.getMedian();
		
		
		/////////////Second sorting round/////////////////
				
		//a) call setComparator() with an argument 1
		aSorter.setComparator(1);    //sorting by y-coordinate
			
		// b) call sort(). 
		aSorter.sort();
		
		//local variable keeping track of time, used later for step (e)
		long yTime = aSorter.coordinateTime;
			
		// c) use a new Point object to store the coordinates of the medianCoordinatePoint
		// d) set the medianCoordinatePoint reference to the object with the correct coordinates.
		
		medianCoordinatePoint = aSorter.getMedian();
		
		
		//e) sum up the times spent on the two sorting rounds and set the instance variable scanTime. 
		scanTime = xTime + yTime;
		
	}
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{

		String line;
		String sort;
		
		if(sortingAlgorithm.equals(Algorithm.QuickSort)) {
			sort = "QuickSort";
			line = String.format("%-15s %-5d %-10d\n", sort, points.length, scanTime);
			return line;
		}
		else if(sortingAlgorithm.equals(Algorithm.MergeSort)) {
			sort = "MergeSort";
			line = String.format("%-15s %-5d %-10d\n", sort, points.length, scanTime);
			return line;
		}
		else if(sortingAlgorithm.equals(Algorithm.InsertionSort)) {
			sort = "InsertionSort";
			line = String.format("%-15s %-5d %-10d\n", sort, points.length, scanTime);
			return line;
		}
		else {
			//if(sortingAlgorithm.equals(Algorithm.SelectionSort))
			sort = "SelectionSort";
			line = String.format("%-15s %-5d %-10d\n", sort, points.length, scanTime);
			return line;
		}
	}
	
	
	/**
	 * Write MCP after a call to scan(),  in the format "MCP: (x, y)"   The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{
		return "MCP: (" + medianCoordinatePoint.getX() + ", " + medianCoordinatePoint.getY() + ")"; 
	}

	
	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException
	{
		
		File file = new File("outPutFileName.txt");
		PrintWriter out = new PrintWriter(file);
		
		for(int i = 0; i < points.length; i++) {
			out.println(points[i]);
		}
		
		//closing...
		out.close();
	}	

	

		
}
