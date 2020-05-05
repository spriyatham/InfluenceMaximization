package com.cs255.im.tdg.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import com.cs255.im.tdg.graph.Graph;
import com.cs255.im.tdg.graph.Node;
import com.cs255.im.tdg.preprocessing.Util;

/**
 * This class contains the implementation Threshold Difference Greedy Algorithm, including all its subroutines.
 * 
 * **/
public class Algorithm {
	Graph graph;
	int d1, d2, d3;
	float threshold;
	PriorityQueue pq;
	Set<Long> seedSet;
	Set<Long> infectedSet;

	public Algorithm(Graph graph, float threshold) {

		super();
		this.graph = graph;
		this.pq = new PriorityQueue(graph.getNumOfVertices());

		this.d1 = 5;
		this.d2 = 20;
		this.d3 = 3;
		this.threshold = threshold;// 0.5f;

		weightThresholdInit();

	}

	
	public Set<Long> getSeedSet() {
		return seedSet;
	}


	public void setSeedSet(Set<Long> seedSet) {
		this.seedSet = seedSet;
	}

	
	
	public Set<Long> getInfectedSet() {
		return infectedSet;
	}


	public void setInfectedSet(Set<Long> infectedSet) {
		this.infectedSet = infectedSet;
	}


	/**
	 * @param graph
	 * @param d1
	 * @param d2
	 * @param d3 - ignore, we did not use d3 in our implementation.
	 */
	public Algorithm(Graph graph, int d1, int d2, int d3, float threshold) {
		super();
		this.graph = graph;
		this.d1 = d1;
		this.d2 = d2;
		this.d3 = d3;
		this.threshold = threshold;
		this.pq = new PriorityQueue(graph.getNumOfVertices());
		weightThresholdInit();
	}

	/**
	 * Assigns a fixed threshold value to each vertex in the graph.
	 * Weights are assigned to all the edges using the following scheme.
	 *  Weight of edge (v,u) = 1 / InDegree(u);
	 * */
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

	/**
	 * This function represents the TDG algorithm.
	 * Computes the spread size and seed set.
	 * */
	public void InfluenceMaximization(int nmax) {
		Set<Long> S = new LinkedHashSet<Long>(); //Changing from HashSet to LinkedHashSet to preserve order
		Set<Long> R = new HashSet<Long>();

		// Compute initial influence for each node.
		Map<Long, Node> nodeMap = this.graph.getNodes();
		System.out.println("Compute Initial Influence..");
		for (Node node : nodeMap.values())
			computeInfluence(node, R);
		System.out.println("Initial Influence Computed..");

		System.out.println("Beginning the influence process...");

		int i = 1;
		while (i <= nmax) {
			// select a node with max influence
			Node seed = pq.poll();// 0;// Select s = argmax v ∈ V\R {Infv} //TODO:maxheap

			if (R.contains(seed.getNodeID()))
				continue;
			
			System.out.println("Added new Seed "+i+" : "+seed.getNodeID());
			
			S.add(seed.getNodeID());
			R.add(seed.getNodeID());
			// UpdateForNewSeed(s,R,vθ,W,d1,d2,G, Inf)
			updateForNewSeed(seed, R, d1, d2, graph);
			i++;
		}

		System.out.println("Seed Set of Size " + nmax + " :" + S);
		//printSeedSet(S);
		setSeedSet(S);
		setInfectedSet(R);

	}
	
	/**
	 * This function is used for running the experiments for various seed sizes like 100,200,300,400.
	 * Instead of re-executing the algorithm for each seed size, we re-use the results.
	 * For example, results of 100 seed size can be reused while computing for 200 seeds.
	 * */
	public void InfluenceMaximizationIncreseSeed(int start, int nmax) {
		Set<Long> S = new LinkedHashSet<Long>(); //Changing from HashSet to LinkedHashSet to preserve order
		
		Set<Long> R = new HashSet<Long>();

		S.addAll(seedSet);
		R.addAll(infectedSet);
		System.out.println("Compute Influence for incresed seed size");
		System.out.println("Beginning the influence process...");

		int i = start;
		while (i <= nmax) {
			// select a node with max influence
			Node seed = pq.poll();// 0;// Select s = argmax v ∈ V\R {Infv} //TODO:maxheap

			if (R.contains(seed.getNodeID()))
				continue;
			
			System.out.println("Added new Seed "+i+" : "+seed.getNodeID());
			
			S.add(seed.getNodeID());
			R.add(seed.getNodeID());
			// UpdateForNewSeed(s,R,vθ,W,d1,d2,G, Inf)
			updateForNewSeed(seed, R, d1, d2, graph);
			i++;
		}

		System.out.println("Seed Set of Size " + nmax + " :" + S);
		//printSeedSet(S);
		setSeedSet(S);
		setInfectedSet(R);

	}
	

