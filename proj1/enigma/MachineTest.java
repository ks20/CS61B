package enigma;

/**
 * Created by Kushal on 10/5/16.
 */
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import enigma.Machine;
import enigma.Rotor;
import enigma.Alphabet;
import enigma.UpperCaseAlphabet;


import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

import static enigma.TestUtils.*;
import static enigma.TestUtils.UPPER;
import static enigma.TestUtils.UPPER_STRING;
import static org.junit.Assert.*;


public class MachineTest {

    private void MakeMachine() {


        MovingRotor test = setRotor("I", NAVALA, "Q");
        _rotors.add(test);
        _rotors.add(setRotor("II", NAVALA, "E"));
        _rotors.add(setRotor("III", NAVALA, "V"));
        _rotors.add(setRotor("IV", NAVALA, "J"));
        _rotors.add(setRotor("V", NAVALA, "Z"));
        _rotors.add(setRotor("VI", NAVALA, "ZM"));
        _rotors.add(setRotor("VII", NAVALA, "ZM"));
        _rotors.add(setRotor("VIII", NAVALA, "ZM"));
        _rotors.add(setRotorFIXED("Beta", NAVALA));
        _rotors.add(setRotorFIXED("Gamma", NAVALA));
        _rotors.add(setRotorREFLECTOR("B", NAVALA));
        _rotors.add(setRotorREFLECTOR("C", NAVALA));

        _alpha = new UpperCaseAlphabet();
        test_Mach0 = new Machine(_alpha, 5, 3, _rotors);
        test_Mach1 = new Machine(_alpha, 2, 0, _rotors);
        test_Mach2 = new Machine(_alpha, 3, 1, _rotors);
        test_Mach3 = new Machine(_alpha, 5, 3, _rotors);

        String[] _rotors_array_true0 = {_rotors.get(10).name(), _rotors.get(8).name(), _rotors.get(0).name(), _rotors.get(1).name(), _rotors.get(2).name()};
        test_Mach0.insertRotors(_rotors_array_true0);

        String[] _rotors_array_true1 = {_rotors.get(10).name(), _rotors.get(8).name()};
        test_Mach1.insertRotors(_rotors_array_true1);

        String[] _rotors_array_true2 = {"C", "Gamma", "IV"};
        test_Mach2.insertRotors(_rotors_array_true2);

        String[] _rotors_array_true3 = {"B", "BETA", "III", "IV", "I"};
        test_Mach3.insertRotors(_rotors_array_true3);

        String _settings0 = "AAAA";
        test_Mach0.setRotors(_settings0);

        String _settings1 = "A";
        test_Mach1.setRotors(_settings1);

        String _settings2 = "AF";
        test_Mach2.setRotors(_settings2);

        String _settings3 = "AXLE";
        test_Mach3.setRotors(_settings3);

        Permutation _fornow0 = new Permutation("", _alpha);
        test_Mach0.setPlugboard(_fornow0);

        Permutation _fornow1 = new Permutation("", _alpha);
        test_Mach1.setPlugboard(_fornow1);

        Permutation _fornow2 = new Permutation("(AR) (ED)", _alpha);
        test_Mach2.setPlugboard(_fornow2);

        Permutation _fornow3 = new Permutation("(HQ) (EX) (IP) (TR) (BY)", _alpha);
        test_Mach3.setPlugboard(_fornow3);

    }

    protected Alphabet _alpha;
    protected Machine test_Mach0;
    protected Machine test_Mach1;
    protected Machine test_Mach2;
    protected Machine test_Mach3;

    /**
     * Testing time limit.
     */
    // @Rule
    //public Timeout globalTimeout = Timeout.seconds(5);

   /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    //NUMROTORS BEGIN
    @Test
    public void numRotors0() throws Exception {
        MakeMachine();
        int true_num_rotors = 5;
        assertEquals("Machine 0" + " wrong number of rotors", true_num_rotors, test_Mach0.numRotors());
    }

    @Test
    public void numRotors1() throws Exception {
        MakeMachine();
        int true_num_rotors = 2;
        assertEquals("Machine 1" + " wrong number of rotors", true_num_rotors, test_Mach1.numRotors());
    }

    @Test
    public void numRotors2() throws Exception {
        MakeMachine();
        int true_num_rotors = 3;
        assertEquals("Machine 2" + " wrong number of rotors", true_num_rotors, test_Mach2.numRotors());
    }
    //NUMROTORS END


    //NUMPAWLS BEGIN
    @Test
    public void numPawls0() throws Exception {
        MakeMachine();
        int true_num_pawls = 3;
        assertEquals("Machine 0" + " wrong number of Pawls", true_num_pawls, test_Mach0.numPawls());
    }

    @Test
    public void numPawls1() throws Exception {
        MakeMachine();
        int true_num_pawls = 0;
        assertEquals("Machine 1" + " wrong number of Pawls", true_num_pawls, test_Mach1.numPawls());
    }

    @Test
    public void numPawls2() throws Exception {
        MakeMachine();
        int true_num_pawls = 1;
        assertEquals("Machine 2" + " wrong number of Pawls", true_num_pawls, test_Mach2.numPawls());
    }
    //NUMPAWLS END


    //INSERTROTOR BEGIN
    @Test
    public void insertRotors0() throws Exception {
        MakeMachine();
        String[] _rotors_array_true = {_rotors.get(10).name(), _rotors.get(8).name(), _rotors.get(0).name(), _rotors.get(1).name(), _rotors.get(2).name()};
        test_Mach0.insertRotors(_rotors_array_true);
    }

    @Test
    public void insertRotors1() throws Exception {
        MakeMachine();
        String[] _rotors_array_true = {_rotors.get(10).name(), _rotors.get(8).name()};
        test_Mach1.insertRotors(_rotors_array_true);
    }

