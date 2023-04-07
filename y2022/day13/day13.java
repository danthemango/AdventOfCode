package adventofcode.y2022.day12;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day13 {
    public static void main(String[] args) {
        try {
            // use filename from args, else use stdin
            InputStream input = System.in;
            if(args.length != 0) {
                if(args[0].equals("--help")) {
                    printUsage();
                    return;
                } else {
                    String fileName = args[0];
                    File inputFile = new File(fileName);
                    input = new FileInputStream(inputFile);
                }
            }

            int indexSum = part1(input);
            System.out.printf("part 1 index sum: %d\n", indexSum);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void printUsage() {
        System.out.printf("Usage: java day13.java inputfile.txt");
    }

    public static final String sampleInput = """
            [1,1,3,1,1]
            [1,1,5,1,1]
            
            [[1],[2,3,4]]
            [[1],4]
            
            [9]
            [[8,7,6]]
            
            [[4,4],4,4]
            [[4,4],4,4,4]
            
            [7,7,7,7]
            [7,7,7]
            
            []
            [3]
            
            [[[]]]
            [[]]
            
            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9] 
            """;

    // just a test of value creation
    public static void test1() throws Exception {
        new Value("51464").print();
        new Value("[5]").print();
        new Value("[5,6]").print();
        new Value("[[5]]").print();
        new Value("[[5],6]").print();
        new Value("[[5],[6,7]]").print();
        new Value("[[[5]],[6,7]]").print();
        new Value("[5,[]]").print();
        new Value("[]").print();
    }

    // a test of orderings
    public static void test2(String input) throws Exception {
        int i = 0;
        String[] lines = input.split("\n");
        while(i < lines.length) {
            
            Value left = new Value(lines[i++]);
            Value right = new Value(lines[i++]);
            System.out.printf("%s < %s ? %s \n", left.toString(), right.toString(), left.compareTo(right) > 0 ? "not right" : "right order");
            if(i < lines.length) {
                if(!lines[i].equals("")) {
                    throw new Exception("unexpected content between signals");
                }
                i++;
            }
        }
    }

    // returns the sum of all indices (starting with 1) of all values that are in the correct order
    public static int part1(InputStream input) throws Exception {
        Scanner sc = new Scanner(input);

        int sum = 0;
        int index = 1;
        String line;
        while(sc.hasNextLine()) {
            line = sc.nextLine();
            Value left = new Value(line);
            line = sc.nextLine();
            Value right = new Value(line);
            System.out.printf("%s%d %s < %s ? %s \n", left.compareTo(right) < 0 ? "-" : " ", index, left.toString(), right.toString(), left.compareTo(right) > 0 ? "not right" : "right order");
            if(left.compareTo(right) < 0) {
                sum += index;
            }
            if(sc.hasNextLine()) {
                line = sc.nextLine();
                if(!line.isBlank()) {
                    throw new Exception("unexpected input between signals");
                }
            }
            index++;
        }
        return sum;
    }

    public static void visSpaces(String in) {
        in.replace(' ', '.');
        System.out.println(in);
    }
}

// either an int or an ArrayList of Value's
class Value {
    // note: only one may be filled at a time
    public Integer intVal = null;
    public ArrayList<Value> arrVal = null;

    /**
     * in string may be structured as just an int
     * or
     * as an array of ints
     * e.g.: 
     * - []
     * - [int]
     * - [int, int]
     * - [[]]
     * - [[int,int]]
     */
    Value(String in) throws Exception {
        in = in.trim();
        // either we have an integer, or an array of values
        if(in.length() == 0) {
            throw new Exception("Nothing to convert to Value");
        } else if(in.charAt(0) != '[') {
            // expect int
            intVal = Integer.parseInt(in);
            return;
        } else {
            /*
             * expect arr of values,
             * split on commas (',') that are found in the current order of nesting
             * and transform each value to Value object
             * 
             * note: we go up in the 'order of nesting' for each open bracket '[' found
             * and down for each close bracket ']'
             */

             // throw an exception if there is no close brace at the end of input
             final int closeBracketPos = in.length()-1;
             if(in.charAt(closeBracketPos) != ']') {
                throw new Exception("missing close bracket ']' at end of input");
             }

            int nestingOrder = 0;
            int i = 1;
            StringBuffer sb = new StringBuffer();
            arrVal = new ArrayList<Value>();
            while(i != in.length()) {
                char c = in.charAt(i);
                if(nestingOrder == 0) {
                    // we are in the current array
                    if(Character.isDigit(c)) {
                        sb.append(c);
                    } else if(c == '[') {
                        sb.append(c);
                        nestingOrder++;
                    } else if (c == ']') {
                        if(i != in.length()-1) {
                            throw new Exception("unexpected extraneous chars found after pos %d".formatted(i));
                        }
                        if(sb.length() > 0) {
                            arrVal.add(new Value(sb.toString()));
                        }
                        sb = null;
                        break;
                    } else if(c == ',') {
                        if(sb.length() == 0) {
                            throw new Exception("missing value content after comma");
                        }
                        arrVal.add(new Value(sb.toString()));
                        sb = new StringBuffer();
                    } else {
                        throw new Exception("unexpected char %c found at pos %d".formatted(c, i));
                    }
                } else {
                    // we are in a nested array
                    sb.append(c);
                    if(c == '[') {
                        nestingOrder++;
                    } else if(c == ']') {
                        nestingOrder--;
                    }
                }
                i++;
            }

            if(sb != null) {
                throw new Exception("Parsed input but did not fing closing brace");
            }
        }
    }

    public String toString() {
        if(intVal != null) {
            return "%d".formatted(intVal);
        } else if(arrVal != null) {
            StringBuffer sb = new StringBuffer();
            sb.append('[');
            boolean first = true;
            for(Value val : arrVal) {
                if(first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append(val.toString());
            }
            sb.append(']');
            return sb.toString();
        } else {
            return "";
        }
    }
    
    public void print() throws Exception {
        System.out.println(this.toString());
    }

    /**
     * returns the ordering of this value and other value
     * -1 == first
     * 0 == same
     * 1 == last
     */
    public int compareTo(Value other) throws Exception {
        if(this.intVal != null && other.intVal != null) {
            // compare two ints
            return this.intVal.compareTo(other.intVal);
        } else if(this.intVal != null && other.arrVal != null) {
            // compare int and array
            Value newThis = new Value("[%d]".formatted(this.intVal));
            return newThis.compareTo(other);
        } else if(other.intVal != null && this.arrVal != null) {
            // compare array and int
            Value newOther = new Value("[%d]".formatted(other.intVal));
            return this.compareTo(newOther);
        } else if(this.arrVal != null && other.arrVal != null) {
            // compare two arrays
            int i = 0;
            while(i < this.arrVal.size() && i < other.arrVal.size()) {
                int compVal = this.arrVal.get(i).compareTo(other.arrVal.get(i));
                if(compVal != 0) {
                    return compVal;
                }
                i++;
            }
            // if all of the values are the same but one array is shorter, it is first
            return this.size() - other.size();
        } else {
            throw new Exception("cannot compare \"%s\" and \"%s\"".formatted(this.toString(), other.toString()));
        }
    }

    /**
     * returns the value at index if it is an array
    */
    public Value get(int index) throws Exception {
        if(this.intVal != null) {
            throw new Exception("cannot get int value");
        }
        return this.arrVal.get(index);
    }

    /** returns the size of array */
    public int size() throws Exception {
        if(this.intVal != null) {
            throw new Exception("cannot get int size");
        }
        return this.arrVal.size();
    }
}