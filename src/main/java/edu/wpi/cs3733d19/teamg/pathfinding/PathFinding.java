package edu.wpi.cs3733d19.teamg.pathfinding;


import edu.wpi.cs3733d19.teamg.KioskApplication;
import edu.wpi.cs3733d19.teamg.PathFindController;
import edu.wpi.cs3733d19.teamg.models.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This abstract class is the general class for path finding methods.
 * It follows the strategy design pattern as required by the Iteration 2 project
 *
 * @author Anqi Shen
 * @version 1.0
 * @since 2019-04-05
 */
public abstract class PathFinding {
    List<Node> graph;
    Node start;
    Node dest;
    private ArrayList<Node> path;

    /**
     * The method constructs a PathFinding object.
     *
     * @param graph a list of nodes.
     * @param start the starting node.
     * @param dest  the destination node.
     */
    PathFinding(List<Node> graph, Node start, Node dest) {
        this.graph = graph;
        this.start = start;
        this.dest = dest;
        this.path = new ArrayList<>();
    }

    public List<Node> getGraph() {
        return graph;
    }

    public void setGraph(List<Node> graph) {
        this.graph = graph;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getDest() {
        return dest;
    }

    public void setDest(Node dest) {
        this.dest = dest;
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    public void setPath(ArrayList<Node> path) {
        this.path = path;
    }

    /**
     * This method deals with different ways to path find.
     * Admin can choose the algorithm and store it in settings.
     */
    public abstract void pathFind();

    /**
     * This method is a helper function that generates the path
     * from a parent chart and a destination Node.
     *
     * @param dest   destination node
     * @param parent parent map
     */
    void pathGen(Node dest, Map<Node, Node> parent) {
        if (parent.get(dest) != null) {
            Node cur = dest;
            this.path.add(dest);
            while (parent.get(cur) != null) {
                this.path.add(0, parent.get(cur));
                cur = parent.get(cur);
            }
        } else {
            System.out.println("path not found");
        }
    }

    /**
     * This is a helper function for DFS and BFS
     * that finds the index of a given Node is a graph .
     * It allows the array for checking whether a Node is visited to work.
     *
     * @param node a given node.
     * @return int integer of the index.
     */

    int findNodeIndex(Node node) {
        int size = this.graph.size();
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (node == this.graph.get(i)) {
                index = i;
            }
        }
        return index;
    }

    /**
     * This function returns the textual directions in a list of strings.
     *
     * @return list of strings
     */
    public ArrayList<ArrayList<String>> textualPath(ArrayList<Node> myPath) {
        int floors;

        ArrayList<Direction> directions = new ArrayList<>();
        ArrayList<Node> cleanPath = new ArrayList<>();
        //filter out none intersection nodes
        boolean inElevator = false;
        cleanPath.add(myPath.get(0));
        if (myPath.get(0).getNodeType().contains("ELEV")
                || myPath.get(0).getNodeType().contains("STAI")) {
            inElevator = true;
            if (myPath.get(0).getFloor().equals(myPath.get(1).getFloor())) {
                inElevator = false;

            }
        }


        for (int index = 1; index < myPath.size() - 1; index++) {
            Node curNode = myPath.get(index);

            if (!curNode.getNodeType().contains("ELEV")
                    && !curNode.getNodeType().contains("STAI")) {
                if (curNode.getNeighbors().size() > 2) {
                    cleanPath.add(myPath.get(index));
                }
            } else {
                if (inElevator) {
                    if (curNode.getFloor().equals(myPath.get(index + 1).getFloor())) {
                        inElevator = false;
                        cleanPath.add(myPath.get(index));
                    }
                } else {
                    if (index == path.size() - 2) {
                        inElevator = false;
                        cleanPath.add(myPath.get(index));
                    } else if (!curNode.getFloor().equals(myPath.get(index + 1).getFloor())) {
                        inElevator = true;
                        cleanPath.add(myPath.get(index));
                    }
                }

            }
        }


        cleanPath.add(myPath.get(myPath.size() - 1));

        PathFindController.setPath(cleanPath);

        for (int index = 1; index < cleanPath.size() - 1; index++) {

            //generate direction
            Node pre = cleanPath.get(index - 1);
            Node cur = cleanPath.get(index);
            Node nxt = cleanPath.get(index + 1);

            //create Direction objects
            directions.add(this.getTurnDir(pre, cur, nxt));
        }



        // first direction
        int firstX = cleanPath.get(1).getXCoord() - cleanPath.get(0).getXCoord();
        int firstY = cleanPath.get(1).getYCoord() - cleanPath.get(0).getYCoord();
        String firstDir = "You are starting at "
                + cleanPath.get(0).getShortName().trim() + " and facing ";
        if (firstX < 0) {
            firstDir += "west.";
        } else if (firstX > 0) {
            firstDir += "east";
        } else if (firstY < 0) {
            firstDir += "south.";
        } else {
            firstDir += "north.";
        }
        ArrayList<String> text = new ArrayList<>();

        text.add(firstDir);


        //check if the first two nodes are elevators or stairs
        Direction stairDir = checkForStairs(cleanPath.get(0), cleanPath.get(1), 0);
        int directionIndex = 0;
        if (stairDir != null) {
            String stairText = stairDir.getTurn();
            text.add(stairText);
            directionIndex = 1;
        }


        // all the intermediate steps
        for (int in = directionIndex; in < directions.size(); in++) {
            text.add(directions.get(in).toString());
        }

        //last step
        int lastDist = (int) Math.sqrt(Math.pow((cleanPath.get(cleanPath.size() - 2).getXCoord()
                - cleanPath.get(cleanPath.size() - 1).getXCoord()), 2)
                + Math.pow((cleanPath.get(cleanPath.size() - 2).getYCoord()
                - cleanPath.get(cleanPath.size() - 1).getYCoord()), 2));
        String lastDir = "Walk for " + lastDist / 6;
        if ((int) lastDist / 6 == 1) {
            lastDir += " foot, and you are at ";
        } else {
            lastDir += " feet, and you are at ";
        }
        lastDir += cleanPath.get(cleanPath.size() - 1).getShortName();

        text.add(lastDir);


        ArrayList<ArrayList<String>> finalText = new ArrayList<>();
        finalText.add(new ArrayList<String>());
        int currFloor = 0;
        for (int i = 0; i < text.size(); i++) {

            finalText.get(currFloor).add(text.get(i));
            if (text.get(i).contains("down") || text.get(i).contains("up")) {
                finalText.add(new ArrayList<String>());
                currFloor++;
            }

        }


        return finalText;
    }

    private Direction checkForStairs(Node n1, Node n2, int dist) {
        String dir = "";
        int floorDiff = n1.floorLookUp(n1.getFloor()) - n2.floorLookUp(n2.getFloor());
        if (floorDiff != 0) {
            if (n1.getShortName().contains("Elevator") && n2.getShortName().contains("Elevator")) {
                dir += "take the elevator " + n1.getShortName();
            } else if (n1.getNodeType().equals("STAI") && n2.getNodeType().equals("STAI")) {
                dir += "take the stairs " + n1.getShortName();
            } else {
                dir += "go from " + n1.getShortName();
            }

            if (floorDiff > 0) {
                dir += " down to floor " + n2.getFloor();
            } else {
                dir += " up to floor " + n2.getFloor();
            }
        } else {
            return null;
        }

        return new Direction(dir, dist);
    }


    /**
     * This function takes 3 consecutive nodes and generates the right direction.
     *
     * @param pre previous node.
     * @param cur current node.
     * @param nxt next node.
     * @return a direction object
     */
    private Direction getTurnDir(Node pre, Node cur, Node nxt) {
        Direction dir = new Direction("", 0);

        // dist is the distance from previous node to current node
        int x1 = cur.getXCoord() - pre.getXCoord();
        int y1 = cur.getYCoord() - pre.getYCoord();
        int x2 = nxt.getXCoord() - cur.getXCoord();
        int y2 = nxt.getYCoord() - cur.getYCoord();


        // dist is the distance from previous node to current node
        int dist = (int) Math.sqrt(x1 * x1 + y1 * y1);
        dir.setDist(dist);

        //get directions from the x and y positions of the nodes

        Direction stairDir = checkForStairs(cur, nxt, dist);
        if (stairDir != null) {
            dir = stairDir;
        } else {
            //find landmark near intersection
            List<Node> neighbors = cur.getNeighbors();
            String landmark = "";
            for (Node nb : neighbors) {
                if (!nb.getNodeType().equals("HALL") && nb != pre
                        && nb.getFloor().equals(cur.getFloor())) {
                    landmark = " when you see " + nb.getShortName();
                }
            }



            if ((Math.abs(x1) < 20 && Math.abs(x2) < 20)
                    || (Math.abs(y1) < 20 && Math.abs(y2) < 20)) {
                dir.setTurn("follow the hall way" + landmark);
            } else if (x1 > 20) {
                if (y2 < -20) {
                    dir.setTurn("turn left" + landmark);
                } else {
                    dir.setTurn("turn right" + landmark);
                }
            } else if (x1 < -20) {
                if (y2 < -20) {
                    dir.setTurn("turn right" + landmark);
                } else {
                    dir.setTurn("turn left" + landmark);
                }
            } else if (y1 > 20) {
                if (x2 < -20) {
                    dir.setTurn("turn right" + landmark);
                } else {
                    dir.setTurn("turn left" + landmark);
                }
            } else {
                if (x2 < -20) {
                    dir.setTurn("turn left" + landmark);
                } else {
                    dir.setTurn("turn right" + landmark);
                }
            }
        }

        return dir;
    }


    /**
     * Private class Direction that holds the directions.
     */

    private class Direction {
        String turn;
        int dist;

        public Direction(String turn, int dist) {
            this.turn = turn;
            this.dist = dist;
        }

        public String getTurn() {
            return turn;
        }

        public void setTurn(String turn) {
            this.turn = turn;
        }

        public int getDist() {
            return dist;
        }

        public void setDist(int dist) {
            this.dist = dist;
        }

        @Override
        public String toString() {
            String toPrint = "Walk for " + this.dist / 6;
            if ((int) this.dist / 6 == 1) {
                toPrint += " foot, then ";
            } else {
                toPrint += " feet, then ";
            }

            toPrint += this.turn;
            return toPrint;
        }

        //to audio method here
    }

}
