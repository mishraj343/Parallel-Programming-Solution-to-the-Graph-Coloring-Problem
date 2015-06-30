// Import statements
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Graph class
 * This class defines a graph in a TreeMap. TreeMap is used as we need
 * to have the vertices in sorted order. 
 */
class Graph {
	TreeMap<Integer, Node> vertices;
	int numVertices;
	int numEdges;

	/**
	 * Constructor
	 */
	public Graph() {
		vertices = new TreeMap<Integer, Node>();
		numVertices = 0;
		numEdges = 0;
	}	

	/**
	 * addVertex method
	 * This method adds a vertex to the graph given its adjacent vertex.
	 * Thus it accepts the input file as a adjacency list with each line 
	 * representing the vertex and its adjacent vertex.
	 *
	 * @param vertex1 Node ID of the vertex
	 * @param adjacentVertex 
	 */
	public void addVertex(int vertex1, int adjVertex) {
		Node nodeVertex1=containsVertex(vertex1);
		Node nodeVertex2=containsVertex(adjVertex);
		nodeVertex1.adjList.add(nodeVertex2);
		++this.numVertices;
	}

	/**
	 * Checks if the vertex is already added in the graph.
	 * If yes, returns the vertex node object else constructs a new vertex object 
	 * and returns it. 
	 * 
	 * @param vertex  Vertex number.
	 * @return  Returns the node object. 
	 */
	public Node containsVertex(int vertex) {
		if(this.vertices.containsKey(vertex))
			return this.vertices.get(vertex);
		else {
			Node newNode=new Node(vertex);
			vertices.put(vertex, newNode);
			return newNode;
		}
	}

	/**
	 * getVertex method
	 * This method returns a reference to a vertex object
	 *
	 * @param name Node ID of the vertex
	 *
	 * @return A reference to the vertex object
	 */
	public Node getVertex(int name) {
		return this.vertices.get(name);
	}

	/**
	 * getVertexSet method
	 * This method returns the TreeMap containing the vertices.
	 *
	 * @return the hashtable of vertices
	 */
	public TreeMap<Integer, Node> getVertexSet() {
		return this.vertices;
	}

	/**
	 * getNumVertices method
	 * This method returns the number of vertices in the graph
	 *
	 * @return Number of vertices
	 */
	public int getNumVertices() {
		return this.numVertices;
	}

	/**
	 * getNumEdges method
	 * This method returns the number of edges in the graph
	 *
	 * @return Number of edges 
	 */
	public int getNumEdges() {
		return this.numEdges;
	}
}
