import java.util.*;      
public class Main {
    public static  void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        CSP graph = new CSP();
        LinkedList<String> colours = new LinkedList<>();
        int wayChosen;
        int numOfColours = 0;
        int numOfVar = 0;
        int numOfAdjVar = 0;
        boolean numOfColFlag = false;
        boolean numOfVarFlag = false;
        boolean numOfAdjFlag = false;


        while (!numOfColFlag) {
            System.out.println("**************************************************");
            System.out.println("How many colours are there in the domain? ");
            if (kb.hasNextInt()) {
                numOfColours = kb.nextInt();
                if (numOfColours > 0) {
                    numOfColFlag = true;
                } else {
                    System.out.println("Invalid input. Please enter a positive integer.");
                }
            } else {
                System.out.println("Invalid input. Please enter a positive integer.");
                kb.next();
            }
        }
        
        System.out.println("Enter the colours domain: ");
        kb.nextLine();
        for (int j = 0; j < numOfColours; j++) {
            System.out.print((j+1)+"-");
            colours.add(j,kb.nextLine());
        }


        while (!numOfVarFlag) {
            System.out.println("**************************************************");
            System.out.println("How many variables you want to add? ");
            if (kb.hasNextInt()) {
                numOfVar = kb.nextInt();
                if (numOfVar > 0) {
                    numOfVarFlag = true;
                } else {
                    System.out.println("Invalid input. Please enter a positive integer.");
                }
            } else {
                System.out.println("Invalid input. Please enter a positive integer.");
                kb.next(); // clear the invalid input
            }
        }


        for (int i = 1; i <= numOfVar; i++) {
            System.out.println("Please enter the name of the variable number " + i + ":");
            System.out.print(i+"-");
            String name = kb.next();
            graph.addVar(name);
        }
        graph.domain_initializer(colours);
        System.out.println("**************************************************");


        // Iterating through variables
        for (int i = 0; i < numOfVar; i++) {
            numOfAdjFlag = false; // Resetting flag for each variable
            numOfAdjVar = 0;

            // Prompting for the number of adjacent variables for the current variable
            while (!numOfAdjFlag) {
                System.out.println("How many adjacent variables for (" + graph.variables.get(i).name + ")?");
                if (kb.hasNextInt()) {
                    numOfAdjVar = kb.nextInt();
                    if (numOfAdjVar > 0) {
                        numOfAdjFlag = true;
                    } else {
                        System.out.println("Invalid input. Please enter a positive integer.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a positive integer.");
                    kb.next(); // clear the invalid input
                }
            }

            // Iterating through adjacent variables
            for (int j = 0; j < numOfAdjVar; j++) {
                System.out.println("What is the adjacent variable of (" + graph.variables.get(i).name + ")?");
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
                    System.out.println("======= The Solution =======");
                    graph.printSol();
                }

            } else if (wayChosen == 2) {
                ////////////////////////////////
            } else
                System.out.println("Wrong input!! ");

        } while (wayChosen != 1 || wayChosen != 2);

    }
}