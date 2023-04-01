package adventofcode.day1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class day1v2 {
    public static void main(String[] args) {
        BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader("input.txt"));
			String line = reader.readLine();

            int total = 0;
            topItems ti = new topItems();
			while (line != null) {
                int val = 0;
                if (line.length() != 0) {
                    val = Integer.parseInt(line);
                    //System.out.printf("%d\n", val);
                    total += val;
                } else {
                    // System.out.printf("total = %d\n", total);
                    ti.add(total);
                    total = 0;
                }
				// System.out.printf("%s - %d - %d\n", line, line.length(), val);
				// read next line
				line = reader.readLine();
			}
            // get top 3 max
            System.out.printf("top3Total = %d\n", ti.getSum());
            ti.print();

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

class topItems {
    int first = 0;
    int second = 0;
    int third = 0;
    int numItems = 0;

    public void add(int val) {
        if (val > first) {
            third = second;
            second = third;
            first = val;
        } else if (val > second) {
            third = second;
            second = val;
        } else if (val > third) {
            third = val;
        }
    }

    public int getSum() {
        return first + second + third;
    }
    
    public void print() {
        System.out.printf("top three: %d, %d, %d\n", first, second, third);
    }
}