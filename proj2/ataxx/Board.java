package ataxx;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Formatter;
import java.util.Observable;

import static ataxx.PieceColor.*;
import static ataxx.GameException.error;

/** An Ataxx board.   The squares are labeled by column (a char value between
 *  'a' - 2 and 'g' + 2) and row (a char value between '1' - 2 and '7'
 *  + 2) or by linearized index, an integer described below.  Values of
 *  the column outside 'a' and 'g' and of the row outside '1' to '7' denote
 *  two layers of border squares, which are always blocked.
 *  This artificial border (which is never actually printed) is a common
 *  trick that allows one to avoid testing for edge conditions.
 *  For example, to look at all the possible moves from a square, sq,
 *  on the normal board (i.e., not in the border region), one can simply
 *  look at all squares within two rows and columns of sq without worrying
 *  about going off the board. Since squares in the border region are
 *  blocked, the normal logic that prevents moving to a blocked square
 *  will apply.
 *
 *  For some purposes, it is useful to refer to squares using a single
 *  integer, which we call its "linearized index".  This is simply the
 *  number of the square in row-major order (counting from 0).
 *
 *  Moves on this board are denoted by Moves.
 *  @author Kushal
 */
class Board extends Observable {

    /** Number of squares on a side of the board. */
    static final int SIDE = 7;
    /** Length of a side + an artificial 2-deep border region. */
    static final int EXTENDED_SIDE = SIDE + 4;
    /** Number of non-extending moves before game ends. */
    static final int JUMP_LIMIT = 25;
    /** Number indicating a full Board. */
    private final int fullBoard = 49;
    /** Index of last square on the board. */
    private final int lastBoardIndex = 120;

    /** A new, cleared board at the start of the game. */
    Board() {
        _board = new PieceColor[EXTENDED_SIDE * EXTENDED_SIDE];
        clear();
    }

    /** A copy of B. */
    Board(Board b) {
        _board = b._board.clone();
        _whoseMove = b.whoseMove();
        totalMoves = b.numMoves();
        jumpMoves = b.numJumps();
        consecutiveJumpMoves = b.getConsecutiveJumpMoves();
        redPieces = b.redPieces();
        bluePieces = b.bluePieces();
        blockPieces = b.blockPieces();
        moves = b.moves;
    }

    /** Return the linearized index of square COL ROW. */
    static int index(char col, char row) {
        return (row - '1' + 2) * EXTENDED_SIDE + (col - 'a' + 2);
    }

    /** Return the linearized index of the square that is DC columns and DR
     *  rows away from the square with index SQ. */
    static int neighbor(int sq, int dc, int dr) {
        return sq + dc + dr * EXTENDED_SIDE;
    }

    /** Clear me to my starting state, with pieces in their initial
     *  positions and no blocks. */
    void clear() {
        for (int i = 0; i < _board.length; i++) {
            boolean cOne = i < EXTENDED_SIDE * 2;
            boolean cTwo = i > lastBoardIndex - (EXTENDED_SIDE * 2);
            boolean cThree = i % 11 == 0;
            boolean cFour = i % 11 == 1;
            boolean cFive = i % 11 == 10;
            boolean cSix = i % 11 == 9;

            if (cOne || cTwo || cThree || cFour || cFive || cSix) {
                set(i, BLOCKED);
                blocksOnBorders++;
            } else {
                set(i, EMPTY);
                emptyPieces++;
            }
        }
        _whoseMove = RED;
        set('a', '7', RED);
        set('g', '1', RED);
        set('a', '1', BLUE);
        set('g', '7', BLUE);
        totalMoves = 0;
        jumpMoves = 0;
        consecutiveJumpMoves = 0;
        redPieces = 2;
        bluePieces = 2;
        moves = new Stack<>();

        setChanged();
        notifyObservers();
    }

