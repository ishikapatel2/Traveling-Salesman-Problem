import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PA11Main {
	public static void main(String[] args) {
        Scanner file = null;
        try {
        	file = new Scanner(new File(args[0]));} 
        catch (FileNotFoundException e) {
            e.printStackTrace();}
		DGraph g = createGraph(file);

		if (args[1].equals("HEURISTIC"))
			g.BFS(0);
		else if (args[1].equals("BACKTRACK"))
			g.hamiltonianCycles(0);
	}
	
	/*
	 * This method creates anew DGraph object by reading all of the lines of the file
	 * and saving the number of vertices as well as creating new Edge objects. 
	 * 
	 * @param Scanner file, an .mtx file containing information about a graph
	 */
    public static DGraph createGraph(Scanner file) 
    {
    	// read past comments
        String startLine = null;
        while (file.hasNextLine()) {
        	startLine = file.nextLine();
            if (!startLine.startsWith("%"))
                break;
        }
        
        // read the number of vertices and create a DGraph
        String[] startLineSplit = startLine.split("\\s+");
        int numVertices = Integer.parseInt(startLineSplit[0]);
        DGraph graph = new DGraph(numVertices);
        
        // read the edge info and add the edges to the graph
        while (file.hasNextLine()) {
            String[] s = file.nextLine().split("\\s+");
            graph.addEdge(Integer.parseInt(s[0])-1, Integer.parseInt(s[1])-1, Float.parseFloat(s[2]));
        }
        return graph;
    }	
}