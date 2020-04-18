package com.cs255.im.tdg.graph;
/**
 * @author Sriarm Priyatham Siram
 *
 */
/**
 * Represnets an elment of the Outward Adjacency list.
 * */
public class AdjNode {
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
	
}
