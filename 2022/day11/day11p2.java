package adventofcode.day11;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.nio.file.*;

public class day11p2 {
    public static void main(final String[] args) {

        try {
            var filepath = "input1.txt";
            var instructions = Instruction.getInstructions(filepath);
            var monkeys = new ArrayList<Monkey>();
            for(Instruction instruction: instructions) {
                monkeys.add(new Monkey(instruction));
            }

            long maxMod = 1;
            for(var monkey : monkeys) {
                maxMod *= monkey.divBy;
            }
            System.out.printf("maxMoxd = %d\n", maxMod);

            int numRounds = 10000;
            for(int roundIdx = 1; roundIdx <= numRounds; roundIdx++) {

                // System.out.printf("Round = %d\n", i+1);
                for(int monkeyIdx = 0; monkeyIdx < monkeys.size(); monkeyIdx++) {
                    // System.out.printf("Monkey %d\n", monkeyIdx);
                    var monkey = monkeys.get(monkeyIdx);

                    while(monkey.careValues.size() != 0) {
                        monkey.timesInspected++;
                        long careValue = monkey.careValues.remove(0);
                        long worryLevel1 = careValue % maxMod;
                        // System.out.printf("  Monkey inspects an item with a worry level of %d\n", worryLevel1);
                        long worryLevel2 = Operation.runOperation(monkey.operation, worryLevel1);
                        // System.out.printf("%s -> %d\n", monkey.operation, worryLevel2);
                        long worryLevel3 = worryLevel2 % maxMod;
                        if(worryLevel3 < 0) {
                            System.out.printf("round = %d worrylevel = %d %d %d maxMod = %d %s\n", roundIdx, worryLevel1, worryLevel2, worryLevel3, maxMod, monkey.operation);
                            throw new Exception("worrylevel too low");
                        }
                        // System.out.printf("    Monkey gets bored with item. Worry level is divided by 3 to %d.\n", worryLevel3);
                        int throwToIdx;
                        if(worryLevel3 % monkey.divBy == 0) {
                            // System.out.printf("    Current worry level is not divisible by %d.\n", monkey.divBy);
                            throwToIdx = monkey.trueIdx;
                        } else {
                            // System.out.printf("    Current worry level is divisible by %d.\n", monkey.divBy);
                            throwToIdx = monkey.falseIdx;
                        }
                        // System.out.printf("    Item with worry level %d is thrown to monkey %d.\n", worryLevel3, throwToIdx);
                        monkeys.get(throwToIdx).careValues.add(worryLevel3);
                    }
                }

                // for(int monkeyIdx = 0; monkeyIdx < monkeys.size(); monkeyIdx++) {
                //     System.out.printf("Monkey %d: ", monkeyIdx);
                //     var monkey = monkeys.get(monkeyIdx);
                //     for(var careValue : monkey.careValues) {
                //         System.out.printf("%d, ", careValue);
                //     }
                //     System.out.println();
                // }
                // System.out.println();

                if(roundIdx == 1 || roundIdx == 20 || roundIdx % 1000 == 0) {
                    System.out.printf("== After round %d ==\n", roundIdx);
                    for(int monkeyIdx = 0; monkeyIdx < monkeys.size(); monkeyIdx++) {
                        var monkey = monkeys.get(monkeyIdx);
                        System.out.printf("Monkey %d inspected items %d times.\n", monkeyIdx, monkey.timesInspected);
                    }
                    System.out.println();
                }
            }

            int max1 = 0;
            int max2 = 0;
            for(int monkeyIdx = 0; monkeyIdx < monkeys.size(); monkeyIdx++) {
                var monkey = monkeys.get(monkeyIdx);
                if(monkey.timesInspected > max1) {
                    max2 = max1;
                    max1 = monkey.timesInspected;
                }
            }
            // TODO: these max values are incorrect, fix later
            System.out.printf("%d * %d = %d\n", max1, max2, max1 * max2);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class Monkey {
    // ... has a bag of items, each with a careValue
    // that corresponds to how much we care about the item.
    public ArrayList<Long> careValues;
    // As the monkey inspects an item, we run an operation on the careValue
    // which tells us how much we care about the item after inspection.
    public String operation;
    // if the careAbout value is divisible by divBy ...
    public long divBy;
    // ... then throw to the monkey idx specified by trueIdx ...
    public int trueIdx;
    // ... else throw to monkey idx specified by falseIdx.
    public int falseIdx;
    // the values that the monkey caught during the round
    public int timesInspected;

    Monkey(Instruction ins) {
        this.careValues = ins.careValues;
        this.operation = ins.operation;
        this.divBy = ins.divBy;
        this.trueIdx = ins.trueIdx;
        this.falseIdx = ins.falseIdx;
        this.timesInspected = 0;

    }
}

class Instruction {
    // the current monkey idx (index) ...
    public int monkeyIdx;
    // ... has a bag of items, each with a careValue
    // that corresponds to how much we care about the item.
    public ArrayList<Long> careValues;
    // As the monkey inspects an item, we run an operation on the careValue
    // which tells us how much we care about the item after inspection.
    public String operation;
    // if the careAbout value is divisible by divBy ...
    public long divBy;
    // ... then throw to the monkey idx specified by trueIdx ...
    public int trueIdx;
    // ... else throw to monkey idx specified by falseIdx.
    public int falseIdx;

    Instruction(
        int monkeyIdx,
        ArrayList<Long> careValues,
        String operation,
        long divBy,
        int trueIdx,
        int falseIdx
    ) {
        this.monkeyIdx = monkeyIdx;
        this.careValues = careValues;
        this.operation = operation;
        this.divBy = divBy;
        this.trueIdx = trueIdx;
        this.falseIdx = falseIdx;
    }

    public String toString() {
        String careValuesString = "";
        for(long careValue : careValues) {
            careValuesString += careValue;
            careValuesString += ",";
        }

        return """
            monkeyIdx = %d
            careValues = %s
            operation = %s
            divBy = %d
            trueIdx = %d
            falseIdx = %d
        """.formatted(
            this.monkeyIdx,
            careValuesString,
            this.operation,
            this.divBy,
            this.trueIdx,
            this.falseIdx
        );
    }

    /**
     * returns a list of instructions from the contents of the filepath.
     * The file is assumed to be of a specified format, with each instruction
     * specified in the form of:
     * 
     * Monkey 1:
     *   Starting items: 54, 65, 75, 74
     *   Operation: new = old + 6
     *   Test: divisible by 19
     *     If true: throw to monkey 2
     *     If false: throw to monkey 0
     * 
     * Monkey 2:
     *   Starting items: 79, 60, 97
     *   Operation: new = old * old
     *   Test: divisible by 13
     *     If true: throw to monkey 1
     *     If false: throw to monkey 
     * 
     * ... and so on, with each instruction being separated by an empty newline
     */
    public static ArrayList<Instruction> getInstructions(final String filePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // create the list of instructions
            var instructions = new ArrayList<Instruction>();

            /*
             * fetch the instruction by expected in the following order:
             * monkey idx
             * starting items
             * operation
             * test divided by
             * if true monkey idx
             * if false monkey idx
             */

            String line;
            while ((line = br.readLine()) != null) {
                if(line.isBlank()) {
                    continue;
                }

                int monkeyIdx = getMonkeyIdx(line);

                if((line = br.readLine()) == null) {
                    throw new Exception("expected starting items");
                } else if(line.isBlank()) {
                    continue;
                }
                ArrayList<Long> careValues = getCareValues(line);

                if((line = br.readLine()) == null) {
                    throw new Exception("expected operation");
                } else if(line.isBlank()) {
                    continue;
                }

                String operation = getOperation(line);

                if((line = br.readLine()) == null) {
                    throw new Exception("expected divided by");
                } else if(line.isBlank()) {
                    continue;
                }
                long divBy = getDivBy(line);

                if((line = br.readLine()) == null) {
                    throw new Exception("if true monkey idx");
                } else if(line.isBlank()) {
                    continue;
                }
                int trueIdx = getTrueIdx(line);

                if((line = br.readLine()) == null) {
                    throw new Exception("if false monkey idx");
                } else if(line.isBlank()) {
                    continue;
                }
                int falseIdx = getFalseIdx(line);

                var instruction = new Instruction(
                    monkeyIdx,
                    careValues,
                    operation,
                    divBy,
                    trueIdx,
                    falseIdx
                );

                System.out.printf("Instruction = \n%s\n\n", instruction.toString());

                instructions.add(instruction);
            }

            return instructions;
        } catch (Exception ex) {
            throw ex;
        }
    }

    // from instruction pattern string returns monkey index
    public static int getMonkeyIdx(String ins) throws Exception {
        Pattern pattern = Pattern.compile("^\\s*Monkey\\s*(\\d+):\\s*$");
        Matcher m = pattern.matcher(ins);
        if(m.find()) {
            return Integer.parseInt(m.group(1));
        } else {
            throw new Exception("getMonkeyIdx, none found");
        }
    }

    // from instruction pattern string array of starting items
    public static ArrayList<Long> getCareValues(String ins) throws Exception {
        Pattern pattern = Pattern.compile("^\\s*Starting items:\\s*([\\d, ]+)+\\s*$");
        Matcher m = pattern.matcher(ins);
        if(m.find()) {
            String str = m.group(1);

            // fetch integer values split on space or comma
            var contentParts = str.split("[\\s,]");
            var nums = new ArrayList<Long>();
            for (var contentPart : contentParts) {
                if (!contentPart.isBlank()) {
                    long num = Long.parseLong(contentPart);
                    nums.add(num);
                }
            }
            return nums;
        } else {
            throw new Exception("getCareValues, none found");
        }
    }

    // from instruction pattern string returns operation string
    public static String getOperation(String ins) throws Exception {
        Pattern pattern = Pattern.compile("^\\s*Operation: new = (.*)$");
        Matcher m = pattern.matcher(ins);
        if(m.find()) {
            return m.group(1);
        } else {
            throw new Exception("getOperation, none found");
        }
    }

    // from instruction pattern string returns divBy integer
    public static long getDivBy(String ins) throws Exception {
        Pattern pattern = Pattern.compile("^\\s*Test: divisible by (\\d*)\\s*$");
        Matcher m = pattern.matcher(ins);
        if(m.find()) {
            long divBy = Long.parseLong(m.group(1));
            return divBy;
        } else {
            throw new Exception("getDivBy, none found");
        }
    }

    // from instruction pattern string returns the trueIdx
    public static int getTrueIdx(String ins) throws Exception {
        Pattern pattern = Pattern.compile("^\\s*If true: throw to monkey\\s*(\\d+)\\s*$");
        Matcher m = pattern.matcher(ins);
        if(m.find()) {
            return Integer.parseInt(m.group(1));
        } else {
            throw new Exception("getTrueIdx, none found");
        }
    }

    // from instruction pattern string returns the falseIdx
    public static int getFalseIdx(String ins) throws Exception {
        Pattern pattern = Pattern.compile("^\\s*If false: throw to monkey\\s*(\\d+)\\s*$");
        Matcher m = pattern.matcher(ins);
        if(m.find()) {
            return Integer.parseInt(m.group(1));
        } else {
            throw new Exception("getFalseIdx, none found");
        }
    }
}

/**
 * an operation is specified in the form of a string
 * which is in the format of "<val> (<op> <val>)*"
 * where new is an integer that is returned,
 * <val> is either an integer or the string "old" which is inputted
 * and op is an operation, like -,+,*,/
 */
class Operation {
    // returns a new value from operation string
    static long runOperation(String opString, long oldVal) throws Exception {
        var contentParts = opString.split(" ");
        if(contentParts.length != 3) {
            throw new Exception("\"%s\" is an invalidly formatted opstring".formatted(opString));
        }

        long beforeVal;
        if(contentParts[0].equals("old")) {
            beforeVal = oldVal;
        } else {
            beforeVal = Long.parseLong(contentParts[0]);
        }

        char op;
        if(contentParts[1].length() != 1) {
            throw new Exception("operation %s not recognized".formatted(contentParts[1]));
        } else {
            op = contentParts[1].charAt(0);
        }

        long afterVal;
        if(contentParts[2].equals("old")) {
            afterVal = oldVal;
        } else {
            afterVal = Long.parseLong(contentParts[2]);
        }

        if(op == '+') {
            return beforeVal + afterVal;
        } else if(op == '-') {
            return beforeVal - afterVal;
        } else if(op == '*') {
            return beforeVal * afterVal;
        } else if(op == '/') {
            return beforeVal / afterVal;
        } else {
            throw new Exception("op %c not recognized".formatted(op));
        }
    }
}
