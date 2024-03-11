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
            if (target==null||v1.adjacent.size()> )
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
                minimum = who_higher_Deg(v1);
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



