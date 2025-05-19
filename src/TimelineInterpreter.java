package src;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

public class TimelineInterpreter {

    private static enum Direction {
        UP,
        LEFT,
        DOWN,
        RIGHT;
    }

    private static final Operator[] mathOperators = {
        Operator.ADD, 
        Operator.SUB,
        Operator.MULT,
        Operator.DIV,
        Operator.POW,
        Operator.MOD,
        Operator.NEG
    };
    private static final Operator[] estimationOperators = {
        Operator.ROUND, 
        Operator.CEIL, 
        Operator.FLOOR,
        Operator.TRUNC
    };
    private static final Operator[] trigOperators = {
        Operator.SIN,
        Operator.COS,
        Operator.TAN,
        Operator.CSC,
        Operator.SEC,
        Operator.COT
    };
    private static final Operator[] bitwise = {
        Operator.BNOT,
        Operator.BAND,
        Operator.BOR,
        Operator.BXOR,
        Operator.BLSHIFT,
        Operator.BRSHIFT,
        Operator.BRSHIFTPLUS
    };
    private static final Operator[] booleanOperators = {
        Operator.NOT,
        Operator.AND,
        Operator.OR
    };
    private static final Operator[] concatenation = {
        Operator.CONCAT,
        Operator.REPEAT
    };
    private static final Operator[] equalityOperators = {
        Operator.EQUALS,
        Operator.NOTEQUALS,
        Operator.LESSTHAN,
        Operator.LESSTHANEQUALS,
        Operator.GREATERTHAN,
        Operator.GREATERTHANEQUALS
    };

    private static final String[] lowercaseLetters = "abcdefghijklmnopqrstuvwxyz".split("");
    private static final String[] uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
    private static final String[] digits = "0123456789".split("");
    private static final String[] symbols = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".split("");
    private static final String[] booleans = {"TRUE", "FALSE"};
    private static final String[] whitespace = {" ", "\n", "\t"};

    private static String[] stdinInput;

