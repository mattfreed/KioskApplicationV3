package edu.wpi.cs3733d19.teamg.pathfinding;

import edu.wpi.cs3733d19.teamg.models.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class PathFindBest extends PathFindingPriority {
    public PathFindBest(List<Node> graph, Node start, Node dest) {
        super(graph, start, dest);
    }


    @Override
    void mapSetup() {
        for (Node n : this.graph) {
            cost.put(n, 0); //0->maxvalue
            parent.put(n, null);
        }

        // start on the start Node
        cost.put(start, 1); //1->0
        queueNode.add(new Priority(start, start.distanceTo(dest))); //dist ->0
    }

    @Override
    void visit(Node current) {
        cost.put(current, 1);
    }

    @Override
    void exploreNeighbors(Node current, Node nb) {
        if (cost.get(nb) == 0 && !queueNode.contains(nb)) {
            //add neighbor node to q with priority
            queueNode.add(new Priority(nb, nb.distanceTo(dest)));
            parent.put(nb, current);
        }

    }
}