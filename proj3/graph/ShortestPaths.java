package graph;

/* See restrictions in Graph.java. */

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.List;

/** The shortest paths through an edge-weighted graph.
 *  By overriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author Kushal
 */
public abstract class ShortestPaths {
    /** Class to represent A* algorithm. */
    private class AStar extends Traversal {

        /** Constructor for A* class.
         * Parameters: Graph G, Queue FRINGE
         * */
        private AStar(Graph G, Queue<Integer> fringe) {
            super(G, fringe);
        }

        @Override
        protected boolean visit(int vert) {
            if (vert == getDest()) {
                return false;
            } else {
                for (Integer successor : _G.successors(vert)) {
                    double vertWeight = getWeight(vert);
                    double succWeight = getWeight(successor);
                    double vertToSucc = getWeight(vert, successor);
                    double edgeVal = vertToSucc + vertWeight;

                    if (edgeVal < succWeight) {
                        setWeight(successor, edgeVal);
                        setPredecessor(successor, vert);
                    }
                }
                return true;
            }
        }
    }

    /** Class to represent Queue used in A* algorithm. */
    private class AStarQueue extends AbstractQueue<Integer> {

        /** Constructor for AStarQueue class. */
        private AStarQueue() {
            pQueue = new PriorityQueue<>(1, new AStarComp());
        }

        @Override
        public boolean offer(Integer a) {
            Vertex newVert = new Vertex(a, getWeight(a) + estimatedDistance(a));
            pQueue.add(newVert);
            return true;
        }

        @Override
        public Integer poll() {
            Vertex v = pQueue.poll();
            if (v == null) {
                return null;
            } else {
                Integer removeVert = v.getVertex();
                return removeVert;
            }
        }

        @Override
        public Integer peek() {
            Vertex v = pQueue.peek();
            if (v == null) {
                return null;
            } else {
                return v.getVertex();
            }
        }

        @Override
        public Iterator<Integer> iterator() {
            return Iteration.iteration(Collections.emptyIterator());
        }

        @Override
        public int size() {
            return pQueue.size();
        }

        /** Class to represent each vertex as a VERTEX. */
        class Vertex {

            /** Constructor to represent each VERTEX with a VALUE. */
            private Vertex(int vertex, double value) {
                vert = vertex;
                val = value;
            }

            /** Getter method for vertex.
             *
             * @return VERT
             */
            public int getVertex() {
                return vert;
            }

            /** Setter method for VERTEX. */
            public void setVertex(int vertex) {
                vert = vertex;
            }

            /** Getter method for value.
             *
             * @return VAL
             */
            public double getValue() {
                return val;
            }

            /** Setter method for VALUE. */
            public void setValue(double value) {
                val = value;
            }

            /** Variable used to store vertex. */
            private int vert;
            /** Variable used to store the value of the vertex. */
            private double val;
        }

        /** Comparator class to compare two vertices. */
        class AStarComp implements Comparator<Vertex> {
            @Override
            public int compare(Vertex vert1, Vertex vert2) {
                if (vert1.getValue() > vert2.getValue()) {
                    return 1;
                } else if (vert1.getValue() < vert2.getValue()) {
                    return -1;
                } else {
                    return -1;
                }
            }
        }

        /** Priority queue of vertices. */
        private PriorityQueue<Vertex> pQueue;
    }


    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        path = new LinkedList<>();
        AStarQueue aQueue = new AStarQueue();
        for (Integer vert : _G.vertices()) {
            setWeight(vert, Double.MAX_VALUE);
            aQueue.add(vert);
        }
        AStar aTraverse = new AStar(_G, aQueue);
        setWeight(_source, 0);
        aTraverse.traverse(_source);
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        while (v != _source) {
            path.addFirst(v);
            v = getPredecessor(v);
        }
        path.addFirst(_source);
        return path;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    /** Linked list from _SOURCE to _DEST. */
    private LinkedList<Integer> path;
}
