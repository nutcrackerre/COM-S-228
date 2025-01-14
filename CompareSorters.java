package edu.iastate.cs228.hw1;

/**
 *  
 * @author Iteoluwakishi Osomo
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class CompareSorters {
	/**
	 * Repeatedly take integer sequences either randomly generated or read from
	 * files. Use them as coordinates to construct points. Scan these points with
	 * respect to their median coordinate point four times, each time using a
	 * different sorting algorithm.
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {
		/*
		 * Given Instructions:
		 * Conducts multiple rounds of comparison of four sorting algorithms. Within
		 * each round, set up scanning as follows:
		 * 
		 * a) If asked to scan random points, calls generateRandomPoints() to initialize
		 * an array of random points.
		 * 
		 * b) Reassigns to the array scanners[] (declared below) the references to four
		 * new PointScanner objects, which are created using four different values of
		 * the Algorithm type: SelectionSort, InsertionSort, MergeSort and QuickSort.
		 * 
		 * 
		 * 
		 * PointScanner[] scanners = new PointScanner[4];
		 * 
		 * For each input of points, do the following.
		 * 
		 * a) Initialize the array scanners[].
		 * 
		 * b) Iterate through the array scanners[], and have every scanner call the
		 * scan() method in the PointScanner class.
		 * 
		 * c) After all four scans are done for the input, print out the statistics
		 * table from section 2.
		 * 
		 * A sample scenario is given in Section 2 of the project description.
		 */

		PointScanner[] scanners = new PointScanner[4];

		Scanner scnr = new Scanner(System.in);
		int numTrials = 0;
		int choice;
		boolean exit = false;
		
		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");
		System.out.println();
		System.out.println("Keys: 1(random integers)  2(file input)  3(exit)");

		while (!exit) {
			numTrials++;
			System.out.print("Trial " + numTrials + ": ");
			choice = scnr.nextInt();

			if (choice == 1) {
				int numPoints;
				Random randNum = new Random();
				System.out.print("Enter a number of random points: ");
				numPoints = scnr.nextInt();

				Point[] points = generateRandomPoints(numPoints, randNum);

				PointScanner selection = new PointScanner(points, Algorithm.SelectionSort);
				PointScanner insertion = new PointScanner(points, Algorithm.InsertionSort);
				PointScanner merge = new PointScanner(points, Algorithm.MergeSort);
				PointScanner quick = new PointScanner(points, Algorithm.QuickSort);

				PointScanner[] tempArray = { selection, insertion, merge, quick };
				scanners = tempArray;
			
				System.out.println();
				System.out.println("algorithm   size   time(ns)");
				System.out.println("----------------------------------");

				for (int i = 0; i < scanners.length; i++) {
					scanners[i].scan();
					System.out.print(scanners[i].stats());
				}

				System.out.println("----------------------------------");
				System.out.println();

			} else if (choice == 2) {
				System.out.println("Points from a file");
				System.out.print("File name: ");
				String fileName = scnr.next();

				PointScanner selection = new PointScanner(fileName, Algorithm.SelectionSort);
				PointScanner insertion = new PointScanner(fileName, Algorithm.InsertionSort);
				PointScanner merge = new PointScanner(fileName, Algorithm.MergeSort);
				PointScanner quick = new PointScanner(fileName, Algorithm.QuickSort);

				PointScanner[] tempArray = { selection, insertion, merge, quick };
				scanners = tempArray;

				System.out.println();
				System.out.println("algorithm   size   time(ns)");
				System.out.println("----------------------------------");

				for (int i = 0; i < scanners.length; i++) {
					scanners[i].scan();
					System.out.print(scanners[i].stats());
				}

				System.out.println("----------------------------------");
				System.out.println();

			} 
			else if (choice == 3) {
				System.out.println("...");
				exit = true;
			}

		}

	}

	/**
	 * This method generates a given number of random points. The coordinates of
	 * these points are pseudo-random numbers within the range [-50,50] � [-50,50].
	 * Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing.
	 * 
	 * @param numPts number of points
	 * @param rand   Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException {
		
		if(numPts < 1) {
			throw new IllegalArgumentException();
		}
		
		ArrayList<Point> list = new ArrayList<>();
		
		for(int i = 0; i < numPts; i++) {
			int x = rand.nextInt(101) - 50;
			int y = rand.nextInt(101) - 50;
			Point p = new Point(x, y);
			list.add(p);
		}
		
		//Deep copy array list into an array
		Point[] pts = new Point[numPts];
		for(int j = 0; j < list.size(); j++) {
			pts[j] = list.get(j);
		}
		
		return pts;
		
	}

}
