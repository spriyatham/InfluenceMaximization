package com.cs255.im.tdg.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.cs255.im.tdg.graph.Graph;
import com.cs255.im.tdg.graph.Node;

public class Algorithm {
	Graph graph;
	/**
	 * TODO Add more parameters..
	 * */
	void computeInfluence(Node v)
	{
		
	}
	
	void computeInitialInfluence()
	{
		int s = 10; // s splits
		Map<Long, Node> nodes = this.graph.getNodes();
		
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(s);
		ArrayList<Node> nodeList = new ArrayList<Node>(nodes.values());
		int numOfVertices = nodeList.size();
		List<List<Node>> subLists = new ArrayList<>();
		int start = 0;
		List<Future<Void>> resultList = new ArrayList<>();
		while(start < numOfVertices)
		{
			int end = (start + s) > numOfVertices ? numOfVertices : start + s;
			List<Node> subList = nodeList.subList(start, end);
			Callable<Void> task = new InfluenceComputer(subList);
			resultList.add(executor.submit(task));
		}
		//This sort of acts like a joint to wait for all the threads to complete execution.
		for(Future<Void> taskResult : resultList)
		{
			try {
				taskResult.get();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
				
				
	}
	
	class InfluenceComputer implements Callable<Void>
	{
		List<Node> nodeList;
		
		public InfluenceComputer(List<Node> nodes)
		{
			this.nodeList = nodes;
		}
		
		@Override
		public Void call() throws Exception {
			
			for(Node node : this.nodeList)
			{
				computeInfluence(node);
			}
			return null;
		}
	}
}
