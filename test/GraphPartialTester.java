import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.io.*;

/**
 * Code to test Project 3; you should definitely add more tests!
 */
public class GraphPartialTester {
    IMDBGraph imdbGraph;
    GraphSearchEngine searchEngine;

    /**
     * Verifies that there is no shortest path between a specific and actor and actress.
     */
    @Test(timeout = 5000)
    public void findShortestPath() throws IOException {
        imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
        final Node actor1 = imdbGraph.getActor("Actor1");
        final Node actress2 = imdbGraph.getActor("Actress2");
        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress2);
        assertNull(shortestPath);  // there is no path between these people
    }

    @Before
    /**
     * Instantiates the graph
     */
    public void setUp() throws IOException {
        // imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
        // searchEngine = new GraphSearchEngineImpl(); todo uncomment when GraphSearchEngineImpl is finished
        imdbGraph = new IMDBGraphImpl("/home/gconrad/Downloads/IMDB/actors.list", "/home/gconrad/Downloads/IMDB/actresses.list");
    }

    @Test
    /**
     * Just verifies that the graphs could be instantiated without crashing.
     */
    public void finishedLoading() {
        assertTrue(true);
        // Yay! We didn't crash
    }

    @Test
    /**
     * Verifies that a specific movie has been parsed.
     */
    public void testSpecificMovie() {
        testFindNode(imdbGraph.getMovies(), "Movie1 (2001)");
    }

    @Test
    /**
     * Verifies that specific performers have been parsed correctly
     */
    public void testSpecificPerformers() {
        // todo extract below test and make one common helper function that can easily test more performers
        testFindNode(imdbGraph.getActors(), "'La Chispa', Tony");
        final int[] i = {0}; // array instead of just int to work inside lambda
        imdbGraph.getActor("'La Chispa', Tony").getNeighbors().forEach((Node movie) -> {
            switch (i[0]) {
                case 0:
                    assertEquals("Caceria de judiciales (1997)", movie.getName());
                    break;
                case 1:
                    assertEquals("El secuestro de un periodista (1992)", movie.getName());
                    break;
                case 2:
                    assertEquals("Violencia en la sierra (1995)", movie.getName());
                    break;
            }
            i[0]++;
        });
        assertEquals(3, i[0]);
        i[0] = 0;
        testFindNode(imdbGraph.getActors(), "Aabel, Hauk (II)");
        imdbGraph.getActor("Aabel, Hauk (II)").getNeighbors().forEach((Node movie) -> {
            switch (i[0]) {
                case 0:
                    assertEquals("A Woman's Face (1941)", movie.getName());
                    break;
                case 1:
                    assertEquals("Bikini Bistro (1995)", movie.getName());
                    break;
                case 2:
                    assertEquals("Erotik auf der Schulbank (1968)", movie.getName());
                    break;
            }
            i[0]++;
        });
        assertEquals(3, i[0]);
    }

    /**
     * Verifies that the specific graph contains a node with the specified name
     *
     * @param graph the IMDBGraph to search for the node
     * @param name  the name of the Node
     */
    private static void testFindNode(Collection<? extends Node> nodes, String name) {
        boolean found = false;
        for (Node node : nodes) {
            if (node.getName().trim().equals(name)) {
                found = true;
            }
        }
        assertTrue(found);
    }
}
