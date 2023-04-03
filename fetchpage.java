/**
 * fetchpage
 * fetches the readme section of the adventofcode day specified
 * requires the jsoup jar, available at: https://jsoup.org/download
 * assuming you downloaded 'jsoup-1.15.4.jar' like i did:
 * usage: java -cp jsoup-1.15.4.jar fetchpage.java
 */

import org.jsoup.*;
import org.jsoup.helper.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.util.*;
import java.io.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class fetchpage {
    private static String topLevelUrl = "https://adventofcode.com";
    public static void main(String[] args) {
        try {
            // get year and day nums from args
            if(args.length < 1) {
                printUsage();
                return;
            }
            int year = getYear(args[0]);

            // path towards this year's saved pages
            String yearFilePath = "./pages/%d".formatted(year);
            java.nio.file.Files.createDirectories(Paths.get(yearFilePath));

            // fetch the one day if specified
            if(args.length >= 2) {
                int day = getDay(args[1]);
                if(year == -1 || day == -1) {
                    printUsage();
                    return;
                }
                String filepath = "%s/day%d.html".formatted(yearFilePath, day);
                String url = date2url(year, day);
                saveMainToFile(url, filepath);
            } else {
                List<String> urls = getUrlsFromYear(year);
                for(String url : urls) {
                    var parts = url.split("/");
                    if(parts.length != 6) {
                        throw new Exception("\"%s\" is missing the day number".formatted(url));
                    }
                    
                    int day = Integer.parseInt(parts[5]);
                    String filepath = "%s/day%d.html".formatted(yearFilePath, day);

                    saveMainToFile(url, filepath);
                    return;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // returns the year value, else -1 if none found
    private static int getYear(String arg) {
        try {
            int year = Integer.parseInt(arg);
            return year;
        } catch(Exception ex) {
            return -1;
        }
    }

    // returns the day value from the arg, -1 if none found
    private static int getDay(String arg) {
        // note: december has 31 days
        try {
            int day = Integer.parseInt(arg);
            if(day >= 1 && day <= 31) {
                return day;
            } else {
                return -1;
            }
        } catch(Exception ex) {
            return -1;
        }
    }

    private static void printUsage() {
        System.out.println("usage: java fetchpage.java <year> <day>");
        System.out.println("or, for all 31 days: java fetchpage.java <year>");
        System.out.println("for 2022 day 11 for example: java fetchpage.java 2022 11");
    }

    private static String date2url(int year, int day) {
        return "%s/%d/day/%d".formatted(topLevelUrl, year, day);
    }

    // fetches and returns a list of all URLs for a given year from the website
    private static List<String> getUrlsFromYear(int year) throws Exception {
        List<String> result = new ArrayList<String>();

        String yearUrl = "%s/%d/".formatted(topLevelUrl, year);
        Document doc = getDocFromUrl(yearUrl);
        var body = doc.select(".calendar a");
        for(Element el : body) {
            String indirectUrl = el.attributes().get("href");
            String directUrl = "%s%s".formatted(topLevelUrl, indirectUrl);
            result.add(directUrl);
        }

        return result;
    }

    // returns Jsoup Document from a url
    private static Document getDocFromUrl(String url) throws Exception {
        return Jsoup.connect(url).get();
    }

    // returns Jsoup Document from a local file
    private static Document getDocFromFile(String pathname) throws Exception {
        File input = new File(pathname);
        return Jsoup.parse(input, "UTF-8", topLevelUrl);
    }

    // saves the main html tag content to file
    private static void saveMainToFile(String url, String filename) throws Exception {
        Path path = Paths.get(filename);
        if(Files.exists(path)) {
            System.out.printf("skipping \"%s\", \"%s\" already exists\n", url, filename);
            return;
        }
        
        System.out.printf("Saving \"%s\" to \"%s\"\n", url, filename);
        Document doc = getDocFromUrl(url);
        Elements body = doc.select("main");
        for(Element el : body) {
            BufferedWriter writer = new BufferedWriter( new FileWriter(filename));
            writer.write(el.outerHtml());
            writer.close();
            return;
        }
    }
}
