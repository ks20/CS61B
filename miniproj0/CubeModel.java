package cube;

import java.util.Observable;

import static java.lang.System.arraycopy;

/** Models an instance of the Cube puzzle: a cube with color on some sides
 *  sitting on a cell of a square grid, some of whose cells are colored.
 *  Any object may register to observe this model, using the (inherited)
 *  addObserver method.  The model notifies observers whenever it is modified.
 *  @author P. N. Hilfinger
 */
class CubeModel extends Observable {

    /** A blank cube puzzle of size 4. */
    CubeModel() {
        this.side = 4;
        this.painted = new boolean[4][4];
        this.facePainted = new boolean[6];
        this.numMoves = 0;

    }

    /** A copy of CUBE. */
    CubeModel(CubeModel cube) {
        initialize(cube);
    }

    /** Initialize puzzle of size THISSIDExTHISSIDE with the cube initially at
     *  ROW0 and COL0, with square r, c painted iff PAINTED[r][c], and
     *  with face k painted iff THISFACEPAINTED[k] (see isPaintedFace).
     *  Assumes that
     *    * THISSIDE > 2.
     *    * THISPAINTED is THISSIDExTHISSIDE.
     *    * 0 <= ROW0, COL0 < THISSIDE.
     *    * THISFACEPAINTED has length 6.
     */
    void initialize(int thisSide, int row0, int col0, boolean[][] thisPainted,
                    boolean[] thisFacePainted) {
        this.side = thisSide;
        this.size = thisSide * thisSide;
        this.currRow = row0;
        this.currCol = col0;
        this.painted = thisPainted;
        this.facePainted = thisFacePainted;

        setChanged();
        notifyObservers();
    }

    /** Initialize puzzle of size THISSIDExTHISSIDE with the cube initially at
     *  ROW0 and COL0, with square r, c painted iff THISPAINTED[r][c].
     *  The cube is initially blank.
     *  Assumes that
     *    * THISSIDE > 2.
     *    * THISPAINTED is THISSIDExTHISSIDE.
     *    * 0 <= ROW0, COL0 < THISSIDE.
     */
    void initialize(int thisSide, int row0, int col0, boolean[][] thisPainted) {
        initialize(thisSide, row0, col0, thisPainted, new boolean[6]);
    }

    /** Initialize puzzle to be a copy of CUBE. */
    void initialize(CubeModel cube) {
        int newCurrRow = cube.currRow;
        int newCurrCol = cube.currCol;
        int newSide = cube.side;
        boolean [][] newPainted = new boolean [newSide][newSide];
        arraycopy(cube.painted, 0, newPainted, 0, newPainted.length);

        boolean [] nFacePainted = new boolean [cube.facePainted.length];
        arraycopy(cube.facePainted, 0, nFacePainted, 0, nFacePainted.length);

        this.numMoves = cube.numMoves;

        initialize(newSide, newCurrRow, newCurrCol, newPainted, nFacePainted);

        setChanged();
        notifyObservers();
    }

    /** Move the cube to (ROW, COL), if that position is on the board and
     *  vertically or horizontally adjacent to the current cube position.
     *  Transfers colors as specified by the rules.
     *  Throws IllegalArgumentException if preconditions are not met.
     */
    void move(int row, int col) {
        if ((row > (this.side - 1)) || (row < 0)) {
            throw new IllegalArgumentException("row number is invalid");
        }
        if ((col < 0) || (col > (this.side - 1))) {
            throw new IllegalArgumentException("col number is invalid");
        } else if ((row == this.currRow + 1) && (col == this.currCol)) {
            boolean [] copied = new boolean[6];
            arraycopy(this.facePainted, 0, copied, 0, 6);
            this.facePainted[0] = copied[4];
            this.facePainted[4] = copied[1];
            this.facePainted[1] = copied[5];
            this.facePainted[5] = copied[0];
            boolean tempTruth = this.facePainted[4];
            this.facePainted[4] = this.painted[row][col];
            this.painted[row][col] = tempTruth;
            this.currRow = row;
            this.numMoves += 1;
        } else if ((row == this.currRow - 1) && (col == this.currCol)) {
            boolean [] copied = new boolean[6];
            arraycopy(this.facePainted, 0, copied, 0, 6);
            this.facePainted[0] = copied[5];
            this.facePainted[5] = copied[1];
            this.facePainted[1] = copied[4];
            this.facePainted[4] = copied[0];
            boolean tempTruth = this.facePainted[4];
            this.facePainted[4] = this.painted[row][col];
            this.painted[row][col] = tempTruth;
            this.currRow = row;
            this.numMoves += 1;
        } else if ((col == this.currCol - 1) && (row == this.currRow))  {
            boolean [] copied = new boolean[6];
            arraycopy(this.facePainted, 0, copied, 0, 6);
            this.facePainted[4] = copied[2];
            this.facePainted[5] = copied[3];
            this.facePainted[3] = copied[4];
            this.facePainted[2] = copied[5];
            boolean tempTruth = this.facePainted[4];
            this.facePainted[4] = this.painted[row][col];
            this.painted[row][col] = tempTruth;
            this.currCol = col;
            this.numMoves += 1;
        } else if ((col == this.currCol + 1) && (row == this.currRow)) {
            boolean [] copied = new boolean[6];
            arraycopy(this.facePainted, 0, copied, 0, 6);
            this.facePainted[4] = copied[3];
            this.facePainted[5] = copied[2];
            this.facePainted[3] = copied[5];
            this.facePainted[2] = copied[4];
            boolean tempTruth = this.facePainted[4];
            this.facePainted[4] = this.painted[row][col];
            this.painted[row][col] = tempTruth;
            this.currCol = col;
            this.numMoves += 1;
        } else {
            throw new IllegalArgumentException("illegal move");
        }
        setChanged();
        notifyObservers();
    }

    /** Return the number of squares on a side. */
    int side() {
        return this.side;
    }

    /** Return true iff square ROW, COL is painted.
     *  Requires 0 <= ROW, COL < board size. */
    boolean isPaintedSquare(int row, int col) {
        if (this.painted[row][col]) {
            return true;
        }
        return false;
    }

    /** Return current row of cube. */
    int cubeRow() {
        return this.currRow;
    }

    /** Return current column of cube. */
    int cubeCol() {
        return this.currCol;
    }

    /** Return the number of moves made on current puzzle. */
    int moves() {
        return this.numMoves;
    }

    /** Return true iff face #FACE, 0 <= FACE < 6, of the cube is painted.
     *  Faces are numbered as follows:
     *    0: Vertical in the direction of row 0 (nearest row to player).
     *    1: Vertical in the direction of last row.
     *    2: Vertical in the direction of column 0 (left column).
     *    3: Vertical in the direction of last column.
     *    4: Bottom face.
     *    5: Top face.
     */
    boolean isPaintedFace(int face) {
        if (this.facePainted[face]) {
            return true;
        }
        return false;
    }

    /** Return true iff all faces are painted. */
    boolean allFacesPainted() {
        boolean test = true;
        for (int i = 0; i <= 5; i++) {
            if (!this.facePainted[i]) {
                test = false;
            }
        }
        return test;
    }

    /** Variable for the current row. */
    private int currRow;
    /** Variable for the current column. */
    private int currCol;
    /** Variable for the current size. */
    private int size;
    /** Variable for the puzzle two-dimensional array. */
    private boolean [][] painted;
    /** Variable for the cube array. */
    private boolean [] facePainted;
    /** Variable for the number of moves. */
    private int numMoves;
    /** Variable for the side of the puzzle. */
    private int side;

}
