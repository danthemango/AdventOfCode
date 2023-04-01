package adventofcode.day10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class day10 {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.printf("could not find input file\n");
                return;
            }
            String inFile = args[0];
            part1(inFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void part1(String inFile) throws Exception {
        var reader = new BufferedReader(new FileReader(inFile));
        String line = reader.readLine();
        while (line != null) {
            // var splitted = line.split(" ");
            System.out.printf("%s\n", line);

            line = reader.readLine();
        }

        reader.close();
    }
}