    /** Return true iff the game is over: i.e., if neither side has
     *  any moves, if one side has no pieces, or if there have been
     *  MAX_JUMPS consecutive jumps without intervening extends. */
    boolean gameOver() {
        if (redPieces() + bluePieces() + blockPieces() == fullBoard) {
            return true;
        }
        if (redPieces() < 1 || bluePieces() < 1) {
            return true;
        }
        if (!canMove(_whoseMove) && !canMove(_whoseMove.opposite())) {
            return true;
        }
        if (getConsecutiveJumpMoves() >= JUMP_LIMIT) {
            return true;
        }
        return false;
    }

    /** Return number of red pieces on the board. */
    int redPieces() {
        return numPieces(RED);
    }

    /** Return number of blue pieces on the board. */
    int bluePieces() {
        return numPieces(BLUE);
    }

    /** Return number of blocks on the playing board. */
    int blockPieces() {
        return blockPieces;
    }

    /** Return number of blocks on the borders of the board. */
    int getBlocksOnBorders() {
        return blocksOnBorders;
    }

    /** Return number of COLOR pieces on the board. */
    int numPieces(PieceColor color) {
        if (color == RED) {
            return redPieces;
        } else if (color == BLUE) {
            return bluePieces;
        } else {
            return emptyPieces;
        }
    }

    /** Increment numPieces(COLOR) by K. */
    private void incrPieces(PieceColor color, int k) {
        if (color == RED) {
            redPieces += k;
        } else {
            bluePieces += k;
        }
    }

    /** Decrement numPieces(COLOR) by K. */
    private void decrPieces(PieceColor color, int k) {
        if (color == RED) {
            redPieces -= k;
        } else {
            bluePieces -= k;
        }
    }

    /** The current contents of square CR, where 'a'-2 <= C <= 'g'+2, and
     *  '1'-2 <= R <= '7'+2.  Squares outside the range a1-g7 are all
     *  BLOCKED.  Returns the same value as get(index(C, R)). */
    PieceColor get(char c, char r) {
        return _board[index(c, r)];
    }

    /** Return the current contents of square with linearized index SQ. */
    PieceColor get(int sq) {
        return _board[sq];
    }

    /** Set get(C, R) to V, where 'a' <= C <= 'g', and
     *  '1' <= R <= '7'. */
    private void set(char c, char r, PieceColor v) {
        set(index(c, r), v);
    }

    /** Set square with linearized index SQ to V.  This operation is
     *  undoable. */
    private void set(int sq, PieceColor v) {
        _board[sq] = v;
    }

    /** Set square at C R to V (not undoable). */
    private void unrecordedSet(char c, char r, PieceColor v) {
        _board[index(c, r)] = v;
    }

    /** Set square at linearized index SQ to V (not undoable). */
    private void unrecordedSet(int sq, PieceColor v) {
        _board[sq] = v;
    }

