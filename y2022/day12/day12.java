package adventofcode.y2022.day12;

import java.io.*;
import java.util.*;

import javax.swing.ToolTipManager;

/**
 * input file is in a format like: 
 * 
 * Sabqponm
 * abcryxxl
 * accszExk
 * acctuvwj
 * abdefghi
 * 
 * you are wandering looking to get a good signal for your phone.
 * 
 * squares marked 'a' are at the lowest elevation
 * 'b' is one higher, 'c' is one higher than 'b' and so forth
 * up to 'z' which is at the highest elevation
 * 
 * S is at the same elevation as 'a'
 * E is at the same elevation as 'z' 
 * 
 * find a path that moves from S to E in as few steps as possible
 * every step you can either move up,down,left,right
 * you can jump down any height
 * but you can only move one elevation level up at a time
 */

public class day12 {
    public static void main(String[] args) {
        try {
            String filename = "sample.txt";

            String input1 = 
            """
                Sabqponm
                abcryxxl
                accszExk
                acctuvwj
                abdefghi""";

            String input2 = 
            """
                abccccccccccccccccccaaaaaaaaacccccccccccccccccccccccccccccccccccccaaaa
                abcccccccccccccccaaaaaaaaaaacccccccccccccccccccccccccccccccccccccaaaaa
                abcaaccaacccccccccaaaaaaaaaacccccccccccccccccccccaaacccccccccccccaaaaa
                abcaaaaaaccccccccaaaaaaaaaaaaacccccccccccccccccccaacccccccccccccaaaaaa
                abcaaaaaacccaaacccccaaaaaaaaaaaccccccccccccccccccaaaccccccccccccccccaa
                abaaaaaaacccaaaaccccaaaaaacaaaacccccccccccaaaacjjjacccccccccccccccccca
                abaaaaaaaaccaaaaccccaaaaaaccccccaccccccccccaajjjjjkkcccccccccccccccccc
                abaaaaaaaaccaaacccccccaaaccccccaaccccccccccajjjjjjkkkaaacccaaaccaccccc
                abccaaacccccccccccccccaaccccaaaaaaaacccccccjjjjoookkkkaacccaaaaaaccccc
                abcccaacccccccccccccccccccccaaaaaaaaccccccjjjjoooookkkkcccccaaaaaccccc
                abcccccccaacccccccccccccccccccaaaacccccccijjjoooooookkkkccaaaaaaaccccc
                abccaaccaaaccccccccccccccccccaaaaacccccciijjooouuuoppkkkkkaaaaaaaacccc
                abccaaaaaaaccccccccccaaaaacccaacaaaccciiiiiooouuuuupppkkklllaaaaaacccc
                abccaaaaaacccccccccccaaaaacccacccaaciiiiiiqooouuuuuupppkllllllacaccccc
                abcccaaaaaaaacccccccaaaaaaccccaacaiiiiiqqqqoouuuxuuupppppplllllccccccc
                abccaaaaaaaaaccaaaccaaaaaaccccaaaaiiiiqqqqqqttuxxxuuuppppppplllccccccc
                abccaaaaaaaacccaaaaaaaaaaacccaaaahiiiqqqttttttuxxxxuuuvvpppplllccccccc
                abcaaaaaaacccaaaaaaaaaaacccccaaaahhhqqqqtttttttxxxxuuvvvvvqqlllccccccc
                abcccccaaaccaaaaaaaaaccccccccacaahhhqqqttttxxxxxxxyyyyyvvvqqlllccccccc
                abcccccaaaccaaaaaaaacccccccccccaahhhqqqtttxxxxxxxyyyyyyvvqqqlllccccccc
                SbcccccccccccaaaaaaaaaccccccccccchhhqqqtttxxxxEzzzyyyyvvvqqqmmlccccccc
                abcccccccccccaaaaaaaacccaacccccccchhhppptttxxxxyyyyyvvvvqqqmmmcccccccc
                abccccccccccaaaaaaaaaaccaacccccccchhhpppptttsxxyyyyyvvvqqqmmmccccccccc
                abcaacccccccaaaaaaacaaaaaaccccccccchhhppppsswwyyyyyyyvvqqmmmmccccccccc
                abaaaacccccccaccaaaccaaaaaaacccccccchhhpppsswwyywwyyyvvqqmmmddcccccccc
                abaaaaccccccccccaaaccaaaaaaacccccccchhhpppsswwwwwwwwwvvqqqmmdddccccccc
                abaaaacccccccccaaaccaaaaaaccccccccccgggpppsswwwwrrwwwwvrqqmmdddccccccc
                abccccccaaaaaccaaaacaaaaaaccccccaacccggpppssswwsrrrwwwvrrqmmdddacccccc
                abccccccaaaaaccaaaacccccaaccccaaaaaacggpppssssssrrrrrrrrrnmmdddaaccccc
                abcccccaaaaaaccaaaccccccccccccaaaaaacggppossssssoorrrrrrrnnmdddacccccc
                abcccccaaaaaaccccccccaaaaccccccaaaaacgggoooossoooonnnrrnnnnmddaaaacccc
                abccccccaaaaaccccccccaaaacccccaaaaaccgggoooooooooonnnnnnnnndddaaaacccc
                abccccccaaaccccccccccaaaacccccaaaaacccgggoooooooffennnnnnnedddaaaacccc
                abcccccccccccccccccccaaacccccccaacccccggggffffffffeeeeeeeeeedaaacccccc
                abccccccccccccccccccaaacccccaccaaccccccggfffffffffeeeeeeeeeecaaacccccc
                abccccccccccccccccccaaaacccaaaaaaaaaccccfffffffaaaaaeeeeeecccccccccccc
                abccccccccaacaaccccaaaaaacaaaaaaaaaaccccccccccaaaccaaaaccccccccccccccc
                abccccccccaaaaacccaaaaaaaaaaacaaaaccccccccccccaaaccccaaccccccccccaaaca
                abcccccccaaaaaccccaaaaaaaaaaacaaaaacccccccccccaaaccccccccccccccccaaaaa
                abcccccccaaaaaacccaaaaaaaaaacaaaaaacccccccccccaaccccccccccccccccccaaaa
                abcccccccccaaaaccaaaaaaaaaaaaaaccaaccccccccccccccccccccccccccccccaaaaa
                    """;

            String[] rows = input2.split("\n");

            // ending position row and column
            Position ePos = new Position(-1, -1);
            Position sPos = new Position(-1, -1);

            // initialize the square info map,
            // and keep track of the starting and ending positions 
            int height = rows.length;
            int width = rows[0].length();
            var squareBoard = new SquareBoard(height, width);

            // part 2: get a list of positions at the lowest elevation
            ArrayList<Position> startingSquares = new ArrayList<Position>();

            for(int y = 0; y < height; y++) {
                String row = rows[y];
                for(int x = 0; x < width; x++) {
                    char letter = row.charAt(x);
                    if(letter == 'S' || letter == 'a') {
                        startingSquares.add(new Position(x,y));
                    }

                    if(letter == 'E') {
                        ePos = new Position(x, y);
                    } else if(letter == 'S') {
                        sPos = new Position(x, y);
                    }
                    squareBoard.set(x, y, new Square(letter));
                }
            }

            /*
             * start at position E, mark it as requiring '0 steps to reach E'
             * add it to the stack with
             * - an 'arrivedFrom' position of null
             * - a step count of 1
             * 
             * while the stack isn't empty, pop position p
             * if the squareBoard has a step count of -1 at position p,
             * or if the squareBoard has a step count that is higher than the current step count of p,
             * then update the squareBoard step count to the traversal step count of p
             * if we updated the squareBoard step count, then check every traversable neighbor which
             * is not equal to the arrivedFrom position, and create a new squaretraversal
             * with a stepcount of p.stepcount+1
             * and add it to the stack
             * 
             * at the end the squareBoard will be filled with the minimum number of steps
             * required for a square to reach E, or -1 if no such traversal exists
             */

            Stack<SquareTraversal> toTraverseStack = new Stack<SquareTraversal>();
            SquareTraversal ePosTraversal = new SquareTraversal(ePos);
            toTraverseStack.push(ePosTraversal);

            while(!toTraverseStack.empty()) {
                SquareTraversal sqTrav = toTraverseStack.pop();
                Position sqPos = sqTrav.position;

                if(!squareBoard.hasTraversed(sqPos) || squareBoard.getCount(sqPos) > sqTrav.stepCount) {
                    squareBoard.setCount(sqPos, sqTrav.stepCount);
                } else {
                    // we found a traversal which is worse than the currently marked traversal, do not
                    // consider the neighbors because it will most likely be worse too
                    continue;
                }

                // get a list of traversable neighbours
                ArrayList<Position> sqNeighbors = getTraversableNeighbors(squareBoard, sqTrav.position);
                for(Position sqNeighbor : sqNeighbors) {
                    // if we did not arrive from this neighbor, then add a new square traversal,
                    // and with 1 + the current step count
                    if(!sqTrav.isArrivedFrom(sqNeighbor) ) {
                        SquareTraversal neighborTrav = new SquareTraversal(sqNeighbor, sqPos, sqTrav.stepCount+1);
                        toTraverseStack.push(neighborTrav);
                    }
                }
            }

            System.out.println("heights:");
            squareBoard.printHeights();
            System.out.println("\nStep Counts:");
            squareBoard.printCounts();

            // for part 2, we need the minimum traversals for all possible starting squares
            // (marked 'S' or 'a')
            System.out.println("Starting Square Traversals:");
            int min = -1;
            for(Position startingSquare : startingSquares) {
                int stepCount = squareBoard.get(startingSquare).stepCount;
                if(stepCount != -1) {
                    System.out.println(stepCount);
                    if(min == -1 || stepCount < min) {
                        min = stepCount;
                    }
                }
            }

            System.out.printf("Part 1 Result: %d\n", squareBoard.get(sPos).stepCount);
            System.out.printf("Part 2 Result: %d\n", min);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // returns a list of neighbor positions of a given square
    private static ArrayList<Position> getNeighbors(SquareBoard SquareBoard, Position p) {
        // go up, down, left, right if these positions are in the board height and width
        var result = new ArrayList<Position>();
        if(p.y-1 >= 0) {
            result.add(new Position(p.x, p.y-1));
        }
        if(p.y+1 < SquareBoard.height) {
            result.add(new Position(p.x, p.y+1));
        }
        if(p.x-1 >= 0) {
            result.add(new Position(p.x-1, p.y));
        }
        if(p.x+1 < SquareBoard.width) {
            result.add(new Position(p.x+1, p.y));
        }
        
        return result;
    }

    // returns a list of neighbors that can traverse to position p
    private static ArrayList<Position> getTraversableNeighbors(SquareBoard SquareBoard, Position p) {
        ArrayList<Position> neighbors = getNeighbors(SquareBoard, p);
        ArrayList<Position> result = new ArrayList<Position>();
        for(Position neighbor : neighbors) {
            if(SquareBoard.canStep(neighbor, p)) {
                result.add(neighbor);
            }
        }
        return result;
    }
}

class Position {
    public int x;
    public int y;
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public String toString() {
        return "(%d, %d)".formatted(this.x, this.y);
    }
    public void println() {
        System.out.printf("%s\n", this.toString());
    }
    public boolean equals(Position other) {
        return this.x == other.x && this.y == other.y;
    }
}

/** keeps a 2D board of squares */
class SquareBoard {
    public int height;
    public int width;

    private Square[][] board;

    SquareBoard(int height, int width) {
        this.height = height;
        this.width = width;
        board = new Square[height][width];
    }

    // initialize one Square
    public void set(int x, int y, Square val) {
        board[y][x] = val;
    }

    public void set(Position p, Square val) {
        this.set(p.x,p.y,val);
    }

    public Square get(int x, int y) {
        return board[y][x];
    }

    public Square get(Position p) {
        return this.get(p.x, p.y);
    }

    // update the stepcount of a square
    public void setCount(Position p, int stepCount) {
        this.get(p).stepCount = stepCount;
    }

    public int getCount(Position p) {
        return this.get(p).stepCount;
    }

    // returns true if we have a stepcount marked on a given square
    public boolean hasTraversed(Position p) {
        return this.get(p).stepCount != -1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(height*width);
        for(Square[] row : board) {
            for(Square sq: row) {
                sb.append(sq.letter);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // prints the map
    public void printMap() {
        System.out.printf("%s\n", this.toString());
    }
    
    // prints the step counts
    public void printCounts() {
        for(Square[] row : board) {
            for(Square sq: row) {
                if(sq.letter == 'S' || sq.letter == 'E') {
                    System.out.printf("  %c ", sq.letter);
                } else if(sq.stepCount == -1) {
                    System.out.printf("  . ", sq.stepCount);
                } else {
                    System.out.printf("%3d ", sq.stepCount);
                }
            }
            System.out.printf("\n");
        }
    }

    // print a height map in ints
    public void printHeights() {
        for(Square[] row : board) {
            for(Square sq: row) {
                System.out.printf("%2d ", sq.getHeight());
            }
            System.out.printf("\n");
        }
    }

    // returns true if we can step from 'start' to 'end'
    // (we can go up only one, or down as far as we need to)
    public boolean canStep(Position start, Position end) {
        int startHeight = this.get(start).getHeight();
        int endHeight = this.get(end).getHeight();
        return endHeight <= startHeight+1;
    }
}

/** contains search information for a given square */
class Square {
     // if we can reach the end, how many steps does it take?
    // -1 if we have no such path yet
    public int stepCount = -1;
    // the character symbol at of the square
    public char letter;
    // returns true if we can step from a given position
    Square(char letter) {
        this.letter = letter;
    }

    // converts letter code to height value
    public int getHeight() {
        switch (letter) {
            case 'S':
                return 0;
            case 'E':
                return 25;
            default:
                return letter - 'a';
        }
    }
}

class SquareTraversal {
    // current position
    Position position;
    // position we came from
    Position arrivedFrom;
    // the number of steps it took to arrive at this square
    int stepCount;
    
    // ending square position, we did not take any steps to arrive here
    SquareTraversal(Position position) {
        this.position = position;
        this.arrivedFrom = null;
        this.stepCount = 0;
    }

    SquareTraversal(Position position, Position arrivedFrom, int stepCount) {
        this.position = position;
        this.arrivedFrom = null;
        this.stepCount = stepCount;
    }
    
    // returns true if we arrived from given position p
    public boolean isArrivedFrom(Position p) {
        return this.arrivedFrom != null && p.equals(this.arrivedFrom);
    }
}
