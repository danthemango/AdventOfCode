package adventofcode.day6;

import java.io.BufferedReader;
import java.io.FileReader;

public class day6 {
    private static void runTest(String val, int seqLength) throws Exception {
        var uniqueChecker = new UniqueChecker(seqLength);
        for(int i = 0; i < val.length(); i++) {
            System.out.printf("%d ", uniqueChecker.push(val.charAt(i)));
        }
        System.out.printf("\n");
    }

    public static void runTests() {
        try {
            var reader = new BufferedReader(new FileReader("adventofcode/day6/input.txt"));
            String line = reader.readLine();

            final int seqLength = 14;

            // test the script with the first few test strings
            runTest("mjqjpqmgbljsphdztnvjfqwrcgsmlb", seqLength);
            runTest("bvwbjplbgvbhsrlpgdmjqwftvncz", seqLength);
            runTest("nppdvjthqldpwncqszvftbrmjlhg", seqLength);
            runTest("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", seqLength);
            runTest("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", seqLength);

            while (line != null) {
                var uniqueChecker = new UniqueChecker(seqLength);
                for(int i = 0; i < line.length(); i++) {
                    System.out.printf("%d ", uniqueChecker.push(line.charAt(i)));
                }
                
                line = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runTests();
    }   
}

class UniqueChecker {
    int[] letterCounts = new int[26];
    char[] recentChars;
    int recentCharIdx = 0;
    int numUnique = 0;
    int numPushed = 0;
    int seqLength;

    // init with the expected sequence length
    UniqueChecker(int _seqLength) {
        this.seqLength = _seqLength;
        recentChars = new char[_seqLength];
    }

    // returns the index value of a given char in our mapper array 
    private int getCharIdx(char c) {
        return c - 'a';
    }

    // pushes this letter to the sequence,
    // updating the letter counts
    // and returning the number of chars that were pushed
    // before we found a unique string of length 'seqLength'
    // or -1 if not found
    public int push(char newChar) throws Exception {
        if(numPushed >= seqLength) {
            // if we already pushed at least 'seqLength'-number chars onto
            // the mapper, we need to fetch the oldest char and 
            // decrement its letter-count, updating the unique-count
            // if necessary
            char oldChar = recentChars[recentCharIdx];
            int oldCharIdx = getCharIdx(oldChar);
            letterCounts[oldCharIdx]--;
            if(letterCounts[oldCharIdx] < 0) {
                 throw new Exception(String.format("letter '%c' could not be found at %d", newChar, numPushed));
            }
            if(letterCounts[oldCharIdx] == 0) {
                numUnique--;
            }
        }

        // add the new letter to the sequence, and check if it's a new unique letter
        int newCharIdx = getCharIdx(newChar);
        if(letterCounts[newCharIdx] == 0) {
            numUnique++;
        }
        letterCounts[newCharIdx]++;

        // add it to the array of recently seen letters
        recentChars[recentCharIdx] = newChar;
        recentCharIdx = (recentCharIdx + 1) % seqLength;
        numPushed++;
        if(numUnique == seqLength) {
            return numPushed;
        } else {
            return -1;
        }
    }
}
