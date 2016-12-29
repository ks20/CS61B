package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author Kushal
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        int total = 0;
        for (int i = 0; i < getGraphRep().size(); i++) {
            ArrayList subList = getGraphRep().get(i);
            String temp = (String) subList.get(0);
            int vert = Integer.parseInt(temp);
            if (vert != v && subList.contains("" + v)) {
                total++;
            } else if (vert == v) {
                if (containsEdge(i, v)) {
                    total++;
                }
            }
        }
        return total;
    }

    @Override
    public int predecessor(int v, int k) {
        ArrayList<Integer> helper = new ArrayList<>();

        for (int i = 0; i < getPredHelper().size(); i++) {
            int getInd = getIndex(getPredHelper().get(i));
            ArrayList target = getGraphRep().get(getInd);
            if (target.contains("" + v)) {
                String val = (String) target.get(0);
                int intVal = Integer.parseInt(val);
                if (intVal != v) {
                    helper.add(intVal);
                } else if (intVal == v) {
                    if (Collections.frequency(target, "" + intVal) == 2) {
                        helper.add(intVal);
                    }
                }
            }
        }

        if (helper.size() == 0) {
            return 0;
        }
        return helper.get(k);
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        ArrayList<Integer> allPredecessors = new ArrayList<>();
        int iterLength = 0;
        for (ArrayList subList : getGraphRep()) {
            if (subList.contains("" + v)) {
                String temp = (String) subList.get(0);
                int vert = Integer.parseInt(temp);
                if (vert != v) {
                    iterLength++;
                } else if (vert == v) {
                    if (Collections.frequency(subList, "" + vert) == 2) {
                        iterLength++;
                    }
                }
            }
        }

        for (int s = 0; s < iterLength; s++) {
            int getPredecessor = predecessor(v, s);
            allPredecessors.add(getPredecessor);
        }

        if (allPredecessors.size() == 0) {
            return Iteration.iteration(Collections.emptyIterator());
        }

        Iterator<Integer> allPredIter = allPredecessors.iterator();
        return Iteration.iteration(allPredIter);
    }

    /** Gets the arrayList from GRAPHREP, whose vertex (i.e.
     * first string element is == U.
     * @return IND
     * */
    private int getIndex(int u) {
        int ind = -1;
        for (int i = 0; i < getGraphRep().size(); i++) {
            ArrayList subList = getGraphRep().get(i);
            String temp = (String) subList.get(0);
            int vert = Integer.parseInt(temp);
            if (vert == u) {
                ind = i;
            }
        }
        return ind;
    }

    /** Returns whether there exists an edge between the vertex
     * and E, assuming I is the correct index of the ArrayList
     * with the vertex.
     * @return true/false
     * */
    private boolean containsEdge(int i, int e) {
        ArrayList temp = getGraphRep().get(i);
        for (int a = 1; a < temp.size(); a++) {
            String hi = (String) temp.get(a);
            int vert = Integer.parseInt(hi);
            if (vert == e) {
                return true;
            }
        }
        return false;
    }
}
