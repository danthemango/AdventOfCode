package adventofcode.day2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class day2 {
    public static void main(String[] args) {
        BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader("input.txt"));
			String line = reader.readLine();

            int totalScore = 0;
			while (line != null) {
                if(line.length() == 0) {
                    continue;
                }

                String[] splitted = line.split("\\s+");
                assert splitted.length == 2;
                assert splitted[0].length() == 1;
                assert splitted[1].length() == 1;

                String opn = getShapeName(splitted[0]);
                Shape opc = getCode(splitted[0]);
                String myn = getShapeName(splitted[1]);
                Shape myc = getCode(splitted[1]);
                String result = getOutcomeName(opc, myc);

                int resultScore = getResultScore(opc, myc);
                totalScore += resultScore;
                System.out.printf("%s v. %s - %s - result %d - total %d\n", opn, myn, result, resultScore, totalScore);

				line = reader.readLine();
			}
            System.out.printf("total score: %d\n", totalScore);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
        }
    }

    public static String getShapeName(String s) throws Exception {
        if(s.equals("A") || s.equals("X")) {
            return "rock";
        } else if (s.equals("B") || s.equals("Y")) {
            return "paper";
        } else if (s.equals("C") || s.equals("Z")) {
            return "scissors";
        }

        String exs = String.format("shape '%s' not recognized", s);
        throw new Exception(exs);
    }

    public static String getShapeNameFromCode(Shape sc) throws Exception {
        if (sc == Shape.ROCK) {
            return "rock";
        } else if (sc == Shape.PAPER) {
            return "paper";
        } else if (sc == Shape.SCISSORS) {
            return "scissors";
        }

        String exs = String.format("shape not ecognized");
        throw new Exception(exs);
    }

    enum Shape {
        ROCK,
        PAPER,
        SCISSORS
    }

    // translates shape letter-code to int-code
    // rock, paper, scissors = 1,2,3
    public static Shape getCode(String s) throws Exception {
        if(s.equals("A") || s.equals("X")) {
            return Shape.ROCK;
        } else if (s.equals("B") || s.equals("Y")) {
            return Shape.PAPER;
        } else if (s.equals("C") || s.equals("Z")) {
            return Shape.SCISSORS;
        }

        String exs = String.format("shape '%s' not recognized", s);
        throw new Exception(exs);
    }

    // win condition
    enum WinCond {
        WIN,
        LOSE,
        DRAW
    }

    public static WinCond getWinCond(Shape opShapeCode, Shape myShapeCode) throws Exception {
        if(opShapeCode == Shape.ROCK) {
            if (myShapeCode == Shape.ROCK) {
                return WinCond.DRAW;
            } else if (myShapeCode == Shape.PAPER) {
                return WinCond.WIN;
            } else if (myShapeCode == Shape.SCISSORS) {
                return WinCond.LOSE;
            }
        } else if (opShapeCode == Shape.PAPER) {
            if (myShapeCode == Shape.ROCK) {
                return WinCond.LOSE;
            } else if (myShapeCode == Shape.PAPER) {
                return WinCond.DRAW;
            } else if (myShapeCode == Shape.SCISSORS) {
                return WinCond.WIN;
            }
        } else if (opShapeCode == Shape.SCISSORS) {
            if (myShapeCode == Shape.ROCK) {
                return WinCond.WIN;
            } else if (myShapeCode == Shape.PAPER) {
                return WinCond.LOSE;
            } else if (myShapeCode == Shape.SCISSORS) {
                return WinCond.DRAW;
            }
        }

        String exs = String.format("shape not recognized");
        throw new Exception(exs);
    }
    
    public static int getOutcomeScore(WinCond w) throws Exception {
        if(w == WinCond.WIN) {
            return 6;
        } else if (w == WinCond.DRAW) {
            return 3;
        } else if (w == WinCond.LOSE) {
            return 0;
        }

        throw new Exception("WinCond not recognized");
    }

    public static String getOutcomeName(Shape op, Shape my) throws Exception {
        WinCond w = getWinCond(op, my);

        if(w == WinCond.WIN) {
            return "win";
        } else if (w == WinCond.DRAW) {
            return "draw";
        } else if (w == WinCond.LOSE) {
            return "lose";
        }

        throw new Exception("WinCond not recognized");
    }

    public static int getShapeScore(Shape myShape) throws Exception {
        if (myShape == Shape.ROCK) {
            return 1;
        } else if (myShape == Shape.PAPER) {
            return 2;
        } else if (myShape == Shape.SCISSORS) {
            return 3;
        }

        throw new Exception("Shape not recognized");
    }

    // returns the shape score plus the outcome score
    public static int getResultScore(Shape opc, Shape myc) throws Exception {
        WinCond w = getWinCond(opc, myc);
        int shapeScore = getShapeScore(myc);
        int outcomeScore = getOutcomeScore(w);
        return shapeScore + outcomeScore;
    }
}
