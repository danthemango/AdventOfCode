package adventofcode.day4;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class day4p2 {
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();

            int count = 0;
            int countContained = 0;
            while (line != null) {
                String[] splittedCom = line.split(",");
                assert splittedCom.length == 2;
                String[] splittedDash1 = splittedCom[0].split("-");
                assert splittedDash1.length == 2;
                String[] splittedDash2 = splittedCom[1].split("-");
                assert splittedDash2.length == 2;
                int start1 = Integer.parseInt(splittedDash1[0]);
                int end1 = Integer.parseInt(splittedDash1[1]);
                int start2 = Integer.parseInt(splittedDash2[0]);
                int end2 = Integer.parseInt(splittedDash2[1]);

                count++;
                if((start1 <= start2 && start2 <= end1)
                    || (start2 <= start1 && start1 <= end2)
                ) {
                    countContained++;
                    System.out.printf("%d-%d,%d-%d\n", start1, end1, start2, end2);
                }
                
                line = reader.readLine();
            }

            System.out.printf("count %d, countContained %d\n", count, countContained);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}