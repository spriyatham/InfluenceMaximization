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
	int d1, d2, d3;
	float threshold;
	PriorityQueue pq;
	/**
	 * TODO Add more parameters.. 1. paramaters of COmpute Influed d1, d2, d3, Graph
	 * 2. All these should instance members of Algorithm.. 3. Computer
	 * Threshold..constant for all nodes..
	 */

	public Algorithm(Graph graph) {

		super();
		this.graph = graph;
		this.pq = new PriorityQueue(graph.getNumOfVertices());
		
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
		System.out.println("Compute Initial Influence..");
		for (Node node : nodeMap.values())
			computeInfluence(node, R); 
		System.out.println("Initial Influence Computed..");

		System.out.println("Beginning the influence process...");
		for (int i = 1; i <= nmax; i++) {
			// select a node with max influence
			Node seed = pq.poll();//0;// Select s = argmax v ∈ V\R {Infv}  //TODO:maxheap
			S.add(seed.getNodeID());
			R.add(seed.getNodeID());
			// UpdateForNewSeed(s,R,vθ,W,d1,d2,G, Inf)
			updateForNewSeed(seed, R, d1, d2, graph);
		}
		System.out.println("Seed Set....");
		System.out.print(S);
		printSeedSet(S);
	}

	void printSeedSet(Set<Long> seedSet)
	{
		Map<Long, Node> nodes =  graph.getNodes();
		for(Long nodeId : seedSet)
		{
			System.out.println(nodes.get(nodeId));
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
		float Infv = 0; //TODO: Check if this has to be initialized to 0.
		L.add(v.getNodeID()); // add in path
		Q.add(v); // Q.enqueue(v);

		HashMap<Long,Float> tempThresholds = new HashMap<Long,Float>();

		while (!Q.isEmpty()) {
			
			int childs=Q.size();
			
			for(int i=0;i<childs;i++)
			{  Node u = Q.poll();
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
					
					if (w_weight >= w_threshold) // weight ≥ θw
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
		   }
			//TODO: level code
			if (l + 1 <= d1)	
			     l++; // Check and update l
			else
				break;
		}

		//update influence of V
		//v.setInfluence(Infv);
		if(pq.contains(v))
			pq.changeInfluence(v, Infv);
		else
			pq.add(v);
		
		// Restore back θ values from θtemp .
		//not required as using tempThresholds. So, original threshold are intact;
	}

	/**
	 * S, R, ThetaV, Weights,d1, d2, G, Inf vector
	 * */
	void updateForNewSeed(Node s, Set<Long> infectedSet, int d1, int d2, Graph graph)
	{
		Map<Long, Node> nodes = graph.getNodes();
		Set<Long> path = new HashSet<Long>();
		Queue<Long> queue = new LinkedList<Long>(); // main queue used to track the bfs progress.
		Queue<Long> newQueue = new LinkedList<Long>(); // auxillary queue used to track levels of the bfs
		
		long sNodeId = s.getNodeID();
		path.add(sNodeId);
		queue.add(sNodeId);
		int level=1;
		
		while(!queue.isEmpty())
		{
			Long nId = queue.poll();
			Node node = nodes.get(nId);
			Map<Long,Float> outAdjMap = node.getOutAdjMap();
			for(Map.Entry<Long, Float> outNodeEntry : outAdjMap.entrySet())
			{
				long outNodeId = outNodeEntry.getKey();
				if(!path.contains(outNodeId) && !infectedSet.contains(outNodeId))
				{
					float weight =  outNodeEntry.getValue();
					Node outNode = nodes.get(outNodeId);
					float outNodeThreshold = outNode.getThreshold();
					if(weight >= outNodeThreshold)
					{
						//add the node to infected set
						infectedSet.add(outNodeId);
						//add the node to path.
						path.add(outNodeId);
						//set threshold to zero
						outNode.setThreshold(0);
						//enqueue if needed
						if(level <= d2)
						{
							// this node is influenced, now it can go and influence its children..
							newQueue.add(outNodeId);
						}
					}
					else
					{
						outNode.setThreshold(outNodeThreshold - weight);
						//this node's threshold is partially reduced, so may be now..other nodes,
						//which previously coudn't influence, can influence this node now. 
						updateIncomingNeighborInfuence(graph, outNode, infectedSet, d1);
					}
				}
			}
			if(queue.isEmpty() && !newQueue.isEmpty())
			{
				level++;
				queue = newQueue;
				newQueue = new LinkedList<Long>();
			}
		}
		
		
		
	}
	//Node, R, Theta, W, d1, G, Inf
	void updateIncomingNeighborInfuence(Graph graph, Node v, Set<Long> infectedSet, int d1)
	{
		Queue<Long> queue = new LinkedList<Long>(); // main queue used to track the bfs progress.
		Queue<Long> newQueue = new LinkedList<Long>(); // auxillary queue used to track levels of the bfs
		Set<Long> path = new HashSet<>(); // visited nodes.
		int level = 1; // counter to check if we covered d1 levels.
		Map<Long, Node> nodes = graph.getNodes();
		
		long cNodeId= v.getNodeID();
		queue.offer(cNodeId);
		path.add(cNodeId);
		
		while(!queue.isEmpty())
		{
			long nid = queue.poll(); // u
			Node node = nodes.get(nid);
			Map<Long, Float> outAdjMap = node.getOutAdjMap(); 
			List<Long> inAdjList = node.getInAdjList();
			for(Long inId : inAdjList) // w ; iterate over all edges w,v
			{
				if(!infectedSet.contains(inId) && !path.contains(inId))
				{
					Node inNode = nodes.get(inId);
					computeInfluence(inNode, infectedSet);
					//TODO: I still need to get more clarity on this, I should understand why we are
					//going to the next level.
					// if the edge(u,w)  exists and weight of (u,w) > theta(w) then add w to the queue.
					if((outAdjMap.containsKey(inId) && (outAdjMap.get(inId) > inNode.getThreshold())) &&
						level <= d1 )
					{
						newQueue.offer(inId);
					}
				}
			}
			if(queue.isEmpty() && !newQueue.isEmpty())
			{
				level++;
				queue = newQueue;
				newQueue = new LinkedList<Long>();
			}
		}
	}
	
	/**
	 * 
	 *  Sample code for tarcking levels, while doing bfs..
	void bfsQueueLevelTracker(Node v, Graph graph)
	{
		Map<Long, Node> nodes = graph.getNodes();
		Queue<Long> queue = new LinkedList<Long>();
		queue.offer(v.getNodeID());
		int currSize = 1;
		int level = 1;
		while(!queue.isEmpty())
		{
			Long nodeId = queue.poll();
			Node node = nodes.get(nodeId);
			currSize--;
			for(Map.Entry<Long, Float> outNodeEntry : node.getOutAdjMap().entrySet())
			{
				queue.offer(outNodeEntry.getKey());
			}
			if(currSize == 0)
			{
				currSize = queue.size();
				level++;
			}
		}
				
	}
	**/
	
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
		
		int nmax=10; //number of seed nodes.

		TDG.InfluenceMaximization(nmax);
		// printGraph
		//directedGraph.printGraph();

	}
}
