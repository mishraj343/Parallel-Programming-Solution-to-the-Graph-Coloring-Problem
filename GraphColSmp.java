import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
	
/**
 * Class GraphColSmp is a parallel program that assigns the near-optimal minimum 
 * number of colors to vertices such that no two adjacent vertices have the same color.
 * It prints the assignment of colors to vertices as well as the maximum number
 * of colors required to color the entire graph.
 * 
 * @author  Shobhit Dutia Shreyas Jayanna
 * @version 10th-Dec-2014
 */
public class GraphColSmp extends Task {
	int noOfVertices,noOfEdges, supersteps;
	int noOfVerticesByProcessors;
	int noOfCores, initialNoOfCores;
	TreeMap<Integer, Node> vertexSet;
	Vector<Integer> colorHashtable;
	VertexSetVbl vertexSetVbl;
	boolean flag=true;
	List<Integer> list = null;
	/**
	  * Main program.
	  */
	public void main(String[] args) throws NumberFormatException, IOException {
		if (args.length != 1) {
			usage();
		}
		GraphColSmp graphColSeq=new GraphColSmp();
		Graph graph=graphColSeq.readInput(args[0],Math.max(cores(), 1));
		Long start=System.currentTimeMillis();
		graphColSeq.colorVertices(graph);
		Long end=System.currentTimeMillis();
		graphColSeq.printAndGetNoOfColors(graph);
		System.out.println("Time taken:"+(end-start)+" msec");
	}
	/**
	  * Prints the final graph and gets the maximum number of colors
	  * required to color the graph.
	  *
	  * @param  graph Input graph.
	  */	
	private void printAndGetNoOfColors(Graph graph) {
		vertexSet=graph.getVertexSet();
		int max=0, color;
		for(int i=0;i<vertexSet.size();i++) {
			color=vertexSet.get(i).getColor();
			System.out.println("Vertex:"+vertexSet.get(i).getNodeName()+
					",color:"+color);
			if(color>max)
				max=color;
		}
		System.out.println("Total number of colors required:"+(max+1));
	}
	/**
	  * Colors the vertices. 
	  *
	  * @param  graph Input graph.
	  */		
	private void colorVertices(Graph graph) {
		vertexSet=graph.getVertexSet();
		//supersteps=1;
		while(vertexSet.size()!=0) {
			noOfVertices=vertexSet.size();
			if(noOfVertices==1) 
				noOfCores=1;
			else
				if(noOfVertices<=noOfCores)
					noOfCores=noOfVertices-1;
			noOfVerticesByProcessors=(int) Math.round((double)noOfVertices/noOfCores);
			if(!flag) {
				list=new ArrayList<Integer>(vertexSet.keySet());
				//System.out.println("List is "+list);
			}
			parallelFor(0,noOfCores-1).exec (new Loop()	{
				int start, end;
				public void start() {
					start=new Integer(noOfVerticesByProcessors*rank());
					end=new Integer(noOfVerticesByProcessors*rank()+noOfVerticesByProcessors);
					if(end>noOfVertices) {
						end=new Integer(noOfVertices);
					}						
					if(rank()==noOfCores-1&&noOfVertices-end>0) {
						end=noOfVertices;
					}
				}
				
				@Override
				public void run(int arg0) throws Exception {
					boolean finished=false;
					Node node;
					ArrayList<Node> adjList;
					ArrayList<Integer> adjColorList=new ArrayList<Integer>();
					for(int i=start;i<end;i++) {
							if(i==end) {
								finished=true;
								break;
							} else {
								if(flag)
									node=vertexSet.get(i);
								else {
									//(List<Integer>) vertexSet.keySet();
									node=vertexSet.get(list.get(i));
								}
								adjList=node.getAdjList();
								for(Node adjNode:adjList) {
									adjColorList.add(adjNode.getColor());
								}
								int possibleColor=0;
								while(adjColorList.contains(possibleColor)) {
									++possibleColor;
								}
								node.setColor(possibleColor);
								adjColorList.clear();
							}
					}
				}	
	        });
			vertexSetVbl=new VertexSetVbl();
			parallelFor (0,noOfCores-1).exec (new Loop()	{
				VertexSetVbl thrVertexSetVbl;
				VertexSetVbl candidate;
				Integer start, end;
				public void start() {
					start=new Integer(noOfVerticesByProcessors*rank());
					end=new Integer(noOfVerticesByProcessors*rank()+noOfVerticesByProcessors);
					if(end>noOfVertices) {
						end=new Integer(noOfVertices);
					}
					thrVertexSetVbl=threadLocal(vertexSetVbl);
					candidate=new VertexSetVbl();
				}
				@Override
				public void run(int arg0) throws Exception {
					Node node;
					ArrayList<Node> adjList;
					for (int i=start;i<end;i++) {
						if(flag) {
							node=vertexSet.get(i);								
						}
						else {
							node=vertexSet.get(list.get(i));
						}
						adjList=node.getAdjList();
						for(Node adjNode:adjList) {
							if(adjNode.getNodeName()>end-1&&adjNode.getColor()==node.getColor()) {
								candidate.add(node.getNodeName(), node);
								thrVertexSetVbl.reduce(candidate);
							}
						}
						/*if boundary vertex and same color and if not added to set, add it*/
					}
				}
	        });
			if(flag)
				flag=false;
			vertexSet=vertexSetVbl.vertexSetTemp;
		}
		
	}
	/**
	    * Reads input file.
	    *
	    * @param  fileName  Name of the file.
	    * @param  cores	  Number of cores required.
	    *
	    * @return  Returns the required graph object.
	    */	
	private Graph readInput(String fileName, int cores) throws IOException {
		this.noOfCores=initialNoOfCores=cores;
		int currVertex1, currVertex2;
		BufferedReader br=new BufferedReader(new FileReader(fileName));
		String line;
		Graph graph=new Graph();
		while((line=br.readLine()) != null) {
            String[] vertex=line.split(" "); 
            currVertex1=Integer.parseInt(vertex[0]);
            currVertex2=Integer.parseInt(vertex[1]);
			graph.addVertex(currVertex1,currVertex2);
			graph.addVertex(currVertex2,currVertex1);
		 }
		return graph;
	}
	/**
	  * Print a usage message and exit.
	  */
	private void usage() {
		System.err.println("Usage: java pj2 GraphColSeq <fileName>");
		throw new IllegalArgumentException();
	}
}