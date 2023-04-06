package adventofcode.day5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.plaf.InsetsUIResource;

public class day5 {
    public static void main(String[] args) {
        try {
            var reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();

            // get the contents of the stack, which are the first few lines of the file
            // (until a newline)
            var stackLines = new Stack<String>();
            while (line.length() != 0) {
                var sb = new StringBuilder();
                for(int i = 1; i < line.length(); i+=4) {
                    sb.append(line.charAt(i));
                }
                var parsedLine = sb.toString();
                stackLines.push(parsedLine);

                line = reader.readLine();
            }

            // get the stack IDs
            String stackNums = stackLines.pop();

            // fill the Stacks class
            var stacks = new Stacks(stackNums.length());
            while(!stackLines.empty()) {
                String oneRow = stackLines.pop();
                for(int i = 0; i < oneRow.length(); i++) {
                    char theItem = oneRow.charAt(i);
                    int stackNum = Integer.parseInt("" + stackNums.charAt(i));
                    if(theItem != ' ') {
                        stacks.add(stackNum, theItem);
                    }
                }
            }
            stacks.print();

            // parse the movement commands
            while (line != null) {
                if(line.length() == 0) {
                    line = reader.readLine();
                    continue;
                }

                var splitted = line.split(" ");
                if(splitted.length != 6) {
                    throw new Exception(String.format("Unrecognized command: %s", line));
                }
                int amount = Integer.parseInt(splitted[1]);
                int fromID = Integer.parseInt(splitted[3]);
                int toID = Integer.parseInt(splitted[5]);
                stacks.move2(amount, fromID, toID);

                line = reader.readLine();
            }

            stacks.print();
            stacks.printTop();

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// an arrangement of stacks
class Stacks {
    ArrayList<Stack<Character>> inside;

    Stacks(int numStacks) {
        inside = new ArrayList<Stack<Character>>(numStacks);
        for(int i = 0; i < numStacks; i++) {
            inside.add(i, new Stack<Character>());
        }
    }

    // CrateMover9000
    public boolean move(int amount, int from, int to) {
        var fromStack = inside.get(from-1);
        var toStack = inside.get(to-1);
        if(amount > fromStack.size()) {
            return false; // too many items requested
        }
        for(int i = 0; i < amount; i++) {
            var item = fromStack.pop();
            toStack.push(item);
        }
        return true;
    }

    // CrateMover9001
    public boolean move2(int amount, int from, int to) {
        var fromStack = inside.get(from-1);
        var toStack = inside.get(to-1);
        var tmpStack = new Stack<Character>();
        if(amount > fromStack.size()) {
            return false; // too many items requested
        }
        for(int i = 0; i < amount; i++) {
            var item = fromStack.pop();
            tmpStack.push(item);
        }
        while(!tmpStack.isEmpty()) {
            var item = tmpStack.pop();
            toStack.push(item);
        }
        return true;
    }

    public void printSizes() {
        for(int i = 0; i < inside.size(); i++) {
            var thisStack = inside.get(i);
            int stackNum = i+1;
            System.out.printf("stack %d has %d items\n", stackNum, thisStack.size());
        }
    }

    public void print() {
        int maxRow = 0;
        for(var stack : inside) {
            if(stack.size() > maxRow) {
                maxRow = stack.size();
            }
        }

        for(int row = maxRow - 1; row >= 0; row--) {
            for(var stack : inside) {
                if(stack.size() > row) {
                    System.out.printf("[%c] ", stack.get(row));
                } else {
                    System.out.printf("    ");
                }
            }
            System.out.printf("\n");
        }
        
        for(int i = 1; i < inside.size()+1; i++) {
            System.out.printf(" %d  ", i);
        }
        System.out.printf("\n");
    }

    public void add(int stackNum, char item) {
        inside.get(stackNum-1).push(item);
    }

    public void printTop() {
        for(var stack : inside) {
            System.out.printf("%c", stack.peek());
        }
        System.out.printf("\n");
    }
}