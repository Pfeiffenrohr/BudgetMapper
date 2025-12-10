package de.lechner.budgetmapper.parser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Eine Hilfsklasse zum Einlesen von Dateien.
 */
public class ReadTextFile {

    /**
     * Liest den Inhalt einer Datei zeilenweise ein und gibt ihn als Liste von Strings zurück.
     *
     * @param filePath Der Pfad zur Datei (z.B. "C:/data/meine-datei.txt").
     * @return Eine Liste mit den Zeilen der Datei. Gibt eine leere Liste zurück,
     *         falls die Datei nicht gelesen werden kann.
     */
    public static List<String> readFileAsLines(String filePath) {
        try {
            // Liest alle Zeilen der Datei mit einem einzigen Befehl.
            // StandardCharsets.UTF_8 sorgt für die korrekte Zeichenkodierung.
            return Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Wenn ein Fehler auftritt (z.B. Datei nicht gefunden), geben wir eine Fehlermeldung aus
            // und eine leere Liste zurück. So stürzt das Programm nicht ab.
            System.err.println("Fehler beim Einlesen der Datei: " + filePath);
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}