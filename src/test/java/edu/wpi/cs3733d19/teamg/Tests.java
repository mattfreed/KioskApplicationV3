package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.Node;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


class Tests {
    /**
     * Converts a Date to a Timestamp.
     * @param date the Date to convert
     * @return the converted Timestamp.
     */
    static Timestamp makeTimestamp(Date date) {
        return new Timestamp(date.getTime());

    }

    /**
     * Returns a new Date object based off input with 0 milliseconds.
     *
     * @param year   the year
     * @param month  the month
     * @param day    the day
     * @param hour   the hour
     * @param minute the minute
     * @param second the second
     * @return the created Date object
     */
    static Date makeDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Returns a new Date object based off input with 0 seconds and 0 milliseconds.
     *
     * @param year   the year
     * @param month  the month
     * @param day    the day
     * @param hour   the hour
     * @param minute the minute
     * @return the created Date object
     */
    static Date makeDate(int year, int month, int day, int hour, int minute) {
        return makeDate(year, month, day, hour, minute, 0);
    }

    /**
     * Searches the given graph for the target Node from the start Node, breadth-first.
     * @param start the Node to start at
     * @param target the Node to search for
     * @param graph the graph of Nodes
     * @return whether or not the target was found
     */
    static boolean bfs(Node start, Node target, List<Node> graph) {
        int size = graph.size();

        boolean[] visited = new boolean[size];
        for (int i = 0; i < size; i++) {
            visited[i] = false;
        }
        LinkedList<Node> queue = new LinkedList<>();

        int rootIndex = findNodeIndex(start, graph);
        if (rootIndex == -1) {
            System.out.println("Starting node doesn't exist in graph");
            return false;
        }

        int targetIndex = findNodeIndex(target, graph);
        if (targetIndex == -1) {
            System.out.println("Target node doesn't exist in graph");
            return false;
        }

        queue.add(start);

        while (!queue.isEmpty()) {
            Node first = queue.getFirst();
            queue.removeFirst();

            int index = findNodeIndex(first, graph);
            if (!visited[index]) {
                visited[index] = true;
                if (first == target) {
                    return true;
                } else {
                    for (Node node : first.getNeighbors()) {
                        if (!visited[findNodeIndex(node, graph)]) {
                            queue.add(node);
                        }
                    }
                }
            }
        }
        return false;
    }

    private static int findNodeIndex(Node node, List<Node> graph) {
        int size = graph.size();
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (node == graph.get(i)) {
                index = i;
            }
        }
        return index;
    }
}
