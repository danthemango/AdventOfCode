package adventofcode.day3;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class day3 {
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();

            int total = 0;
            while (line != null) {
                assert line.length() % 2 == 0;

                int numItems = line.length();
                int numItemsPer = numItems / 2;
                String firstHalf = line.substring(0, numItemsPer);
                String secondHalf = line.substring(numItemsPer, numItems);
                String firstHalfSorted = getPrioSorted(firstHalf);
                String secondHalfSorted = getPrioSorted(secondHalf);

                char sameChar = findSame(firstHalfSorted, secondHalfSorted);
                int sameCharPrio = getPriority(sameChar);
                total += sameCharPrio;

                System.out.printf("%c %d\n", sameChar, sameCharPrio);

                line = reader.readLine();
            }
            reader.close();
            System.out.printf("%d\n", total);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int getPriority(char c) {
        if(c >= 'A' && c <= 'Z') {
            return c - 'A' + 27;
        } else if (c >= 'a' && c <= 'z') {
            return c - 'a' + 1;
        }
        return -1;
    }

    // returns a string sorted by priority
    static String getPrioSorted(String s) {
        Character[] sChars = s.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
        Arrays.sort(sChars, new Comparator<Character>() {
            @Override
            public int compare(Character c1, Character c2) {
                return getPriority(c1) - getPriority(c2);
            }
        });
        StringBuilder sb = new StringBuilder(s.length());
        for (Character c : sChars) {
            sb.append(c.charValue());
        }
        return sb.toString();
    }

    // returns the character which is found in both strings sorted by priority
    static char findSame(String s1, String s2) {
        int i1 = 0;
        int i2 = 0;

        while(true) {
            int p1 = getPriority(s1.charAt(i1));
            int p2 = getPriority(s2.charAt(i2));
            if(p1 == p2) {
                return s1.charAt(i1);
            } else if(p1 > p2) {
                i2++;
            } else if (p2 > p1) {
                i1++;
            }
        }
    }
}
