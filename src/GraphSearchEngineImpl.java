import java.io.IOException;
import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    @Override
    public List<Node> findShortestPath(Node s, Node t) throws IOException {
        Queue<Node> queueToVisit = new LinkedList<>();
        HashMap<Node, Integer> alreadyVisited = new HashMap<Node, Integer>();
//        addNeighborsToQueue(s, queueToVisit, alreadyVisited, 0);
//        for (Node neighbor : n.getNeighbors()) {
//            queueToVisit.add(neighbor);
//            nodeToDistance.put(neighbor, distanceFromStart + 1);
//        }
        int distanceFromStart = 0;
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
        Queue<Node> queueToVisit = new LinkedList<>();
//        HashMap<Node, Integer> alreadyVisited = new HashMap<Node, Integer>();
        Set<Node> alreadyVisited = new HashSet<Node>();
        addNeighborsToQueue(s, queueToVisit, alreadyVisited);
        int i = 0;
        while (queueToVisit.size() > 0) {
            Node n = queueToVisit.poll();
            if (!nodeToDistance.containsKey(n)
            ) {
                if (n == t){

                }else{


                    for (Node neighbor : node.getNeighbors()) {
                        linkedList.add(neighbor);
                        aV.add(neighbor);
                    }
                }
            }
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
