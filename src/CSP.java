import java.util.*;

class CSP {
//public LinkedList<String> domain;
public LinkedList<Variable> variables;

    public CSP() {
        this.variables = new LinkedList<Variable>();
    }

    //This method for adding new vertex to the variables list
    public boolean addVar(String name){
        Variable var = new Variable(name);
        var.name = name;
        return variables.add(var);
    }

    //domain initializer tends to initialize every domain for every independent
    public void domain_initializer(LinkedList<String> d)
    {
        int size= variables.size();
        int size_for_parameter=d.size();
        variables.getFirst();
        for (Variable v1 : variables) {
            for (int j = 0; size_for_parameter > j; j++) {
                v1.domain.add(d.get(j));
            }
        }
    }


    // Letting the user add the adjacency between the variables.
    public void setAdj(Variable var1, Variable var2){
            var1.adjacent.add(var2);
    }

    // helper methode for serAdj.
    public Variable getVarByName(String name){
      for (Variable v1: variables)
      {
          if(v1.name.equalsIgnoreCase(name))
          {
              return v1;
          }
      }
      return null;
    }

    public void printWithout(String name){
        for(Variable v1: variables){
            if(!(v1.name.equalsIgnoreCase(name))){
                System.out.println(v1.name);
            }
        }
    }

    // Method for sorting domain colors of a variable using the least constraining value heuristic
    public void sortDomainByLCV(Variable variable) {
        // A map to store colors and their counts across all adjacent variables' domains
        HashMap<String, Integer> colorConstraints = new HashMap<>();

        // Initialize the count for all colors of my domain to zero
        for (String color : variable.domain) {
            colorConstraints.put(color, 0);
        }

        // Count how many times each color appears in the domain of adjacent variables
        for (Variable adjacentVariable : variable.adjacent) {
            for (String color : adjacentVariable.domain) {
                colorConstraints.put(color, colorConstraints.getOrDefault(color, 0) + 1);
            }
        }

        // Convert the PriorityQueue to a list for sorting
        List<String> domainList = new LinkedList<>(variable.domain);

        // Sort the domain colors based on the least number of constraints imposed
        domainList.sort(new Comparator<String>() {
            @Override
            public int compare(String color1, String color2) {
                return Integer.compare(colorConstraints.get(color1), colorConstraints.get(color2));
            }
        });

        // Clear the original domain PriorityQueue and repopulate it with the sorted colors
        variable.domain.clear();
        for (String color : domainList) {
            variable.domain.offer(color);
        }
    }

    // Method to apply LCV sorting to all variables
    public void applyLCVToAll() {
        for (Variable variable : this.variables) {
            sortDomainByLCV(variable);
        }
    }


//forward checking will reduce complexity due to deleting values from the domain of the variable
public boolean forwardChecking( Variable v, boolean gate) { //using gate boolean variable to create a virtual
    String chosen_color = v.chosen_color;                    //  assignment to be able to backtrack
    PriorityQueue<String>virtualDomain=new PriorityQueue<>();
    if (chosen_color == null)
        return false;//to check variable  has chosen a color to be able to forward_check

    for (Variable neighbor : v.adjacent)//iteration over the adjacent nodes to our variable
    {
        if (gate) {//gate behave as gate to non-virtual assignment
            if (neighbor.domain.contains(chosen_color)) {
                neighbor.domain.remove(chosen_color);
                if (neighbor.domain.isEmpty())
                    return false;
            }
        }
        else
        {
            virtualDomain.addAll(neighbor.domain);//
            if (virtualDomain.contains(chosen_color))
            {
                virtualDomain.remove(chosen_color);
                if (virtualDomain.isEmpty())
                    return false;
            }
        }
    }
    return true;
}

