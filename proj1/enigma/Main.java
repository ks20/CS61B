package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Kushal Singh
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine configMach = readConfig();
        while (_input.hasNextLine()) {
            String setting = _input.nextLine();
            String[] strArr = setting.split(" ");

            if (!strArr[0].equals("*") && _input.hasNext()) {
                setting = _input.nextLine();
                _output.println();
            } else if (!strArr[0].equals("*")) {
                throw new EnigmaException("First line must be settings line.");
            }

            setUp(configMach, setting);

            String mssg = "";
            while (!_input.hasNext("\\*.*") && _input.hasNextLine()) {
                if (_input.hasNext("\\*.*")) {
                    throw new EnigmaException("Improper Format for setting.");
                }

                mssg = _input.nextLine();
                mssg = mssg.toUpperCase();
                String[] mssgArr = mssg.split("\\ ");
                String[] convertedMsgArr = new String[mssgArr.length];
                for (int i = 0; i < mssgArr.length; i++) {
                    convertedMsgArr[i] = configMach.convert(mssgArr[i]);
                }
                String output = "";
                for (String str : convertedMsgArr) {
                    output = (output + str);
                }
                output = output.trim();
                printMessageLine(output);
            }
        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphaBeta = _config.next();
            if (alphaBeta.length() == 0) {
                throw new EnigmaException("Specify # rotor slots, # pawls");
            }
            _alphabet = new UpperCaseAlphabet();
            int rotorSlots = _config.nextInt();
            int numPawls = _config.nextInt();
            _allRots = new ArrayList<>();

            while (_config.hasNext()) {
                String name = _config.next();
                String typeAndNotches = _config.next();
                char rotorType = typeAndNotches.charAt(0);
                int typeLen = typeAndNotches.length();
                String notchVals = typeAndNotches.substring(1, typeLen);
                String cycleBuildUp = "";
                boolean check = true;
                String x = "";
                while (check) {
                    x = _config.next();
                    if (x.charAt(0) == '(') {
                        cycleBuildUp += x;
                    }
                    if (x.charAt(0) == ')') {
                        cycleBuildUp += x;
                    }
                    if (!_config.hasNext("\\(.*")) {
                        check = false;
                    }
                }
                Permutation currPerm = new Permutation(cycleBuildUp, _alphabet);
                Rotor r = null;

                if (rotorType == 'R') {
                    r = new Reflector(name, currPerm);
                } else if (rotorType == 'N') {
                    r = new FixedRotor(name, currPerm);
                } else {
                    r = new MovingRotor(name, currPerm, notchVals);
                }
                _allRots.add(r);
            }
            return new Machine(_alphabet, rotorSlots, numPawls, _allRots);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        settings = settings.toUpperCase();
        settings = settings.replaceAll("\\s+", " ");
        String[] contents = settings.split(" ");

        ArrayList<String> seen = new ArrayList<>();
        for (int i = 1; i <= M.numRotors(); i++) {
            if (seen.contains(contents[i])) {
                throw new EnigmaException("Rotor repeated.");
            } else {
                seen.add(contents[i]);
            }
        }
        String[] rotorArr = new String[M.numRotors()];
        System.arraycopy(contents, 1, rotorArr, 0, rotorArr.length);
        M.insertRotors(rotorArr);

        if (contents[M.numRotors() + 1].length() > (M.numRotors() - 1)) {
            throw new EnigmaException("Wheel settings too long");
        }

        if (contents[M.numRotors() + 1].length() < (M.numRotors() - 1)) {
            throw new EnigmaException("Wheel settings too short");
        }

        for (int i = 0; i < contents[M.numRotors() + 1].length(); i++) {
            if (!_alphabet.contains(contents[M.numRotors() + 1].charAt(i))) {
                throw new EnigmaException("Some characters not in alphabet.");
            }
        }

        M.setRotors(contents[M.numRotors() + 1]);
        String plugString = "";
        for (int x = M.numRotors() + 2; x < contents.length; x++) {
            plugString = plugString + contents[x];
        }

        M.setPlugboard(new Permutation(plugString, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        char[] msgArr = msg.toCharArray();
        int counter = 0;
        for (char c : msgArr) {
            if (counter == 5) {
                _output.print(" ");
                counter = 0;
            }
            _output.print(c);
            counter++;
        }
        _output.println();
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Accumulation of all rotors to be used from config file. */
    private ArrayList<Rotor> _allRots;
}
