package adventofcode.day9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class UniqueCounter {
    HashMap<Integer, HashMap<Integer,Boolean>> map;
    UniqueCounter() {
        map = new HashMap<Integer, HashMap<Integer,Boolean>>();
    }
    public void set(Coord coord) {
        var ymap = map.get(coord.y);
        if(ymap == null) {
            map.put(coord.y, new HashMap<Integer, Boolean>());
            ymap = map.get(coord.y);
        }
        ymap.put(coord.x, true);
    }
    public int getCount() {
        int count = 0;
        for(var y : map.keySet()) {
            var ymap = map.get(y);
            count += ymap.keySet().size();
        }
        return count;
    }
}

class Coord {
    int x;
    int y;
    Coord(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }
    public void print() {
        System.out.printf("%s\n", String.format("(%d,%d)", this.x, this.y));
    }
    public String toString() {
        return String.format("(%d,%d)", this.x, this.y);
    }
    public void update(Direction dir, int steps) {
        if(dir == Direction.UP) {
            this.y += steps;
        } else if(dir == Direction.DOWN) {
            this.y -= steps;
        } else if(dir == Direction.LEFT) {
            this.x -= steps;
        } else if(dir == Direction.RIGHT) {
            this.x += steps;
        }
    }
    public void update(Direction dir) {
        if(dir == Direction.UP) {
            this.y++;
        } else if(dir == Direction.DOWN) {
            this.y--;
        } else if(dir == Direction.LEFT) {
            this.x--;
        } else if(dir == Direction.RIGHT) {
            this.x++;
        }
    }
    public void follow(Coord H) throws Exception {

        int dx = this.x - H.x;
        int dy = this.y - H.y;
        int adx = Math.abs(dx);
        int ady = Math.abs(dy);

        if(adx > 2 || ady > 2) {
            throw new Exception("too far!");
        }

        if((adx == 2 && ady > 0) || (adx > 0 && ady == 2)) {
            if(dx < 0) {
                this.x++;
            } else if(dx > 0) {
                this.x--;
            }
            if(dy < 0) {
                this.y++;
            } else if(dy > 0) {
                this.y--;
            }
        } else if(dx == -2) {
            this.x++;
        } else if(dx == 2) {
            this.x--;
        } else if(dy == -2) {
            this.y++;
        } else if(dy == 2) {
            this.y--;
        }
    }
    public boolean equals(Coord c) {
        return this.x == c.x && this.y == c.y;
    }
}

