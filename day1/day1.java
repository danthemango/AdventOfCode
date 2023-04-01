package adventofcode.day1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class day1 {
    public static void main(String[] args) {
        BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader("input.txt"));
			String line = reader.readLine();

            int total = 0;
            List<Integer> totals = new ArrayList<Integer>();
			while (line != null) {
                int val = 0;
                if (line.length() != 0) {
                    val = Integer.parseInt(line);
                    //System.out.printf("%d\n", val);
                    total += val;
                } else {
                    System.out.printf("total = %d\n", total);
                    totals.add(total);
                    total = 0;
                }
				// System.out.printf("%s - %d - %d\n", line, line.length(), val);
				// read next line
				line = reader.readLine();
			}
            Collections.sort(totals);
            // get top 3 max
            System.out.println(totals.toString());
            int top3Total = 0;
            for(int i = totals.size()-1; i > totals.size()-4; i--) {
                System.out.printf("in the top three = %d\n", totals.get(i));
                top3Total += totals.get(i);
            }
            System.out.printf("top3Total = %d\n", top3Total);

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
