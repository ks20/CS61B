package graph;

import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.assertTrue;

/**
 * Created by Kushal on 11/24/16.
 */
public class TestTraversal {

    private class TestBFS extends BreadthFirstTraversal {
        private TestBFS(Graph G) {
            super(G);
        }

        @Override
        protected boolean visit(int v) {
            checkList.add(v);
            return true;
        }

        private ArrayList<Integer> getList() {
            return checkList;
        }
        private ArrayList<Integer> checkList = new ArrayList<Integer>();
    }

    private class TestDFS extends DepthFirstTraversal {
        private TestDFS(Graph G) {
            super(G);
            _G = G;
        }

        @Override
        protected boolean visit(int v) {
            visitedList.add(v);
            return true;
        }

        @Override
        protected boolean postVisit(int v) {
            postVisitedList.add(v);
            return true;
        }

        @Override
        protected boolean shouldPostVisit(int v) {
            if (_G.outDegree(v) == 0) {
                return true;
            }
            return !postVisitedList.contains(v);
        }

        protected ArrayList<Integer> getList() {
            return visitedList;
        }

        private ArrayList<Integer> visitedList = new ArrayList<Integer>();
        private ArrayList<Integer> postVisitedList = new ArrayList<Integer>();
        private Graph _G;
    }

    @Test
    public void bfTraverse() {
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
        g.add();
        g.add();
        g.add(1, 4);
        g.add(4, 5);
        g.add(5, 6);
        g.add(1, 7);
        g.add(7, 8);
        g.add(8, 9);
        g.add(1, 10);
        g.add(10, 11);
        g.add(11, 12);
        ArrayList<Integer> check1 = new ArrayList<Integer>();
        check1.add(1);
        check1.add(4);
        check1.add(7);
        check1.add(10);
        check1.add(5);
        check1.add(8);
        check1.add(11);
        check1.add(6);
        check1.add(9);
        check1.add(12);
        ArrayList<Integer> check2 = new ArrayList<Integer>();
        check2.add(1);
        check2.add(10);
        check2.add(7);
        check2.add(4);
        check2.add(5);
        check2.add(8);
        check2.add(11);
        check2.add(12);
        check2.add(9);
        check2.add(6);

        TestBFS test = new TestBFS(g);
        test.traverse(1);
        ArrayList<Integer> tList = test.getList();
        assertTrue((tList.equals(check1)) || (tList.equals(check2))
                || (tList.equals(getOption())));
    }

    private ArrayList getOption() {
        ArrayList<Integer> check3 = new ArrayList<Integer>();
        check3.add(1);
        check3.add(10);
        check3.add(7);
        check3.add(4);
        check3.add(11);
        check3.add(8);
        check3.add(5);
        check3.add(12);
        check3.add(9);
        check3.add(6);
        return check3;
    }


    @Test
    public void dfTraverseDG() {
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
        g.add();
        g.add();

        g.add(1, 4);
        g.add(4, 5);
        g.add(5, 6);
        g.add(1, 7);
        g.add(7, 8);
        g.add(8, 9);
        g.add(1, 10);
        g.add(10, 11);
        g.add(11, 12);

        ArrayList<Integer> check1 = new ArrayList<Integer>();
        check1.add(1);
        check1.add(4);
        check1.add(5);
        check1.add(6);
        check1.add(7);
        check1.add(8);
        check1.add(9);
        check1.add(10);
        check1.add(11);
        check1.add(12);

        ArrayList<Integer> check2 = new ArrayList<Integer>();
        check2.add(1);
        check2.add(10);
        check2.add(11);
        check2.add(12);
        check2.add(7);
        check2.add(8);
        check2.add(9);
        check2.add(4);
        check2.add(5);
        check2.add(6);

        TestDFS test = new TestDFS(g);
        test.traverse(1);
        ArrayList<Integer> testList = test.getList();
        assertTrue((testList.equals(check1))
                || (testList.equals(check2)));
    }

    @Test
    public void dfTraverseUG() {
        UndirectedGraph g = new UndirectedGraph();
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
        g.add();
        g.add();
        g.add(1, 4);
        g.add(4, 5);
        g.add(5, 6);
        g.add(1, 7);
        g.add(1, 9);
        g.add(6, 1);
        g.add(7, 8);
        g.add(8, 9);
        g.add(1, 10);
        g.add(10, 11);
        g.add(11, 12);

        ArrayList<Integer> check1 = new ArrayList<Integer>();
        check1.add(1);
        check1.add(6);
        check1.add(5);
        check1.add(4);
        check1.add(7);
        check1.add(8);
        check1.add(9);
        check1.add(10);
        check1.add(11);
        check1.add(12);

        ArrayList<Integer> check2 = new ArrayList<Integer>();
        check2.add(1);
        check2.add(9);
        check2.add(8);
        check2.add(7);
        check2.add(10);
        check2.add(11);
        check2.add(12);
        check2.add(6);
        check2.add(5);
        check2.add(4);

        TestDFS test = new TestDFS(g);
        test.traverse(1);
        ArrayList<Integer> testList = test.getList();
        assertTrue((testList.equals(check1))
                || (testList.equals(check2))
                || (testList.equals(getOptionII()))
                || (testList.equals(getOptionIII()))
                || (testList.equals(getOptionIV())));
    }

    private ArrayList getOptionII() {
        ArrayList<Integer> check3 = new ArrayList<Integer>();
        check3.add(1);
        check3.add(10);
        check3.add(11);
        check3.add(12);
        check3.add(7);
        check3.add(8);
        check3.add(9);
        check3.add(4);
        check3.add(5);
        check3.add(6);
        return check3;
    }

    private ArrayList getOptionIII() {
        ArrayList<Integer> check4 = new ArrayList<Integer>();
        check4.add(1);
        check4.add(10);
        check4.add(11);
        check4.add(12);
        check4.add(6);
        check4.add(5);
        check4.add(4);
        check4.add(9);
        check4.add(8);
        check4.add(7);
        return check4;
    }

    private ArrayList getOptionIV() {
        ArrayList<Integer> check5 = new ArrayList<Integer>();
        check5.add(1);
        check5.add(9);
        check5.add(8);
        check5.add(7);
        check5.add(10);
        check5.add(11);
        check5.add(12);
        check5.add(6);
        check5.add(5);
        check5.add(4);
        return check5;
    }
}
