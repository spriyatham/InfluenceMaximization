package com.cs255.im.tdg.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sriarm Priyatham Siram
 *
 */

/**
 * This class encloses the following information about a node.
 * 1. nodeID
 * 2. outDegree
 * 3. outWard Adjacency List - Linked List of adjacent nodes. Each entry of the linked list contains <NodeID,Weight> pairs.
 *	//TODO: Look at how edge waits are represented in adjacency list representation.
 * 4. InDegree Adjacency List - Only node IDs..
 * 
 * Values 5 and 6 will not be computed in the pre-processing step as the values are dependent on the inputs.
 * 5. Threshold value.
 * 6. Influence.
 * 
 * */

public class Node implements Serializable{
	long nodeID;
	
	int outDegree;
	Map<Long,Float> outAdjMap;
	
	int inDegree;
	List<Long> inAdjList;
	
	/**
	 * @note: These will be computed during the algorithm execution.
	 * */
	
	float threshold;
	float influence;
	
	
	
	/**
	 * @param nodeID
	 * @param outDegree
	 * @param outAdjList
	 * @param inDegree
	 * @param inAdjList
	 * @param threshold
	 * @param influence
	 */
	public Node() {
		super();
		this.outAdjMap = new HashMap<Long, Float>();
		this.inAdjList = new ArrayList<Long>();
		
	}
	
	public Node(long nodeID) {
		this.nodeID = nodeID;
		this.outAdjMap = new HashMap<Long, Float>();
		this.inAdjList = new ArrayList<Long>();
		
	}
	public long getNodeID() {
		return nodeID;
	}
	public void setNodeID(long nodeID) {
		this.nodeID = nodeID;
	}
	public int getOutDegree() {
		return outDegree;
	}
	public void setOutDegree(int outDegree) {
		this.outDegree = outDegree;
	}
	public Map<Long, Float> getOutAdjMap() {
		return this.outAdjMap;
	}
	public void setOutAdjList(Map<Long, Float> outAdjMap) {
		this.outAdjMap = outAdjMap;
	}
	public int getInDegree() {
		return inDegree;
	}
	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}
	public List<Long> getInAdjList() {
		return this.inAdjList;
	}
	public void setInAdjList(List<Long> inAdjList) {
		this.inAdjList = inAdjList;
	}
	public float getThreshold() {
		return threshold;
	}
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
	public float getInfluence() {
		return influence;
	}
	public void setInfluence(float influence) {
		this.influence = influence;
	}

	@Override
	public String toString() {
		return  ":"+ String.format("%-10s [outDegree: %-2s inDegree:%-2s threshold: %s influence: %s]",nodeID,outDegree, inDegree,threshold,influence )
				+ " outAdjList=" + outAdjMap.toString() 
				+ "   inAdjList=" + inAdjList.toString(); 
				
	}
	
		
}
