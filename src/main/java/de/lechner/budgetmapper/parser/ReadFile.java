package de.lechner.budgetmapper.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

public class ReadFile{

    public static String[] readFileToArray(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            return lines.toArray(new String[0]);

        } catch (NoSuchFileException e) {
            System.err.println("Datei nicht gefunden: " + filePath);
            return new String[0];

        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Datei: " + e.getMessage());
            return new String[0];
        }
    }

    public String [] inputFile(String[] args) {
        String[] fileLines = readFileToArray("beispiel.txt");

        if (fileLines.length > 0) {
            System.out.println("Erfolgreich " + fileLines.length + " Zeilen gelesen:");
            for (int i = 0; i < fileLines.length; i++) {
                System.out.println("[" + i + "] " + fileLines[i]);
            }
        } else {
            System.out.println("Keine Zeilen gelesen oder Datei leer.");
        }
        return fileLines;
    }
}