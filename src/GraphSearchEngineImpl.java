import java.io.IOException;
import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    @Override
    public List<Node> findShortestPath(Node s, Node t) throws IOException {
        Queue<Node> queueToVisit = new LinkedList<>();
        HashMap<Node, Integer> alreadyVisited = new HashMap<Node, Integer>();
        int distanceFromStart = 0;
        queueToVisit.add(s);
        while (queueToVisit.size() > 0) {
            Node n = queueToVisit.poll();
            if (!alreadyVisited.containsKey(n)) {
                if (n == t){
                   return generatePathToStart(s, t, alreadyVisited, distanceFromStart);
                }else{
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




    private List<Node> generatePathToStart(Node s, Node t, HashMap nodeToDistance, int distanceFromStart) {
        Set<Node> queueToVisit = new HashSet<Node>();
        List<Node> path = new List<Node>();
        int distanceFromStart = 0;
        queueToVisit.add(t);
        while (queueToVisit.size() > 0) {
            Node n = queueToVisit.remove();
            if (!alreadyVisited.contains(n)) {
                if (n == t){
                    return path;
                }else{
                    for (Node neighbor : n.getNeighbors()) {
                        queueToVisit.add(neighbor);
                        alreadyVisited.add(neighbor);
                    }
                }
            }
            distanceFromStart++;
        }
        return null;
    }


//    /**
//     * Adds the neighbors of a node to linked list
//     * @param node       Note to get neighbors from
//     * @param linkedList to add to
//     * @param aV alreadyVisited hashmap
//     * @param distanceFromStart the distance from the start node
//     */
//    private Queue addNeighborsToQueue(Node node, Queue<Node> linkedList, HashMap aV, int distanceFromStart) {
//
//        for (Node neighbor : node.getNeighbors()) {
//            linkedList.add(neighbor);
//            aV.put(neighbor, distanceFromStart + 1);
//        }
//        return linkedList;
//    }

    /**
     * Adds the neighbors of a node to linked list
     * @param node       Note to get neighbors from
     * @param linkedList to add to
     * @param aV alreadyVisited hashmap
     * @param distanceFromStart the distance from the start node
     */
    private Queue addNeighborsToQueue(Node node, LinkedList<Node> linkedList, Set<Node> aV) {

        for (Node neighbor : node.getNeighbors()) {
            linkedList.add(neighbor);
            aV.add(neighbor);
        }
        return linkedList;
    }

}
