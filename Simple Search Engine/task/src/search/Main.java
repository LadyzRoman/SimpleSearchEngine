package search;

import java.util.*;

public class Main {
    private final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        SearchEngine engine = new SearchEngine();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--data")) {
                engine.fillDatabase(args[i + 1]);
                break;
            }
        }

        int option;
        while ((option = menu()) != 0) {
            switch (option) {
                case 1:
                    engine.setStrategy(getStrategy(engine));
                    System.out.println("Enter a name or email to search all suitable people.");
                    engine.executeQuery(sc.nextLine());
                    break;
                case 2:
                    engine.printAll();
                    break;
            }
        }
    }

    /**
     * prints the menu and return user input if it correct
     * @return an integer representing menu option
     */
    public static int menu() {
        do {
            System.out.println();
            System.out.println("=== Menu ===");
            System.out.println("1. Find a person");
            System.out.println("2. Print all people");
            System.out.println("0. Exit");
            try {
                int i =  Integer.parseInt(sc.nextLine());
                if (i < 0 || i > 2) {
                    System.out.println("Incorrect option! Try again.");
                } else {
                    return i;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
    }

    public static Strategy getStrategy(SearchEngine engine) {

        do {
            System.out.println();
            System.out.println("Select a matching strategy: ALL, ANY, NONE");
            try {
                String strategy = sc.nextLine();
                switch (strategy) {
                    case "ANY":
                        return engine.new StrategyAny();
                    case "ALL":
                        return engine.new StrategyAll();
                    case "NONE":
                        return engine.new StrategyNone();
                    default:
                        System.out.println("Error!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);

    }


}
