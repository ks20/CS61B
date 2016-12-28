package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Kushal Singh
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    /** Returns ASCII version of characters in notches. */
    int[] getNotchesArr() {
        int[] temp = new int[_notches.length()];
        for (int i = 0; i < _notches.length(); i++) {
            temp[i] = (_notches.charAt(i));
        }
        return temp;
    }

    @Override
    void advance() {
        _setting = (setting() + 1) % size();

    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            int cur = _notches.charAt(i);
            if (('A' + permutation().wrap((setting() - 1))) == cur) {
                return true;
            }
        }
        return false;
    }

    @Override
    int convertForward(int p) {
        int permInput = permutation().wrap(p + setting());
        int temp = (permutation().permute(permInput) - setting());
        return permutation().wrap(temp);
    }

    @Override
    int convertBackward(int e) {
        int invInput = permutation().wrap(e + setting());
        int temp = (permutation().invert(invInput) - setting());
        return permutation().wrap(temp);
    }

    /** String value of notches. */
    private String _notches;


}
