package de.lechner.budgetmapper.parser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class KontoItemProtokollierer {

    private final String dateiPfad;
    private final Set<KontoItem> bekannteItems = new HashSet<>();
    private static final String TRENNZEICHEN = ";";
    private static final String VERARBEITET_MARKER = "1";

    public KontoItemProtokollierer(String dateiPfad) {
        this.dateiPfad = dateiPfad;
        ladeBestehendeDaten();
    }

    /**
     * Prüft, ob das gegebene Item bereits als verarbeitet (mit einer "1") in der Datei steht.
     *
     * @param itemToCheck Das zu prüfende Item (der Flag-Wert darin wird ignoriert).
     * @return true, wenn eine Version dieses Items mit dem "verarbeitet"-Flag existiert.
     */
    public boolean istItemVerarbeitet(KontoItem itemToCheck) {
        // Erstelle eine "Ziel"-Version des Items, wie es aussehen würde, wenn es verarbeitet wäre.
        KontoItem verarbeiteteVersion = new KontoItem(
                itemToCheck.getBuchungstag(),
                itemToCheck.getVerwendungszweck(),
                itemToCheck.getBetrag(),
                itemToCheck.getName(),
                VERARBEITET_MARKER
        );

        // Prüfe, ob genau diese Version im Set existiert.
        return bekannteItems.contains(verarbeiteteVersion);
    }

    /**
     * Schreibt ein neues KontoItem in die Datei, falls es noch nicht als verarbeitet markiert ist
     * und auch noch nicht mit leerem Flag existiert.
     *
     * @param newItem Das zu schreibende Item.
     * @return true, wenn das Item neu geschrieben wurde.
     * @throws IOException
     */
    public boolean schreibeItemWennNeu(KontoItem newItem) throws IOException {
        // 1. Prüfen, ob der Eintrag bereits als verarbeitet markiert ist.
        if (istItemVerarbeitet(newItem)) {
            System.out.println("Eintrag ist bereits als verarbeitet markiert und wird nicht geschrieben: " + newItem.getVerwendungszweck());
            return false;
        }

        // 2. Wir setzen den Flag für den neuen Eintrag explizit auf leer.
        newItem.setVerarbeitetFlag("");

        // 3. Prüfen, ob der Eintrag bereits mit leerem Flag existiert (um Duplikate zu vermeiden).
        if (bekannteItems.contains(newItem)) {
            System.out.println("Eintrag existiert bereits (unverarbeitet) und wird nicht erneut geschrieben: " + newItem.getVerwendungszweck());
            return false;
        }

        // 4. Wenn alles passt: schreiben!
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dateiPfad, true))) {
            String zeile = String.join(TRENNZEICHEN,
                    newItem.getBuchungstag(),
                    newItem.getVerwendungszweck(),
                    String.valueOf(newItem.getBetrag()),
                    newItem.getName(),
                    newItem.getVerarbeitetFlag() // wird ein leerer String sein
            );
            writer.write(zeile);
            writer.newLine();
        }

        bekannteItems.add(newItem);
        System.out.println("Neuer, unverarbeiteter Eintrag geschrieben: " + newItem.getVerwendungszweck());
        return true;
    }

    private void ladeBestehendeDaten() {
        if (!Files.exists(Paths.get(dateiPfad))) {
            System.out.println("Datei existiert nicht, wird bei Bedarf neu erstellt.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dateiPfad))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                if (zeile.isBlank()) continue;

                // Wichtig: limit -1, damit leere Felder am Ende nicht ignoriert werden
                String[] teile = zeile.split(TRENNZEICHEN, -1);

                if (teile.length >= 4) { // Mindestens 4 Felder müssen da sein
                    try {
                        String buchungstag = teile[0];
                        String verwendungszweck = teile[1];
                        Double betrag = Double.parseDouble(teile[2]);
                        String name = teile[3];
                        // Das fünfte Feld (Flag) kann fehlen oder leer sein
                        String flag = (teile.length > 4) ? teile[4] : "";

                        bekannteItems.add(new KontoItem(buchungstag, verwendungszweck, betrag, name, flag));
                    } catch (NumberFormatException e) {
                        System.err.println("Fehler beim Parsen einer Zeile (wird ignoriert): " + zeile);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der bestehenden Daten: " + e.getMessage());
        }
        System.out.println(bekannteItems.size() + " bestehende Einträge geladen.");
    }
}