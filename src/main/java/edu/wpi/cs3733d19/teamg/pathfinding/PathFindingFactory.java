package edu.wpi.cs3733d19.teamg.pathfinding;

import edu.wpi.cs3733d19.teamg.Settings;
import edu.wpi.cs3733d19.teamg.models.Node;

import java.util.ArrayList;


/**
 * This PathFindFactory class is a factory for path finding objects.
 * It creates appropriate pathfinding objects based on the mode sslected in settings
 *
 * @author Anqi Shen
 * @version 1.0
 * @since 2019-04-07
 */
public class PathFindingFactory {

    /**
     * This function creates the right PathFinding object.
     *
     * @param graph all the nodes
     * @param start the start node
     * @param dest  the destination node
     * @return a PathFinding object
     */
    public static PathFinding makePathFinding(ArrayList<Node> graph, Node start, Node dest) {
        //access setting to determine which pathfinding object to make
        String mode = (String) Settings.getSettings().get("pathfinding-mode");
        switch (mode) {
            case "astar":
                PathFinding astar = new PathFindAstar(graph, start, dest);
                return astar;
            case "dfs":
                PathFinding bfs = new PathFindBfs(graph, start, dest);
                return bfs;
            case "bfs":
                PathFinding dfs = new PathFindDfs(graph, start, dest);
                return dfs;
            case "dijkstra":
                PathFinding dijkstra = new PathFindDijkstra(graph, start, dest);
                return dijkstra;
            case "best first":
                PathFinding best = new PathFindBest(graph, start, dest);
                return best;
            default:
                System.out.println("Path finding mode not valid");
                return null;
        }

    }
}
