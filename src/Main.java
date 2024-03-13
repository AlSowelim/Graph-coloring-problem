import java.util.*;      
public class Main {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        CSP graph = new CSP();
        LinkedList<String> colours = new LinkedList<>();
        int wayChosen, numOfColours=0, numOfVar=0, numOfAdjVar=0 ;

        boolean numOfColFlag = false, numOfVarFlag=false ,numOfAdjFlag= false;

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
            System.out.print((j + 1) + "-");
            colours.add(j, kb.nextLine());
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
            System.out.print(i + "-");
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
                System.out.print("Variable list: ");
                graph.printWithout(graph.variables.get(i).name);
                System.out.println("");
                System.out.println("");
                String varName = "null";
                boolean flag = false;
                try {
                    varName = kb.next();
                    Variable v1 = graph.getVarByName(varName);
                    if (v1 == null)//
                        throw new InputMismatchException();
                } catch (Exception e) {
                    flag = true;
                    while (flag) {
                        System.out.println(varName + " dose not exist. Please Enter a valid variable name:");
                        System.out.print("Variable list: ");
                        graph.printWithout(graph.variables.get(i).name);
                        varName = kb.next();
                        Variable v1 = graph.getVarByName(varName);
                        if (v1 != null) {
                            flag = false;
                        }
                    }
                }

                if (graph.variables.get(i).adjacent.contains(graph.getVarByName(varName))) {
                    System.out.println("already in");

                } else {
                    graph.setAdj(graph.variables.get(i), graph.getVarByName(varName));
                }
            }
        }

        while (true) {
            System.out.println("**************************************************");
            System.out.println("Ready to get the solution?\nType '1' to get it!");
            String SolInput = kb.next();

            if (SolInput.equals("1")) {
                if (graph.backtracking()) {
                    System.out.println("======= The Solution =======");
                    graph.printSol();
                } else {
                    System.out.println("No Solution!");
                }
                System.out.println("Solution retrieved!");
                break; // Exit the loop as '1' is entered
            } else {
                System.out.println("Wrong input! Try again..");
                System.out.println("**************************************************");
            }
        }
    }
}