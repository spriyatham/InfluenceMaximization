package com.cs255.im.tdg.preprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.cs255.im.tdg.graph.AdjNode;
import com.cs255.im.tdg.graph.Graph;
import com.cs255.im.tdg.graph.Node;

/**
 * @author Charulata Lodha
 *
 */
public class CreateGraph {

	static Graph directedGraph;
	static String file = "/Users/charulatalodha/MyFolder/Spring2020/DAA/Project-InfluenceMax/twitterdata/f1/12831.edges";

	public static void main(String[] args) {
		// create an instance of Graph
		directedGraph = new Graph();

		try {

			File myObj = new File(file);
			Scanner myReader = new Scanner(myObj);

			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String edge[] = data.split(" ");
				if (edge.length == 2) {
					directedGraph.increseNumOfEdges();
//					System.out.println(" Edge:" + data);
					// create a node vertex
					Node vertex1 = createVertexNode(edge[0]);
					Node vertex2 = createVertexNode(edge[1]);

					// record the outgoing edge
					vertex1.getOutAdjList().add(new AdjNode(vertex2.getNodeID(), 0));
					
					//	record the incoming edge
					vertex2.getInAdjList().add(vertex1.getNodeID());
					

				} else if (edge.length == 1) {
					System.out.println("Single Node :" + data);
					createVertexNode(edge[0]);
				} else {
					System.out.println("Empty Node :" + data);
				}
			} // while ends
			
			myReader.close();
			
			//record vertices count of Graph
			directedGraph.setNumOfVertices(directedGraph.getNodes().size());
			
			//set in and out degrees
			directedGraph.setInOutDegrees();
			//printGraph
			directedGraph.printGraph();
			
			
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public static Node createVertexNode(String v) {
		
		// add new vertex in Graph if not already added before
		return directedGraph.addNode(new Node(Long.parseLong(v)));

		
	}

}
