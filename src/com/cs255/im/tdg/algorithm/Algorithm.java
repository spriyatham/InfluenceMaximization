package com.cs255.im.tdg.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.cs255.im.tdg.graph.AdjNode;
import com.cs255.im.tdg.graph.Graph;
import com.cs255.im.tdg.graph.Node;
import com.cs255.im.tdg.preprocessing.Util;

public class Algorithm {
	Graph graph;
	float d1, d2, d3;
	float threshold;

	/**
	 * TODO Add more parameters.. 1. paramaters of COmpute Influed d1, d2, d3, Graph
	 * 2. All these should instance members of Algorithm.. 3. Computer
	 * Threshold..constant for all nodes..
	 */

	public Algorithm(Graph graph) {

		super();
		this.graph = graph;

		this.d1 = 5;
		this.d2 = 20;
		this.d3 = 3;
		this.threshold = 0.5f;

		weightThresholdInit();

	}

	/**
	 * @param graph
	 * @param d1
	 * @param d2
	 * @param d3
	 */
	public Algorithm(Graph graph, float d1, float d2, float d3, float threshold) {
		super();
		this.graph = graph;
		this.d1 = d1;
		this.d2 = d2;
		this.d3 = d3;
		this.threshold = threshold;
		weightThresholdInit();
	}

	void weightThresholdInit() {
		Map<Long, Node> nodeMap = this.graph.getNodes();
		for (Node node : nodeMap.values()) { // set threshold
			node.setThreshold(threshold);

			// calculate weight for all edges : outgoingEdge
			for (Map.Entry<Long, Float> outwardsNode : node.getOutAdjMap().entrySet()) {
				outwardsNode.setValue((1.00f / nodeMap.get(outwardsNode.getKey()).getInDegree()));
			}
		}
	}

	void InfluenceMaximization(int nmax) {
		Set<Long> S = new HashSet<Long>();
		Set<Long> R = new HashSet<Long>();

		// Compute initial influence for each node.
		Map<Long, Node> nodeMap = this.graph.getNodes();
		for (Node node : nodeMap.values())
			computeInfluence(node, R); 

		for (int i = 1; i <= nmax; i++) {
			// select a node with max influence
			long s = 0;// Select s = argmax v ∈ V\R {Infv}  //TODO:maxheap
			S.add(s);
			R.add(s);
			// See Algorithm 5.
			// UpdateForNewSeed(s,R,vθ,W,d1,d2,G, Inf)
		}
	}


	void computeInfluence(Node v, Set<Long> R) {

		Queue<Node> Q = new LinkedList<Node>();
		Set<Long> L = new HashSet<Long>(); // Path list
		int l = 0; // level

		/*
		 * Infv is the influence exerted by node v on its neighborhood up through, and
		 * including, level d1.
		 */
		float Infv = 0;
		L.add(v.getNodeID()); // add in path
		Q.add(v); // Q.enqueue(v);

		HashMap<Long,Float> tempThresholds = new HashMap<Long,Float>();

		while (!Q.isEmpty()) {
			Node u = Q.poll();

			for (Map.Entry<Long, Float> outwardNode : u.getOutAdjMap().entrySet())
			{
				float w_weight=outwardNode.getValue();
				long  w_id=outwardNode.getKey();
				
				if (R.contains(w_id) || L.contains(w_id) ) 
					continue;
				
				Node w_node = graph.getNodes().get(w_id);

				if(!tempThresholds.containsKey(w_id))
					tempThresholds.put(w_id,w_node.getThreshold());
								
				Float w_threshold= tempThresholds.get(w_id);
				
				if (w_weight > w_threshold) // weight ≥ θw
				{
					Infv = Infv + 1;
					if (l <= d1)
						Q.add(w_node);
					L.add(w_id);
				}
				else
				{
					Infv += w_weight/w_threshold;
					w_threshold -=w_weight;
					tempThresholds.replace(w_id, w_threshold);
				}
			}
			//TODO: level code
			if (l + 1 <= d1)	
			     l++; // Check and update l
			else
				break;
		}

		//update influence of V
		v.setInfluence(Infv);
		
		// Restore back θ values from θtemp .
		//not required as using tempThresholds. So, original threshold are intact;
	}

	void computeInitialInfluence() {
		int s = 10; // s splits
		Map<Long, Node> nodes = this.graph.getNodes();

		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(s);
		ArrayList<Node> nodeList = new ArrayList<Node>(nodes.values());
		int numOfVertices = nodeList.size();
		List<List<Node>> subLists = new ArrayList<>();
		int start = 0;
		List<Future<Void>> resultList = new ArrayList<>();
		while (start < numOfVertices) {
			int end = (start + s) > numOfVertices ? numOfVertices : start + s;
			List<Node> subList = nodeList.subList(start, end);
			Callable<Void> task = new InfluenceComputer(subList);
			resultList.add(executor.submit(task));
		}
		// This sort of acts like a joint to wait for all the threads to complete
		// execution.
		for (Future<Void> taskResult : resultList) {
			try {
				taskResult.get();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	class InfluenceComputer implements Callable<Void> {
		List<Node> nodeList;

		public InfluenceComputer(List<Node> nodes) {
			this.nodeList = nodes;
		}

		@Override
		public Void call() throws Exception {

			for (Node node : this.nodeList) {
				computeInfluence(node);
			}
			return null;
		}

		private void computeInfluence(Node node) {
			// TODO Auto-generated method stub
			
		}
	}

	public static void main(String[] args) {

		Graph directedGraph = Util.loadGraph("twittergraph.ser");

		Algorithm TDG = new Algorithm(directedGraph);
		
		System.out.print("Hello: Algo");
		
		int nmax=6; //number of seed nodes.

		TDG.InfluenceMaximization(nmax);
		// printGraph
		directedGraph.printGraph();

	}
}
