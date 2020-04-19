package com.cs255.im.tdg.preprocessing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.cs255.im.tdg.graph.Graph;

public class Util {

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
}
