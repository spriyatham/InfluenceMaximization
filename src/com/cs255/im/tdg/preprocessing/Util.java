package com.cs255.im.tdg.preprocessing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs255.im.tdg.graph.Graph;
import com.cs255.im.tdg.graph.Node;

public class Util {

	/**
	 * Serialize the Graph.
	 * */
	public static void saveGraph(Graph graph, String file) throws IOException
	{
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			System.out.println("saveGraph: "+ file+ " absent, re-throwing error.");
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			System.out.println("saveGraph: some IO exception, re-throwing error");
			e.printStackTrace();
			throw e;
		}
		out.writeObject(graph);
		out.close();
		System.out.println("saveGraph: graph successfully saved.");
	}
	
	/**
	 * Deserialize and return a Graph object. 
	 * */
	public static Graph loadGraph(String file)
	{   
		ObjectInputStream in = null;
		Graph graph = null;
		try {
			in = new ObjectInputStream(new FileInputStream(file));
			graph = (Graph) in.readObject();
		}
		catch(Exception e)
		{
			System.out.println("saveGraph: some exception, re-throwing error");
			e.printStackTrace();
		}
		return graph;
	}
	
	/**
	 * Another version of EdgeWeight Computation
	 * 
	 * */
	public static void computeEdgeWeight(Graph graph)
	{
		Map<Long, Node> nodes = graph.getNodes();
		for(Map.Entry<Long, Node> node: nodes.entrySet())
		{
			//for each node in the graph
			Node nodeObj = node.getValue();
			long nodeId = nodeObj.getNodeID();
			int inDegree = nodeObj.getInDegree();
			if(inDegree != 0)
			{
				//determine the weight as a function of inDegree.
				float weight = 1 / (float)inDegree;
				List<Long> inAdjList = nodeObj.getInAdjList();
				for(Long inNodeId : inAdjList)
				{
					//update the weight of each incident edge; this is done by updating the adjacency map of 
					//each incident node.
					Node inNode = nodes.get(inNodeId);
					Map<Long, Float> adjMap = inNode.getOutAdjMap();
					adjMap.put(nodeId, weight);
				}
			}
		}
	} 
}
