import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMDBGraphImpl implements IMDBGraph {
    /**
     * Whether or not to read the entire file
     */
    private static final boolean READ_ENTIRE_FILE = true;
    /**
     * If READ_ENTIRE_FILE is false, how many lines of actual data the program should read
     * If READ_ENTIRE_FILE is true, this field is ignored
     */
    private static final int LINES_TO_READ = 100000;
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
    @SuppressWarnings("WeakerAccess")
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
        for (int i = 0; i < LINES_TO_READ && scanner.hasNextLine(); ) {
            if (!READ_ENTIRE_FILE) ++i;
            final String currLine = scanner.nextLine();
            if (currLine.length() < 3) continue; // if the line is effectively empty (< 3 accounts for whitespace)
            if (currLine.charAt(0) == '\t') { // A new movie entry for the current performer
                addMovieToList(currLine, movies);
            } else { // A new performer
                // Make sure the line is properly formatted
                final int tabIndex = currLine.indexOf('\t');
                if (tabIndex < 0) continue;
                // Process the old performer
                processPerformer(currPerformer, movies);
                // Setup the new performer
                currPerformer = currLine.substring(0, tabIndex);
                movies = new ArrayList<>();
                // Add the movie on the current line to the performer's movies collection
                addMovieToList(currLine, movies);
            }
        }
    }

    /**
     * Trims a line from the database to just the movie's title
     * static so it can be easier used in testing
     *
     * @param line the line to trim down to just the movie's title
     * @return the movie's title and its year in the format: Movie Name (2019) or null
     */
    static String trimLineToTitle(String line) {
        if (line == null || line.contains("(TV)")) return null; // either null or a TV movie
        int startIndex = line.indexOf('\t'), endIndex;
        if (startIndex == -1) return null; // no tabs in line, meaning it is not a data entry
        while (startIndex < line.length() && line.charAt(startIndex) == '\t') {
            ++startIndex;
        }
        // Stop if line was just all tabs with no actual data or if the line is a TV series
        if (startIndex >= line.length() || line.charAt(startIndex) == '"') return null;
        // At this point, startIndex is set to the first character of the movie title
        // Now we need to find the index at which the year in parentheses ends (the endIndex)
        // This is easiest through finding the endIndex of a regex match for the year in parentheses
        final Matcher yearMatcher = Pattern.compile("\\((\\?{4}|\\d{4,})(/[IVXLCDM]+)?\\)").matcher(line);
        if (yearMatcher.find()) endIndex = yearMatcher.end();
        else return null; // could not find a year (of 4+ digits) in parentheses, so not a valid entry
        return line.substring(startIndex, endIndex);
    }

    /**
     * Parses the given line and adds it to the movies collection if it is a valid movie
     *
     * @param movieInfoLine the current line from the database
     * @param movies        the movies collection to add the current movie to
     */
    private void addMovieToList(String movieInfoLine, Collection<String> movies) {
        final String movieTitle = trimLineToTitle(movieInfoLine);
        if (movieTitle != null) movies.add(movieTitle);
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
            NodeImpl movieNode = movies.get(title);
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
