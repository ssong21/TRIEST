import java.util.*;
import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.lang.Integer;
public class TriestBase implements DataStreamAlgo {
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
	public TriestBase(int samsize) {
		samStorage = samsize;
	}

	public void handleEdge(Edge edge) {
		currentTime++;
		//System.out.println("currentTime: " + currentTime);
		boolean identical = false;
		if (edge.u == edge.v){
			System.out.println("not a valid edge");
			currentTime--;
			return;
		}
	   if (graph.containsKey(edge.u) && graph.containsKey(edge.v) && (graph.get(edge.u).contains(edge.v))){
			System.out.println("same edge is already in graph, basically don't process the same edge (u,v) or (v,u) again.");
			currentTime--;
			return; //same edge already has been put into graph, basically don't process the same edge (u,v) or (v,u) again.
		}
		if ((currentTime <= samStorage)){ //t <= M, deterministically insert edge into graph
			boolean finishEarly = false;
			if (!graph.containsKey(edge.u)){
				//System.out.println("adding node u:" + edge.u);
				graph.putIfAbsent(edge.u,new HashSet<Integer>()); //adds the node if never added before to graph
				finishEarly = true;
			}
			if (!graph.containsKey(edge.v)){
				//System.out.println("adding node v:" + edge.v);
				graph.putIfAbsent(edge.v,new HashSet<Integer>()); //adds the node if never added before to graph
				finishEarly = true;
			}
			graph.get(edge.u).add(edge.v); //adds edge associate with u
			graph.get(edge.v).add(edge.u); //adds the edge reversed
			edges.add(edge); //adds to arraylist of edges, for removing later, MAY NEED TO CHECK IF SAME EDGE
		   if (finishEarly){
				return;
			}
			HashSet<Integer> tempCopy = new HashSet<Integer>();
			tempCopy.addAll(graph.get(edge.u));
			tempCopy.retainAll(graph.get(edge.v));
			if (tempCopy.isEmpty()){
				return;
			}
			else{
				//System.out.println("Adding " + tempCopy.size() + " triangles!");
				counter = counter + tempCopy.size();
				return;
			}
		}
		else if ((currentTime > samStorage)){ //check to see if equality is right, something here messes up and causes my estimate int to go	to 2147483647 and -23 
			//System.out.println("storage has run out");			
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
				int removeU = edges.get(indRemove).u;
				int removeV = edges.get(indRemove).v;
				HashSet<Integer> tempRemove = new HashSet<Integer>();
				//System.out.println("Removing Edge: " + edges.get(indRemove));
				tempRemove.addAll(graph.get(removeU));
				tempRemove.retainAll(graph.get(removeV));
				if (!tempRemove.isEmpty()){
					//System.out.println("Removing " + tempRemove.size() + " triangles!");
					counter = counter - tempRemove.size();
					if (counter <= 0){
						//System.out.println("how is this even possibleASDKASMKDKMASDMKASDKMSAMKDASMKDKMASDMKASMDKMASKDMKADMKAMKSDMKASDMKAMKSD");
						counter = 0;
					}
				}
				graph.get(removeU).remove(removeV); //remove selected edge from graph
				graph.get(removeV).remove(removeU);
				edges.remove(indRemove); //remove selected edge from arraylist
				boolean finishEarly = false;
				if (!graph.containsKey(edge.u)){
					//System.out.println("adding node u:" + edge.u);
					graph.putIfAbsent(edge.u,new HashSet<Integer>()); //adds the node if never added before to graph
					finishEarly = true;
				}
				if (!graph.containsKey(edge.v)){
					//System.out.println("adding node v:" + edge.v);
					graph.putIfAbsent(edge.v,new HashSet<Integer>()); //adds the node if never added before to grap
					finishEarly = true;
				}
				graph.get(edge.u).add(edge.v); //adds edge associate with u
				graph.get(edge.v).add(edge.u); //adds the edge reversed
				//System.out.println("Adding Edge: " + edge);
				edges.add(edge);
				if (finishEarly){
					return;
				}
				HashSet<Integer> tempCopy = new HashSet<Integer>();
				tempCopy.addAll(graph.get(edge.u));
				tempCopy.retainAll(graph.get(edge.v));
				if (tempCopy.isEmpty()){	
					return;
				}
				else{
					//System.out.println("Adding " + tempCopy.size() + " triangles!");
					counter = counter + tempCopy.size();
					return;
				}
			}
		}
	}

	public int getEstimate() {
		if (currentTime < samStorage){ //check this inequality AGAIN
			return counter;
		}
		else{
			double constant = calcConstant(currentTime,samStorage);
			double estimate = (double)counter*constant;
			return (int)estimate;
		}
	}
	public double calcConstant(int currTime, int storage){
		double first = (double)((currTime-0.0)/(storage-0.0));
		//System.out.println("first: " + first);
		double second = (double)((currTime-1.0)/(storage-1.0));
		//System.out.println("second: " + second);
		double third = (double)((currTime-2.0)/(storage-2.0));
		//System.out.println("third: " + third);
		double constant = first*second*third;
		if (constant <= 1){
			//System.out.println("equal to 1, constant: " + constant);
			return 1;
		}
		else{
			//System.out.println("over 1, constant: " + constant);
			return constant;
		}
	}
}

