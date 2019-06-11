package edu.wpi.cs3733d19.teamg;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Comparator;

public class FuzzySearchComparator implements Comparator<javafx.scene.Node> {
    private String searchString;
    private NodeToString nodeToString;
    private LevenshteinDistance lv;

    /**
     * Creates a new comparator for ordering JavaFX nodes based
     * on their similarity to some search string.
     *
     * @param searchString the search string
     * @param nodeToString a method that returns the string to be ordered by from the given node
     */
    public FuzzySearchComparator(String searchString, NodeToString nodeToString) {
        this.searchString = searchString.toLowerCase();
        this.nodeToString = nodeToString;
        lv = new LevenshteinDistance();
    }

    @Override
    public int compare(javafx.scene.Node o1, javafx.scene.Node o2) {
        if (searchString.isEmpty()) {
            return 0;
        }
        int dis = distance(nodeToString.convert(o1)) - distance(nodeToString.convert(o2));
        if (dis == 0) {
            dis = applyLv(nodeToString.convert(o1)) - applyLv(nodeToString.convert(o2));
        }
        return dis;
    }

    private int distance(String str) {
        int min = Integer.MAX_VALUE;
        String shortName = str.toLowerCase();
        if (searchString.length() > shortName.length()) {
            return lv.apply(shortName, searchString);
        } else {
            for (int i = 0; i < shortName.length() - searchString.length(); i++) {
                int distance = applyLv(shortName.substring(i, i + searchString.length()));
                if (distance < min) {
                    min = distance;
                }
            }
        }
        return min;
    }

    private int applyLv(String str) {
        return lv.apply(str, searchString);
    }
}