    /** Return true iff MOVE is legal on the current board. */
    boolean legalMove(Move move) {
        if (move.isPass()) {
            return true;
        }
        char destCol = move.col1();
        char destRow = move.row1();
        if ((get(destCol, destRow).equals(EMPTY))) {
            if (move.isJump() || move.isExtend()) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff player TURN can move, ignoring whether it is
     *  that player's move and whether the game is over. */
    boolean canMove(PieceColor turn) {
        for (int c = 0; c <= 6; c++) {
            for (int r = 0; r <= 6; r++) {
                char t1 = (char) ('a' + c);
                char t2 = (char) ('1' + r);
                int t3 = index(t1, t2);
                if (get(t3).equals(turn)) {
                    for (int dc = -2; dc <= 2; dc++) {
                        for (int dr = -2; dr <= 2; dr++) {
                            if (get(neighbor(t3, dc, dr)).equals(EMPTY)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /** Return the color of the player who has the next move.  The
     *  value is arbitrary if gameOver(). */
    PieceColor whoseMove() {
        return _whoseMove;
    }

    /** Return total number of moves and passes since the last
     *  clear or the creation of the board. */
    int numMoves() {
        return totalMoves;
    }

    /** Return number of non-pass moves made in the current game since the
     *  last extend move added a piece to the board (or since the
     *  start of the game). Used to detect end-of-game. */
    int numJumps() {
        return jumpMoves;
    }

    /** Return number of non-pass moves made in the current game since the
     *  last extend move added a piece to the board (or since the
     *  start of the game). Used to detect end-of-game. */
    int getConsecutiveJumpMoves() {
        return consecutiveJumpMoves;
    }

    /** Perform the move C0R0-C1R1, or pass if C0 is '-'.  For moves
     *  other than pass, assumes that legalMove(C0, R0, C1, R1). */
    void makeMove(char c0, char r0, char c1, char r1) {
        assertConditions(c0, r0, c1, r1);
        if (c0 == '-') {
            makeMove(Move.pass());
        } else {
            makeMove(Move.move(c0, r0, c1, r1));
        }
    }

    /** Throws errors if illegal moves.
     * C0: inital column
     * R0: initial row
     * C1: destination column
     * R1: destination row. */
    void assertConditions(char c0, char r0, char c1, char r1) {
        if (!get(c0, r0).equals(whoseMove())) {
            throw error("Illegal. Try again.");
        } else if ((c1 - c0) > 2 || (r1 - r0) > 2) {
            throw error("Illegal. Try again.");
        } else if ((c0 - c1) > 2 || (r0 - r1) > 2) {
            throw error("Illegal. Try again.");
        } else if ((c1 - c0) == 0 && (r1 - r0) == 0) {
            throw error("Illegal. Try again.");
        } else if (get(c1, r1) != EMPTY) {
            throw error("Illegal. Try again.");
        } else if (c0 < 'a') {
            throw error("Illegal. Try again.");
        }
    }

    /** Make the MOVE on this Board, assuming it is legal. */
    void makeMove(Move move) {
        assert legalMove(move);
        moves.push(move);
        char initCol = move.col0();
        char initRow = move.row0();
        char destCol = move.col1();
        char destRow = move.row1();
        totalMoves++;
        if (move.isPass()) {
            pass();
            return;
        } else if (move.isJump()) {
            set(destCol, destRow, _whoseMove);
            set(initCol, initRow, EMPTY);
            checkAdjacentSquares(destCol, destRow);
            jumpMoves++;
            consecutiveJumpMoves++;
        } else if (move.isExtend()) {
            set(destCol, destRow, _whoseMove);
            incrPieces(_whoseMove, 1);
            checkAdjacentSquares(destCol, destRow);
            consecutiveJumpMoves = 0;
        }
        _whoseMove = _whoseMove.opposite();
        setChanged();
        notifyObservers();
    }

    /** Checks adjacent squares after a move is made and converts
     *  them to WHOSEMOVE if square is WHOSEMOVE.OPPOSITE().
     *  Checks squares adjacent to column COL and row ROW.*/
    private void checkAdjacentSquares(char col, char row) {
        char rightCol = (char) (col + 1);
        char copyRow = (char) (row + 1);
        if (rightCol <= 'g' || copyRow <= '7') {
            for (int i = 0; i < 3; i++) {
                if (copyRow >= '1') {
                    if (get(rightCol, copyRow).equals(_whoseMove.opposite())) {
                        int linearIndex = index(rightCol, copyRow);
                        colorChange.add(linearIndex);
                        set(rightCol, copyRow, _whoseMove);
                        incrPieces(_whoseMove, 1);
                        decrPieces(_whoseMove.opposite(), 1);
                    }
                    copyRow = (char) (copyRow - 1);
                }
            }
        }

        copyRow = (char) (row + 1);
        char leftCol = (char) (col - 1);
        if (leftCol >= 'a' || copyRow <= '7') {
            for (int i = 0; i < 3; i++) {
                if (copyRow >= '1') {
                    if (get(leftCol, copyRow).equals(_whoseMove.opposite())) {
                        int linearIndex = index(leftCol, copyRow);
                        colorChange.add(linearIndex);
                        set(leftCol, copyRow, _whoseMove);
                        incrPieces(_whoseMove, 1);
                        decrPieces(_whoseMove.opposite(), 1);
                    }
                    copyRow = (char) (copyRow - 1);
                }
            }
        }

        copyRow = (char) (row + 1);
        char currCol = col;
        if (copyRow <= '7') {
            for (int i = 0; i < 3; i++) {
                if (copyRow >= '1') {
                    if (get(currCol, copyRow).equals(_whoseMove.opposite())) {
                        int linearIndex = index(currCol, copyRow);
                        colorChange.add(linearIndex);
                        set(currCol, copyRow, _whoseMove);
                        incrPieces(_whoseMove, 1);
                        decrPieces(_whoseMove.opposite(), 1);
                    }
                    copyRow = (char) (copyRow - 1);
                }
            }
        }
    }

    /** Checks adjacent squares after an undo move and reverts them back
     *  to the original. RIGHTCOL is used to denote column to the right
     *  of COL. COPYROW is used to denote rows adjacent to ROW.*/
    private void undoSquares(char col, char row) {
        char rightCol = (char) (col + 1);
        char copyRow = (char) (row + 1);
        if (rightCol <= 'g' || copyRow <= '7') {
            for (int i = 0; i < 3; i++) {
                if (copyRow >= '1') {
                    int tempIndex = index(rightCol, copyRow);
                    boolean condTwo = colorChange.contains(tempIndex);
                    if (get(rightCol, copyRow).equals(_whoseMove) && condTwo) {
                        set(rightCol, copyRow, _whoseMove.opposite());
                        incrPieces(_whoseMove.opposite(), 1);
                        decrPieces(_whoseMove, 1);
                    }
                    copyRow = (char) (copyRow - 1);
                }
            }
        }

        copyRow = (char) (row + 1);
        char leftCol = (char) (col - 1);
        if (leftCol >= 'a' || copyRow <= '7') {
            for (int i = 0; i < 3; i++) {
                if (copyRow >= '1') {
                    int tempIndex = index(leftCol, copyRow);
                    boolean condTwo = colorChange.contains(tempIndex);
                    if (get(leftCol, copyRow).equals(_whoseMove) && condTwo) {
                        set(leftCol, copyRow, _whoseMove.opposite());
                        incrPieces(_whoseMove.opposite(), 1);
                        decrPieces(_whoseMove, 1);
                    }
                    copyRow = (char) (copyRow - 1);
                }
            }
        }

        copyRow = (char) (row + 1);
        char currCol = col;
        if (copyRow <= '7') {
            for (int i = 0; i < 3; i++) {
                if (copyRow >= '1') {
                    int tempIndex = index(currCol, copyRow);
                    boolean condTwo = colorChange.contains(tempIndex);
                    if (get(currCol, copyRow).equals(_whoseMove) && condTwo) {
                        set(currCol, copyRow, _whoseMove.opposite());
                        incrPieces(_whoseMove.opposite(), 1);
                        decrPieces(_whoseMove, 1);
                    }
                    copyRow = (char) (copyRow - 1);
                }
            }
        }
    }


    /** Update to indicate that the current player passes, assuming it
     *  is legal to do so.  The only effect is to change whoseMove(). */
    void pass() {
        if (canMove(_whoseMove)) {
            throw error("Illegal. Try again.");
        }
        _whoseMove = _whoseMove.opposite();
        setChanged();
        notifyObservers();
    }

    /** Undo the last move. */
    void undo() {
        _whoseMove = _whoseMove.opposite();
        totalMoves--;
        Move temp = moves.pop();
        char toCol = temp.col1();
        char toRow = temp.row1();
        if (temp.isJump()) {
            set(temp.col0(), temp.row0(), _whoseMove);
            set(toCol, toRow, EMPTY);
        } else {
            set(toCol, toRow, EMPTY);
            decrPieces(_whoseMove, 1);
        }
        undoSquares(toCol, toRow);

        setChanged();
        notifyObservers();
    }

    /** Indicate beginning of a move in the undo stack. */
    private void startUndo() { }

    /** Return true iff it is legal to place a block at C R. */
    boolean legalBlock(char c, char r) {
        if ((c == 'a' || c == 'g') && (r == '1' || r == '7')) {
            return false;
        } else if (get(c, r) == RED || get(c, r) == BLUE) {
            return false;
        }
        return true;
    }

    /** Return true iff it is legal to place a block at CR. */
    boolean legalBlock(String cr) {
        return legalBlock(cr.charAt(0), cr.charAt(1));
    }

    /** Set a block on the square C R and its reflections across the middle
     *  row and/or column, if that square is unoccupied and not
     *  in one of the corners. Has no effect if any of the squares is
     *  already occupied by a block.  It is an error to place a block on a
     *  piece. */
    void setBlock(char c, char r) {
        if (!legalBlock(c, r)) {
            throw error("Illegal block position.");
        }
        char midSquareCol = (char) ('a' + SIDE / 2);
        char midSquareRow = (char) ('1' + SIDE / 2);
        int finCol1 = midSquareCol - c;
        int finRow1 = midSquareRow - r;
        char col2 = (char) (midSquareCol + finCol1);
        char row2 = (char) (midSquareRow + finRow1);

        if (get(c, r).equals(EMPTY)) {
            set(c, r, BLOCKED);
            set(col2, r, BLOCKED);
            set(c, row2, BLOCKED);
            set(col2, row2, BLOCKED);
            blockPieces += 4;
        }
        setChanged();
        notifyObservers();
    }

    /** Place a block at CR. */
    void setBlock(String cr) {
        setBlock(cr.charAt(0), cr.charAt(1));
    }

    /** Returns the board list. */
    PieceColor[] board() {
        return _board;
    }

    /** Return a list of all moves made since the last clear (or start of
     *  game). */
    Stack<Move> allMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /* .equals used only for testing purposes. */
    @Override
    public boolean equals(Object obj) {
        Board other = (Board) obj;
        if (redPieces - other.redPieces != 0) {
            return false;
        }
        if (bluePieces - other.bluePieces != 0) {
            return false;
        }
        if (numJumps() - other.numJumps() != 0) {
            return false;
        }
        if (numMoves() - other.numMoves() != 0) {
            return false;
        }

        return Arrays.equals(_board, other._board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(_board);
    }

    /** Return a text depiction of the board (not a dump).  If LEGEND,
     *  supply row and column numbers around the edges. */
    String toString(boolean legend) {
        Formatter out = new Formatter();
        return out.toString();
    }

    /** For reasons of efficiency in copying the board,
     *  we use a 1D array to represent it, using the usual access
     *  algorithm: row r, column c => index(r, c).
     *
     *  Next, instead of using a 7x7 board, we use an 11x11 board in
     *  which the outer two rows and columns are blocks, and
     *  row 2, column 2 actually represents row 0, column 0
     *  of the real board.  As a result of this trick, there is no
     *  need to special-case being near the edge: we don't move
     *  off the edge because it looks blocked.
     *
     *  Using characters as indices, it follows that if 'a' <= c <= 'g'
     *  and '1' <= r <= '7', then row c, column r of the board corresponds
     *  to board[(c -'a' + 2) + 11 (r - '1' + 2) ], or by a little
     *  re-grouping of terms, board[c + 11 * r + SQUARE_CORRECTION]. */
    private PieceColor[] _board;

    /** ArrayList that keeps track of square indices that change color. */
    private ArrayList<Integer> colorChange = new ArrayList<>();

    /** Stack of moves. */
    private Stack<Move> moves;

    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** Total number of red pieces. */
    private int redPieces;

    /** Total number of blue pieces. */
    private int bluePieces;

    /** Total number of moves. */
    private int totalMoves;

    /** Number of jump moves. */
    private int jumpMoves;

    /** Number of consecutive jump moves. */
    private int consecutiveJumpMoves;

    /** Number of blocks on board (excluding # of blocks on borders). */
    private int blockPieces;

    /** Number of empty pieces on board. */
    private int emptyPieces;

    /** Number of blocks on borders. */
    private int blocksOnBorders;
}
