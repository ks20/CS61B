package graph;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author Kushal
 */
public class GraphTesting {

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("There are not 0 vertices!", 0, g.vertexSize());
        assertEquals("There are not 0 edges!", 0, g.edgeSize());
        assertEquals("Initial Max Vertex is incorrect", 0, g.maxVertex());
    }

    @Test
    public void emptyUGraph() {
        UndirectedGraph g = new UndirectedGraph();
        assertEquals("There are not 0 vertices!", 0, g.vertexSize());
        assertEquals("There are not 0 edges!", 0, g.edgeSize());
        assertEquals("Initial Max Vertex is incorrect", 0, g.maxVertex());
    }

    @Test
    public void vertexSize() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        assertEquals(10, g.vertexSize());
        g.remove(1);
        assertEquals(9, g.vertexSize());
    }

    @Test
    public void maxVertex() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        assertEquals(10, g.vertexSize());
        assertEquals(10, g.maxVertex());
        g.remove(10);
        assertEquals(9, g.vertexSize());
        assertEquals(9, g.maxVertex());

        g.remove(9);
        assertEquals(8, g.vertexSize());
        assertEquals(8, g.maxVertex());

        g.add();
        g.remove(9);
        assertEquals(8, g.vertexSize());
        assertEquals(8, g.maxVertex());
    }

    @Test
    public void predecessor() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(6, 2);
        g.add(3, 2);
        g.add(1, 5);
        g.add(4, 3);
        g.add(4, 2);
        g.add(2, 5);
        g.add(2, 3);
        assertEquals(6, g.predecessor(2, 0));
        assertEquals(3, g.predecessor(2, 1));
    }

    @Test
    public void predecessorII() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(6, 2);
        g.add();
        g.add(3, 2);
        g.add();
        g.add(1, 5);
        g.add(4, 3);
        g.add(4, 2);
        g.add(2, 5);
        g.add(2, 3);
        assertEquals(6, g.predecessor(2, 0));
        assertEquals(3, g.predecessor(2, 1));
    }

    @Test
    public void testDegree() {
        GraphObj testUGraph1 = new UndirectedGraph();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();


        testUGraph1.add(7, 1);
        testUGraph1.add(7, 3);
        testUGraph1.add(3, 2);
        testUGraph1.add(1, 2);
        testUGraph1.add(1, 4);
        testUGraph1.add(4, 5);


        assertEquals("Incorrect degree", 2, testUGraph1.inDegree(7));
        assertEquals("Incorrect degree", 2, testUGraph1.inDegree(3));
        assertEquals("Incorrect degree", 2, testUGraph1.inDegree(2));
        assertEquals("Incorrect degree", 3, testUGraph1.inDegree(1));
        assertEquals("Incorrect degree", 2, testUGraph1.inDegree(4));
        assertEquals("Incorrect degree", 1, testUGraph1.inDegree(5));
    }

    @Test
    public void testNumEdges() {
        GraphObj testUGraph2 = new UndirectedGraph();
        testUGraph2.add();
        testUGraph2.add();
        testUGraph2.add(1, 2);
        assertEquals("Incorrect Number of Edges", 1, testUGraph2.edgeSize());

        testUGraph2.add(2, 3);
        assertEquals("Incorrect Number of Edges", 2, testUGraph2.edgeSize());

        testUGraph2.add(2, 3);
        assertEquals("Incorrect Number of Edges", 2, testUGraph2.edgeSize());

        testUGraph2.add(2, 2);
        assertEquals("Incorrect Number of Edges", 3, testUGraph2.edgeSize());
    }

    @Test
    public void testContains() {
        GraphObj testUGraph1 = new UndirectedGraph();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();

        testUGraph1.add(7, 1);
        testUGraph1.add(7, 3);
        testUGraph1.add(3, 2);
        testUGraph1.add(1, 2);
        testUGraph1.add(1, 4);
        testUGraph1.add(4, 5);

        assertTrue("Edge does not exist.", testUGraph1.contains(7, 1));
        assertTrue("Edge does not exist.", testUGraph1.contains(7, 3));
        assertTrue("Edge does not exist.", testUGraph1.contains(3, 2));
        assertTrue("Edge does not exist.", testUGraph1.contains(1, 2));
        assertTrue("Edge does not exist.", testUGraph1.contains(1, 4));
        assertTrue("Edge does not exist.", testUGraph1.contains(4, 5));

        assertTrue("Edge does not exist.", testUGraph1.contains(1, 7));
        assertTrue("Edge does not exist.", testUGraph1.contains(3, 7));
        assertTrue("Edge does not exist.", testUGraph1.contains(2, 3));
        assertTrue("Edge does not exist.", testUGraph1.contains(2, 1));
        assertTrue("Edge does not exist.", testUGraph1.contains(4, 1));
        assertTrue("Edge does not exist.", testUGraph1.contains(5, 4));

        assertFalse("Edge does not exist.", testUGraph1.contains(3, 5));
    }

    @Test
    public void testPredecessor() {
        GraphObj testUGraph2 = new UndirectedGraph();
        testUGraph2.add();
        testUGraph2.add();
        testUGraph2.add();
        testUGraph2.add();
        testUGraph2.add();
        testUGraph2.add();
        testUGraph2.add();

        testUGraph2.add(7, 1);
        testUGraph2.add(7, 3);
        testUGraph2.add(3, 2);
        testUGraph2.add(1, 2);
        testUGraph2.add(1, 4);
        testUGraph2.add(4, 5);

        assertEquals("Incorrect Predecessor", 3, testUGraph2.predecessor(2, 0));
        assertEquals("Incorrect Predecessor", 1, testUGraph2.predecessor(2, 1));
        assertEquals("Incorrect Predecessor", 1, testUGraph2.predecessor(4, 0));
        assertEquals("Incorrect Predecessor", 7, testUGraph2.predecessor(3, 0));
        assertEquals("Incorrect Edge Case", 0, testUGraph2.predecessor(6, 0));
    }

    @Test
    public void testPredecessors() {
        GraphObj testUGraph5 = new UndirectedGraph();
        testUGraph5.add();
        testUGraph5.add();
        testUGraph5.add();
        testUGraph5.add();
        testUGraph5.add();
        testUGraph5.add();
        testUGraph5.add();

        testUGraph5.add(7, 1);
        testUGraph5.add(7, 3);
        testUGraph5.add(3, 2);
        testUGraph5.add(1, 2);
        testUGraph5.add(1, 4);
        testUGraph5.add(4, 5);

        ArrayList<Integer> verify = new ArrayList<>();
        verify.add(3);
        verify.add(1);

        ArrayList<Integer> check = new ArrayList<>();
        for (Integer s : testUGraph5.predecessors(2)) {
            check.add(s);
        }
        assertTrue("Incorrect Predecessors",
                (verify.containsAll(check) && check.containsAll(verify)));

        for (int i = 0; i < verify.size(); i++) {
            assertEquals("Incorrect Order", verify.get(i), check.get(i));
        }

        ArrayList<Integer> verify2 = new ArrayList<>();
        verify2.add(4);

        ArrayList<Integer> check2 = new ArrayList<>();
        for (Integer s : testUGraph5.predecessors(5)) {
            check2.add(s);
        }
        assertEquals("Incorrect Order", verify2.get(0), check2.get(0));

        ArrayList<Integer> check3 = new ArrayList<>();
        for (Integer s : testUGraph5.predecessors(6)) {
            check3.add(s);
        }
        assertEquals("Failed Edge Case", 0, check3.size());
    }

    @Test
    public void testSuccessors() {
        GraphObj testUGraph4 = new UndirectedGraph();
        testUGraph4.add(7, 1);
        testUGraph4.add(7, 3);
        testUGraph4.add(3, 2);
        testUGraph4.add(1, 2);
        testUGraph4.add(1, 4);
        testUGraph4.add(4, 5);

        ArrayList<Integer> verify = new ArrayList<>();
        verify.add(1);
        verify.add(3);

        ArrayList<Integer> check = new ArrayList<>();
        for (Integer s : testUGraph4.successors(7)) {
            check.add(s);
        }

        assertTrue("Incorrect Successors",
                (verify.containsAll(check) && check.containsAll(verify)));

        for (int i = 0; i < verify.size(); i++) {
            assertEquals("Incorrect Order", verify.get(i), check.get(i));
        }
    }

    @Test
    public void testOutDegree() {
        GraphObj testGraph1 = new DirectedGraph();
        testGraph1.add();
        testGraph1.add();
        testGraph1.add(1, 2);
        testGraph1.add(1, 3);
        testGraph1.add(1, 4);
        testGraph1.add(2, 4);
        testGraph1.add(2, 5);

        assertEquals("Incorrect outdegree", 3, testGraph1.outDegree(1));
        assertEquals("Incorrect outdegree", 2, testGraph1.outDegree(2));
    }

    @Test
    public void testInDegree() {
        GraphObj testGraph1 = new DirectedGraph();
        testGraph1.add();
        testGraph1.add();
        testGraph1.add();
        testGraph1.add();
        testGraph1.add();
        testGraph1.add();
        testGraph1.add();
        testGraph1.add();
        testGraph1.add();
        testGraph1.add();

        testGraph1.add(1, 10);
        testGraph1.add(2, 10);
        testGraph1.add(4, 10);
        testGraph1.add(6, 10);
        testGraph1.add(8, 10);
        testGraph1.add(4, 10);
        testGraph1.add(8, 10);
        testGraph1.add(10, 10);

        assertEquals("Incorrect indegree", 6, testGraph1.inDegree(10));
        assertEquals("Incorrect # of edges", 6, testGraph1.edgeSize());
    }

    @Test
    public void testRemove() {
        GraphObj testUGraph1 = new DirectedGraph();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();

        testUGraph1.add(7, 1);
        testUGraph1.add(7, 3);
        testUGraph1.add(3, 2);
        testUGraph1.add(1, 2);
        testUGraph1.add(1, 4);
        testUGraph1.add(4, 5);
        assertEquals("Incorrect # of vertices", 7, testUGraph1.vertexSize());

        testUGraph1.remove(7, 1);
        assertEquals("Incorrect # of vertices", 7, testUGraph1.vertexSize());

        testUGraph1.remove(1, 2);
        assertEquals("Incorrect # of vertices", 7, testUGraph1.vertexSize());

        testUGraph1.remove(1, 4);
        assertEquals("Incorrect # of vertices", 6, testUGraph1.vertexSize());

        testUGraph1.remove(7);
        assertEquals("Incorrect # of vertices", 5, testUGraph1.vertexSize());
    }

    @Test
    public void testNumEdgesDGraph() {
        GraphObj testUGraph3 = new DirectedGraph();
        testUGraph3.add();
        testUGraph3.add();
        testUGraph3.add();
        testUGraph3.add();
        testUGraph3.add();
        testUGraph3.add();
        testUGraph3.add();

        testUGraph3.add(7, 1);
        testUGraph3.add(7, 3);
        testUGraph3.add(3, 2);
        testUGraph3.add(1, 2);
        testUGraph3.add(1, 4);
        testUGraph3.add(4, 5);
        testUGraph3.add(4, 4);
        assertEquals("Incorrect Number of Edges", 7, testUGraph3.edgeSize());

        testUGraph3.remove(7, 1);
        assertEquals("Incorrect Number of Edges", 6, testUGraph3.edgeSize());
    }


    @Test
    public void testSuccessor() {
        GraphObj testUGraph3 = new DirectedGraph();
        testUGraph3.add();
        testUGraph3.add();
        testUGraph3.add();
        testUGraph3.add();
        testUGraph3.add();
        testUGraph3.add();
        testUGraph3.add();

        testUGraph3.add(7, 1);
        testUGraph3.add(7, 3);
        testUGraph3.add(3, 2);
        testUGraph3.add(1, 2);
        testUGraph3.add(1, 4);
        testUGraph3.add(4, 5);
        assertEquals("Incorrect Successor", 0, testUGraph3.successor(7, 2));
        assertEquals("Incorrect Successor", 1, testUGraph3.successor(7, 0));
        assertEquals("Incorrect Successor", 3, testUGraph3.successor(7, 1));
    }


    @Test
    public void testPredecessorDGraph() {
        GraphObj testUGraph6 = new DirectedGraph();
        testUGraph6.add();
        testUGraph6.add();
        testUGraph6.add();
        testUGraph6.add();
        testUGraph6.add();
        testUGraph6.add();
        testUGraph6.add();

        testUGraph6.add(7, 1);
        testUGraph6.add(7, 3);
        testUGraph6.add(3, 2);
        testUGraph6.add(1, 2);
        testUGraph6.add(1, 4);
        testUGraph6.add(4, 5);

        assertEquals("Incorrect Predecessor", 3, testUGraph6.predecessor(2, 0));
        assertEquals("Incorrect Predecessor", 1, testUGraph6.predecessor(2, 1));
        assertEquals("Incorrect Predecessor", 7, testUGraph6.predecessor(3, 0));
        assertEquals("Incorrect Edge Case", 0, testUGraph6.predecessor(6, 0));
    }


    @Test
    public void testContainsUGraph() {
        GraphObj testUGraph1 = new DirectedGraph();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();
        testUGraph1.add();

        testUGraph1.add(7, 1);
        testUGraph1.add(7, 3);
        testUGraph1.add(3, 2);
        testUGraph1.add(1, 2);
        testUGraph1.add(1, 4);
        testUGraph1.add(4, 5);

        assertTrue("Edge does not exist.", testUGraph1.contains(7, 1));
        assertTrue("Edge does not exist.", testUGraph1.contains(7, 3));
        assertTrue("Edge does not exist.", testUGraph1.contains(3, 2));
        assertTrue("Edge does not exist.", testUGraph1.contains(1, 2));
        assertTrue("Edge does not exist.", testUGraph1.contains(1, 4));
        assertTrue("Edge does not exist.", testUGraph1.contains(4, 5));


        assertFalse("Edge does not exist.", testUGraph1.contains(3, 5));
    }
}
