package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author Kushal
 */
abstract class GraphObj extends Graph {

    /** A new, empty Graph. */
    GraphObj() {
        graphRep = new ArrayList<ArrayList<String>>();
        predHelper = new ArrayList<>();
        mysteryHelper = new ArrayList<>();
        seen = new ArrayList<>();
        mystery = 0;
    }

    /** Getter method for graphRep.
     * @return GRAPHREP
     * */
    protected ArrayList<ArrayList<String>> getGraphRep() {
        return graphRep;
    }

    /** Getter method for predHelper.
     * @return PREDEHELPER
     * */
    protected ArrayList<Integer> getPredHelper() {
        return predHelper;
    }

    @Override
    public int vertexSize() {
        return graphRep.size();
    }

    @Override
    public int maxVertex() {
        int max = 0;
        for (ArrayList subList : graphRep) {
            String temp = (String) subList.get(0);
            int vert = Integer.parseInt(temp);
            if (vert > max) {
                max = vert;
            }
        }
        return max;
    }

    @Override
    public int edgeSize() {
        int numEdges = 0;
        for (ArrayList subList : graphRep) {
            numEdges = numEdges + subList.size() - 1;
        }
        if (isDirected()) {
            return numEdges;
        }
        return (numEdges + mystery) / 2;
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        int getInd = getIndex(v);
        if (getInd == -1) {
            return 0;
        }
        int outDeg = graphRep.get(getInd).size() - 1;
        return outDeg;
    }

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        for (ArrayList subList : graphRep) {
            String temp = (String) subList.get(0);
            int vert = Integer.parseInt(temp);
            if (vert == u) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(int u, int v) {
        if (isDirected()) {
            int gInd = getIndex(u);
            if (gInd == -1) {
                return false;
            }
            if (u == v) {
                return (Collections.frequency(graphRep.get(gInd), "" + v) == 2);
            }
            return contains(u) && graphRep.get(gInd).contains("" + v);
        } else if (!isDirected()) {
            int gInd = getIndex(u);
            int getInd2 = getIndex(v);
            if (gInd == -1 || getInd2 == -1) {
                return false;
            }
            if (u == v) {
                return (Collections.frequency(graphRep.get(gInd), "" + v) == 2);
            }
            return ((contains(u) && graphRep.get(gInd).contains("" + v))
                    && (contains(v) && graphRep.get(getInd2).contains("" + u)));
        }
        return false;
    }

    @Override
    public int add() {
        int max = maxVertex();
        boolean flag = false;
        int newVertex = 0;
        for (int i = 1; i < max; i++) {
            int getter = getIndex(i);
            if (getter == -1) {
                ArrayList<String> put = new ArrayList<>();
                put.add("" + i);
                graphRep.add(put);
                newVertex = i;
                flag = true;
                break;
            }
        }
        if (flag) {
            return newVertex;
        } else {
            int item = max + 1;
            ArrayList<String> adder = new ArrayList<>();
            adder.add("" + item);
            graphRep.add(adder);
            return max + 1;
        }
    }

    @Override
    public int add(int u, int v) {
        if (!contains(u)) {
            ArrayList<String> adder = new ArrayList<>();
            adder.add("" + u);
            graphRep.add(adder);
        }
        if (!predHelper.contains(u)) {
            predHelper.add(u);
        }
        int vertexIndex = getIndex(u);
        if (containsEdge(vertexIndex, v)) {
            return u;
        }

        if (isDirected()) {
            graphRep.get(vertexIndex).add("" + v);
        } else if (!isDirected()) {
            if (u == v) {
                mysteryHelper.add("" + u);
                mystery++;
            }
            if (!contains(v)) {
                ArrayList<String> adder2 = new ArrayList<>();
                adder2.add("" + v);
                graphRep.add(adder2);
            }
            int vIndex1 = getIndex(u);
            int vIndex2 = getIndex(v);
            graphRep.get(vIndex1).add("" + v);
            if (containsEdge(vIndex2, u)) {
                return u;
            }
            graphRep.get(vIndex2).add("" + u);
        }
        return u;
    }

    @Override
    public void remove(int v) {
        int listIndex = getIndex(v);
        ArrayList ter = graphRep.get(listIndex);
        ArrayList<Integer> accum = new ArrayList<>();
        if (predHelper.size() > 0) {
            if (predHelper.contains(v)) {
                predHelper.remove(predHelper.indexOf(v));
            }
        }
        if (isDirected()) {
            for (int i = 0; i < graphRep.size(); i++) {
                ArrayList check = graphRep.get(i);
                if (check.contains("" + v)) {
                    String str = (String) check.get(0);
                    int pop = Integer.parseInt(str);
                    accum.add(pop);
                }
            }
        } else {
            for (int i = 1; i < ter.size(); i++) {
                String str = (String) ter.get(i);
                int conv = Integer.parseInt(str);
                int tempInd = getIndex(conv);
                if (containsEdge(tempInd, v)) {
                    accum.add(conv);
                }
            }
        }
        for (int i : accum) {
            remove(i, v);
        }
        graphRep.remove(ter);
    }

    @Override
    public void remove(int u, int v) {
        if (u == v) {
            int getInd = getIndex(u);
            List<String> y = graphRep.get(getInd);
            y.subList(1, y.size()).remove("" + v);
            mysteryHelper.remove("" + u);
            mystery--;
            return;
        }
        if (isDirected()) {
            int getInd = getIndex(u);
            if (getInd == -1) {
                return;
            }
            ArrayList<String> x = graphRep.get(getInd);
            x.remove("" + v);
            if (x.size() == 1) {
                graphRep.remove(x);
            }
        } else {
            int getFirst = getIndex(u);
            int getSecond = getIndex(v);
            if (getFirst == -1 || getSecond == -1) {
                return;
            }
            ArrayList<String> arrFirst = graphRep.get(getFirst);
            ArrayList<String> arrSecond = graphRep.get(getSecond);
            arrFirst.remove("" + v);
            arrSecond.remove("" + u);
        }
    }

    @Override
    public Iteration<Integer> vertices() {
        ArrayList<Integer> accumVertices = new ArrayList<>();
        for (ArrayList sub : graphRep) {
            String temp = (String) sub.get(0);
            int vert = Integer.parseInt(temp);
            accumVertices.add(vert);
        }
        Iterator<Integer> iter = accumVertices.iterator();
        return Iteration.iteration(iter);
    }

    @Override
    public int successor(int v, int k) {
        int getInd = getIndex(v);
        if (getInd == -1) {
            return 0;
        }
        if (k >= graphRep.get(getInd).size() - 1) {
            return 0;
        } else if (outDegree(v) == 0) {
            return 0;
        } else {
            ArrayList temp = graphRep.get(getInd);
            String strTemp = (String) temp.get(k + 1);
            int vert = Integer.parseInt(strTemp);
            return vert;
        }
    }

    @Override
    public abstract int predecessor(int v, int k);

    @Override
    public Iteration<Integer> successors(int v) {
        int getInd = getIndex(v);
        if (getInd == -1) {
            return Iteration.iteration(Collections.emptyIterator());
        }
        int iterLen = graphRep.get(getInd).size();
        ArrayList<Integer> allSuccessors = new ArrayList<>();
        if (contains(v)) {
            for (int s = 0; s < iterLen; s++) {
                int getSuccessor = successor(v, s);
                if (getSuccessor != 0) {
                    allSuccessors.add(getSuccessor);
                }
            }
        }
        Iterator<Integer> allSuccIter = allSuccessors.iterator();
        return Iteration.iteration(allSuccIter);
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        int[] edge;
        ArrayList<int[]> edges = new ArrayList<int[]>();

        if (isDirected()) {
            for (ArrayList sub : graphRep) {
                String temp = (String) sub.get(0);
                int tempss = Integer.parseInt(temp);
                for (int i = 1; i < sub.size(); i++) {
                    String s = (String) sub.get(i);
                    int ss = Integer.parseInt(s);
                    edge = new int[]{tempss, ss};
                    edges.add(edge);
                }
            }
        } else {
            int[] invEdge;
            for (ArrayList sub : graphRep) {
                String temp = (String) sub.get(0);
                int tempss = Integer.parseInt(temp);
                for (int i = 1; i < sub.size(); i++) {
                    String s = (String) sub.get(i);
                    int ss = Integer.parseInt(s);
                    edge = new int[]{tempss, ss};
                    invEdge = new int[]{ss, tempss};

                    boolean flag = false;
                    for (int[] p : seen) {
                        int v1 = p[0];
                        int v2 = p[1];

                        int e1 = edge[0];
                        int e2 = edge[1];

                        if (v1 == e1 && v2 == e2) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        edges.add(edge);
                    }
                    seen.add(edge);
                    seen.add(invEdge);
                }
            }
        }
        Iterator<int[]> iter = edges.iterator();
        return Iteration.iteration(iter);
    }

    @Override
    protected void checkMyVertex(int v) {
        super.checkMyVertex(v);
    }

    @Override
    protected int edgeId(int u, int v) {
        if (contains(u, v)) {
            if (isDirected()) {
                int iD = ((u + v) * (u + v + 1) + v);
                return iD;
            } else if (!isDirected()) {
                if (u > v) {
                    int temp = v;
                    v = u;
                    u = temp;
                }
                int iD = ((u + v) * (u + v + 1) + v);
                return iD;
            }
        }
        return 0;
    }

    /** Gets the arrayList from GRAPHREP, whose vertex (i.e.
     * first string element is == U.
     * @return IND
     * */
    private int getIndex(int u) {
        int ind = -1;
        for (int i = 0; i < graphRep.size(); i++) {
            ArrayList subList = graphRep.get(i);
            String temp = (String) subList.get(0);
            int vert = Integer.parseInt(temp);
            if (vert == u) {
                ind = i;
            }
        }
        return ind;
    }

    /** Returns whether there exists an edge between the vertex
     * and ED, assuming I is the correct index of the ArrayList
     * with the vertex.
     * @return true/false
     * */
    private boolean containsEdge(int i, int ed) {
        ArrayList temp = graphRep.get(i);
        for (int a = 1; a < temp.size(); a++) {
            String hi = (String) temp.get(a);
            int vert = Integer.parseInt(hi);
            if (vert == ed) {
                return true;
            }
        }
        return false;
    }

    /** Nested ArrayList of Strings representing the Graph. */
    private ArrayList<ArrayList<String>> graphRep;
    /** ArrayList that helps with finding predecessors. */
    private ArrayList<Integer> predHelper;
    /** ArrayList of int arrays that helps generate the edges. */
    private ArrayList<int[]> seen;
    /** Variable that accounts for double counts of edges (i.e. (1, 1)). */
    private static int mystery;
    /** ArrayList that also helps with identifying double counts. */
    private static ArrayList<String> mysteryHelper;
}
