package de.lechner.budgetmapper.kontofiles;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KontofileService {
    public String readCsvFile(String str) throws Exception {
        List <Kontofile> list = new ArrayList<Kontofile>();
        System.out.println(str);

        list = readAllLines(str);

        for (Kontofile konto : list )
        {
            //System.out.println(konto.getVerwendungszweck());
            //System.out.println(konto.getBetrag());
        }


        return "File read";
    }

    public List<Kontofile> readAllLines(String filePath) throws Exception {
        CsvToBean kontofile = new CsvToBeanBuilder(new FileReader(filePath))
                .withType(Kontofile.class)
                .withSeparator(';')
                .withIgnoreLeadingWhiteSpace(true)
                .withSkipLines(1)
                .build();


      /*  for (Kontofile  konto : (Iterable<Kontofile>) kontofile) {
            System.out.println("Betrag: " + konto.getBetrag());
            System.out.println("Verwendungszweck: " + konto.getVerwendungszweck());
            System.out.println("Buchungstag: " + konto.getBuchungstag());
        }*/
        return (List<Kontofile>) kontofile.parse();
        }
}
