package de.lechner.budgetmapper.parser;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



/**
 * Parses the "Kontoauszug and fills the Kontofile
 */
@Service
public class Parser {

    public void parse () {
        String[] lines;
        String pfad = "/Users/lechnerri/Downloads/Konto_0000539619-Auszug_2025_0005.txt";
        List<String> dateiInhalt = ReadTextFile.readFileAsLines(pfad);
        for (int i=0; i<dateiInhalt.size(); i++) {
            System.out.println(dateiInhalt.get(i));
            i++;
        }
        Kontofile kontofile = new Kontofile();
    }
}
