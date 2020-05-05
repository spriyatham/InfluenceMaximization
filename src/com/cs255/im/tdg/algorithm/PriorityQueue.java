package com.cs255.im.tdg.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.cs255.im.tdg.graph.Node;
/**
 * Code taken from https://www.techiedelight.com/min-heap-max-heap-implementation-in-java/
 * */
// class for implementing Priority Queue 
class PriorityQueue
{
	// vector to store heap elements
	private Vector<Node> A;
	private Map<Long, Integer> reverseIndex;
	// constructor: use default initial capacity of vector
	public PriorityQueue()
	{
		A = new Vector();
		reverseIndex = new HashMap<Long, Integer>();
	}

	// constructor: set custom initial capacity for vector
	public PriorityQueue(int capacity)
	{
		A = new Vector(capacity);
		reverseIndex = new HashMap<Long, Integer>();
	}

	// return parent of A.get(i)
	private int parent(int i)
	{
		// if i is already a root node
		if (i == 0)
			return 0;

		return (i - 1) / 2;
	}

	// return left child of A.get(i)
	private int LEFT(int i)
	{
		return (2 * i + 1);
	}

	// return right child of A.get(i)
	private int RIGHT(int i)
	{
		return (2 * i + 2);
	}

	// swap values at two indexes
	void swap(int x, int y)
	{
		// swap with child having greater value
		Node temp = A.get(x);
		Node node1 = A.get(y);
		A.setElementAt(node1, x);
		A.setElementAt(temp, y);
		reverseIndex.put(node1.getNodeID(), x);
		reverseIndex.put(temp.getNodeID(), y);
	}

	// Recursive Heapify-down procedure. Here the node at index i
	// and its two direct children violates the heap property
	private void heapify_down(int i)
	{
		// get left and right child of node at index i
		int left = LEFT(i);
		int right = RIGHT(i);

		int largest = i;

		// compare A.get(i) with its left and right child
		// and find smallest value
		if (left < size() && A.get(left).getInfluence() > A.get(i).getInfluence()) {
			largest = left;
		}

		if (right < size() && A.get(right).getInfluence() > A.get(largest).getInfluence()) {
			largest = right;
		}

		if (largest != i)
		{
			// swap with child having lesser value
			swap(i, largest);

			// call heapify-down on the child
			heapify_down(largest);
		}
	}

	// Recursive Heapify-up procedure
	private void heapify_up(int i)
	{
		// check if node at index i and its parent violates
		// the heap property
		if (i > 0 && A.get(parent(i)).getInfluence() < A.get(i).getInfluence())
		{
			// swap the two if heap property is violated
			swap(i, parent(i));

			// call Heapify-up on the parent
			heapify_up(parent(i));
		}
	}

	// return size of the heap
	public int size()
	{
		return A.size();
	}

	// check if heap is empty or not
	public Boolean isEmpty()
	{
		return A.isEmpty();
	}

	// insert specified key into the heap
	public void add(Node key)
	{
		// insert the new element to the end of the vector
		A.addElement(key);
		
		// get element index and call heapify-up procedure
		int index = size() - 1;
		reverseIndex.put(key.getNodeID(), index);
		heapify_up(index);
	}

	// function to remove and return element with highest priority 
	// (present at root). It returns null if queue is empty
	public Node poll()
	{
		try {
			// if heap is empty, throw an exception
			if (size() == 0) {
				throw new Exception("Index is out of range (Heap underflow)");
			}

			// element with highest priority
			Node root = A.firstElement();	// or A.get(0);
			reverseIndex.remove(root.getNodeID());
			
			// replace the root of the heap with the last element of the vector
			Node lastNode = A.lastElement();
			A.setElementAt(lastNode, 0);
			A.remove(size()-1);
			reverseIndex.put(lastNode.getNodeID(), 0);
			// call heapify-down on root node
			heapify_down(0);

			// return root element
			return root;
		}
		// catch and print the exception
		catch (Exception ex) {
			System.out.println(ex);
			return null;
		}
	}
	
	/*
	 * This function increases or decreases the priority(Influence) of node and heapifies the Priority Queue again.
	 * */
	void changeInfluence(Node node, float newVal)
	{
		long nodeId = node.getNodeID();
		int index = reverseIndex.get(nodeId);
		float oldVal = node.getInfluence();
		node.setInfluence(newVal);
		if(newVal < oldVal)
		{
			//heapifyDown
			heapify_down(index);
		}
		else if(newVal > oldVal)
		{
			heapify_up(index);
		}
	}

	// function to return, but does not remove, element with highest priority 
	// (present at root). It returns null if queue is empty
	public Node peek()
	{
		try {
			// if heap has no elements, throw an exception
			if (size() == 0) {
				throw new Exception("Index out of range (Heap underflow)");
			}

			// else return the top (first) element
			return A.firstElement();	// or A.get(0);
		}
		// catch the exception and print it, and return null
		catch (Exception ex) {
			System.out.println(ex);
			return null;
		}
	}

	// function to remove all elements from priority queue
	public void clear()
	{
		System.out.print("Emptying queue: ");
		while (!A.isEmpty()) {
			System.out.print(poll() + " ");
		}
		System.out.println();
	}

	// returns true if queue contains the specified element
	public Boolean contains(Node i)
	{
		Long id = i.getNodeID();
		if(reverseIndex.containsKey(id))
		{
			return true;
		}
		return false;
	}

	// returns an array containing all elements in the queue
	public Node[] toArray()
	{
		return A.toArray(new Node[size()]);
	}
}

class Main
{
	// Program for Max Heap Implementation in Java
	public static void main (String[] args)
	{
		// create a Priority Queue of initial capacity 10
		// Priority of an element is decided by element's value
		PriorityQueue pq = new PriorityQueue(10);

		// insert three integers
		Node n1 = new Node(1); n1.setInfluence(4);
		Node n2 = new Node(2); n2.setInfluence(2);
		Node n3 = new Node(3); n3.setInfluence(8);
		Node n4 = new Node(4); n4.setInfluence(1);
		Node n5 = new Node(5); n5.setInfluence(9);
		Node n6 = new Node(6); n6.setInfluence(5);
		pq.add(n1);
		pq.add(n2);
		pq.add(n3);
		pq.add(n4);
		pq.add(n5);
		pq.add(n6);
		Node top = pq.peek();
		System.out.println(top.getNodeID() + " : " + top.getInfluence());
		pq.changeInfluence(n2, 10);
		top = pq.peek();
		System.out.println(top.getNodeID() + " : " + top.getInfluence());

		// print Priority Queue size
		System.out.println("Priority Queue Size is " + pq.size());

		// search 2 in Priority Queue
		//Integer searchKey = 2;

		

		// empty queue
		pq.clear();

		if (pq.isEmpty()) {
			System.out.println("Queue is Empty");
		}

		System.out.println("\nCalling remove operation on an empty heap");
		System.out.println("Element with highest priority is " + pq.poll() + '\n');

		System.out.println("Calling peek operation on an empty heap");
		System.out.println("Element with highest priority is " + pq.peek() + '\n');

		// again insert three integers
		/*
		 * pq.add(5); pq.add(4); pq.add(45);
		 * 
		 * // construct array containing all elements present in the queue Integer[] I =
		 * pq.toArray(); System.out.println("Printing array: " + Arrays.toString(I));
		 * 
		 * System.out.println("\nElement with highest priority is " + pq.poll());
		 * System.out.println("Element with highest priority is " + pq.peek());
		 */
	}
}