package search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchEngine {
    private List<String> database = new ArrayList<>();
    private  Map<String, Set<Integer>> invertedIndexDatabase = new HashMap<>();
    private Strategy strategy;


    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void executeQuery(String query) {
        //GET INDEXES FOR EACH QUERY
        List<Set<Integer>> sets = new ArrayList<>();
        for (var word : query.split(" ")) {
            sets.add(getIndexesByQuery(word));
        }
        //GET STRINGS BY STRATEGY
        List<String> result = strategy.getResultStrings(sets);

        //PRINT RESULT
        printResult(result);
    }

    private Set<Integer> getIndexesByQuery(String query) {
        var result = new HashSet<Integer>();
        for (int i = 0; i < database.size(); i++) {
            String line = database.get(i);
            for (var word : line.split(" ")) {
                word = word.trim();
                if (word.equalsIgnoreCase(query)) {
                    result.add(i);
                    break;
                }
            }
        }

        return result;
    }

    public void fillDatabase(String fileName) {
        try {
            database.addAll(Files.readAllLines(Path.of(fileName)));
            for (var line : database) {
                for (var word : line.split(" ")) {
                    invertedIndexDatabase.put(word.toLowerCase(), getIndexesByQuery(word));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printAll() {
        System.out.println("=== List of people ===");
        database.forEach(System.out::println);
    }


    private void printResult(List<String> res) {
        if (res.size() != 0) {
            System.out.println("Found people:");
            res.forEach(System.out::println);
        } else {
            System.out.println("No matching people found.");
        }
    }

    public class StrategyAll implements Strategy {
        @Override
        public List<String> getResultStrings(List<Set<Integer>> sets) {
            if (sets.size() == 0)
                return new ArrayList<>();

            Set<Integer> allSet = new HashSet<>(sets.get(0));
            for (var set : sets) {
                allSet.retainAll(set);
            }

            return allSet.stream()
                    .map(database::get)
                    .collect(Collectors.toList());
        }
    }

    public class StrategyAny implements Strategy {
        @Override
        public List<String> getResultStrings(List<Set<Integer>> sets) {
            Set<Integer> anySet = new HashSet<>();
            for (var set : sets) {
                anySet.addAll(set);
            }
            return anySet.stream()
                    .map(database::get)
                    .collect(Collectors.toList());
        }
    }

    public class StrategyNone implements Strategy {
        @Override
        public List<String> getResultStrings(List<Set<Integer>> sets) {
            Set<Integer> noneSet = IntStream.range(0, database.size())
                    .boxed()
                    .collect(Collectors.toSet());

            for (var set: sets) {
                noneSet.removeAll(set);
            }
            return noneSet.stream()
                    .map(database::get)
                    .collect(Collectors.toList());
        }
    }
}
