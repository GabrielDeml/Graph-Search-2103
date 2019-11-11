import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.io.*;

/**
 * Code to test Project 3; you should definitely add more tests!
 */
public class GraphPartialTester {
    private IMDBGraph imdbGraph;
    private GraphSearchEngine searchEngine;

    // todo -- make actors_test.list and actresses_test.list the first 100,000 lines of their respective files
    // todo -- in addition, make READ_ENTIRE_FILE true in IMDBGraphImpl when this happens
    // todo --    and do the todo in setUp()

    /**
     * Verifies that there is no shortest path between a specific and actor and actress.
     */
    @Test(timeout = 5000)
    public void findShortestPath() throws IOException {
        imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
        final Node actor1 = imdbGraph.getActor("Actor1");
        final Node actress2 = imdbGraph.getActor("Actress2");
        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress2);
//        System.out.println(shortestPath.toString());
        assertNull(shortestPath);  // there is no path between these people
    }

    /**
     * Verifies that there is no shortest path between a specific and actor and actress.
     */
    @Test(timeout = 5000)
    public void findShortestPathNotNull() throws IOException {
        imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
        final Node actor1 = imdbGraph.getActor("Actor1");
        final Node actress1 = imdbGraph.getActor("Actress1");
        assertNotNull(actress1);
        System.out.println(actress1.getName());
        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress1);
        System.out.println(shortestPath.toString());
