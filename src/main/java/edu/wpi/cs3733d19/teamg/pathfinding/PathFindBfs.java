package edu.wpi.cs3733d19.teamg.pathfinding;

import edu.wpi.cs3733d19.teamg.models.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * This PathFindBfs class is an instance of PathFinding
 * The path produced by this class is generated with the BFS approach
 *
 * @author Anqi Shen
 * @version 1.0
 * @since 2019-04-07
 */

public class PathFindBfs extends PathFinding {
    public PathFindBfs(List<Node> graph, Node start, Node dest) {
        super(graph, start, dest);
    }

    @Override
    public void pathFind() {
        Node startNode = this.start;
        Node destNode = this.dest;
        ArrayList<Node> path = new ArrayList<>();
        Map<Node, Node> parent = new HashMap<>();
        LinkedList<Node> queue = new LinkedList<>();

        int size = this.graph.size();

        boolean[] visited = new boolean[size];
        for (int i = 0; i < size; i++) {
            visited[i] = false;
        }

        //Visiting the root node
        queue.add(startNode);
        path.add(startNode);

        while (!queue.isEmpty()) {
            //dequeue
            Node vnode = queue.getFirst();
            queue.removeFirst();
            int vindex = this.findNodeIndex(vnode);
            if (!visited[vindex]) {
                visited[vindex] = true;
                if (vnode == destNode) {
                    break;
                } else {
                    List<Node> curNeighbors = vnode.getNeighbors();
                    for (Node node : curNeighbors) {
                        int curIndex = findNodeIndex(node);

                        // when encountering a new Node, add to the stack
                        if (!visited[curIndex] && !queue.contains(node)) {
                            //enqueue
                            queue.add(node);
                            parent.put(node, vnode);

                        }
                    }
                }
            }
        }

        //generate the path and store in the path attribute
        this.pathGen(destNode, parent);
    }

}
