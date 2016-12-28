package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Kushal Singh
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new ArrayList<Rotor>(allRotors);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int count = 0;
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor checkName : _allRotors) {
                String rotorName = rotors[i].toUpperCase();
                String check = checkName.name().toUpperCase();
                if (rotorName.equals(check)) {
                    count++;
                }
            }
        }
        if (count != rotors.length) {
            throw new EnigmaException("Rotors named incorrectly.");
        }

        String reflectorName = rotors[0];
        Rotor x = null;
        for (Rotor rotor : _allRotors) {
            if (rotor.name().equals(reflectorName)) {
                x = rotor;
            }
        }
        if (!x.reflecting()) {
            throw new EnigmaException("The first rotor must be a reflector.");
        }
        _usingRotors = new Rotor[rotors.length];
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor r : _allRotors) {
                if (rotors[i].toUpperCase().equals(r.name().toUpperCase())) {
                    _usingRotors[i] = r;
                }
            }
        }

        int counter = 0;
        for (int i = 0; i < _usingRotors.length; i++) {
            if (_usingRotors[i].rotates()) {
                counter += 1;
            }
        }
        if (counter != numPawls()) {
            throw new EnigmaException("Wrong number of arguments");
        }

    }

    /** Set my rotors according to SETTING, which must be a string of four
     *  upper-case letters. The first letter refers to the leftmost
     *  rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        Rotor r;
        int len = setting.length();
        for (int i = numRotors() - 1; i >= 1; i--) {
            r = _usingRotors[i];
            r.set((setting.charAt(len - 1)));
            len--;
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugBoard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        _usingRotors[_usingRotors.length - 1].advance();
        int index = _plugBoard.permute(c);
        index = _usingRotors[_usingRotors.length - 1].convertForward(index);
        boolean maybe = false;

        for (int i = _usingRotors.length - 2; i >= 0; i--) {
            Rotor r = _usingRotors[i];
            int count = 0;
            if (_usingRotors[i + 1].atNotch() && i + 1 == numRotors() - 1) {
                r.advance();
                if (_usingRotors[i].atNotch()) {
                    count++;
                    _usingRotors[i - 1].advance();
                }
                count++;
            }
            if (((i) != (numRotors() - 1)) && r.rotates()) {
                int[] arr = r.getNotchesArr();
                for (int k = 0; k < arr.length; k++) {
                    int cur = arr[k];
                    if (('A' + r.permutation().wrap((r.setting()))) == cur) {
                        maybe = true;
                    }
                }
                if (maybe && count == 0 && _usingRotors[i - 1].rotates()) {
                    r.advance();
                    _usingRotors[i - 1].advance();
                    index = r.convertForward(index);
                    index = _usingRotors[i - 1].convertForward(index);
                    count++;
                    i--;
                } else if (maybe) {
                    index = r.convertForward(index);
                } else {
                    count++;
                    index = r.convertForward(index);
                }
            }
            if (!maybe && !r.atNotch() && i == 3 && count == 0)  {
                index = r.convertForward(index);
            } else if ((!r.rotates()) || i == 0) {
                index = r.convertForward(index);
            }
        }

        for (int i = 1; i < _usingRotors.length; i++) {
            Rotor r = _usingRotors[i];
            index = r.convertBackward(index);
        }
        return _plugBoard.permute(index);
    }


    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] msgChars = msg.toCharArray();
        String result = "";
        for (char c: msgChars) {
            if (c == ' ') {
                result = result + " ";
            } else {
                char x = (char) (convert((c - 'A')) + 'A');
                result = result + x;
            }
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors used in setting line. */
    private int _numRotors;

    /** Number of pawls used in setting line. */
    private int _pawls;

    /** ArrayList of rotors in config file. */
    private ArrayList<Rotor> _allRotors;

    /** Current array of rotors used in setting line. */
    private Rotor[] _usingRotors;

    /** _plugboard permutation used. */
    private Permutation _plugBoard;

}
