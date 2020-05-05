package com.cs255.im.tdg.preprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.cs255.im.tdg.graph.Graph;
import com.cs255.im.tdg.graph.Node;

/**
 * @author Charulata Lodha
 *
 *usage CreateGraphV2 <Absolute path of input Directory or File> <absolutepath of outputFile>
 */
public class CreateGraphV2 {

	static Graph directedGraph;
	public static void main(String[] args) throws IOException {
		// create an instance of Graph
		directedGraph = new Graph();
		String dir = args[0];
		String serFile = args[1];
		
		File folder = new File(dir);
		ArrayList<File> listOfFiles = new ArrayList<File>();
		if(folder.isDirectory())
		{
			for(File file :folder.listFiles())
			{
				if (file.isFile() && file.getName().contains(".edges"))
				{
					listOfFiles.add(file);
				}
			}
		}
		else
		{
			listOfFiles.add(folder);
		}
		for (File file : listOfFiles) {
		    
		
		 
//		  File file = new File(directory);

		try {

			
			Scanner myReader = new Scanner(file);

			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String edge[] = data.split("\\s+");
				
				if (edge.length == 3) {
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
		
		//record vertices count of Graph
		directedGraph.setNumOfVertices(directedGraph.getNodes().size());
		
		//set in and out degrees
		directedGraph.setInOutDegrees();
		//printGraph
		directedGraph.printGraph();
	
		//save graph in a file
		Util.saveGraph(directedGraph, serFile);
		
		Long n1 =13577l;
		Node n= directedGraph.getNodes().get(n1);
		
		System.out.println(n.getInAdjList());
		System.out.println(n.getOutAdjMap().keySet());
	
		System.out.println("Cycle :");
		
		List<Long> l3 = new ArrayList<Long>(n.getOutAdjMap().keySet());
		l3.retainAll(n.getInAdjList());
		
		System.out.println(l3);
		
		
		
	

	}

	public static Node createNode(String v) {
		
		// add new vertex in Graph if not already added before
		return directedGraph.addNode(new Node(Long.parseLong(v)));

		
	}

}