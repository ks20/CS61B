package ataxx;

import static ataxx.PieceColor.*;

/** A Player that receives its moves from its Game's getMoveCmnd method.
 *  @author Kushal
 */
class Manual extends Player {

    /** A Player that will play MYCOLOR on GAME, taking its moves from
     *  GAME. */
    Manual(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    @Override
    Move myMove() {
        Game thisGame = game();
        Command cmnd = null;
        if (myColor().equals(RED)) {
            cmnd = thisGame.getMoveCmnd("Red : ");
        } else {
            cmnd = thisGame.getMoveCmnd("Blue : ");
        }

        if (cmnd.commandType().equals(Command.Type.PASS)) {
            return Move.pass();
        } else {
            char col0 = cmnd.operands()[0].charAt(0);
            char row0 = cmnd.operands()[1].charAt(0);
            char col1 = cmnd.operands()[2].charAt(0);
            char row1 = cmnd.operands()[3].charAt(0);
            Move mov = Move.move(col0, row0, col1, row1);
            return mov;
        }
    }
}