	public void printSeedSet(Set<Long> seedSet) {
		Map<Long, Node> nodes = graph.getNodes();
		
		for (Long nodeId : seedSet) {

			System.out.printf("\n[Node : %d, Inflv : %10f, InDegree: %5d ,OutDegree: %5d ]", nodeId,
					nodes.get(nodeId).getInfluence(), nodes.get(nodeId).getInDegree(),
					nodes.get(nodeId).getOutDegree());
		}
	}

	/**
	 * Computes the influence exerted by node v on its neighborhood up through, and
	 * including, level d1.
	 * The Node's influence field will be updated with new value
	 * BFS is used to traverse the graph, until d1 levels
	 * @param v is current Node whose influence is being computed.
	 * @param R is the infected node set.
	 * 
	 */
	void computeInfluence(Node v, Set<Long> R) {

		Queue<Node> Q = new LinkedList<Node>();
		Set<Long> L = new HashSet<Long>(); // Path list
		int l = 0; // level

		/*
		 * Infv is the influence exerted by node v on its neighborhood up through, and
		 * including, level d1.
		 */
		float Infv = 0.0f; // TODO: Check if this has to be initialized to 0.
		L.add(v.getNodeID()); // add in path
		Q.add(v); // Q.enqueue(v);

		HashMap<Long, Float> tempThresholds = new HashMap<Long, Float>();

		while (!Q.isEmpty()) {

			int childs = Q.size();

			for (int i = 0; i < childs; i++) {
				Node u = Q.poll();
				for (Map.Entry<Long, Float> outwardNode : u.getOutAdjMap().entrySet()) {
					float w_weight = outwardNode.getValue();
					long w_id = outwardNode.getKey();

					if (R.contains(w_id) || L.contains(w_id))
						continue;

					Node w_node = graph.getNodes().get(w_id);

					if (!tempThresholds.containsKey(w_id))
						tempThresholds.put(w_id, w_node.getThreshold());

					Float w_threshold = tempThresholds.get(w_id);

					if (w_weight >= w_threshold) // weight ≥ θw
					{
						Infv = Infv + 1.0f;
						if (l <= d1)
							Q.add(w_node);
						L.add(w_id);
					} else {
						Infv += 1.00f * (w_weight / w_threshold);
						w_threshold -= w_weight;
						tempThresholds.replace(w_id, w_threshold);
					}
				}
			}
			// TODO: level code
			if (l + 1 <= d1)
				l++; // Check and update l
			else
				break;
		}

		// update influence of V
		// v.setInfluence(Infv);
		if (pq.contains(v))
			pq.changeInfluence(v, Infv);
		else {
			v.setInfluence(Infv);
			pq.add(v);

		}

		// Restore back θ values from θtemp .
		// not required as using tempThresholds. So, original threshold are intact;
	}

	/**
	 * S - new Seed, R - Infected NodeSet,
	 * This function accepts a new seed Node s, infected nodes set and Graph object.
	 * It spreads the influence of Node s on its neighborhood by recursively performing BFS starting from s and goes 
	 * until d2 level.
	 * 
	 */
	void updateForNewSeed(Node s, Set<Long> infectedSet, int d1, int d2, Graph graph) {
		Map<Long, Node> nodes = graph.getNodes();
		Set<Long> path = new HashSet<Long>();
		Queue<Long> queue = new LinkedList<Long>(); // main queue used to track the bfs progress.
		Queue<Long> newQueue = new LinkedList<Long>(); // auxillary queue used to track levels of the bfs

		long sNodeId = s.getNodeID();
		path.add(sNodeId);
		queue.add(sNodeId);
		int level = 1;

		while (!queue.isEmpty()) {
			Long nId = queue.poll();
			Node node = nodes.get(nId);
			Map<Long, Float> outAdjMap = node.getOutAdjMap();
			for (Map.Entry<Long, Float> outNodeEntry : outAdjMap.entrySet()) {
				long outNodeId = outNodeEntry.getKey();
				if (!path.contains(outNodeId) && !infectedSet.contains(outNodeId)) {
					float weight = outNodeEntry.getValue();
					Node outNode = nodes.get(outNodeId);
					float outNodeThreshold = outNode.getThreshold();
					// completely influence a node.
					if (weight >= outNodeThreshold) {
						// add the node to infected set
						infectedSet.add(outNodeId);
						// add the node to path.
						path.add(outNodeId);
						// set threshold to zero
						outNode.setThreshold(0);
						// enqueue if needed
						if (level <= d2) {
							// this node is influenced, now it can go and influence its children..
							newQueue.add(outNodeId);
						}
					} else {
						outNode.setThreshold(outNodeThreshold - weight);
						// this node's threshold is partially reduced, so may be now..other nodes,
						// which previously coudn't influence, can influence this node now.

					}

					updateIncomingNeighborInfuence(graph, outNode, infectedSet, d1);
				}

			}
			if (queue.isEmpty() && !newQueue.isEmpty()) {
				level++;
				queue = newQueue;
				newQueue = new LinkedList<Long>();
			}
		}

	}