    private static final Direction[] movement1 = {Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.UP};
    private static final Direction[] movement2 = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
    private static final Direction[] movement3 = {Direction.RIGHT, Direction.UP, Direction.LEFT, Direction.DOWN};
    private static final Direction[] movement4 = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};

    // While we have the mod function, we use the remainder operator since indices won't be negative.
    private static Operator getMathOperator(int layer) {return mathOperators[layer % mathOperators.length];}
    private static Operator getEstimationOperator(int layer) {return estimationOperators[layer % estimationOperators.length];}
    private static Operator getTrigOperator(int layer) {return trigOperators[layer % trigOperators.length];}
    private static Operator getBitwise(int layer) {return bitwise[layer % bitwise.length];}
    private static Operator getBooleanOperator(int layer) {return booleanOperators[layer % booleanOperators.length];}
    private static Operator getConcatenation(int layer) {return concatenation[layer % concatenation.length];}
    private static Operator getEqualityOperator(int layer) {return equalityOperators[layer % equalityOperators.length];}

    private static String getLowercaseLetter(int layer) {return lowercaseLetters[layer % lowercaseLetters.length];}
    private static String getUppercaseLetter(int layer) {return uppercaseLetters[layer % uppercaseLetters.length];}
    private static String getDigit(int layer) {return digits[layer % digits.length];}
    private static String getSymbol(int layer) {return symbols[layer % symbols.length];}
    private static String getWhitespace(int layer) {return whitespace[layer % whitespace.length];}
    private static String getBoolean(int layer) {return booleans[layer % booleans.length];}
    private static String getStdinInput(int layer) {return stdinInput[layer % stdinInput.length];}

    private static Direction getMovement1(int layer) {return movement1[layer % movement1.length];}
    private static Direction getMovement2(int layer) {return movement2[layer % movement2.length];}
    private static Direction getMovement3(int layer) {return movement3[layer % movement3.length];}
    private static Direction getMovement4(int layer) {return movement4[layer % movement4.length];}

    private static char[][] retrieveCode() {
        /**
         * Takes the timeline file and establishes the board for the interpreter
         * to run on, allowing the program to run.
         * 
         * @return          the timeline code
         */

        String[] lines = new BufferedReader(new InputStreamReader(System.in))
                        .lines()
                        .toArray(String[]::new);
        int rows = lines.length;
        int cols = Arrays.asList(lines.clone())
                        .stream()
                        .map(text -> text.length())
                        .reduce(0, (acc, curr) -> Math.max(acc, curr));
        char[][] code = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < lines[r].length(); c++) {
                code[r][c] = lines[r].charAt(c);
            }
        }
        // for (int r = 0; r < rows; r++) {
        //     for (int c = 0; c < cols; c++) {
        //         System.out.print(code[r][c]);
        //     }
        //     System.out.println();
        // }
        return code;
    }

    private static int mod(int a, int b) {
        /**
         * Returns the modulo of a mod b.
         * 
         * @param a         the dividend
         * @param b         the divisor
         * @return          the modulo result
         */
        if (Integer.signum(a) == Integer.signum(b)) {
            return Math.abs(a) % Math.abs(b) * Integer.signum(b);
        } else if (a == 0) {
            return 0;
        } else {
            return (Math.abs(b) - Math.abs(a) % Math.abs(b)) * Integer.signum(b);
        }
    } 

    private static int lcm(int a, int b) {
        /**
         * Calculates the Least Common Multiple between
         * two numbers.
         * 
         * Algorithm credit is from
         * https://www.baeldung.com/java-least-common-multiple.
         * 
         * @ param a            natural number
         * @ param b            natural number
         * @ return             the lcm between a and b
         */

        if (a == 0 || b == 0) {
            return 0;
        }
        int absNumber1 = Math.abs(a);
        int absNumber2 = Math.abs(b);
        int absHigherNumber = Math.max(absNumber1, absNumber2);
        int absLowerNumber = Math.min(absNumber1, absNumber2);
        int lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }

    private static int getResetLayer() {
        /**
         * Resets the layer index if the current layer state is the same as
         * the initial layer state.
         * 
         * @ return          the reset layer based on the lcm of each infinity layer size
         */

        List<Integer> infinityCells = Arrays.asList(
            mathOperators.length,
            estimationOperators.length,
            trigOperators.length,
            lowercaseLetters.length,
            uppercaseLetters.length,
            digits.length,
            symbols.length,
            bitwise.length,
            booleanOperators.length,
            movement1.length,
            movement2.length,
            movement3.length,
            movement4.length,
            concatenation.length,
            whitespace.length,
            booleans.length,
            equalityOperators.length,
            stdinInput.length
        );
        return infinityCells.stream().reduce(1, (acc, curr) -> lcm(acc, curr));
    }

    private static Direction turnClockwise(Direction direction) {
        switch (direction) {
            case UP: return Direction.RIGHT;
            case LEFT: return Direction.UP;
            case DOWN: return Direction.LEFT;
            case RIGHT: return Direction.DOWN;
            default: return null;   // Not sure why Java wants this.
        }
    }

    private static Direction turnCounterclockwise(Direction direction) {
        switch (direction) {
            case UP: return Direction.LEFT;
            case LEFT: return Direction.DOWN;
            case DOWN: return Direction.RIGHT;
            case RIGHT: return Direction.UP;
            default: return null;   // Not sure why Java wants this.
        }
    }

    private static void interpretCode() {
        /**
         * Interprets the timeline code and, if so, prints out its output.
         * For the interpreter itself, it will just return the output from
         * the timeline code.
         * 
         * @param input     the input that gets fed to the timeline code
         * @return          the output of the timeline code
         */

        // Code References
        char[][] code = retrieveCode();
        Point loc = new Point(0, 0);
        int layer = 0;
        int resetLayer = getResetLayer();
        Direction direction = Direction.RIGHT;

        // Internal Storage
        Accumulator accumulator = new Accumulator();
        LinkedList<HashMap<Point,String>> drops = new LinkedList<HashMap<Point,String>>();
        
        for (int i = 0 ; i < 10 ; i++) {
            drops.add(new HashMap<Point, String>());
        }

        program:
        for (;;) {
            // System.out.println("\n>> I am at: " + loc.r + ", " + loc.c + " doing " + code[loc.r][loc.c]);
            switch(code[loc.r][loc.c]) {
                case 'A':
                    accumulator.push(getBoolean(layer));
                    break;
                case 'B':
                    accumulator.push( getBooleanOperator(layer));
                    break;
                case 'C':
                    accumulator.push(getConcatenation(layer));
                    break;
                case 'D':
                    accumulator.push(getDigit(layer));
                    break;
                case 'E':
                    accumulator.push(getEqualityOperator(layer));
                    break;
                case 'I':
                    accumulator.push(getStdinInput(layer));
                    break;
                case 'L':
                    accumulator.push(getLowercaseLetter(layer));
                    break;
                case 'M':
                    accumulator.push(getMathOperator(layer));
                    break;
                case 'N':
                    accumulator.push(getBitwise(layer));
                    break;
                case 'R':
                    accumulator.push(getEstimationOperator(layer));
                    break;
                case 'S':
                    accumulator.push(getSymbol(layer));
                    break;
                case 'T':
                    accumulator.push(getTrigOperator(layer));
                    break;
                case 'U':
                    accumulator.push(getUppercaseLetter(layer));
                    break;
                case 'W':
                    accumulator.push(getWhitespace(layer));
                    break;
                case 'a':
                    direction = getMovement1(layer);
                    break;
                case 'b':
                    direction = getMovement2(layer);
                    break;
                case 'c':
                    direction = getMovement3(layer);
                    break;
                case 'd':
                    direction = getMovement4(layer);
                    break;
                case '>':
                    direction = turnClockwise(direction);
                    break;
                case '<':
                    direction = turnCounterclockwise(direction);
                    break;
                case ')':
                    if (accumulator.isTrue()) {
                        direction = turnClockwise(direction);
                    }
                    break;
                case '(':
                    if (accumulator.isTrue()) {
                        direction = turnCounterclockwise(direction);
                    }
                    break;
                case '@':
                    layer = (layer + 1) % resetLayer;
                    drops.poll();
                    drops.add(new HashMap<Point,String>());
                    break;
                case '#':
                    switch(direction) {
                        case UP:
                            loc.r = mod(loc.r - 1, code.length);
                            break;
                        case LEFT:
                            loc.c = mod(loc.c - 1, code[0].length);
                            break;
                        case DOWN:
                            loc.r = mod(loc.r + 1, code.length);
                            break;
                        case RIGHT:
                            loc.c = mod(loc.c + 1, code[0].length);
                            break;
                    }
                    break;
                case '0':
                    if (drops.get(0).containsKey(loc) && accumulator.left.isEmpty())
                        accumulator.left = drops.get(0).remove(loc);
                    else if (drops.get(0).containsKey(loc) && accumulator.right.isEmpty())
                        accumulator.right = drops.get(0).remove(loc);
                    else if (!drops.get(0).containsKey(loc) && !accumulator.left.isEmpty())
                        drops.get(0).put(loc, accumulator.left);
                    break;
                case '1':
                    if (drops.get(0).containsKey(loc) && accumulator.left.isEmpty())
                        accumulator.left = drops.get(0).remove(loc);
                    else if (drops.get(0).containsKey(loc) && accumulator.right.isEmpty())
                        accumulator.right = drops.get(0).remove(loc);
                    else if (!drops.get(1).containsKey(loc) && !accumulator.left.isEmpty())
                        drops.get(1).put(loc, accumulator.left);
                    break;
                case '2':
                    if (drops.get(0).containsKey(loc) && accumulator.left.isEmpty())
                        accumulator.left = drops.get(0).remove(loc);
                    else if (drops.get(0).containsKey(loc) && accumulator.right.isEmpty())
                        accumulator.right = drops.get(0).remove(loc);
                    else if (!drops.get(2).containsKey(loc) && !accumulator.left.isEmpty())
                        drops.get(2).put(loc, accumulator.left);
                    break;
                case '3':
                    if (drops.get(0).containsKey(loc) && accumulator.left.isEmpty())
                        accumulator.left = drops.get(0).remove(loc);
                    else if (drops.get(0).containsKey(loc) && accumulator.right.isEmpty())
                        accumulator.right = drops.get(0).remove(loc);
                    else if (!drops.get(3).containsKey(loc) && !accumulator.left.isEmpty())
                        drops.get(3).put(loc, accumulator.left);
                    break;
                case '4':
                    if (drops.get(0).containsKey(loc) && accumulator.left.isEmpty())
                        accumulator.left = drops.get(0).remove(loc);
                    else if (drops.get(0).containsKey(loc) && accumulator.right.isEmpty())
                        accumulator.right = drops.get(0).remove(loc);
                    else if (!drops.get(4).containsKey(loc) && !accumulator.left.isEmpty())
                        drops.get(4).put(loc, accumulator.left);
                    break;
                case '5':
                    if (drops.get(0).containsKey(loc) && accumulator.left.isEmpty())
                        accumulator.left = drops.get(0).remove(loc);
                    else if (drops.get(0).containsKey(loc) && accumulator.right.isEmpty())
                        accumulator.right = drops.get(0).remove(loc);
                    else if (!drops.get(5).containsKey(loc) && !accumulator.left.isEmpty())
                        drops.get(5).put(loc, accumulator.left);
                    break;
                case '6':
                    if (drops.get(0).containsKey(loc) && accumulator.left.isEmpty())
                        accumulator.left = drops.get(0).remove(loc);
                    else if (drops.get(0).containsKey(loc) && accumulator.right.isEmpty())
                        accumulator.right = drops.get(0).remove(loc);
                    else if (!drops.get(6).containsKey(loc) && !accumulator.left.isEmpty())
                        drops.get(6).put(loc, accumulator.left);
                    break;
                case '7':
                    if (drops.get(0).containsKey(loc) && accumulator.left.isEmpty())
                        accumulator.left = drops.get(0).remove(loc);
                    else if (drops.get(0).containsKey(loc) && accumulator.right.isEmpty())
                        accumulator.right = drops.get(0).remove(loc);
                    else if (!drops.get(7).containsKey(loc) && !accumulator.left.isEmpty())
                        drops.get(7).put(loc, accumulator.left);
                    break;
                case '8':
                    if (drops.get(0).containsKey(loc) && accumulator.left.isEmpty())
                        accumulator.left = drops.get(0).remove(loc);
                    else if (drops.get(0).containsKey(loc) && accumulator.right.isEmpty())
                        accumulator.right = drops.get(0).remove(loc);
                    else if (!drops.get(8).containsKey(loc) && !accumulator.left.isEmpty())
                        drops.get(8).put(loc, accumulator.left);
                    break;
                case '9':
                    if (drops.get(0).containsKey(loc) && accumulator.left.isEmpty())
                        accumulator.left = drops.get(0).remove(loc);
                    else if (drops.get(0).containsKey(loc) && accumulator.right.isEmpty())
                        accumulator.right = drops.get(0).remove(loc);
                    else if (!drops.get(9).containsKey(loc) && !accumulator.left.isEmpty())
                        drops.get(9).put(loc, accumulator.left);
                    break;
                case '?':
                    accumulator.clear();
                    break;
                case '.':
                    accumulator.print();
                    accumulator.clear();
                    break;
                case ',':
                    accumulator.print();
                    break;
                case 'X':
                    break program;
            }
            if (accumulator.isAmorphous || !accumulator.evaluate()) {
                // System.out.println("ACCUMULATOR IS AMORPHOUS");
                accumulator.amorphousClear();
            }   
            // System.out.println("Operator right now is " + operator);
            switch(direction) {
                case UP:
                    //System.out.println("Going UP");
                    loc.r = mod(loc.r - 1, code.length);
                    break;
                case LEFT:
                    //System.out.println("Going LEFT");
                    loc.c = mod(loc.c - 1, code[0].length);
                    break;
                case DOWN:
                    //System.out.println("Going DOWN");
                    loc.r = mod(loc.r + 1, code.length);
                    break;
                case RIGHT:
                    //System.out.println("Going RIGHT");
                    loc.c = mod(loc.c + 1, code[0].length);
                    break;
            }
            // System.out.println("\n"+ loc.x + " " + loc.y +  " Row Length:" + code.length);
        }
    }
    public static void main(String[] args) {
        stdinInput = String.join(" ", args).concat("\0").split("");
        interpretCode();
    }
}