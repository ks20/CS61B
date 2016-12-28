package ataxx;

/* Author: P. N. Hilfinger */

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;

import static ataxx.PieceColor.*;
import static ataxx.Game.State.*;
import static ataxx.Command.Type.*;
import static ataxx.GameException.error;

/** Controls the play of the game.
 *  @author Kushal
 */
class Game {

    /** States of play. */
    static enum State {
        SETUP, PLAYING, FINISHED;
    }

    /** A new Game, using BOARD to play on, reading initially from
     *  BASESOURCE and using REPORTER for error and informational messages. */
    Game(Board board, CommandSource baseSource, Reporter reporter) {
        _inputs.addSource(baseSource);
        _board = board;
        _reporter = reporter;
    }

    /** Run a session of Ataxx gaming.  Use an AtaxxGUI iff USEGUI. */
    void process(boolean useGUI) {
        Player red, blue;
        redAutoFlag = false; redManFlag = false;
        blueAutoFlag = false; blueManFlag = false;
        GameLoop:
        while (true) {
            doClear(null);
            red = new Manual(this, RED);
            blue = new AI(this, BLUE);
            _state = SETUP;
            SetupLoop:
            while (_state == SETUP) {
                doCommand();
            }
            _state = PLAYING;
            if (redAutoFlag) {
                red = new AI(this, RED);
            }
            if (redManFlag) {
                red = new Manual(this, RED);
            }
            if (blueAutoFlag) {
                blue = new AI(this, BLUE);
            }
            if (blueManFlag) {
                blue = new Manual(this, BLUE);
            }
            while (_state != SETUP && !board().gameOver()) {
                Move move;
                if (board().whoseMove().equals(RED)) {
                    player = red;
                } else {
                    player = blue;
                }
                move = player.myMove();
                if (board().gameOver()) {
                    _state = FINISHED;
                }
                if (_state == PLAYING) {
                    board().makeMove(move);
                }
                if (player instanceof AI) {
                    helper(move);
                }
            }
            if (_state != SETUP) {
                reportWinner();
            }
            if (_state == PLAYING) {
                _state = FINISHED;
            }
            while (_state == FINISHED) {
                doCommand();
            }
        }
    }

    /** Helper method to output messages using MOVE. */
    private void helper(Move move) {
        String msg;
        char c0 = move.col0();
        char r0 = move.row0();
        char c1 = move.col1();
        char r1 = move.row1();
        if (player.myColor().equals(RED)) {
            if (move.isPass()) {
                msg = "Red passes.";
                _reporter.outcomeMsg(msg);
            } else {
                move.toString();
                msg = "Red moves " + c0 + r0 + "-" + c1 + r1 + ".";
                _reporter.outcomeMsg(msg);
            }
        } else if (player.myColor().equals(BLUE)) {
            if (move.isPass()) {
                msg = "Blue passes.";
                _reporter.outcomeMsg(msg);
            } else {
                msg = "Blue moves " + c0 + r0 + "-" + c1 + r1 + ".";
                _reporter.outcomeMsg(msg);
            }
        }

    }

    /** Return a view of my game board that should not be modified by
     *  the caller. */
    Board board() {
        return _board;
    }

    /** Perform the next command from our input source. */
    void doCommand() {
        try {
            Command cmnd =
                    Command.parseCommand(_inputs.getLine("ataxx: "));
            _commands.get(cmnd.commandType()).accept(cmnd.operands());
        } catch (GameException excp) {
            _reporter.errMsg(excp.getMessage());
        }
    }

    /** Read and execute commands until encountering a move or until
     *  the game leaves playing state due to one of the commands. Return
     *  the terminating move command, or null if the game first drops out
     *  of playing mode. If appropriate to the current input source, use
     *  PROMPT to prompt for input. */
    Command getMoveCmnd(String prompt) {
        while (_state == PLAYING) {
            try {
                Command cmnd = Command.parseCommand(_inputs.getLine(prompt));
                if (cmnd.commandType().equals(PASS)) {
                    return cmnd;
                } else if (cmnd.commandType().equals(PIECEMOVE)) {
                    return cmnd;
                }
                _commands.get(cmnd.commandType()).accept(cmnd.operands());

            } catch (GameException excp) {
                _reporter.errMsg(excp.getMessage());
            }
        }
        return null;
    }

    /** Return random integer between 0 (inclusive) and MAX>0 (exclusive). */
    int nextRandom(int max) {
        return _randoms.nextInt(max);
    }

    /** Report a move, using a message formed from FORMAT and ARGS as
     *  for String.format. */
    void reportMove(String format, Object... args) {
        _reporter.moveMsg(format, args);
    }

    /** Report an error, using a message formed from FORMAT and ARGS as
     *  for String.format. */
    void reportError(String format, Object... args) {
        _reporter.errMsg(format, args);
    }

    /* Command Processors */

    /** Perform the command 'auto OPERANDS[0]'. */
    void doAuto(String[] operands) {
        String whoseColor = operands[0];
        if (whoseColor.equals("red")) {
            redAutoFlag = true;
        } else if (whoseColor.equals("blue")) {
            blueAutoFlag = true;
        }
    }

