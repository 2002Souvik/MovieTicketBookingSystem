package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    // Read all lines from a file (if not exists returns empty list)
    public static List<String> readAllLines(String path) {
        List<String> lines = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()) return lines;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }

    // Append a line to a file (creates file if missing)
    public static void appendLine(String path, String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    // Rewrite entire file
    public static void writeAllLines(String path, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, false))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
