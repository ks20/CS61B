package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayDeque;
import java.util.Collections;

/** Implements a depth-first traversal of a graph.  Generally, the
 *  client will extend this class, overriding the visit and
 *  postVisit methods, as desired (by default, they do nothing).
 *  @author Kushal
 */
public class DepthFirstTraversal extends Traversal {

    /** A depth-first Traversal of G. */
    protected DepthFirstTraversal(Graph G) {
        super(G, Collections.asLifoQueue(new ArrayDeque<Integer>() { }));
        _G = G;
    }

    @Override
    protected boolean visit(int v) {
        return super.visit(v);
    }

    @Override
    protected boolean postVisit(int v) {
        if (!marked(v)) {
            mark(v);
            for (Integer edg : _G.successors(v)) {
                traverse(edg);
            }
            visit(v);
        }
        return super.postVisit(v);
    }

    @Override
    protected boolean shouldPostVisit(int v) {
        return true;
    }

    /** Pointer to the Graph. */
    private final Graph _G;
}
