package adventofcode.day7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class day7 {
    public static void main(String[] args) {
        try {
            var reader = new BufferedReader(new FileReader(args[0]));
            String line = reader.readLine();

            var topDir = new Dir("/", null);
            var curDir = topDir;

            while(line != null) {
                System.out.printf("%s\n", line);
                var splitted = line.split(" ");

                // parse command
                if(splitted.length == 0) {
                    // do nothing
                } else if (splitted[0].equals("$") && splitted.length > 1) {
                    // cmd
                    if(splitted[1].equals("ls")) {
                        // expect file and dir contents soon
                    } else if (splitted[1].equals("cd")) {
                        if (splitted.length < 3) {
                            throw new Exception("can't cd without a destination");
                        } else if (splitted[2].equals("/")) {
                            curDir = topDir;
                        } else if(splitted[2].equals("..")) {
                            curDir = curDir.parent;
                        } else {
                            String dirName = splitted[2];
                            var child = curDir.getDir(dirName);
                            // create the directory if it doesn't already exist
                            if(child == null) {
                                // curDir.addDir(new Dir(dirName));
                                throw new Exception("dir not found");
                            }
                            curDir = child;
                        }
                    } else {
                        throw new Exception("command not found");
                    }

                } else if(splitted[0].equals("dir")) {
                    // directory found
                    if(splitted.length < 2) {
                        throw new Exception("expected directory but couldn't find the name");
                    }
                    String dirName = splitted[1];
                    var newDir = new Dir(dirName, curDir);
                    curDir.addDir(newDir);
                } else {
                    // we will assume there's a file here with a size
                    if(splitted.length < 2) {
                        throw new Exception("expected file but couldn't find the name");
                    }
                    int fileSize = Integer.parseInt(splitted[0]);
                    String fileName = splitted[1];
                    var newFile = new File(fileName, fileSize);
                    curDir.addFile(newFile);
                }

                line = reader.readLine();
            }

            System.out.printf("\nFileSystem:\n");
            topDir.print();
            System.out.printf("\n\n");
            // int count = topDir.countDirs(100000);
            // System.out.printf("total count: %d\n\n", count);
            final int capacity = 70000000;
            final int neededSpace = 30000000;
            int currentSpace = capacity - topDir.getSize();
            int neededRemoved = neededSpace - currentSpace;
            System.out.printf("capacity: %d\n", capacity);
            System.out.printf("neededSpace: %d\n", neededSpace);
            System.out.printf("currentSpace: %d\n", currentSpace);
            System.out.printf("neededRemoved: %d\n", neededRemoved);
            if(neededRemoved > 0) {
                Dir smallestDir = topDir.getSmallest(neededRemoved);
                System.out.printf("smallest dir large enough: %s size=%d\n", smallestDir.getFullPath(), smallestDir.getSize());
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class File {
    String name;
    int size;
    Dir parent;
    File(String _name, int _size) {
        this.name = _name;
        this.size = _size;
    }
    public void print(int indents) {
        String indentString = "  ".repeat(indents);
        System.out.printf("%s- %s (file, size=%d)\n", indentString, this.name, this.size);
    }
}

// consider making a map instead of an arraylist

class Dir {
    String name;
    HashMap<String, File> files;
    HashMap<String, Dir> dirs;
    Dir parent;

    Dir(String _name, Dir _parent) {
        this.name = _name;
        files = new HashMap<String, File>();
        dirs = new HashMap<String, Dir>();
        this.parent = _parent;
    }

    public String getName() {
        return name;
    }

    public void addDir(Dir newDir) throws Exception {
        if(this.dirs.get(newDir.name) != null) {
            throw new Exception(String.format("directory %s already exists", newDir.name));
        }
        dirs.put(newDir.name, newDir);
    }
    public void addFile(File newFile) throws Exception {
        if(this.files.get(newFile.name) != null) {
            throw new Exception(String.format("file %s already exists", newFile.name));
        }
        files.put(newFile.name, newFile);
    }

    public int getSize() {
        int size = 0;
        for(var file : files.values()) {
            size += file.size;
        }
        for(var dir : dirs.values()) {
            size += dir.getSize();
        }
        return size;
    }

    public void print(int indents) {
        String indentString = "  ".repeat(indents);
        System.out.printf("%s- %s (dir, size=%d)\n", indentString, this.name, this.getSize());
        SortedSet<String> fileNames = new TreeSet<>(files.keySet());
        for(var fileName : fileNames) {
            files.get(fileName).print(indents+1);
        }
        SortedSet<String> dirNames = new TreeSet<>(dirs.keySet());
        for(var dirName : dirNames) {
            dirs.get(dirName).print(indents+1);
        }
    }

    public void print() {
        this.print(0);
    }

    public Dir getDir(String name) {
        return dirs.get(name);
    }
    // returns the total size of all subdirectories with at most 'max' size
    // public int countDirs(int max) {
    //     int total = 0;
    //     for(var item : items.values()) {
    //         if(item instanceof Dir) {
    //             var theDir = (Dir) item;
    //             int size = theDir.getSize();
    //             if(size <= max) {
    //                 total += size;
    //             }
    //             total += theDir.countDirs(max);
    //         }
    //     }
    //     return total;
    // }
    // returns the smallest Directory that is at least of 'min' size
    public Dir getSmallest(int min) {
        Dir minDir = null;
        for(var dir : dirs.values()) {
            Dir subMinDir = dir.getSmallest(min);
            if(subMinDir == null) {
                continue;
            } else if (minDir == null || minDir.getSize() > subMinDir.getSize()) {
                minDir = subMinDir;
            }
        }
        if(minDir != null) {
            return minDir;
        } else if(this.getSize() >= min) {
            return this;
        } else {
            return null;
        }
    }

    public String getFullPath() {
        StringBuilder sb = new StringBuilder();
        Dir curDir = this;

        while(curDir != null && !curDir.name.equals("/")) {
            sb.insert(0, curDir.name);
            sb.insert(0, "/");
            curDir = curDir.parent;
        }

        return sb.toString();
    }
}
