import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    @Override
    public List<Node> findShortestPath(Node s, Node t) {
        // Make the queue of nodes we need to visit
        Queue<Node> queueToVisit = new LinkedList<>();
        // These are all the nodes we have already visited or will visit and stores the distance from start node
        HashMap<Node, Integer> alreadyVisited = new HashMap<>();
        // Init the distance from start
        int distanceFromStart = 0;
        // Add the first node to the queue and already visited
        queueToVisit.add(s);
        alreadyVisited.put(s, 0);
        // While we still have things is queue
        while (queueToVisit.size() > 0) {
            // Grab the first node from the queue
            Node n = queueToVisit.poll();
            // If we haven't seen this node
            if (!alreadyVisited.containsKey(n)) {
                // If we found the node we are looking for generate the path to start
                if (n == t) {
                    return generatePathToStart(s, t, alreadyVisited, distanceFromStart);
                } else {
                    // add all of the neighbors to the queue and already visited
                    for (Node neighbor : n.getNeighbors()) {
                        queueToVisit.add(neighbor);
                        // We add one to the depth of the parent to keep the distance from start right
                        alreadyVisited.put(neighbor, alreadyVisited.get(n) + 1);
                    }
                }
            }
        }
        return null;
    }

    private List<Node> generatePathToStart(Node s, Node t, HashMap<Node, Integer> nodeToDistance, Integer distanceFromStart) {

        Queue<Node> queueToVisit = new LinkedList<>();
        List<Node> path = new ArrayList<>();
        queueToVisit.add(t);
        while (queueToVisit.size() > 0) {
            Node n = queueToVisit.poll();
            if (nodeToDistance.get(n) == distanceFromStart - 1) {
                distanceFromStart--;
                path.add(n);
                if (n == s) {
                    Collections.reverse(path);
                    return path;
                } else {
                    queueToVisit.addAll(n.getNeighbors());
                }
            }
        }
        return null;
    }
}