//        assert(shortestPath);  // there is no path between these people
    }

    /**
     * Instantiates the graph
     */
    @Before
    public void setUp() throws IOException {
        // imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list"); todo uncomment
        searchEngine = new GraphSearchEngineImpl();
        imdbGraph = new IMDBGraphImpl("D:\\OneDrive - Worcester Polytechnic Institute (wpi.edu)\\B term F\\CS 2103\\project3\\project3\\IMDB\\actors.list",
                "D:\\OneDrive - Worcester Polytechnic Institute (wpi.edu)\\B term F\\CS 2103\\project3\\project3\\IMDB\\actresses.list"); // todo comment out
    }

    /**
     * Just verifies that the graphs could be instantiated without crashing.
     */
    @Test
    public void finishedLoading() {
        assertTrue(true);
        // Yay! We didn't crash
    }

    /**
     * Tests the trimToTitle method
     */
    @Test
    public void testTrimToTitle() {
        assertNull(IMDBGraphImpl.trimLineToTitle(""));
        assertEquals("Natas es Satan (1977)",
                IMDBGraphImpl.trimLineToTitle("'Cartucho' Pena, Ramon\tNatas es Satan (1977)  [Nigth Club Owner]\n"));
        assertEquals("All Out War (2013)",
                IMDBGraphImpl.trimLineToTitle("'Casper' Brown, Jesse\tAll Out War (2013)  (as Jesse Brown)"));
        assertEquals("Avril Lavigne: The Best Damn Tour - Live in Toronto (2008)",
                IMDBGraphImpl.trimLineToTitle("\t\t\tAvril Lavigne: The Best Damn Tour - Live in Toronto (2008) \n"));
        assertEquals("B-Girl (2009)",
                IMDBGraphImpl.trimLineToTitle("\t\t\tB-Girl (2009)  [Battle Judge]  <25>\n"));
        assertEquals("Battle of the Year (2013)",
                IMDBGraphImpl.trimLineToTitle("\t\t\tBattle of the Year (2013)  [Rebel]  <9>"));
        assertEquals("Breakin' Till Dawn (2011)",
                IMDBGraphImpl.trimLineToTitle("\t\t\tBreakin' Till Dawn (2011)  (as Jesse Brown)  [Mini-Van]\n"));
        assertEquals("DJ Dizzy's Dance Hizzy (2005)",
                IMDBGraphImpl.trimLineToTitle("\t\t\tDJ Dizzy's Dance Hizzy (2005) (V)  (as Jesse Brown)  [Casper]"));
        assertNull(IMDBGraphImpl.trimLineToTitle("\t\t\tLemonade Mouth (2011) (TV)  (as Jesse Casper")); // tv movie
        assertNull(IMDBGraphImpl.trimLineToTitle("\t\t\t\"The LXD: The Legion of Extraordinary Dancers\" (2010)"));
        assertNull(IMDBGraphImpl.trimLineToTitle("\t\t\t\"Treme\" (2010) {Carnival Time (#2.7)}  ")); // tv series
        assertNull(IMDBGraphImpl.trimLineToTitle("\t\t\t\"Star Camp\" (2007) {Finale (#1.6)} ")); // tv series
        assertEquals("The Emancipation of Anemone (2016)",
                IMDBGraphImpl.trimLineToTitle("'Cherry, Sahel  \t\tThe Emancipation of Anemone (2016)  [Chorus]\n"));
        assertEquals("Out of Bounds (2012)",
                IMDBGraphImpl.trimLineToTitle("'Chippa' Wilson, Chris \tOut of Bounds (2012)  [Himself]  <1>\n"));
        assertNull(IMDBGraphImpl.trimLineToTitle("'Chu' Estes, Marquis\t\"Moesha\" (1996) {The List (#2.1)}  "));
    }

    /**
     * Verifies that specific movies have been parsed.
     */
    @Test
    public void testSpecificMovies() {
        testFindNode(imdbGraph.getMovies(), "Caceria de judiciales (1997)");
        testFindNode(imdbGraph.getMovies(), "El secuestro de un periodista (1992)");
        testFindNode(imdbGraph.getMovies(), "Violencia en la sierra (1995)");
        assertTrue(imdbGraph.getMovie("Violencia en la sierra (1995)").getNeighbors()
                .contains(imdbGraph.getActor("'La Chispa', Tony")));
        testFindNode(imdbGraph.getMovies(), "A Woman's Face (1941)");
        testFindNode(imdbGraph.getMovies(), "Bikini Bistro (1995)");
        testFindNode(imdbGraph.getMovies(), "Erotik auf der Schulbank (1968)");
        assertTrue(imdbGraph.getMovie("Erotik auf der Schulbank (1968)").getNeighbors()
                .contains(imdbGraph.getActor("Aabel, Hauk (II)")));
    }

    /**
     * Verifies that specific performers have been parsed correctly
     */
    @Test
    public void testSpecificPerformers() {
        // Test for an actor
        testFindNode(imdbGraph.getActors(), "'La Chispa', Tony");
        final Node actor = imdbGraph.getActor("'La Chispa', Tony");
        assertTrue(actor.getNeighbors().contains(imdbGraph.getMovie("Caceria de judiciales (1997)")));
        assertTrue(actor.getNeighbors().contains(imdbGraph.getMovie("El secuestro de un periodista (1992)")));
        assertTrue(actor.getNeighbors().contains(imdbGraph.getMovie("Violencia en la sierra (1995)")));
        assertEquals(3, actor.getNeighbors().size());
        // Test for an actress
        testFindNode(imdbGraph.getActors(), "Aabel, Hauk (II)");
        final Node actress = imdbGraph.getActor("Aabel, Hauk (II)");
        assertTrue(actress.getNeighbors().contains(imdbGraph.getMovie("A Woman's Face (1941)")));
        assertTrue(actress.getNeighbors().contains(imdbGraph.getMovie("Bikini Bistro (1995)")));
        assertTrue(actress.getNeighbors().contains(imdbGraph.getMovie("Erotik auf der Schulbank (1968)")));
        assertEquals(3, actress.getNeighbors().size());
    }

    /**
     * Verifies that the specific graph's nodes contains a node with the specified name
     *
     * @param nodes the nodes of the graph to search from
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

//    /**
//     * Verifies that there is no shortest path between a specific and actor and actress.
//     */
//    @Test(timeout = 5000)
//    public void findShortestPath() throws IOException {
//        imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
//        final Node actor1 = imdbGraph.getActor("Actor1");
//        final Node actress2 = imdbGraph.getActor("Actress2");
//        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress2);
//        assertNull(shortestPath);  // there is no path between these people
//    }
}