public class day9 {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                throw new Exception("could not find input file");
            }
            String inFile = args[0];
            part2(inFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void part1(String inFile) throws Exception {
        var reader = new BufferedReader(new FileReader(inFile));
        var Hcoord = new Coord(0,0);
        var Tcoord = new Coord(0,0);
        var TcoordCounter = new UniqueCounter();
        TcoordCounter.set(Tcoord);
        System.out.printf("H%s T%s\n", Hcoord.toString(), Tcoord.toString());
        String line = reader.readLine();
        while (line != null) {
            var splitted = line.split(" ");
            if(splitted.length != 2) {
                throw new Exception(String.format("command unrecognized: (%s)", line));
            }
            var dir = getDirectionFromString(splitted[0]);
            var steps = Integer.parseInt(splitted[1]);
            var dirName = getDirectionName(dir);
            System.out.printf("%s %d\n: ", dirName, steps);

            for(int i = 0; i < steps; i++) {
                Hcoord.update(dir);
                Tcoord.follow(Hcoord);
                TcoordCounter.set(Tcoord);
                System.out.printf("H%s T%s count=%d\n", Hcoord.toString(), Tcoord.toString(), TcoordCounter.getCount());
            }

            line = reader.readLine();
        }

        reader.close();
    }

    public static void part2(String inFile) throws Exception {
        var reader = new BufferedReader(new FileReader(inFile));
        var Hcoord = new Coord(0,0);
        final int numTails = 9;
        var tailArr = new ArrayList<Coord>(numTails);
        for(int i = 0; i < numTails; i++) {
            tailArr.add(new Coord(0,0));
        }
        var tailEnd = tailArr.get(numTails-1);
        var TcoordCounter = new UniqueCounter();
        TcoordCounter.set(tailEnd);

        // printout
        printCounts(TcoordCounter, Hcoord, tailArr);
        printMap(Hcoord, tailArr);
        System.out.printf("\n");

        String line = reader.readLine();
        while (line != null) {
            var splitted = line.split(" ");
            if(splitted.length != 2) {
                throw new Exception(String.format("command unrecognized: (%s)", line));
            }
            var dir = getDirectionFromString(splitted[0]);
            var steps = Integer.parseInt(splitted[1]);
            var dirName = getDirectionName(dir);
            System.out.printf("%s %d:\n", dirName, steps);

            for(int i = 0; i < steps; i++) {
                Hcoord.update(dir);
                tailArr.get(0).follow(Hcoord);
                for(int j = 1; j < tailArr.size(); j++) {
                    tailArr.get(j).follow(tailArr.get(j-1));
                }
                TcoordCounter.set(tailEnd);
                printCounts(TcoordCounter, Hcoord, tailArr);
            }

            line = reader.readLine();
        }
        printMap(Hcoord, tailArr);

        reader.close();
    }

    public static void printMap(Coord Hcoord, ArrayList<Coord> tailArr) {
        int minX = Hcoord.x;
        int maxX = Hcoord.x;
        int minY = Hcoord.y;
        int maxY = Hcoord.y;
        for(var segment : tailArr) {
            minX = segment.x < minX ? segment.x : minX;
            maxX = segment.x > maxX ? segment.x : maxX;
            minY = segment.y < minY ? segment.y : minY;
            maxY = segment.y > maxY ? segment.y : maxY;
        }
        minX = 0 < minX ? 0 : minX;
        maxX = 0 > maxX ? 0 : maxX;
        minY = 0 < minY ? 0 : minY;
        maxY = 0 > maxY ? 0 : maxY;
        System.out.printf("\n");
        for(int y = maxY; y >= minY; y--) {
            for(int x = minX; x <= maxX; x++) {
                String c = ".";
                var curCoord = new Coord(x,y);
                if(x == 0 && y == 0) {
                    c = "s";
                }
                for(int i = tailArr.size()-1; i >= 0; i--) {
                    if(curCoord.equals(tailArr.get(i))) {
                        int val = i+1;
                        c = Integer.toString(val);
                    }
                }
                if(curCoord.equals(Hcoord)) {
                    c = "H";
                }
                System.out.printf("%s", c);
            }
            System.out.printf("\n");
        }
        System.out.printf("\n");
    }

    public static void printCounts(UniqueCounter TcoordCounter, Coord Hcoord, ArrayList<Coord> tailArr) {
        System.out.printf("count=%d ", TcoordCounter.getCount());
        System.out.printf("H%s ", Hcoord.toString());
        int i = 1;
        for(var tailSegment : tailArr) {
            System.out.printf("%d%s ", i, tailSegment.toString());
            i++;
        }
        System.out.printf("\n");
    }

    public static Direction getDirectionFromString(String val) throws Exception {
        Map<String, Direction> dirs = Map.of(
            "U", Direction.UP,
            "D", Direction.DOWN,
            "L", Direction.LEFT,
            "R", Direction.RIGHT
        );
        var dir = dirs.get(val);
        if(dir == null) {
            throw new Exception(String.format("direction %s unrecognized", val));
        }
        return dir;
    }

    public static String getDirectionName(Direction val) {
        Map<Direction, String> dirs = Map.of(
            Direction.UP, "U",
            Direction.DOWN, "D",
            Direction.LEFT, "L", 
            Direction.RIGHT, "R"
        );

        return dirs.get(val);
    }

}

enum Direction {
    UP, DOWN, LEFT, RIGHT
}
