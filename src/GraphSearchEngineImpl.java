import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    /**
     * Find the shortest path between two nodes
     * @param s the start node.
     * @param t the target node.
     * @return the shortest path between two nodes
     */

    @Override
    public List<Node> findShortestPath(Node s, Node t) {
        // Make the queue of nodes we need to visit
        Queue<Node> queueToVisit = new LinkedList<>();
        // These are all the nodes we have already visited or will visit and stores the distance from start node
        HashMap<Node, Integer> distanceMap = new HashMap<>();
        // Add the first node to the queue and already visited and distance map
        queueToVisit.add(s);
        distanceMap.put(s, 0);
        // While we still have things is queue
        while (queueToVisit.size() > 0) {
            // Grab the first node from the queue
            Node n = queueToVisit.poll();
            // If we found the node we are looking for generate the path to start
            if (n == t) {
                return generatePathToStart(s, t, distanceMap);
            } else {
                // add all of the neighbors to the queue and already visited
                if (n != null && n.getNeighbors() != null && n.getNeighbors().size() != 0) {
                    for (Node neighbor : n.getNeighbors()) {
                        // If we haven't seen this node
                        if (!distanceMap.containsKey(neighbor)) {
                            //Add to queue
                            queueToVisit.add(neighbor);
                            // We add one to the depth of the parent to keep the distance from start right
                            distanceMap.put(neighbor, distanceMap.get(n) + 1);
                        }
                    }
                }
            }

        }
        return null;
    }

    /**
     * Generate the path to the first node
     * @param s the first node
     * @param t the last node
     * @param nodeToDistance map of a node to the distance to start
     * @return the path from the first node to the last
     */

    private List<Node> generatePathToStart(Node s, Node t, HashMap<Node, Integer> nodeToDistance) {
        // Make the queue and list path to start list
        Queue<Node> queueToVisit = new LinkedList<>();
        // List of what we are going to return
        List<Node> path = new ArrayList<>();
        // find distance from start
        int distanceFromStart = nodeToDistance.get(t);
        // Add the end first node we want to visit
        queueToVisit.add(t);
        // While there is something in queue to visit
        while (queueToVisit.size() > 0) {
            //Get the first thing on the list
            Node n = queueToVisit.poll();
            // When we find a node that is a shorter distance from start
            if (nodeToDistance.containsKey(n) && nodeToDistance.get(n) <= distanceFromStart) {
                // Set the distance closer
                distanceFromStart--;
                // Add N to the path to get to start
                path.add(n);
                // If we found the start node
                if (n == s) {
                    // Get the list in the right order
                    Collections.reverse(path);
                    return path;
                } else {
                    // add all the neighbors to nodes to visit
                    queueToVisit.addAll(n.getNeighbors());
                }
            }
        }
        return null;
    }
}