    private Variable highestDegree()//helper method to apply Degree heuristic
    {
        Variable target =null;
        int size=variables.size();
        for (Variable v1:variables)
        {
            if (target==null||v1.adjacent.size()>target.adjacent.size() )
            {
                target=v1;
            }
        }
        return target;
    }
    private Variable who_higher_Deg(Variable v1, Variable v2)
    {
       return (v1.adjacent.size()>v2.adjacent.size())?v1:v2;
    }
// applying mrv technique to choose the correct variable
    public Variable mrv()
    {
        Variable minimum=null;
        for (Variable v1: variables)
        {
            if (minimum==null||v1.domain.size()<minimum.domain.size())
            {
                minimum=v1;
            }
            else if (v1.domain.size()==minimum.domain.size())
            {//now we apply degree heuristic
                minimum=who_higher_Deg(v1,minimum);
            }
        }
        return minimum;

    }

    private boolean allVariablesAssigned() {
        for (Variable variable : variables) {
            if (variable.chosen_color == null) {
                return false; // If any variable is not assigned, return false
            }
        }
        return true; // If all variables are assigned, return true
    }

    private boolean isValueConsistent(Variable variable, String value) {
        for (Variable adjacentVariable : variable.adjacent) {
            if (adjacentVariable.chosen_color.equals(value)) {//tbc
                return false; // If the assigned color is the same as the value, it violates the constraint
            }
        }
        return true; // If no violations found, value is consistent
    }

    public boolean backtracking() {
        return backtrack(this);
    }

    private boolean backtrack(CSP csp) {
        if (csp.allVariablesAssigned()) {
            return true; // If all variables are assigned, a solution is found
        }
        Variable variable = csp.mrv(); // Choose the next variable using MRV heuristic
        applyLCVToAll();
        for (String value : variable.domain) {
            if (csp.isValueConsistent(variable, value)) {
                variable.chosen_color = value; // Assign the value to the variable
                if (csp.forwardChecking(variable, true)) { // Forward checking
                    if (backtrack(csp)) { // Recursively backtrack
                        return true;
                    }
                }
                variable.chosen_color = null; // Unassign the value if solution not found
            }
        }
        return false; // If no solution found
    }

}

    /*
     * public class ForwardChecking {

    static int N = 8; // Change N for different board sizes

    // Function to print the solution
    static void printSolution(int board[][]) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(" " + board[i][j] + " ");
            System.out.println();
        }
    }

    // Function to check if a queen can be placed on board[row][col]
    static boolean isSafe(int board[][], int row, int col) {
        int i, j;

        // Check this row on the left side
        for (i = 0; i < col; i++)
            if (board[row][i] == 1)
                return false;

        // Check upper diagonal on left side
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        // Check lower diagonal on left side
        for (i = row, j = col; j >= 0 && i < N; i++, j--)
            if (board[i][j] == 1)
                return false;

        return true;
    }

    // Recursive function to solve N-Queens problem with forward checking
    static boolean solveNQUtil(int board[][], int col, ArrayList<Integer> availableRows) {
        // If all queens are placed then return true
        if (col >= N)
            return true;

        // Try placing this queen in all rows one by one
        for (int i = 0; i < availableRows.size(); i++) {
            int row = availableRows.get(i);
            // Check if the queen can be placed on board[i][col]
            if (isSafe(board, row, col)) {
                // Place this queen in board[i][col]
                board[row][col] = 1;

                // Update available rows for forward checking
                ArrayList<Integer> updatedRows = new ArrayList<>(availableRows);
                updatedRows.remove(Integer.valueOf(row));

                // Recur to place rest of the queens
                if (solveNQUtil(board, col + 1, updatedRows))
                    return true;

                // If placing queen in board[i][col] doesn't lead to a solution, backtrack
                board[row][col] = 0; // Backtrack
            }
        }
        return false; // If the queen cannot be placed in any row in this column
    }

    // Function to solve N-Queens problem with forward checking
    static void solveNQ() {
        int board[][] = new int[N][N];

        // Initialize all cells of board[][] to 0
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                board[i][j] = 0;

        // List of available rows for each column
        ArrayList<Integer> availableRows = new ArrayList<>();
        for (int i = 0; i < N; i++)
            availableRows.add(i);

        // Start with the first column
        if (!solveNQUtil(board, 0, availableRows)) {
            System.out.println("Solution does not exist");
            return;
        }

        // Solution found, print the board
        printSolution(board);
    }

    // Main method to test the code
    public static void main(String args[]) {
        solveNQ();
    }
}

 /*
 public class BacktrackingNQueens {

    static int N = 8; // Change N for different board sizes

    // Function to print the solution
    static void printSolution(int board[][]) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(" " + board[i][j] + " ");
            System.out.println();
        }
    }

    // Function to check if a queen can be placed on board[row][col]
    static boolean isSafe(int board[][], int row, int col) {
        int i, j;

        // Check this row on the left side
        for (i = 0; i < col; i++)
            if (board[row][i] == 1)
                return false;

        // Check upper diagonal on left side
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        // Check lower diagonal on left side
        for (i = row, j = col; j >= 0 && i < N; i++, j--)
            if (board[i][j] == 1)
                return false;

        return true;
    }

    // Recursive function to solve N-Queens problem with backtracking
    static boolean solveNQUtil(int board[][], int col) {
        // If all queens are placed then return true
        if (col >= N)
            return true;

        // Try placing this queen in all rows one by one
        for (int i = 0; i < N; i++) {
            // Check if the queen can be placed on board[i][col]
            if (isSafe(board, i, col)) {
                // Place this queen in board[i][col]
                board[i][col] = 1;

                // Recur to place rest of the queens
                if (solveNQUtil(board, col + 1))
                    return true;

                // If placing queen in board[i][col] doesn't lead to a solution, backtrack
                board[i][col] = 0; // Backtrack
            }
        }
        return false; // If the queen cannot be placed in any row in this column
    }

    // Function to solve N-Queens problem with backtracking
    static void solveNQ() {
        int board[][] = new int[N][N];

        // Initialize all cells of board[][] to 0
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                board[i][j] = 0;

        // Start with the first column
        if (!solveNQUtil(board, 0)) {
            System.out.println("Solution does not exist");
            return;
        }

        // Solution found, print the board
        printSolution(board);
    }

    // Main method to test the code
    public static void main(String args[]) {
        solveNQ();
    }
}
     */
