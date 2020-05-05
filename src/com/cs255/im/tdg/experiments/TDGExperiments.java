package com.cs255.im.tdg.experiments;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.cs255.im.tdg.algorithm.Algorithm;
import com.cs255.im.tdg.graph.Graph;
import com.cs255.im.tdg.graph.Node;
import com.cs255.im.tdg.preprocessing.Util;

public class TDGExperiments {

	/***
	 * Usage: TDGExperiments <graphPath> <d1> <d2> <d3> <threshold> <csv of maxseednums> <outputfile>
	 * @throws IOException 
	 * 
	 * */
	public static void main(String[] args) throws IOException {
		int d1,d2,d3;
		int[] seedSizes;
		String logFile;
		float threshold;
		
		String graphFilePath = args[0];
		
		d1 = args[1].equalsIgnoreCase("inf")? Integer.MAX_VALUE : Integer.parseInt(args[1]);
		d2 = args[2].equalsIgnoreCase("inf")? Integer.MAX_VALUE : Integer.parseInt(args[2]);
		d3 = args[3].equalsIgnoreCase("inf")? Integer.MAX_VALUE : Integer.parseInt(args[3]);
		threshold = Float.parseFloat(args[4]);
		String[] seedSizesArr = args[5].split(",");
		seedSizes = new int[seedSizesArr.length];
		int i = 0;
		for(String seedSize : seedSizesArr)
		{
			seedSizes[i++] = Integer.parseInt(seedSize);
		}
		logFile = args[6];
		
		 
		Tracer tracer = new Tracer(logFile, d1, d2, d3, threshold,seedSizes[0]);
		Graph graph = Util.loadGraph(graphFilePath);
		Algorithm algorithm = new Algorithm(graph, d1, d2, d3, threshold);
		
		//first initialization
		tracer.trace("SeedSize = " + seedSizes[0]);
		tracer.trace("Threshold = " + threshold);
		tracer.trace("Total Nodes: " + graph.getNumOfVertices());
		tracer.trace("Total Edges: " + graph.getNumOfEdges());
		
		long influenceStartTime = System.currentTimeMillis();
		algorithm.InfluenceMaximization(seedSizes[0]);
		long influenceEndTime = System.currentTimeMillis();
		long duration = (influenceEndTime - influenceStartTime); // Total execution time in milli seconds

		tracer.trace("\n\nInfluence calculated in : " + duration + "ms");
		//Print Seed Set...
		printSeedSet(algorithm.getSeedSet(), graph, tracer);
		//InfectedNode size..
		tracer.trace("Infected set size : " + algorithm.getInfectedSet().size()+"\n\n");
		
		//do incremental calculation for each seed size
		for(int j =1; j < seedSizes.length; j++)
		{   tracer = new Tracer(logFile, d1, d2, d3, threshold,seedSizes[j]);
			tracer.trace("SeedSize = " + seedSizes[j]);
			tracer.trace("Threshold = " + threshold);
			tracer.trace("Total Nodes: " + graph.getNumOfVertices());
			tracer.trace("Total Edges: " + graph.getNumOfEdges());
			
			//poll more seeds
			 algorithm.InfluenceMaximizationIncreseSeed(seedSizes[j-1] + 1,seedSizes[j]);
			 influenceEndTime = System.currentTimeMillis();
			 duration = (influenceEndTime - influenceStartTime); // Total execution time in milli seconds

			tracer.trace("\n\nInfluence calculated in : " + duration + "ms");
			//Print Seed Set...
			printSeedSet(algorithm.getSeedSet(), graph, tracer);
			//InfectedNode size..
			tracer.trace("Infected set size : " + algorithm.getInfectedSet().size()+"\n\n");
		}
		
		
	}
	
	public static void printSeedSet(Set<Long> seedSet, Graph graph, Tracer tracer) {
		Map<Long, Node> nodes = graph.getNodes();
		tracer.trace("seed set size : " + seedSet.size());
		for (Long nodeId : seedSet) {

			tracer.trace(String.format("\n[Node : %d, Inflv : %10f, InDegree: %5d ,OutDegree: %5d ]", nodeId,
					nodes.get(nodeId).getInfluence(), nodes.get(nodeId).getInDegree(),
					nodes.get(nodeId).getOutDegree()));
		}
	}

}
