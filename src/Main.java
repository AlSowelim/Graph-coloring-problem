import java.util.*;
public class Main {
    public static  void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        CSP graph = new CSP();
        LinkedList<String> colours = null;
        int wayChosen;

        System.out.println("Welcome to Graph-coloring Program!");
        System.out.println("**************************************************");
        System.out.println("How many colours are there in the domain? ");
        int numOfcolours = kb.nextInt();

        System.out.println("Enter the colours domain: ");
        for (int j = 1; j <= numOfcolours; j++) {
            colours.add(kb.next());
        }

        System.out.println("How many variables you want to add? ");
        int numOfVar = kb.nextInt();

        for (int i = 1; i <= numOfVar; i++) {
            System.out.println("Please enter the name of the " + i + " varible: ");
            String name = kb.next();
            graph.addVar(name);
        }

        do {
            System.out.println("");
            System.out.println("**************************************************");
            System.out.println("Which way would you like to solve this problem with? ");
            System.out.println("1. Backtracking.");
            System.out.println("2. Tree Decomposition.");
            wayChosen = kb.nextInt();

            if (wayChosen == 1) {
                ////////////////////////////////
            } else if (wayChosen == 2) {
                ////////////////////////////////
            } else
                System.out.println("Wrong input!! ");

        } while (wayChosen != 1 || wayChosen != 2);


        // Letting the user define the adjacency.
        for(int i = 0; i < numOfVar; i++){
            System.out.println("What is the adjacent variables of (" + graph.variables.get(i).name + ")?");
            graph.printWithout(graph.variables.get(i).name);
            String varName = kb.next();
            graph.setAdj(graph.variables.get(i), graph.getVarByName(varName));
        }

    }
}