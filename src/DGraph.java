import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class DGraph {
	
	/*
	 * The inner Edge class which creates Edge objects by saving the vertex
	 * of the edge and the weight associated with that edge.
	 */
	public class Edge {
		int label;
		double weight;
		public Edge(int v, double w)
		{
			label = v;
			weight = w;
		}
	}
	
	private int numVertices;
	private ArrayList<LinkedList<Edge>> adjList = new ArrayList<>();
	private double minWeight = Double.MAX_VALUE;
	private List<Integer> minPath = new ArrayList<>();
	
	/* A constructor that constructs a new instance of DGraph class 
	 * by initializing an arraylist of linked lists of Edge objects
	 * and the number of vertices 
	 * 
	 * @param int numVertices, number of vertices found from matrix file 	
	*/
	public DGraph(int numVertices) {
		this.numVertices = numVertices; 
		for (int i = 0; i < numVertices; i++)
			adjList.add(new LinkedList<Edge>());
	}
	
	/*
	 * This method creates and adds an Edge object to an array. 
	 * 
	 * @param int u, a vertex of a graph
	 * @param int v, a vertex who is connected to vertex u by an edge
	 * @param w, the weight of the edge connecting vertices u and v.
	 */
	void addEdge(int u, int v, double w) { 
		adjList.get(u).add(new Edge(v,w)); 
	} 

	/*
	 * This method finds an edge from the arraylist
	 * 
	 * @param int u, a vertex of a graph
	 * @param int v, a vertex who is connected to vertex v by an edge
	 * @return Edge, returns an edge between vertices u and v; null if not found
	 */
	Edge getEdge(int u, int v) { 
		for (int i = 0; i < adjList.get(u).size(); i++)
			if (adjList.get(u).get(i).label == v)
				return adjList.get(u).get(i);
		return null;
	}

	/*
	 * This method finds an approximate solution to the traveling salesman problem using a
	 * heuristics approach. Each edge will be compared and the edge with the smallest
	 * weight will be used to build a path. The path that is least costly will be printed 
	 * along with the actual cost. 
	 * 
	 * @param int start, a vertex of a graph (the starting vertex)
	 */
	void BFS(int start) { 
		boolean visited[] = new boolean[numVertices]; 
		LinkedList<Integer> queue = new LinkedList<>(); 
		ArrayList<Integer> visitOrder = new ArrayList<>();
		double totalWeight = 0;
		int minV = 0;
		double minVal = Double.MAX_VALUE;
		queue.add(start); 
		visited[start]= true; 

		while (queue.size() != 0)  { 
			int u = queue.pollFirst();
			visitOrder.add(u+1);
		    minVal = 10000000.0;
			
			//iterates through adjacent vertices of a given vertex
			for (int i = 0; i < adjList.get(u).size(); i++) {
				
				//finds the least costly path
				if (adjList.get(u).get(i).weight < minVal && !visited[adjList.get(u).get(i).label]) {
					minVal = adjList.get(u).get(i).weight;
					minV = adjList.get(u).get(i).label;
				}
			}
			if (minVal != 10000000.0) 
				totalWeight += minVal;
			if (!visited[minV]) { 
				visited[minV] = true; 
				queue.add(minV); 
			} 
		}
		visited = new boolean[numVertices];
		queue.add(minV);	
		
		//finds the path back to the start vertex from the end vertex
		while (minV != start) {
			int u = queue.pollFirst();
			minVal = 10000000.0;
			minV = 0;
			for (int i = 0; i < adjList.get(u).size(); i++) {
				
				//finds the least costly path
				if (adjList.get(u).get(i).weight < minVal && !visited[adjList.get(u).get(i).label] ||
						adjList.get(u).get(i).label == start) {
					minVal = adjList.get(u).get(i).weight;
					minV = adjList.get(u).get(i).label;
					if (adjList.get(u).get(i).label == start) {break;}
				}
				
				// checks if a vertex has already been visited
				if (!visited[minV]) { 
					visited[minV] = true; 
					queue.add(minV); 
				} 
			}
			if (minVal != 10000000.0) {
				totalWeight += minVal;
			}
		}
		System.out.println("cost = " + String.format("%.1f", totalWeight) + ", visitOrder = " + visitOrder);
	}
	
	/*
	 * This method finds uses recursive backtracking to solve the TSP. It will find the path
	 * that is least costly by exploring every possible solution and backing to the previous
	 * vertex to make a different decision and record the value of that solution. The 
	 * path that is least costly will be printed along with the actual cost.  
	 * 
	 * @param int start, a vertex of a graph (the starting vertex)
	 */
    public void hamiltonianCycles(int start)
    {
    	boolean[] visited = new boolean[numVertices];
    	List<Integer> path = new ArrayList<>();
    	path.add(start+1);
    	double total = 0;
    	hamiltonianCycles(start, visited, path, total);
    	System.out.println("cost = " + String.format("%.1f", minWeight) + ", visitOrder = " + minPath);
    }
    
    /*
	 * This is a helper function for the hamiltonianCycles method that performs the recursion
	 *  to find the least costly path and saves it into a field of the class.  
	 * 
	 * @param int u, a vertex of a graph (initially the starting vertex)
	 * @param boolean[] visited, a list of booleans that tells us if a vertex has already
	 *  been visited
	 * @param List<Integer> path, a  possible path found the graph
	 * @param doube total, the total weight of a path
	 */
    public void hamiltonianCycles(int u, boolean[] visited, List<Integer> path, double total)
    {
		visited[u] = true;

    	// if all the vertices are visited, then the Hamiltonian cycle exists
    	if (path.size() == numVertices){    		
    		Edge t = getEdge(u, 0);
    		total += t.weight;
    		if (total < minWeight) {
    			minWeight = total;
    			List<Integer> temp = new ArrayList<>();
    			temp.addAll(path);
    			minPath = temp;
    		}
       		return;
    	}

    	// Check if every edge starting from vertex `u` leads to a solution or not
    	for (int i = 0; i < adjList.get(u).size(); i++) {
    		Integer v = adjList.get(u).get(i).label;
    		if (!visited[v]) {
    			path.add(v+1);
    			total += adjList.get(u).get(i).weight;
    			hamiltonianCycles(v, visited, path, total);
    			
    			// backtrack for the path so v could be used in another path
    			visited[v] = false;  
    			path.remove(path.size() - 1);
    			total -= adjList.get(u).get(i).weight;
    		}
    	}
    }
    
    /*
	 * This method is very similar to the hamiltonianCycles method. The main difference
	 * is that this method is more efficient than hamiltonianCycles. As it is finding all
	 * possible paths, if a path is already larger than the current minimum weight, the 
	 * recursion to find the remaining path will be stopped early. 
	 * 
	 * @param int start, a vertex of a graph (the starting vertex)
	 */
    public void myOwn1(int start)
    {
    	boolean[] visited = new boolean[numVertices];
    	List<Integer> path = new ArrayList<>();
    	path.add(start+1);
    	double total = 0;
       	myOwn1(start, visited, path, total);
    	System.out.println("cost = " + String.format("%.1f", minWeight) + ", visitOrder = " + minPath);
    }
    
    /*
   	 * This is a helper function for the myOwn1 method that performs the recursion
   	 *  to find the least costly path and saves it into a field of the class.  
   	 * 
   	 * @param int u, a vertex of a graph (initially the starting vertex)
   	 * @param boolean[] visited, a list of booleans that tells us if a vertex has already
   	 *  been visited
   	 * @param List<Integer> path, a  possible path found the graph
   	 * @param doube total, the total weight of a path
   	 */
    public void myOwn1(int u, boolean[] visited, List<Integer> path, double total)
    {
		visited[u] = true;

    	// if all the vertices are visited, then the Hamiltonian cycle exists
    	if (path.size() == numVertices){    		
    		Edge t = getEdge(u, 0);
    		total += t.weight;
    		if (total < minWeight) {
    			minWeight = total;
    			List<Integer> temp = new ArrayList<>();
    			temp.addAll(path);
    			minPath = temp;
    		}
       		return;
    	}

    	// Check if every edge starting from vertex `u` leads to a solution or not
    	for (int i = 0; i < adjList.get(u).size(); i++) {
    		Integer v = adjList.get(u).get(i).label;
    		if (!visited[v]) {
    			path.add(v+1);
    			total += adjList.get(u).get(i).weight;
    			if (total > minWeight) 
    				return;
    			hamiltonianCycles(v, visited, path, total);
    			
    			// backtrack for the path so v could be used in another path
    			visited[v] = false;  
    			path.remove(path.size() - 1);
    			total -= adjList.get(u).get(i).weight;
    		}
    	}
    }
}