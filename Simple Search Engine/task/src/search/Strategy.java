package search;

import java.util.List;
import java.util.Set;

public interface Strategy {
    List<String> getResultStrings(List<Set<Integer>> sets);
}
