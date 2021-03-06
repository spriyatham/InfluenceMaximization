package com.cs255.im.tdg.preprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.cs255.im.tdg.graph.Graph;
import com.cs255.im.tdg.graph.Node;

/**
 * @author Charulata Lodha
 *
 */
public class CreateGraph {

	static Graph directedGraph;
	static String edgefile = "C:\\Users\\tirum\\Documents\\Priyatham\\SJSU\\courses\\Sprint2020\\CS255\\project\\datasets\\twitter.tar\\twitter\\twitter\\12831.edges";
	static String directory= "/Users/charulatalodha/MyFolder/Spring2020/DAA/Project-InfluenceMax/twitterdata";
	public static void main(String[] args) throws IOException {
		// create an instance of Graph
		directedGraph = new Graph();
		
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().contains(".edges")) {
		 

		try {

//			File myObj = new File(file);
			Scanner myReader = new Scanner(file);

			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String edge[] = data.split(" ");
				if (edge.length == 2) {
					directedGraph.increseNumOfEdges();
//					System.out.println(" Edge:" + data);
					// create a node vertex
					Node vertex1 = createNode(edge[0]);
					Node vertex2 = createNode(edge[1]);

					// record the outgoing edge
					vertex1.getOutAdjMap().put((Long)vertex2.getNodeID(), (Float)0.0f);
					
					//	record the incoming edge
					vertex2.getInAdjList().add(vertex1.getNodeID());
					

				} else if (edge.length == 1) {
					System.out.println("Single Node :" + data);
					createNode(edge[0]);
				} else {
					System.out.println("Empty Node :" + data);
				}
			} // while ends
			
			myReader.close();
				
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
	    System.out.println(file.getName());
		    }
		    }
		
		//record vertices count of Graph
		directedGraph.setNumOfVertices(directedGraph.getNodes().size());
		
		//set in and out degrees
		directedGraph.setInOutDegrees();
		//printGraph
//		directedGraph.printGraph();
		//save graph in a file
		Util.saveGraph(directedGraph, "twittergraph.ser");
	
		
	

	}

	public static Node createNode(String v) {
		
		// add new vertex in Graph if not already added before
		return directedGraph.addNode(new Node(Long.parseLong(v)));

		
	}

}
