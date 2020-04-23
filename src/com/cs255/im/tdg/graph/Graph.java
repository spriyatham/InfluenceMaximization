package com.cs255.im.tdg.graph;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Graph implements Serializable{
	
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
		   node.setOutDegree(node.outAdjMap.size());
		}
		
	}
	
	
	
}
