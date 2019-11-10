import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    @Override
    public List<Node> findShortestPath(Node s, Node t) {
        Queue<Node> queueToVisit = new LinkedList<>();
        HashMap<Node, Integer> alreadyVisited = new HashMap<>();
        Integer distanceFromStart = 0;
        queueToVisit.add(s);
        while (queueToVisit.size() > 0) {
            Node n = queueToVisit.poll();
            if (!alreadyVisited.containsKey(n)) {
                if (n == t) {
                    return generatePathToStart(s, t, alreadyVisited, distanceFromStart);
                } else {
                    for (Node neighbor : n.getNeighbors()) {
                        queueToVisit.add(neighbor);
                        alreadyVisited.put(neighbor, distanceFromStart);
                    }
                }
            }
            distanceFromStart++;
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
                path.add(n);
                if (n == s) {
                    Collections.reverse(path);
                    return path;
                } else {
                    queueToVisit.addAll(n.getNeighbors());
                }
            }
            distanceFromStart--;
        }
        return null;
    }
}
