package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** Represents an undirected graph.  Out edges and in edges are not
 *  distinguished.  Likewise for successors and predecessors.
 *
 *  @author Kushal
 */
public class UndirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public int inDegree(int v) {
        return outDegree(v);
    }

    @Override
    public int predecessor(int v, int k) {
        int getter = getIndex(v);
        if (getter == -1) {
            return 0;
        }
        List<String> targ = getGraphRep().get(getter).subList(1,
                getGraphRep().get(getter).size());

        if (targ.size() == 0) {
            return 0;
        }
        return Integer.parseInt(targ.get(k));
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        ArrayList<Integer> allPredecessors = new ArrayList<>();
        int getter = getIndex(v);
        if (getter == -1) {
            return Iteration.iteration(Collections.emptyIterator());

        }
        ArrayList target = getGraphRep().get(getter);

        for (int s = 0; s < target.size() - 1; s++) {
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
}
