package com.cs255.im.tdg.graph;

import java.io.Serializable;

/**
 * @author Sriarm Priyatham Siram 
 *
 */
/**
 * Represents an element of the Outward Adjacency list.
 * */
public class AdjNode implements Serializable{
	long nodeID;
	float weight;
	
	public AdjNode(long nodeID, float weight) {
	
		this.nodeID = nodeID;
		this.weight = weight;
	}

	public long getNodeID() {
		return nodeID;
	}
	
	public void setNodeID(long nodeID) {
		this.nodeID = nodeID;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return ""+ this.nodeID+"";
	}
	
	public String toString2() {
		return "AdjNode [nodeID=" + nodeID + ", weight=" + weight + "]";
	}

	
}
