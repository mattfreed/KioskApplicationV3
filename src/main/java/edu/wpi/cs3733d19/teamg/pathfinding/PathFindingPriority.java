package edu.wpi.cs3733d19.teamg.pathfinding;

import edu.wpi.cs3733d19.teamg.models.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public abstract class PathFindingPriority extends PathFinding {
    Map<Node, Integer> cost = new HashMap<>();
    Map<Node, Node> parent = new HashMap<>();
    PriorityQueue<Priority> queueNode = new PriorityQueue<>();

    public PathFindingPriority(List<Node> graph, Node start, Node dest) {
        super(graph, start, dest);
    }


    abstract void mapSetup();

    abstract void visit(Node current);

    abstract void exploreNeighbors(Node current, Node nb);

    @Override
    public void pathFind() {
        // set up the initial map
        mapSetup();

        //explore graph
        while (!queueNode.isEmpty()) {
            Node current = queueNode.poll().getCurNode();
            visit(current);
            if (current == dest) {
                break;
            } else {
                for (Node nb : current.getNeighbors()) {
                    exploreNeighbors(current, nb);
                }
            }
        }

        //create path
        this.pathGen(dest, parent);
    }


    /**
     * Package private class priority holds a Node and an integer representing
     * the cost-so-far of that Node.
     * This class is needed for implementing a priority queue for A*.
     */
    class Priority implements Comparable {
        Node curNode;
        int priority;

        Priority(Node curNode, int priority) {
            this.curNode = curNode;
            this.priority = priority;
        }

        Node getCurNode() {
            return curNode;
        }

        void setCurNode(Node curNode) {
            this.curNode = curNode;
        }

        int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }


        /**
         * The priority queue uses this function to determine the order of the Nodes.
         *
         * @param myP a priority
         * @return int compare method
         */
        @Override
        public int compareTo(Object myP) {
            Priority newP = (Priority) myP;
            if (this.getPriority() == newP.getPriority()) {
                return 0;
            }

            return this.getPriority() > newP.getPriority() ? 1 : -1;
        }
    }

}