    @Test
    public void insertRotors2() throws Exception {
        MakeMachine();
        String[] _rotors_array_true = {"C", "Gamma", "IV"};
        test_Mach2.insertRotors(_rotors_array_true);
    }
    //INSERTROTOR END


    //SETROTORS BEGIN
    @Test
    public void setRotors0() throws Exception {
        MakeMachine();
        String _settings = "AAAA";
        test_Mach0.setRotors(_settings);
    }

    @Test
    public void setRotors1() throws Exception {
        MakeMachine();
        String _settings = "";
        test_Mach1.setRotors(_settings);
    }

    @Test
    public void setRotors2() throws Exception {
        MakeMachine();
        String _settings = "F";
        test_Mach2.setRotors(_settings);
    }
    //SETROTORS END


    //SETPLUGBOARDS BEGIN
    @Test
    public void setPlugboard0() throws Exception {
        MakeMachine();
        enigma.Permutation _fornow = new enigma.Permutation("", _alpha);
        test_Mach0.setPlugboard(_fornow);
    }

    @Test
    public void setPlugboard1() throws Exception {
        MakeMachine();
        enigma.Permutation _fornow = new enigma.Permutation("", _alpha);
        test_Mach1.setPlugboard(_fornow);
    }

    @Test
    public void setPlugboard2() throws Exception {
        MakeMachine();
        enigma.Permutation _fornow = new enigma.Permutation("(FG) (AD)", _alpha);
        test_Mach2.setPlugboard(_fornow);
    }
    //SETPLUGBOARDS END


    //CONVERTINT BEGIN
    @Test
    public void convert0() throws Exception {
        MakeMachine();

        int char1, char2;
        char1 = 'H' - 'A';
        char2 = 'E' - 'A';
        int res_char1 = test_Mach0.convert(char1);
        int res_char2 = test_Mach0.convert(char2);
        assertEquals("Machine 0" + " wrong Converted char1 (H)", ('I' - 'A'), res_char1);
        assertEquals("Machine 0" + " wrong Converted char2 (E)", ('L' - 'A'), res_char2);
    }

    @Test
    public void convert1() throws Exception {
        MakeMachine();

        int char1, char2;
        char1 = 'H' - 'A';
        char2 = 'E' - 'A';
        int res_char1 = test_Mach1.convert(char1);
        int res_char2 = test_Mach1.convert(char2);
        assertEquals("Machine 1" + " wrong Converted char1 (H)", ('D' - 'A'), res_char1);
        assertEquals("Machine 1" + " wrong Converted char2 (E)", ('Q' - 'A'), res_char2);

    }

    @Test
    public void convert2() throws Exception {
        MakeMachine();
        int char1, char2;
        char1 = 'H' - 'A';
        char2 = 'E' - 'A';
        int res_char1 = test_Mach2.convert(char1);
        int res_char2 = test_Mach2.convert(char2);
        assertEquals("Machine 2" + " wrong Converted char1 (H)", ('T' - 'A'), res_char1);
        assertEquals("Machine 2" + " wrong Converted char2 (E)", ('O' - 'A'), res_char2);
    }

    @Test
    public void convert3() throws Exception {
        MakeMachine();
        int char1, char2;
        char1 = 'H';
        char2 = 'E';
        int res_char1 = test_Mach0.convert(char1);
        int res_char2 = test_Mach0.convert(char2);
        assertEquals("Machine 0" + " wrong Converted char1 (H)", ('I' - 'A'), res_char1);
        assertEquals("Machine 0" + " wrong Converted char2 (E)", ('L' - 'A'), res_char2);
    }

    //CONVERTINT END


    @Test
    public void convert_str0() throws Exception {
        MakeMachine();

        String str1, str2;
        str1 = "HELLO WORLD";
        str2 = "ILBDA AMTAZ";
        String res_str1 = test_Mach0.convert(str1);
        String res_str2 = test_Mach0.convert(str2);
        assertEquals("Machine 0" + " wrong Converted str1 (\"HELLO WORLD\")", "ILBDA AMTAZ", res_str1);
        assertEquals("Machine 0" + " wrong Converted str2 (\"ILBDA AMTAZ\")", "HELLO WORLD", res_str2);
    }

    @Test
    public void convert_str1() throws Exception {
        MakeMachine();

        String str3, str4;
        str3 = "FROM HIS SHOULDER";
        //str4 = "ILBDA AMTAZ";
        String res_str3 = test_Mach3.convert(str3);
        //String res_str2 = test_Mach0.convert(str4);
        assertEquals("Machine 0" + " wrong Converted str1 (\"FROM HIS SHOULDER\")", "QVPQ SOK OILPUBKJ", res_str3);
        //assertEquals("Machine 0" + " wrong Converted str2 (\"ILBDA AMTAZ\")", "HELLO WORLD", res_str2);
    }

    public static ArrayList<Rotor> _rotors = new ArrayList<Rotor>();

    public static MovingRotor setRotor(String name, HashMap<String, String> rotors,
                                       String notches) {
        Rotor rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                notches);
        return (MovingRotor) rotor;
    }


    public static FixedRotor setRotorFIXED(String name, HashMap<String, String> rotors) {
        enigma.FixedRotor rotor = new enigma.FixedRotor(name, new enigma.Permutation(rotors.get(name), UPPER));
        return (FixedRotor) rotor;
    }

    public static Reflector setRotorREFLECTOR(String name, HashMap<String, String> rotors) {
        enigma.Reflector rotor = new enigma.Reflector(name, new enigma.Permutation(rotors.get(name), UPPER));
        return (Reflector) rotor;
    }
}

