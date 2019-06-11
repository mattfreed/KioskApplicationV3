package edu.wpi.cs3733d19.teamg.pathfinding;

import edu.wpi.cs3733d19.teamg.models.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This PathFindAstar class is an instance of PathFinding
 * The path produced by this class is generated with the A* approach
 *
 * @author  Anqi Shen
 * @version 1.0
 * @since   2019-04-05
 */

public class PathFindAstar extends PathFindingPriority {
    public PathFindAstar(List<Node> graph, Node start, Node dest) {
        super(graph, start, dest);
    }

    @Override
    void mapSetup() {
        for (Node n : this.graph) {
            cost.put(n, Integer.MAX_VALUE);
            parent.put(n, null);
        }

        // start on the start Node
        cost.put(start, 0);
        queueNode.add(new PathFindAstar.Priority(start, 0));
    }

    @Override
    void visit(Node current) {
    }

    @Override
    void exploreNeighbors(Node current, Node nb) {
        int newCost = cost.get(current) + current.costTo(nb);
        if (newCost < cost.get(nb)) {
            int priority = newCost + current.distanceTo(dest);
            //add neighbor node to q with priority
            queueNode.add(new PathFindAstar.Priority(nb, priority));
            cost.put(nb, newCost);
            parent.put(nb, current);
        }
    }

}
