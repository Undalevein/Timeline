import java.util.regex.Pattern;

public class Accumulator {
    public String left;
    public Operator operator;
    public String right;
    public boolean isAmorphous;

    public Accumulator() {
        left = "";
        right = "";
        operator = Operator.NULL;
        isAmorphous = false;
    }

    public Accumulator(Accumulator acc) {
        left = acc.left;
        right = acc.right; 
        operator = acc.operator;
        isAmorphous = acc.isAmorphous;
    }

    public void push(String value) {
        /**
         * Adds the new value to the accumulator
         * 
         * @param value      The value to add to the accumulator.
         * @throw            Throws if the accumulator is full before adding.
         */
        if (left.isEmpty()) {left = value;}
        else if (right.isEmpty()) {right = value;}
        else {throw new RuntimeException("Accumulator is full! Can't add more items!");}
    }

    public void push(Operator operator) {
        /**
         * Adds the operator to the accumulator.
         * 
         * @param operator      The operator to add to the accumulator.
         */
        if (this.operator == Operator.NULL) {this.operator = operator;}
        else {isAmorphous = true;}
    }
    
    private int pow(int a, int b) {
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

    private int mod(int a, int b) {
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

    private int truncate(double n) {
        /**
         * Truncates the double to an integer.
         * 
         * @param n             the double to be truncated
         * @return              the integer that is truncated
         */
        return ((n < 0) ? -1 : 1) * (int)(Math.floor(Math.abs(n)));
    }

    private boolean canParseInt(String str) {
        /**
         * Checks if the string can be parse to a integer.
         * 
         * @param           The string that is being evaluated.
         * @return          Whether or not the string can be parsed.
         */
        return Pattern.matches("^-?[\\d]+$", str);
    }

    private boolean canParseFloat(String str) {
        /**
         * Checks if the string can be parse to a float.
         * 
         * @param           The string that is being evaluated.
         * @return          Whether or not the string can be parsed.
         */
        return Pattern.matches("^-?[\\d]+(.[\\d]+)?$", str);
    }

    public boolean evaluate() {
        /**
         * Evaluates the accumulator from the given values
         * and operators. This will change the accumulator and remove the
         * operator to null.
         * 
         * @return              the boolean if the evaluation does not lead to an Amorphous
         *                          [0] <= Checks if evaluation has been changed.
         *                          [1] <= Checks if the current evaluation state is valid.
         */
        // System.out.println("|" + left + "| " +  operator + " |" + right + "| " + isAmorphous);
        boolean isValid = true;
        try {
            if (!right.isEmpty()) {
                isValid = evaluateBinary();
            } else if (!left.isEmpty()) {
                isValid = evaluateUnary();
            } else {
                isValid = operator == Operator.NULL; 
                operator = Operator.NULL;
            }
        } catch (Exception e) {
            // System.out.println("EXCEPTION CAUGHT");
            isValid = false;
        }
        if (!isValid) {
            amorphousClear();
            isAmorphous = true;
        }
        return isValid;
    }

    private boolean evaluateBinary() {
        boolean isValid = true;
        switch (operator) {
            case ADD:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(Integer.parseInt(left) + Integer.parseInt(left));
                else if (canParseFloat(left) && canParseFloat(right))
                    left = Double.toString(Double.parseDouble(left) + Double.parseDouble(right));
                else
                    isValid = false;
                break;
            case SUB:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(Integer.parseInt(left) - Integer.parseInt(left));
                else if (canParseFloat(left) && canParseFloat(right))
                    left = Double.toString(Double.parseDouble(left) - Double.parseDouble(right));
                else
                    isValid = false;
                break;
            case MULT:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(Integer.parseInt(left) * Integer.parseInt(left));
                else if (canParseFloat(left) && canParseFloat(right))
                    left = Double.toString(Double.parseDouble(left) * Double.parseDouble(right));
                else
                    isValid = false;
                break;
            case DIV:
                if (canParseInt(left) && canParseFloat(right))
                    left = Integer.toString(Integer.parseInt(left) / Integer.parseInt(right));
                else if (canParseFloat(left) && canParseFloat(right))
                    left = Double.toString(Double.parseDouble(left) / Double.parseDouble(right));
                else
                    isValid = false;
                break;
            case POW:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(pow(Integer.parseInt(left), Integer.parseInt(left)));
                else if (canParseFloat(left) && canParseFloat(right))
                    left = Double.toString(Math.pow(Double.parseDouble(left), Double.parseDouble(right)));
                else
                    isValid = false;
                break;
            case MOD:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(mod(Integer.parseInt(left), Integer.parseInt(right)));
                else
                    isValid = false;
                break;
            case BAND:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(Integer.parseInt(left) & Integer.parseInt(right));
                else
                    isValid = false;
                break;
            case BOR:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(Integer.parseInt(left) | Integer.parseInt(right));
                else
                    isValid = false;
                break;
            case BXOR:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(Integer.parseInt(left) ^ Integer.parseInt(right));
                else
                    isValid = false;
                break;
            case BLSHIFT:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(Integer.parseInt(left) << Integer.parseInt(right));
                else
                    isValid = false;
                break;
            case BRSHIFT:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(Integer.parseInt(left) >> Integer.parseInt(right));
                else
                    isValid = false;
                break;
            case BRSHIFTPLUS:
                if (canParseInt(left) && canParseInt(right))
                    left = Integer.toString(Integer.parseInt(left) >>> Integer.parseInt(right));
                else
                    isValid = false;
                break;
            case AND:
                if (!left.equals("FALSE") && !right.equals("FALSE")){
                    left = "TRUE";
                } else {
                    left = "FALSE";
                }
                break;
            case OR:
                if (left.equals("FALSE") && right.equals("FALSE")){
                    left = "FALSE";
                } else {
                    left = "TRUE";
                }
                break;
            case CONCAT:
                left += right;
                break;
            case REPEAT:
                if (canParseInt(right)){
                    String temp = "";
                    for (int i = 0; i < Integer.parseInt(right); i++) {
                        temp += left;
                    }
                    left = temp;
                } else {
                    isValid = false;
                }
                break;
            case EQUALS:
                left = (left.equals(right)) ? "TRUE" : "FALSE";
                break;
            case NOTEQUALS:
                left = (!left.equals(right)) ? "TRUE" : "FALSE";
                break;
            case LESSTHAN:
                left = (left.compareTo(right) < 0) ? "TRUE" : "FALSE";
                break;
            case LESSTHANEQUALS:
                left = (left.compareTo(right) <= 0) ? "TRUE" : "FALSE";
                break;
            case GREATERTHAN:
                left = (left.compareTo(right) > 0) ? "TRUE" : "FALSE";
                break;
            case GREATERTHANEQUALS:
                left = (left.compareTo(right) >= 0) ? "TRUE" : "FALSE";
                break;
            default:
                isValid = false;
                break;
        }
        right = "";
        operator = Operator.NULL;
        return isValid;
    }

    private boolean evaluateUnary() {
        switch (operator) {
            case NULL:
                return true;
            case NEG:
                if (canParseInt(left)) {
                    left = Integer.toString(-Integer.parseInt(left));
                } else if (canParseFloat(left)) {
                    left = Double.toString(-Double.parseDouble(left));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case ROUND:
                if (canParseFloat(left)) {
                    left = Integer.toString((int)Math.round(Double.parseDouble(left)));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case CEIL:
                if (canParseFloat(left)) {
                    left = Integer.toString((int)Math.ceil(Double.parseDouble(left)));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case FLOOR:
                if (canParseFloat(left)) {
                    left = Integer.toString((int)Math.floor(Double.parseDouble(left)));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case TRUNC:
                if (canParseFloat(left)) {
                    left = Integer.toString((int)truncate(Double.parseDouble(left)));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case SIN:
                if (canParseFloat(left)) {
                    left = Double.toString(Math.sin(Double.parseDouble(left)));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case COS:
                if (canParseFloat(left)) {
                    left = Double.toString(Math.cos(Double.parseDouble(left)));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case TAN:
                if (canParseFloat(left)) {
                    left = Double.toString(Math.tan(Double.parseDouble(left)));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case CSC:
                if (canParseFloat(left)) {
                    left = Double.toString(1 / Math.sin(Double.parseDouble(left)));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case SEC:
                if (canParseFloat(left)) {
                    left = Double.toString(1 / Math.cos(Double.parseDouble(left)));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case COT:
                if (canParseFloat(left)) {
                    left = Double.toString(1 / Math.tan(Double.parseDouble(left)));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case BNOT:
                if (canParseInt(left)) {
                    left = Integer.toString(~ Integer.parseInt(left));
                } else {
                    return false;
                }
                operator = Operator.NULL;
                return true;
            case NOT:
                if (left.equals( "FALSE")) {
                    left = "TRUE";
                } else {
                    left = "FALSE";
                }
                operator = Operator.NULL;
                break;
            default:
                return true;
        }
        return false;       // Line exists for compiler to be happy -_-
    }

    public boolean isTrue() {
        /**
         * Returns the accumulator's truthiness. It is false
         * if the evaluation of the accumulator is the following:
         *      - Accumulator is amorphous
         *      - Accumulator has not been evaluated
         *      - Accumulator is empty
         *      - Accumulator has "False" in the left of it
         * 
         * @return          The accumulator's truthiness
         */
        return !isAmorphous && operator != Operator.NULL && !left.isEmpty() && !left.equals("FALSE");
    }

    public void print() {
        /**
         * Prints the item in the accumulator, if it can. If
         * the accumulator is amorphous, the it prints AMORPHOUS.
         * Otherwise, if the accumulator contains an operator, then
         * print UNEVALUATEED. Otherwise, print the item in the
         * left variable.
         */
        if (isAmorphous) {
            System.out.print("AMORPHOUS");
        } else if (operator != Operator.NULL) {
            System.out.print("UNEVALUATED");
        } else {
            System.out.print(left);
        }
    }

    public void clear() {
        /**
         * Clears the accumulator.
         */
        left = "";
        right = "";
        operator = Operator.NULL;
        isAmorphous = false;
    } 

    public void amorphousClear() {
        /**
         * Clears the accumulator, but makes
         * isAmorphous true
         */
        left = "";
        right = "";
        operator = Operator.NULL;
    }
}