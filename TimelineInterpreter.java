import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class TimelineInterpreter {
    private static enum Direction {
        UP,
        LEFT,
        DOWN,
        RIGHT;
    }

    private static enum Operator {
        NULL,
        ADD,
        SUB,
        MULT,
        DIV,
        POW,
        MOD,
        NEG,
        ROUND,
        CEIL,
        FLOOR,
        TRUNC,
        SIN,
        COS,
        TAN,
        CSC,
        SEC,
        COT,
        BNOT,
        BAND,
        BOR,
        BXOR,
        BLSHIFT,
        BRSHIFT,
        BRSHIFTPLUS,
        NOT,
        AND,
        OR,
        CONCAT,
        REPEAT,
        EQUALS,
        NOTEQUALS,
        LESSTHAN,
        LESSTHANEQUALS,
        GREATERTHAN,
        GREATERTHANEQUALS;
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
    private static final String[] symbols = "`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?".split("");
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

    private static int pow(int a, int b) {
        /**
         * Returns the exponent of a ** b.
         * 
         * @param a         the base
         * @param b         the power
         * @return          the exponent result
         */
        int exp = 1;
        for (int i = 0; i < b; i++) {exp *= a;}
        return exp;
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

    private static int truncate(double n) {
        /**
         * Truncates the double to an integer.
         * 
         * @param n             the double to be truncated
         * @return              the integer that is truncated
         */
        return ((n < 0) ? -1 : 1) * (int)(Math.floor(Math.abs(n)));
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

    private static boolean[] evaluate(String[] accumulator, Operator operator, int accSize) {
        /**
         * Evaluates the accumulator from the given values
         * and operators. This will change the accumulator and remove the
         * operator to null.
         * 
         * @param accumulator   the accumulator of the timeline code
         * @param operator      the operator to evaluate the code
         * @param accSize       the size of the accumulator
         * @return              the boolean if the evaluation does not lead to an Amorphous
         *                          [0] <= Checks if evaluation has been changed.
         *                          [1] <= Checks if the current evaluation state is valid.
         */
        // System.out.println(accumulator[0] + " " +  operator + " " + accumulator[1] + " " + accSize);
        boolean hasChanged = accSize >= 2;
        boolean isValid = true;
        try {
            switch (accSize) {
                case 2:     
                    // Binary Operations
                    switch (operator) {
                        case ADD:
                            accumulator[0] = Integer.toString(
                                Integer.parseInt(accumulator[0]) + Integer.parseInt(accumulator[1]));
                            accumulator[1] = null;
                            break;
                        case SUB:
                            accumulator[0] = Integer.toString(
                                Integer.parseInt(accumulator[0]) - Integer.parseInt(accumulator[1]));
                            accumulator[1] = null;
                            break;
                        case MULT:
                            accumulator[0] = Integer.toString(
                                Integer.parseInt(accumulator[0]) * Integer.parseInt(accumulator[1]));
                            accumulator[1] = null;
                            break;
                        case DIV:
                            accumulator[0] = Integer.toString(
                                Integer.parseInt(accumulator[0]) / Integer.parseInt(accumulator[1]));
                            accumulator[1] = null;
                            break;
                        case POW:
                            accumulator[0] = Integer.toString(
                                pow(Integer.parseInt(accumulator[0]), Integer.parseInt(accumulator[1])));
                            accumulator[1] = null;
                            break;
                        case MOD:
                            accumulator[0] = Integer.toString(
                                mod(Integer.parseInt(accumulator[0]), Integer.parseInt(accumulator[1])));
                            accumulator[1] = null;
                            break;
                        case BAND:
                            accumulator[0] = Integer.toString(
                                Integer.parseInt(accumulator[0]) & Integer.parseInt(accumulator[1]));
                            accumulator[1] = null;
                            break;
                        case BOR:
                            accumulator[0] = Integer.toString(
                                Integer.parseInt(accumulator[0]) | Integer.parseInt(accumulator[1]));
                            accumulator[1] = null;
                            break;
                        case BXOR:
                            accumulator[0] = Integer.toString(
                                Integer.parseInt(accumulator[0]) ^ Integer.parseInt(accumulator[1]));
                            accumulator[1] = null;
                            break;
                        case BLSHIFT:
                            accumulator[0] = Integer.toString(
                                Integer.parseInt(accumulator[0]) << Integer.parseInt(accumulator[1]));
                            accumulator[1] = null;
                            break;
                        case BRSHIFT:
                            accumulator[0] = Integer.toString(
                                Integer.parseInt(accumulator[0]) >> Integer.parseInt(accumulator[1]));
                            accumulator[1] = null;
                            break;
                        case BRSHIFTPLUS:
                            accumulator[0] = Integer.toString(
                                Integer.parseInt(accumulator[0]) >>> Integer.parseInt(accumulator[1]));
                            accumulator[1] = null;
                            break;
                        case AND:
                            if (!accumulator[0].equals("FALSE") && !accumulator[1].equals("FALSE")){
                                accumulator[0] = "TRUE";
                            } else {
                                accumulator[0] = "FALSE";
                            }
                            accumulator[1] = null;
                            break;
                        case OR:
                            if (accumulator[0].equals("FALSE") && accumulator[1].equals("FALSE")){
                                accumulator[0] = "FALSE";
                            } else {
                                accumulator[0] = "TRUE";
                            }
                            accumulator[1] = null;
                            break;
                        case CONCAT:
                            accumulator[0] += accumulator[1];
                            accumulator[1] = null;
                            break;
                        case REPEAT:
                            String temp = "";
                            for (int i = 0; i < Integer.parseInt(accumulator[1]); i++) {
                                temp += accumulator[0];
                            }
                            accumulator[0] = temp;
                            accumulator[1] = null;
                            break;
                        case EQUALS:
                            accumulator[0] = (accumulator[0].equals(accumulator[1])) ? "TRUE" : "FALSE";
                            accumulator[1] = null;
                            break;
                        case NOTEQUALS:
                            accumulator[0] = (!accumulator[0].equals(accumulator[1])) ? "TRUE" : "FALSE";
                            accumulator[1] = null;
                            break;
                        case LESSTHAN:
                            accumulator[0] = (accumulator[0].compareTo(accumulator[1]) < 0) ? "TRUE" : "FALSE";
                            accumulator[1] = null;
                            break;
                        case LESSTHANEQUALS:
                            accumulator[0] = (accumulator[0].compareTo(accumulator[1]) <= 0) ? "TRUE" : "FALSE";
                            accumulator[1] = null;
                            break;
                        case GREATERTHAN:
                            accumulator[0] = (accumulator[0].compareTo(accumulator[1]) > 0) ? "TRUE" : "FALSE";
                            accumulator[1] = null;
                            break;
                        case GREATERTHANEQUALS:
                            accumulator[0] = (accumulator[0].compareTo(accumulator[1]) >= 0) ? "TRUE" : "FALSE";
                            accumulator[1] = null;
                            break;
                        default:
                            isValid = false;
                            break;
                    }
                    break;
                case 1:
                    // Unary Operations
                    switch (operator) {
                        case null:
                            hasChanged = false;
                            break;
                        case NEG:
                            accumulator[0] = Integer.toString(-Integer.parseInt(accumulator[0]));
                            break;
                        case ROUND:
                            accumulator[0] = Long.toString(Math.round(Double.parseDouble(accumulator[0])));
                            break;
                        case CEIL:
                            accumulator[0] = Double.toString(Math.ceil(Double.parseDouble(accumulator[0])));
                            break;
                        case FLOOR:
                            accumulator[0] = Double.toString(Math.floor(Double.parseDouble(accumulator[0])));
                            break;
                        case TRUNC:
                            accumulator[0] = Double.toString(truncate(Double.parseDouble(accumulator[0])));
                            break;
                        case SIN:
                            accumulator[0] = Double.toString(Math.sin(Double.parseDouble(accumulator[0])));
                            break;
                        case COS:
                            accumulator[0] = Double.toString(Math.cos(Double.parseDouble(accumulator[0])));
                            break;
                        case TAN:
                            accumulator[0] = Double.toString(Math.tan(Double.parseDouble(accumulator[0])));
                            break;
                        case CSC:
                            accumulator[0] = Double.toString(1 / Math.sin(Double.parseDouble(accumulator[0])));
                            break;
                        case SEC:
                            accumulator[0] = Double.toString(1 / Math.cos(Double.parseDouble(accumulator[0])));
                            break;
                        case COT:
                            accumulator[0] = Double.toString(1 / Math.tan(Double.parseDouble(accumulator[0])));
                            break;
                        case BNOT:
                            accumulator[0] = Integer.toString(~ Integer.parseInt(accumulator[0]));
                            break;
                        case NOT:
                            if (accumulator[0].equals( "FALSE")) {
                                accumulator[0] = "TRUE";
                            } else {
                                accumulator[0] = "FALSE";
                            }
                            break;
                        default:
                            hasChanged = false;
                            break;
                    }
                    break;
                default:
                    isValid = operator == Operator.NULL;
                    break;
            }
        } catch (Exception e) {
            // System.out.println("EXCEPTION CAUGHT");
            isValid = false;
        }
        // System.out.println("accumulator[0] == " + accumulator[0]);
        return new boolean[]{hasChanged, isValid};
    }

    private static void clear(String[] accumulator) {
        /**
         * Clears the accumulator.
         * 
         * @param           the accumulator
         */
        accumulator[0] = null;
        accumulator[1] = null;
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
        int row = 0, col = 0;
        int layer = 0;
        int resetLayer = getResetLayer();
        Direction direction = Direction.RIGHT;

        // Internal Storage
        String[] accumulator = new String[2];
        int accSize = 0;
        Operator operator = Operator.NULL;
        boolean isAmorphous = false;

        program:
        for (;;) {
            // System.out.println("\n>> I am at: " + row + ", " + col + " doing " + code[row][col]);
            switch(code[row][col]) {
                case 'A':
                    accumulator[accSize] = getBoolean(layer);
                    accSize++;
                    break;
                case 'B':
                    operator = getBooleanOperator(layer);
                    break;
                case 'C':
                    operator = getConcatenation(layer);
                    break;
                case 'D':
                    accumulator[accSize] = getDigit(layer);
                    accSize++;
                    break;
                case 'E':
                    operator = getEqualityOperator(layer);
                    break;
                case 'I':
                    accumulator[accSize] = getStdinInput(layer);
                    accSize++;
                    break;
                case 'L':
                    accumulator[accSize] = getLowercaseLetter(layer);
                    accSize++;
                    break;
                case 'M':
                    operator = getMathOperator(layer);
                    break;
                case 'N':
                    operator = getBitwise(layer);
                    break;
                case 'R':
                    operator = getEstimationOperator(layer);
                    break;
                case 'S':
                    accumulator[accSize] = getSymbol(layer);
                    accSize++;
                    break;
                case 'T':
                    operator = getTrigOperator(layer);
                    break;
                case 'U':
                    accumulator[accSize] = getUppercaseLetter(layer);
                    accSize++;
                    break;
                case 'W':
                    accumulator[accSize] = getWhitespace(layer);
                    accSize++;
                    break;
                case '1':
                    direction = getMovement1(layer);
                    break;
                case '2':
                    direction = getMovement2(layer);
                    break;
                case '3':
                    direction = getMovement3(layer);
                    break;
                case '4':
                    direction = getMovement4(layer);
                    break;
                case '>':
                    direction = turnClockwise(direction);
                    break;
                case '<':
                    direction = turnCounterclockwise(direction);
                    break;
                case ')':
                    if (!isAmorphous && operator == null && accSize != 0) {
                        direction = turnClockwise(direction);
                    }
                    break;
                case '(':
                    if (!isAmorphous && operator == null && accSize != 0) {
                        direction = turnCounterclockwise(direction);
                    }
                    break;
                case '@':
                    layer = (layer + 1) % resetLayer;
                    break;
                case '#': 
                    switch(direction) {
                        case UP:
                            row = mod(row - 1, code.length);
                            break;
                        case LEFT:
                            col = mod(col - 1, code[0].length);
                            break;
                        case DOWN:
                            row = mod(row + 1, code.length);
                            break;
                        case RIGHT:
                            col = mod(col + 1, code[0].length);
                            break;
                    }
                    break;
                case '!': // TODO
                    break;
                case '?':
                    clear(accumulator);
                    operator = Operator.NULL;
                    accSize = 0;
                    isAmorphous = false;
                    break;
                case '.':
                    if (isAmorphous) {
                        System.out.print("AMORPHOUS");
                    } else if (operator != Operator.NULL) {
                        System.out.print("UNEVALUATED");
                    } else if (accSize != 0){
                        System.out.print(accumulator[0]);
                    }
                    clear(accumulator);
                    operator = Operator.NULL;
                    accSize = 0;
                    isAmorphous = false;
                    break;
                case ',':
                    if (isAmorphous) {
                        System.out.print("AMORPHOUS");
                    } else if (operator != null) {
                        System.out.print("UNEVALUATED");
                    } else if (accSize != 0){
                        System.out.print(accumulator[0]);
                    }
                    break;
                case 'X':
                    break program;
            }
            
            boolean[] evaluation = evaluate(accumulator, operator, accSize);
            if (evaluation[0]){
                operator = Operator.NULL;
            }
            if (isAmorphous || !evaluation[1]) {
                // System.out.println("ACCUMULATOR IS AMORPHOUS");
                isAmorphous = true;
                clear(accumulator);
                operator = Operator.NULL;
                accSize = 0;
            }   
            // System.out.println("Operator right now is " + operator);
            switch(direction) {
                case UP:
                    //System.out.println("Going UP");
                    row = mod(row - 1, code.length);
                    break;
                case LEFT:
                    //System.out.println("Going LEFT");
                    col = mod(col - 1, code[0].length);
                    break;
                case DOWN:
                    //System.out.println("Going DOWN");
                    row = mod(row + 1, code.length);
                    break;
                case RIGHT:
                    //System.out.println("Going RIGHT");
                    col = mod(col + 1, code[0].length);
                    break;
            }
            // System.out.println("\n"+ row + " " + col +  " Row Length:" + code.length);
        }
    }
    public static void main(String[] args) {
        stdinInput = String.join(" ", args).split("");
        // System.out.println(stdinInput.length);
        interpretCode();
    }
}