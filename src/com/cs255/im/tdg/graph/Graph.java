package com.cs255.im.tdg.graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Graph {
	
	//Graph has a map of nodes -> each node has out & in adjacency list
	private Map<Long, Node> nodes;
	
	
	//Some Statistics
	private int numOfVertices;
	private int numOfEdges;
	
	
	public Graph() {
		 nodes = new HashMap<Long, Node>();
	}
	//add nodes
	public Node addNode(Node vertex) {
		if (this.nodes.get(vertex.getNodeID()) == null)
			this.nodes.put(vertex.nodeID, vertex);
		
	  return this.getNodes().get(vertex.getNodeID());
		
		
	}
	public Map<Long, Node> getNodes() {
		return nodes;
	}
	public void setNodes(Map<Long, Node> nodes) {
		this.nodes = nodes;
	}
	public int getNumOfVertices() {
		return numOfVertices;
	}
	public void setNumOfVertices(int numOfVertices) {
		this.numOfVertices = numOfVertices;
	}
	public int getNumOfEdges() {
		return numOfEdges;
	}
	public void setNumOfEdges(int numOfEdges) {
		this.numOfEdges = numOfEdges;
	}
	
	public void increseNumOfEdges() {
		this.numOfEdges ++;
	}
	
	//print complete graph
	public void printGraph() {
		
		System.out.println("Graph : [ numOfVertices=" + numOfVertices + ", numOfEdges=" + numOfEdges + "] \nVertices List:");
		
		printNodeMap();
		
	}
	
	//print all nodes of the graph
	private String printNodeMap() {
		
		int n=0;
		for (Node node : this.nodes.values())  
		{ System.out.println("Vertex "+ (n++)+ node.toString());}
		
		return null;
	}
	
	public void setInOutDegrees() {
		// TODO Auto-generated method stub
		
		for (Node node : this.nodes.values())
		{
		   node.setInDegree(node.inAdjList.size());
		   node.setOutDegree(node.outAdjList.size());
		}
		
	}
	
	
	public String createFile()
	{
		   
		    try {
		      File myObj = new File("Graph.txt");
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		        
		      } else {
		        System.out.println("File already exists.");
		      }
		      
		      return myObj.getPath();
		      
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
			return "Graph.txt";
		    
		   
	}
	
	public void writeToFile() {
		  
		    try {
		    	
		      FileWriter myWriter = new FileWriter(this.createFile());
		      myWriter.write("add code here, call : printGraph()");
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		  }
	
}
