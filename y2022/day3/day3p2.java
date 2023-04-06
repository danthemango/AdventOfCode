package adventofcode.day3;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class day3p2 {
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();

            int total = 0;
            String[] groupStrings = new String[3];
            int i = 0;
            while (line != null) {
                assert line.length() % 2 == 0;
                groupStrings[0] = getPrioSorted(line);
                System.out.printf("%d %s\n", i, line);
                line = reader.readLine();
                groupStrings[1] = getPrioSorted(line);
                System.out.printf("%d %s\n", i, line);
                line = reader.readLine();
                groupStrings[2] = getPrioSorted(line);
                System.out.printf("%d %s\n", i, line);
                line = reader.readLine();
                char sameChar = findSameThree(groupStrings[0], groupStrings[1], groupStrings[2]);
                int sameCharPrio = getPriority(sameChar);
                total += sameCharPrio;
                System.out.printf("-%d %d %c %s %s %s\n", i, sameCharPrio, sameChar, groupStrings[0], groupStrings[1], groupStrings[2]);
                i++;
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

    // returns the character which is found in all three strings sorted by priority
    static char findSameThree(String s1, String s2, String s3) {
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;

        while(true) {
            int p1 = getPriority(s1.charAt(i1));
            int p2 = getPriority(s2.charAt(i2));
            int p3 = getPriority(s3.charAt(i3));
            if(p1 == p2 && p2 == p3) {
                return s1.charAt(i1);
            } else if(p1 <= p2 && p1 <= p3) {
                i1++;
            } else if (p2 <= p1 && p2 <= p3) {
                i2++;
            } else if (p3 <= p1 && p3 <= p2) {
                i3++;
            }
        }
    }
}
