import java.util.*;      
public class Main {
    public static  void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        CSP graph = new CSP();
        LinkedList<String> colours = new LinkedList<>();
        int wayChosen;

        System.out.println("Welcome to Graph-coloring Program!");
        System.out.println("**************************************************");
        System.out.println("How many colours are there in the domain? ");
        int numOfColours = kb.nextInt();

        System.out.println("Enter the colours domain: ");
        kb.nextLine();
        for (int j = 0; j < numOfColours; j++) {
            colours.add(j,kb.nextLine());
        }
        System.out.println("**************************************************");
        System.out.println("How many variables you want to add? ");
        int numOfVar = kb.nextInt();

        for (int i = 1; i <= numOfVar; i++) {
            System.out.println("Please enter the name of the variable number " + i + ":");
            String name = kb.next();
            graph.addVar(name);
        }
        graph.domain_initializer(colours);
        System.out.println("**************************************************");
        // Letting the user define the adjacency.
        for(int i = 0; i < numOfVar; i++) {
            System.out.println("How many adjacent variables for (" + graph.variables.get(i).name + ")?");
            int numOfAdjVar = kb.nextInt();
            for (int j = 0; j < numOfAdjVar; j++) {
                System.out.println("What is the adjacent variables of (" + graph.variables.get(i).name + ")?");
                graph.printWithout(graph.variables.get(i).name);
                System.out.println("");
                System.out.println("");
                String varName = kb.next();
                if (graph.variables.get(i).adjacent.contains(graph.getVarByName(varName)))
                {
                    System.out.println("already in");
                }
                else
                {
                graph.setAdj(graph.variables.get(i), graph.getVarByName(varName));
                }
            }
        }

        do {
            System.out.println("");
            System.out.println("**************************************************");
            System.out.println("Which way would you like to solve this problem with? ");
            System.out.println("1. Backtracking.");
            System.out.println("2. Tree Decomposition.");
            wayChosen = kb.nextInt();

            if (wayChosen == 1) {
                if (graph.backtracking()){
                    graph.printSol();
                }

            } else if (wayChosen == 2) {
                ////////////////////////////////
            } else
                System.out.println("Wrong input!! ");

        } while (wayChosen != 1 || wayChosen != 2);

    }
}