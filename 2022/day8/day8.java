package adventofcode.day8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

class Grid {
    int numRows;
    int numCols;
    private ArrayList<ArrayList<Integer>> grid;
    Grid() {
        grid = new ArrayList<ArrayList<Integer>>();
        numRows = 0;
        numCols = 0;
    }
    // adds a new row to the grid
    public void newRow() {
        var newRow = new ArrayList<Integer>();
        grid.add(newRow);
        numRows++;
    }
    public void addCol(int val) throws Exception {
        if(numRows == 0) {
            throw new Exception("add a row before adding a column");
        }
        var curRow = grid.get(numRows-1);
        if(0 < numCols && numCols < curRow.size()) {
            throw new Exception("row of invalid length found");
        }
        curRow.add(val);
        if(curRow.size() > numCols) {
            numCols = curRow.size();
        }
    }
    public int get(int r, int c) {
        return grid.get(r).get(c);
    }
    public void print() {
        for (var row : grid) {
            for (var col : row) {
                System.out.printf("%d", col);
            }
            System.out.printf("\n");
        }
    }
}

// a grid to keep track of the number of visible trees are found
class VisiGrid {
    int numRows;
    int numCols;
    ArrayList<ArrayList<Boolean>> visiGrid;
    VisiGrid(int _numRows, int _numCols) {
        this.numRows = _numRows;
        this.numCols = _numCols;
        this.visiGrid = new ArrayList<ArrayList<Boolean>>(_numRows);
        for (int r = 0; r < _numRows; r++) {
            var visiRow = new ArrayList<Boolean>(_numCols);
            visiGrid.add(visiRow);
            for (int c = 0; c < _numCols; c++) {
                visiRow.add(false);
            }
        }
    }
    public void set(int r, int c) {
        this.visiGrid.get(r).set(c, true);
    }
    public int getCount() {
        int count = 0;
        for(var row : visiGrid) {
            for(var col : row) {
                if(col) {
                    count++;
                }
            }
        }
        return count;
    }
    public void print() {
        for (var row : visiGrid) {
            for (var col : row) {
                int val = col ? 1 : 0;
                System.out.printf("%d", val);
            }
            System.out.printf("\n");
        }
    }
}

public class day8 {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                throw new Exception("could not find input file");
            }
            String inFile = args[0];
            var reader = new BufferedReader(new FileReader(inFile));
            String line = reader.readLine();
            var grid = new Grid();
            while (line != null) {
                grid.newRow();
                for (int i = 0; i < line.length(); i++) {
                    String c = line.substring(i, i + 1);
                    int val = Integer.parseInt(c);
                    grid.addCol(val);
                }

                line = reader.readLine();
            }

            printTreeScores(grid);

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getTreeScore(Grid grid, int rChoice, int cChoice) {
        int choiceHeight = grid.get(rChoice, cChoice);
        int upCount = 0;
        int score = 1;
        // looking up
        for (int r = rChoice - 1; r >= 0; r--) {
            int curHeight = grid.get(r, cChoice);
            upCount++;
            if (curHeight >= choiceHeight) {
                break;
            }
        }
        score *= upCount;
        // looking down
        int downCount = 0;
        for (int r = rChoice + 1; r < grid.numRows; r++) {
            int curHeight = grid.get(r, cChoice);
            downCount++;
            if (curHeight >= choiceHeight) {
                break;
            }
        }
        score *= downCount;
        // looking left
        int leftCount = 0;
        for (int c = cChoice - 1; c >= 0; c--) {
            int curHeight = grid.get(rChoice, c);
            leftCount++;
            if (curHeight >= choiceHeight) {
                break;
            }
        }
        score *= leftCount;
        // looking right
        int rightCount = 0;
        for (int c = cChoice + 1; c < grid.numCols; c++) {
            int curHeight = grid.get(rChoice, c);
            rightCount++;
            if (curHeight >= choiceHeight) {
                break;
            }
        }
        score *= rightCount;
        return score;
    }

    private static void printTreeScores(Grid grid) {
        System.out.printf("\ngetting the tree score for every tree\n");

        int maxScore = 0;
        for(int r = 0; r < grid.numRows; r++) {
            for(int c = 0; c < grid.numCols; c++) {
                int curScore = getTreeScore(grid, r, c);
                System.out.printf("%d, ", curScore);
                if(curScore > maxScore) {
                    maxScore = curScore;
                }
            }
            System.out.printf("\n");
        }
        System.out.printf("maxScore = %d\n", maxScore);
    }

    private static int getNumVisible(Grid grid) {
        System.out.printf("num edge trees: %d\n", grid.numRows * 2 + grid.numCols * 2 - 4);
        var visiGrid = new VisiGrid(grid.numRows, grid.numCols);

        // left to right
        for (int r = 0; r < grid.numRows; r++) {
            int minHeight = -1;
            for (int c = 0; c < grid.numCols; c++) {
                int height = grid.get(r, c);
                if (height > minHeight) {
                    minHeight = height;
                    visiGrid.set(r,c);
                }
            }
        }
        // right to left
        for (int r = 0; r < grid.numRows; r++) {
            int minHeight = -1;
            for (int c = grid.numCols - 1; c >= 0; c--) {
                int height = grid.get(r, c);
                if (height > minHeight) {
                    minHeight = height;
                    visiGrid.set(r,c);
                }
            }
        }
        // top to bottom
        for (int c = 0; c < grid.numCols; c++) {
            int minHeight = -1;
            for (int r = 0; r < grid.numRows; r++) {
                int height = grid.get(r, c);
                if (height > minHeight) {
                    minHeight = height;
                    visiGrid.set(r,c);
                }
            }
        }
        // bottom to top
        for (int c = 0; c < grid.numCols; c++) {
            int minHeight = -1;
            for (int r = grid.numRows - 1; r >= 0; r--) {
                int height = grid.get(r, c);
                if (height > minHeight) {
                    minHeight = height;
                    visiGrid.set(r,c);
                }
            }
        }
        return visiGrid.getCount();
    }
}
