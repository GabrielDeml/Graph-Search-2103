import java.io.File;
import java.io.IOException;
import java.util.*;

public class IMDBGraphImpl implements IMDBGraph {
    /**
     * A map representing the performers (the performer's name maps to their Node)
     */
    private Map<String, Node> performers;

    /**
     * A map representing the movies (the movie's name maps to its Node)
     * (NodeImpl chosen instead of Node because .addNeighbor() will need to be called on the movie Nodes)
     */
    private Map<String, NodeImpl> movies;

    /**
     * Constructs a IMDBGraphImpl using the given database files
     *
     * @param actorsFilename    the path to the actors' database file
     * @param actressesFilename the path to the actress' database file
     * @throws IOException if either file is non-existent, cannot be opened, etc.
     */
    public IMDBGraphImpl(String actorsFilename, String actressesFilename) throws IOException {
        performers = new HashMap<>();
        movies = new HashMap<>();
        processFile(new Scanner(new File(actorsFilename), "ISO-8859-1"));
        processFile(new Scanner(new File(actressesFilename), "ISO-8859-1"));
    }

    /**
     * Populates the performers and movies maps from the given scanner
     *
     * @param scanner the scanner to read the information from
     */
    private void processFile(Scanner scanner) {
        // First, move to the start of the data
        while (scanner.hasNextLine()) {
            if (scanner.nextLine().equals("----\t\t\t------")) break;
        }
        // Now, we need to populate the data
        String currPerformer = null;
        Collection<String> movies = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final String currLine = scanner.nextLine();
            if (currLine.isEmpty()) continue; // fixme this may need to be .length() < 4 or something
            if (currLine.charAt(0) == '\t') { // A new movie entry for the current performer
                addMovieToList(currLine, movies);
            } else { // A new performer
                processPerformer(currPerformer, movies); // processes the old performer
                // Setup the new performer
                currPerformer = currLine.substring(currLine.indexOf('\t')); // fixme may need to be (index of tab) - 1? (to account for trailing space)
                if (currPerformer.charAt(currPerformer.length() - 1) == ' ') { // todo remove this (a note to self in case code doesn't work)
                    System.out.println("Current performer ends in a space character. Fix this!"); // todo remove this
                } // todo remove this
                movies = new ArrayList<>();
                // Add the movie on the current line to the performer's movies collection
                addMovieToList(currLine, movies);
            }
        }
    }

    /**
     * Parses the given line and adds it to the movies collection if it is a valid movie
     *
     * @param movieInfo the current line from the database
     * @param movies    the movies collection to add the current movie to
     */
    private void addMovieToList(String movieInfo, Collection<String> movies) {
        if (movieInfo.contains(" (TV) ")) return; // a TV movie
        // Trim everything before the title
        String preTitleTrimmed = null;
        for (int i = movieInfo.indexOf('\t'); i != -1 && i < movieInfo.length(); ++i) {
            if (movieInfo.charAt(i) != '\t') {
                preTitleTrimmed = movieInfo.substring(i);
                break;
            }
        }
        if (preTitleTrimmed == null) return; // something went wrong in parsing the title
        if (preTitleTrimmed.charAt(0) == '"') return; // a TV series
        // Find where the title and year stops (by finding (####))
        // The reason we are finding the index like this is in case the title itself contains parentheses
        int cutOffIndex = -1;
        boolean leftYearParenEncountered = false;
        for (int i = 0; i < preTitleTrimmed.length(); ++i) {
            final char c = preTitleTrimmed.charAt(i);
            if (c == '(') {
                leftYearParenEncountered = true;
            } else if (c == ')') {
                if (leftYearParenEncountered) {
                    cutOffIndex = i; // fixme probably needs to be i + 1 to work properly with substring
                    break;
                }
            } else if (c < '0' || c > '9') {
                leftYearParenEncountered = false;
            }
        }
        if (cutOffIndex == -1) return;
        movies.add(preTitleTrimmed.substring(0, cutOffIndex));
    }

    /**
     * Adds a given performer and their movies to the graph
     *
     * @param name        the name of the performer
     * @param movieTitles the titles of all the movies the performer is in
     */
    private void processPerformer(String name, Collection<String> movieTitles) {
        if (name == null || movieTitles == null || name.isEmpty() || movieTitles.isEmpty()) return;
        final NodeImpl performer = new NodeImpl(name);
        final Collection<Node> performersMovies = new ArrayList<>(movieTitles.size());
        for (String title : movieTitles) {
            NodeImpl movieNode = this.movies.get(title);
            if (movieNode == null) movieNode = new NodeImpl(title);
            movieNode.addNeighbor(performer);
            movies.put(title, movieNode);
            performersMovies.add(movieNode);
        }
        performer.addNeighbors(performersMovies);
        performers.put(name, performer);
    }

    /**
     * Gets all the actor nodes in the graph.
     *
     * @return a collection of all the actor and actress nodes in the graph.
     */
    @Override
    public Collection<? extends Node> getActors() {
        return performers.values();
    }

    /**
     * Gets all the movie nodes in the graph.
     *
     * @return a collection of all the movie nodes in the graph.
     */
    @Override
    public Collection<? extends Node> getMovies() {
        return movies.values();
    }

    /**
     * Returns the movie node having the specified name.
     *
     * @param name the name of the requested movie
     * @return the movie node associated with the specified name or null
     * if no such movie exists.
     */
    @Override
    public Node getMovie(String name) {
        return movies.get(name);
    }

    /**
     * Returns the actor node having the specified name.
     *
     * @param name the name of the requested actor
     * @return the actor node associated with the specified name or null
     * if no such actor exists.
     */
    @Override
    public Node getActor(String name) {
        return performers.get(name);
    }
}