    /** Perform a 'help' command. */
    void doHelp(String[] unused) {
        String getter = "ataxx/help.txt";
        InputStream helpIn =
                Game.class.getClassLoader().getResourceAsStream(getter);
        if (helpIn == null) {
            System.err.println("No help available.");
        } else {
            try {
                BufferedReader r
                        = new BufferedReader(new InputStreamReader(helpIn));
                while (true) {
                    String line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
                r.close();
            } catch (IOException e) {
                /* Ignore IOException */
            }
        }
    }

    /** Perform the command 'load OPERANDS[0]'. */
    void doLoad(String[] operands) {
        try {
            FileReader reader = new FileReader(operands[0]);
            ReaderSource src = new ReaderSource(reader, true);
            _inputs.addSource(src);
        } catch (IOException e) {
            throw error("Cannot open file %s", operands[0]);
        }
    }

    /** Perform the command 'manual OPERANDS[0]'. */
    void doManual(String[] operands) {
        String whoseColor = operands[0];
        if (whoseColor.equals("red")) {
            redManFlag = true;
        } else if (whoseColor.equals("blue")) {
            blueManFlag = true;
        }
    }

    /** Exit the program. */
    void doQuit(String[] unused) {
        System.exit(0);
    }

    /** Perform the command 'start'. */
    void doStart(String[] unused) {
        checkState("start", SETUP);
        _state = PLAYING;
    }

    /** Perform the move OPERANDS[0]. */
    void doMove(String[] operands) {
        operands[0].toUpperCase();
        char initialCol = operands[0].charAt(0);
        char initialRow = operands[1].charAt(0);
        char destCol = operands[2].charAt(0);
        char destRow = operands[3].charAt(0);
        board().makeMove(initialCol, initialRow, destCol, destRow);
    }

    /** Cause current player to pass. */
    void doPass(String[] unused) {
        board().pass();
    }

    /** Perform the command 'clear'. */
    void doClear(String[] unused) {
        board().clear();
    }

    /** Perform the command 'dump'. */
    void doDump(String[] unused) {
        System.out.println("===");
        System.out.print("  ");
        boolean flag = false;
        for (int row = 6; row >= 0; row--) {
            for (int col = 0; col < board().SIDE; col++) {
                if (col == 6 && row != 0) {
                    flag = true;
                }
                char c = (char) ('a' + col);
                char r = (char) ('1' + row);
                PieceColor type = board().get(c, r);
                if (type.equals(BLOCKED)) {
                    System.out.print("X ");
                } else if (type.equals(EMPTY)) {
                    System.out.print("- ");
                } else if (type.equals(RED)) {
                    System.out.print("r ");
                } else if (type.equals(BLUE)) {
                    System.out.print("b ");
                }

                if (flag) {
                    System.out.println();
                    System.out.print("  ");
                    flag = false;
                }
            }
        }
        System.out.println();
        System.out.println("===");

    }

    /** Execute 'seed OPERANDS[0]' command, where the operand is a string
     *  of decimal digits. Silently substitutes another value if
     *  too large. */
    void doSeed(String[] operands) {
        _randoms.setSeed(Long.parseLong(operands[0]));
    }

    /** Execute the command 'block OPERANDS[0]'. */
    void doBlock(String[] operands) {
        board().setBlock(operands[0]);
    }

    /** Execute the artificial 'error' command. */
    void doError(String[] unused) {
        throw error("Command not understood");
    }


    /** Report the outcome of the current game. */
    void reportWinner() {
        String msg;
        if (board().redPieces() > board().bluePieces()) {
            msg = "Red wins.";
        } else if (board().redPieces() < board().bluePieces()) {
            msg = "Blue wins.";
        } else {
            msg = "Draw.";
        }
        _reporter.outcomeMsg(msg);
    }

    /** Check that game is currently in one of the states STATES, assuming
     *  CMND is the command to be executed. */
    private void checkState(Command cmnd, State... states) {
        for (State s : states) {
            if (s == _state) {
                return;
            }
        }
        throw error("'%s' command is not allowed now.", cmnd.commandType());
    }

    /** Check that game is currently in one of the states STATES, using
     *  CMND in error messages as the name of the command to be executed. */
    private void checkState(String cmnd, State... states) {
        for (State s : states) {
            if (s == _state) {
                return;
            }
        }
        throw error("'%s' command is not allowed now.", cmnd);
    }

    /** Mapping of command types to methods that process them. */
    private final HashMap<Command.Type, Consumer<String[]>> _commands =
            new HashMap<>();

    {
        _commands.put(AUTO, this::doAuto);
        _commands.put(BLOCK, this::doBlock);
        _commands.put(CLEAR, this::doClear);
        _commands.put(DUMP, this::doDump);
        _commands.put(HELP, this::doHelp);
        _commands.put(MANUAL, this::doManual);
        _commands.put(PASS, this::doPass);
        _commands.put(PIECEMOVE, this::doMove);
        _commands.put(SEED, this::doSeed);
        _commands.put(START, this::doStart);
        _commands.put(LOAD, this::doLoad);
        _commands.put(QUIT, this::doQuit);
        _commands.put(ERROR, this::doError);
        _commands.put(EOF, this::doQuit);
    }

    /** Input source. */
    private final CommandSources _inputs = new CommandSources();

    /** My board. */
    private Board _board;
    /** Current game state. */
    private State _state;
    /** Used to send messages to the user. */
    private Reporter _reporter;
    /** Source of pseudo-random numbers (used by AIs). */
    private Random _randoms = new Random();

    /** Flag to determine whether red player is a Manual Player. */
    private boolean redManFlag;
    /** Flag to determine whether blue player is a Manual Player. */
    private boolean blueManFlag;
    /** Flag to determine whether red player is an AI. */
    private boolean redAutoFlag;
    /** Flag to determine whether blue player is an AI. */
    private boolean blueAutoFlag;
    /** Determines which player is playing. */
    private Player player;
}