	/**
	 * This method computes the potential influence of nodes incident to a partially influenced node.
	 * The idea is, since the node's threshold has been reduced, may be now, one of its incident nodes might 
	 * influence it.
	 * */
	void updateIncomingNeighborInfuence(Graph graph, Node v, Set<Long> infectedSet, int d1) {
		Queue<Long> queue = new LinkedList<Long>(); // main queue used to track the bfs progress.
		Queue<Long> newQueue = new LinkedList<Long>(); // auxillary queue used to track levels of the bfs
		Set<Long> path = new HashSet<>(); // visited nodes.
		int level = 1; // counter to check if we covered d1 levels.
		Map<Long, Node> nodes = graph.getNodes();

		long cNodeId = v.getNodeID();
		queue.offer(cNodeId);
		path.add(cNodeId);

		while (!queue.isEmpty()) {
			long nid = queue.poll(); // u
			Node node = nodes.get(nid);
			Map<Long, Float> outAdjMap = node.getOutAdjMap();
			List<Long> inAdjList = node.getInAdjList();
			for (Long inId : inAdjList) // w ; iterate over all edges w,v
			{
				if (!infectedSet.contains(inId) && !path.contains(inId)) {
					Node inNode = nodes.get(inId);
					computeInfluence(inNode, infectedSet);
					// if the edge(u,w) exists and weight of (u,w) > theta(w) then add w to the
					// queue.
					if ((outAdjMap.containsKey(inId) && (outAdjMap.get(inId) > inNode.getThreshold())) && level <= d1) {
						newQueue.offer(inId);
					}
				}
			}
			if (queue.isEmpty() && !newQueue.isEmpty()) {
				level++;
				queue = newQueue;
				newQueue = new LinkedList<Long>();
			}
		}
	}

	/**
	 * 
	 * Sample code for tracking levels, while doing bfs.. void
	 * bfsQueueLevelTracker(Node v, Graph graph) { Map<Long, Node> nodes =
	 * graph.getNodes(); Queue<Long> queue = new LinkedList<Long>();
	 * queue.offer(v.getNodeID()); int currSize = 1; int level = 1;
	 * while(!queue.isEmpty()) { Long nodeId = queue.poll(); Node node =
	 * nodes.get(nodeId); currSize--; for(Map.Entry<Long, Float> outNodeEntry :
	 * node.getOutAdjMap().entrySet()) { queue.offer(outNodeEntry.getKey()); }
	 * if(currSize == 0) { currSize = queue.size(); level++; } }
	 * 
	 * }
	 **/

	/**
	 *  **NOTE: Expeimental code**
	 * The set of nodes in the graph are divided into 10 subsets.
	 * Influence of each subset of nodes is computed by one thread. 
	 * */
	void computeInitialInfluence(Algorithm algorithm) {
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
			Callable<Void> task = new InfluenceComputer(subList,algorithm);
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

	/** Experimental code **/
	class InfluenceComputer implements Callable<Void> {
		List<Node> nodeList;
		Algorithm algorithm;
		
		public InfluenceComputer(List<Node> nodes,Algorithm algorithm) {
			this.nodeList = nodes;
			this.algorithm = algorithm;
		}

		@Override
		public Void call() throws Exception {

			for (Node node : this.nodeList) {
				//we can pass an empty set because, in the initial step, infected set will be empty
				this.algorithm.computeInfluence(node, new HashSet<Long>());
			}
			return null;
		}

	}

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();

//		Graph directedGraph = Util.loadGraph("twittergraph.ser");
		Graph directedGraph = Util.loadGraph("facebookgraph.ser");
		
		System.out.println("Total Nodes: " + directedGraph.getNumOfVertices());
		System.out.println("Total Edges: " + directedGraph.getNumOfEdges());

		long graphLoadingendTime = System.currentTimeMillis();

		long duration = (graphLoadingendTime - startTime); // Total execution time in milli seconds

		System.out.println("Graph loaded in :" + duration + "ms");

		System.out.println("Calculating for Threshold = 0.5");
		Algorithm TDG = new Algorithm(directedGraph, 0.5f);

		int nmax = 10; // number of seed nodes.

		long influenceStartTime = System.currentTimeMillis();

		TDG.InfluenceMaximization(nmax);
		TDG.InfluenceMaximizationIncreseSeed(20-nmax+1,20);
		
		// printGraph
		 directedGraph.printGraph();

		long influenceEndTime = System.currentTimeMillis();

		duration = (influenceEndTime - influenceStartTime); // Total execution time in milli seconds

		System.out.println("\n\nInfluence calculated in : " + duration + "ms");

	}
}
