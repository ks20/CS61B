package ataxx;

import static ataxx.PieceColor.*;

/** A Player that computes its own moves.
 *  @author Kushal
 *  */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;
    /** A position magnitude indicating a win (for red if positive, blue
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. */
    AI(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    @Override
    Move myMove() {
        if (!board().canMove(myColor())) {
            return Move.pass();
        }
        Move move = findMove();
        return move;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        maximizer = b.whoseMove();
        minimizer = b.whoseMove().opposite();
        findMove(b, MAX_DEPTH, -INFTY, INFTY);
        return _lastFoundMove;
    }

    /** Used to communicate best moves found by findMove, when asked for. */
    private Move _lastFoundMove;

    /** Updates value of _lasFoundMove.
     * Parameters: BOARD, DEPTH, ALPHA, BETA
     * RETURNS an int
     * */
    private int findMove(Board board, int depth, int alpha, int beta) {
        if (depth == 0) {
            return staticScore(board);
        }
        for (int c = 0; c <= 6; c++) {
            for (int r = 0; r <= 6; r++) {
                char t1 = (char) ('a' + c);
                char t2 = (char) ('1' + r);
                if (board.get(t1, t2).equals(myColor())) {
                    int index = board.index(t1, t2);
                    for (int dc = -2; dc <= 2; dc++) {
                        for (int dr = -2; dr <= 2; dr++) {
                            int n = Board.neighbor(index, dc, dr);
                            if (board.get(n).equals(EMPTY)) {
                                char t3 = (char) ('a' + c + dc);
                                char t4 = (char) ('1' + r + dr);
                                Move move = Move.move(t1, t2, t3, t4);
                                if (board.legalMove(move)) {
                                    board.makeMove(move);
                                } else {
                                    break;
                                }
                                _lastFoundMove = move;
                                return 1;
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }


    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        int temp = board.numPieces(maximizer) - board.numPieces(minimizer);
        return temp;
    }

    /** Variable that acts as the maximizer in the game tree. */
    private PieceColor maximizer;

    /** Variable that acts as the minimizer in the game tree. */
    private PieceColor minimizer;
}