/*
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreeDecomposition {

    static class Graph {
        int V;
        List<Integer>[] adjList;

        @SuppressWarnings("unchecked")
        Graph(int V) {
            this.V = V;
            adjList = new ArrayList[V];
            for (int i = 0; i < V; i++) {
                adjList[i] = new ArrayList<>();
            }
        }

        void addEdge(int u, int v) {
            adjList[u].add(v);
            adjList[v].add(u);
        }
    }

    // Function to perform tree decomposition for Vertex Cover problem
    static List<Set<Integer>> treeDecomposition(Graph graph) {
        // Perform dynamic programming
        List<Set<Integer>> bags = new ArrayList<>();
        boolean[] visited = new boolean[graph.V];
        boolean[] processed = new boolean[graph.V];
        for (int i = 0; i < graph.V; i++) {
            if (!processed[i]) {
                Set<Integer> bag = new HashSet<>();
                bag.add(i);
                dfs(graph, visited, processed, bag, i);
                bags.add(bag);
            }
        }
        return bags;
    }

    static void dfs(Graph graph, boolean[] visited, boolean[] processed, Set<Integer> bag, int u) {
        visited[u] = true;
        for (int v : graph.adjList[u]) {
            if (!visited[v]) {
                dfs(graph, visited, processed, bag, v);
            }
        }
        processed[u] = true;
    }

    public static void main(String[] args) {
        // Create a graph
        int V = 5;
        Graph graph = new Graph(V);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);

        // Perform tree decomposition
        List<Set<Integer>> bags = treeDecomposition(graph);

        // Print the bags
        for (int i = 0; i < bags.size(); i++) {
            System.out.print("Bag " + i + ": ");
            Set<Integer> bag = bags.get(i);
            for (int vertex : bag) {
                System.out.print(vertex + " ");
            }
            System.out.println();
        }
    }
}
 */



