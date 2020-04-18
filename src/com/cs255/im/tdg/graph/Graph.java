package com.cs255.im.tdg.graph;

import java.util.Map;

public class Graph {
	private Map<Long, Node> nodes;
	//Some Statistices
	private int numOfVertices;
	private int numOfEdges;
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
	
	
}
