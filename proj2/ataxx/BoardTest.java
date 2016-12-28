package ataxx;

import org.junit.Test;
import static org.junit.Assert.*;

/** Tests of the Board class.
 *  @KushalSingh
 */
public class BoardTest {

    private static final String[]
        GAME1 = { "a7-b7", "a1-a2",
                  "a7-a6", "a2-a3",
                  "a6-a5", "a3-a4" };

    private static final String[]
            GAME2 = { "a7-a6", "a1-a2", "a6-c7", "a2-a4", "c7-e7", "a4-a6",
                "e7-f7"};

    private static final String[]
            GAME3 = {"a7-a5", "g7-g5", "a5-a3", "g5-g3", "a3-a2", "g3-g2"};

    private static final String[]
            GAME4 = { "a7-a6", "g7-f5",
                "a6-a5", "a1-a2",
                "g1-e3", "f5-f4",
                "a5-c5", "a2-c2"};

    private static final String[]
            BLOCKS = {"b2", "c3"};

    private static void setBlocks(Board b, String[] moves) {
        for (String s : moves) {
            b.setBlock(s.charAt(0), s.charAt(1));
        }
    }

    private static void makeMoves(Board b, String[] moves) {
        for (String s : moves) {
            b.makeMove(s.charAt(0), s.charAt(1),
                       s.charAt(3), s.charAt(4));
        }
    }

    @Test public void testInitialConfig() {
        Board b0 = new Board();
        assertEquals(72, b0.getBlocksOnBorders());
        assertEquals(49, b0.numPieces(PieceColor.EMPTY));
    }

    @Test public void testEdgeCase1() {
        Board b0 = new Board();
        makeMoves(b0, GAME2);
        assertEquals(3, b0.bluePieces());
        assertEquals(4, b0.redPieces());
        assertEquals(7, b0.numMoves());
    }

    @Test public void testEdgeCase2() {
        Board b0 = new Board();
        makeMoves(b0, GAME3);
        assertEquals(3, b0.bluePieces());
        assertEquals(3, b0.redPieces());
    }

    @Test public void testEquals() {
        Board b0 = new Board();
        Board bCopy = new Board(b0);
        assertTrue(b0.equals(bCopy));
    }

    @Test public void testBlocks() {
        Board b0 = new Board();
        setBlocks(b0, BLOCKS);
        assertTrue((b0.get('b', '6')).equals(PieceColor.BLOCKED));
        assertTrue((b0.get('c', '5')).equals(PieceColor.BLOCKED));
        assertTrue((b0.get('f', '2')).equals(PieceColor.BLOCKED));
        assertTrue((b0.get('e', '5')).equals(PieceColor.BLOCKED));
        assertTrue((b0.get('f', '6')).equals(PieceColor.BLOCKED));
        assertEquals(8, b0.blockPieces());
    }

    @Test public void testIllegalBlock() {
        Board b0 = new Board();
        assertFalse(b0.legalBlock('a', '1'));
        assertFalse(b0.legalBlock('g', '1'));
        assertFalse(b0.legalBlock('a', '7'));
        assertFalse(b0.legalBlock('g', '7'));
    }

    @Test public void testUndo() {
        Board b0 = new Board();
        Board b1 = new Board(b0);
        makeMoves(b0, GAME1);
        Board b2 = new Board(b0);
        for (int i = 0; i < GAME1.length; i += 1) {
            b0.undo();
        }
        assertEquals("failed to return to start", b1, b0);
        makeMoves(b0, GAME1);
        assertEquals("second pass failed to reach same position", b2, b0);
    }

    @Test public void testAllVals() {
        Board b0 = new Board();
        makeMoves(b0, GAME4);
        assertEquals(3, b0.redPieces());
        assertEquals(5, b0.bluePieces());
        assertEquals(8, b0.numMoves());
        assertEquals(4, b0.numJumps());
    }
}
