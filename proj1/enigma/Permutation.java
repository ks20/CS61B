package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Kushal Singh
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters not
     *  included in any cycle map to themselves. Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        int countOpen = 0;
        int countClosed = 0;
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i) == '(') {
                countOpen++;
            }
            if (_cycles.charAt(i) == ')') {
                countClosed++;
            }
        }
        if (countOpen != countClosed) {
            throw new EnigmaException("Wrong # setting arguments.");
        }

        permutations = new int[size()];
        int arrayTraverse = 0;
        for (int i = 0; i < _cycles.length(); i++) {
            if ((int) _cycles.charAt(i) >= 'A') {
                permutations[arrayTraverse] = (int) _cycles.charAt(i);
                arrayTraverse++;
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        if (_cycles.length() == 0) {
            return p;
        }

        int val = wrap(p) + 'A';
        char temp = (char) val;
        char temp2 = permute(temp);
        return (int) temp2 - 'A';

    }

    /** Return the result of applying the inverse of this permutation
     *  to C modulo the alphabet size. */
    int invert(int c) {
        if (_cycles.length() == 0) {
            return c;
        }

        int val = wrap(c) + 'A';
        char temp = (char) val;
        int temp2 = invert(temp);
        return temp2 - 'A';

    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (_cycles.length() == 0) {
            return p;
        }

        for (int aIter = 0; aIter < permutations.length; aIter++) {
            if ((char) permutations[aIter] == p) {
                int stringIndex = _cycles.indexOf(p);
                if (_cycles.charAt(stringIndex + 1) == ')') {
                    int start = stringIndex;
                    char startChar = _cycles.charAt(stringIndex);
                    while (startChar != '(') {
                        start = start - 1;
                        startChar = _cycles.charAt(start);
                    }
                    return _cycles.charAt(start + 1);
                }
                return (char) permutations[aIter + 1];
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        if (_cycles.length() == 0) {
            return (int) c;
        }

        for (int aIter = 0; aIter < permutations.length; aIter++) {
            if ((char) permutations[aIter] == c) {
                int stringIndex = _cycles.indexOf(c);
                if (_cycles.charAt(stringIndex - 1) == '(') {
                    int start = stringIndex;
                    char startChar = _cycles.charAt(start);
                    while (startChar != ')') {
                        start = start + 1;
                        startChar = _cycles.charAt(start);
                    }
                    return (int) _cycles.charAt(start - 1);
                }
                return permutations[aIter - 1];
            }
        }
        return (int) c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** String of cycles as stated in config file. */
    private String _cycles;

    /** Return the permutations array used to initialize this Permutation. */
    private int[] permutations;
}

