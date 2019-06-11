package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.Node;

import java.util.List;

public class PathFindController {

    private static List<Node> path;

    public static void setPath(List<Node> newPath) {
        path = newPath;
    }

    public static List<Node> getPath() {
        return path;
    }
}
