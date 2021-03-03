import java.util.*;
import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.lang.Integer;
public class TriestImpr implements DataStreamAlgo {
	int samStorage; //Storage space
	int currentTime; //current
	ArrayList<Edge> edges = new ArrayList<Edge>();
	HashMap<Integer,HashSet<Integer>> graph = new HashMap<Integer,HashSet<Integer>>(); //Graph w/ edges
	int counter; //Triangle counter
    /*
     * Constructor.
     * The parameter samsize denotes the size of the sample, i.e., the number of
     * edges that the algorithm can store.
     */
	public TriestImpr(int samsize) {
		samStorage = samsize;
	}

	public void handleEdge(Edge edge) {
		currentTime++;
		//System.out.println("currentTime: " + currentTime);
		boolean updateCounter = true;
		if (!graph.containsKey(edge.u) || !graph.containsKey(edge.v)){
			updateCounter = false;
		}
		boolean identical = false;
		if (graph.containsKey(edge.u) && graph.containsKey(edge.v) && (graph.get(edge.u).contains(edge.v))){
			System.out.println("same edge is already in graph, basically don't process the same edge (u,v) or (v,u) again.");
			currentTime--;
			return; //same edge already has been put into graph, basically don't process the same edge (u,v) or (v,u) again
		}
		if (edge.u == edge.v){
			currentTime--;
			System.out.println("not a valid edge");
			return;
		}
		if (updateCounter){
			HashSet<Integer> induceTriangles = new HashSet<Integer>();
			induceTriangles.addAll(graph.get(edge.u));
			induceTriangles.retainAll(graph.get(edge.v));
			if (!induceTriangles.isEmpty()){
				double nFirst = (double)(currentTime-1)/(samStorage);
				double nSecond = (double)(currentTime-2)/(samStorage-1);
				double nConstant = nFirst*nSecond;
				//System.out.println("nConstant: " + nConstant);
				if (nConstant <= 1){
					//System.out.println("Found: " + induceTriangles.size());
					counter = counter + induceTriangles.size();
				}
				else{
					//System.out.println("Found: " + induceTriangles.size());
					counter = counter + (int)(induceTriangles.size()*nConstant);
				}
			}
		}
		if ((currentTime <= samStorage)){ //t <= M, deterministically insert edge into graph
			if (identical){
				return;
			}
			if (!graph.containsKey(edge.u)){
				//System.out.println("adding NEW node u:" + edge.u);
				graph.putIfAbsent(edge.u,new HashSet<Integer>()); //adds the node if never added before to graph
			}
			if (!graph.containsKey(edge.v)){
				//System.out.println("adding NEW node v:" + edge.v);
				graph.putIfAbsent(edge.v,new HashSet<Integer>());
			}
			graph.get(edge.u).add(edge.v); //adds edge associate with u
			graph.get(edge.v).add(edge.u); //adds the edge reversed
			edges.add(edge); //adds to arraylist of edges, for removing later, MAY NEED TO CHECK IF SAME EDGE
			return;
		}
		else if ((currentTime > samStorage)){ //check to see if equality is right,
			boolean tails = false; //flip coin with tail bias
			int detSide = (int)(Math.random()*currentTime);
			if (detSide < samStorage){
				//System.out.println("Got tails!: " + detSide);
				tails = true;
			}				

			if (!tails){ // if flip coin was heads, do nothing
				//System.out.println("Got heads!: " + detSide);
				return;
			}
			else{ //flip coin was a tail
				int indRemove = (int)(Math.random()*edges.size());
				//System.out.println("indRemove: " + indRemove);
				int removeU = edges.get(indRemove).u;
				//System.out.println("removeU: " + removeU);
				int removeV = edges.get(indRemove).v;
				//System.out.println("removeV: " + removeV);
				graph.get(Integer.valueOf(removeU)).remove(removeV); //remove selected edge from graph
				graph.get(Integer.valueOf(removeV)).remove(removeU);
				edges.remove(indRemove); //remove selected edge from arraylist
				if (identical){
					//System.out.println("edge was already present, terminating early after removing edge");
					return;
				}
				if (!graph.containsKey(edge.u)){
					//System.out.println("adding node u:" + edge.u);
					graph.putIfAbsent(edge.u,new HashSet<Integer>()); //adds the node if never added before to graph
				}
				if (!graph.containsKey(edge.v)){
					//System.out.println("adding node v:" + edge.v);
					graph.putIfAbsent(edge.v,new HashSet<Integer>());
				}
				graph.get(edge.u).add(edge.v); //adds edge associate with u
				graph.get(edge.v).add(edge.u); //adds the edge reversed
				edges.add(edge);
			}
		}
	}

	public int getEstimate() {
		return counter;
	}
}
