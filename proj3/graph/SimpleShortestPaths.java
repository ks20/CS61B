package graph;

/* See restrictions in Graph.java. */

/** A partial implementation of ShortestPaths that contains the weights of
 *  the vertices and the predecessor edges.   The client needs to
 *  supply only the two-argument getWeight method.
 *  @author Kushal
 */
public abstract class SimpleShortestPaths extends ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public SimpleShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public SimpleShortestPaths(Graph G, int source, int dest) {
        super(G, source, dest);
        int edges = G.vertexSize();
        weights = new Object[edges + 1];
        pred = new int[edges + 1];
    }

    @Override
    public double getWeight(int v) {
        if (_G.contains(v)) {
            return (double) weights[v];
        } else {
            return Double.MAX_VALUE;
        }
    }

    @Override
    protected void setWeight(int v, double w) {
        weights[v] = w;
    }

    @Override
    public int getPredecessor(int v) {
        if (v < pred.length) {
            return pred[v];
        } else {
            return 0;
        }
    }

    @Override
    protected void setPredecessor(int v, int u) {
        pred[v] = u;
    }

    /** Array representing an int vertex and its double weight. */
    private Object[] weights;
    /** Array representing an the predecessor of a vertex. */
    private int[] pred;

}
