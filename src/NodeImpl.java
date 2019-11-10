import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple wrapper to implement a Node
 */
public class NodeImpl implements Node {
    /**
     * This Node's name
     */
    private String name;

    /**
     * This Node's neighbors
     */
    private Collection<Node> neighbors;

    /**
     * Constructs a NodeImpl with the given name and no neighbors
     *
     * @param name the name to construct the node with
     */
    @SuppressWarnings("WeakerAccess")
    public NodeImpl(String name) {
        this.name = name;
        this.neighbors = new ArrayList<>();
    }

    /**
     * Adds a neighbor to the node
     *
     * @param neighbor the neighbor to add
     */
    @SuppressWarnings("WeakerAccess")
    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }

    /**
     * Adds a collection of nodes as neighbors
     *
     * @param neighbors the neighboring nodes to add
     */
    @SuppressWarnings("WeakerAccess")
    public void addNeighbors(Collection<Node> neighbors) {
        this.neighbors.addAll(neighbors);
    }

    /**
     * Returns the name of the node (e.g., "Judy Garland").
     *
     * @return the name of the Node.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the Collection of neighbors of the node.
     *
     * @return the Collection of all the neighbors of this Node.
     */
    @Override
    public Collection<? extends Node> getNeighbors() {
        return neighbors;
    }
}
