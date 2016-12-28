package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /**
     * Check that perm has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */
    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkIdTransform1() {
        String cycles1 = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        perm = new Permutation(cycles1, UPPER);
        checkPerm("identity", UPPER_STRING, "EKMFLGDQVZNTOWYHXUSPAIBRCJ");
    }

    @Test
    public void checkIdTransform2() {
        String cycles2 = "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)";
        perm = new Permutation(cycles2, UPPER);
        checkPerm("identity", UPPER_STRING, "AJDKSIRUXBLHWTMCQGZNPYFVOE");
    }

    @Test
    public void checkIdTransform3() {
        perm = new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", UPPER);
        checkPerm("identity", UPPER_STRING, "BDFHJLCPRTXVZNYEIWGAKMUSQO");
    }

    @Test
    public void checkIdTransform4() {
        String cycles3First = "(AE) (BN) (CK) (DQ) (FU) (GY)";
        String cycles3Second = "(HW) (IJ) (LO) (MP) (RX) (SZ) (TV)";
        String cycles3 = cycles3First + cycles3Second;
        perm = new Permutation(cycles3, UPPER);
        checkPerm("identity", UPPER_STRING, "ENKQAUYWJICOPBLMDXZVFTHRGS");
    }
}

