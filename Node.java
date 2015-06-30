import java.util.ArrayList;

/**
 * Node class
 * This class defines a node(vertex) in a graph
 */
class Node {
	int name,color;		
	ArrayList<Node> adjList;
	/**
	 * Constructor. Sets default color value to -1. 
	 *
	 * @param value The node ID
	 */
	Node(int name) {
		this.name = name;
		color=-1;
		adjList=new ArrayList<Node>();
	}

	/**
	 * getNodeName method
	 * This method returns the node ID of the current node
	 *
	 * @return Node ID / Name of the node
	 */
	public int getNodeName() {
		return this.name;
	}
	/**
	 * getColor method
	 * This method returns the node color
	 *
	 * @return Node color of the node
	 */	
	public int getColor() {
		return this.color;
	}
	/**
	 * setColor method
	 * This method sets the node color
	 *
	 * @return Node color of the node
	 */	
	public void setColor(int color) {
		this.color=color;
	}
	/**
	 * toString method
	 * This method prints the node color and name. 
	 *
	 * @return print node name color of the node
	 */	
	public String toString() {
		return Integer.toString(name)+" "+"color="+color;
	}
	/**
	 * Gets list of adjacent vertices of current node. 
	 * 
	 * @return Arraylist of Node
	 */
	public ArrayList<Node> getAdjList() {
		return this.adjList;
	}
}
