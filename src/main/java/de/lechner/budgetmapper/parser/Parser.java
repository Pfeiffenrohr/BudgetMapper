package de.lechner.budgetmapper.parser;

import de.lechner.budgetmapper.kontofiles.KontofileService;
import de.lechner.budgetmapper.transaction.ApiCall;
import de.lechner.budgetmapper.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


/**
 * Parses the "Kontoauszug and fills the Kontofile
 */
@Service
public class Parser {
    private static final Logger LOG = LoggerFactory.getLogger(KontofileService.class);
    @Autowired
    ApiCall apicall;

    public void parse() throws Exception {
        String[] lines;
        KontoItemProtokollierer protokollierer = new KontoItemProtokollierer("kontoauszug.csv");
        ApiCall apicall = new ApiCall();
        //String directoryPath = "C:\\temp\\Konto";
        String directoryPath = "konto";
        List<String> allFiles = getAllFiles(directoryPath);
        for (String path : allFiles) {
            // String pfad = "C:/temp/Konto_0000539619-Auszug_2025_0009.txt";
            List<String> dateiInhalt = ReadTextFile.readFileAsLines(path);
            for (int i = 0; i < dateiInhalt.size(); i++) {
                if (hasRequiredPrefix(dateiInhalt.get(i))) {
                    //System.out.println(dateiInhalt.get(i).trim());
                    KontoItem kontoItem = new KontoItem();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String[] items = dateiInhalt.get(i).trim().replaceAll(" +", " ").split(" ");
                    //System.out.println(items[0]+"§"+dateiInhalt.get(i+1)+"§"+items[items.length-1].replace(".","").replace(",","."));
                    if (!isDouble(items[items.length - 1].replace(".", "").replace(",", "."))) {
                        // System.out.println(items[items.length-1].replace(".","").replace(",",".")+" is not Double");
                        continue;
                    }
                    if (items.length < 3  ) {
                        continue;
                    }
                    kontoItem.setBuchungstag(items[0]);
                    kontoItem.setName(items[1]);
                    kontoItem.setVerwendungszweck(dateiInhalt.get(i + 1));
                    if (i + 2 < dateiInhalt.size() && !hasRequiredPrefix(dateiInhalt.get(i + 2))) {
                        kontoItem.setVerwendungszweck(kontoItem.getVerwendungszweck() + dateiInhalt.get(i + 2));
                    }
                    kontoItem.setBetrag(Double.parseDouble(items[items.length - 1].replace(".", "").replace(",", ".")));
                    //*i++;
                    //System.out.println(dateiInhalt.get(i).trim());
                    boolean found = false;
                    Calendar calstart = Calendar.getInstance();
                    Calendar calend = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
                    calstart.setTime(sdf.parse(kontoItem.getBuchungstag()));
                    calstart.add(Calendar.DATE, -3);
                    calend.setTime(sdf.parse(kontoItem.getBuchungstag()));
                    calend.add(Calendar.DATE, 3);
                    //LOG.info("Startdate = "+kontoItem.getBuchungstag());
                    List<Transaction> transactions = apicall.getTransactions(formatter.format(calstart.getTime()),
                            formatter.format(calend.getTime()));
                    //LOG.info("Found " + transactions.size() +" Transactions  for " +kontoItem.getVerwendungszweck());
                    for (Transaction trans : transactions) {
                        //LOG.info("transWert = " + trans.getWert());
                        //LOG.info("Kontowert = " + new Double(konto.getBetrag().replaceAll(",",".")));

                        if (Objects.equals(trans.getWert(), (kontoItem.getBetrag()))) {
                            //System.out.println("Found Match: " +trans.getName() + " <=> "+kontoItem.getVerwendungszweck());
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Ihr Einkauf bei Wuensche")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Ihr Einkauf bei Norma")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Ihr Einkauf bei PayPal Inc")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Ihr Einkauf bei EDEKA")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Baeckerei Wiesender")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Ihren Einkauf bei Kaufland")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().startsWith("102")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Grundsteuer")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Einkauf bei REWE")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Netto Ma")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("hr Einkauf bei Netto Marken-Discount")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Ihr Einkauf bei Baeckerei Sipl")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Ihr Einkauf bei Backhaus Hackner")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Ihr Einkauf bei Baecker Bachmeier")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Ihr Einkauf bei Fressnapf")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Netto Marken-Discount")) {
                            found = true;
                        }
                        if (kontoItem.getVerwendungszweck().contains("Kaufland")) {
                            found = true;
                        }
                    }
                    if (!found) {
                        if (! protokollierer.istItemVerarbeitet(kontoItem)) {
                            protokollierer.schreibeItemWennNeu(kontoItem);
                            System.out.println(kontoItem.getBuchungstag() + "  " + kontoItem.getBetrag() + " " + kontoItem.getVerwendungszweck() + "NOT FOUND!!!!");
                        }
                    }
                }
            }
        }
    }

    private static boolean hasRequiredPrefix(String input) {
        // ^ = Anfang, \d = Ziffer, \\. = Punkt, {4} = genau 4 Wiederholungen
        String regex = "^[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}.*";
        return input != null && input.matches(regex);
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str.trim());
        } catch (Exception ev) {
            return false;
        }
        return true;
    }

    public static List<String> getAllFiles(String directoryPath) {
        File dir = new File(directoryPath);

        List<String> result = new ArrayList<>();

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Pfad existiert nicht oder ist kein Verzeichnis: " + directoryPath);
            return result;
        }

        collectFiles(dir, result);
        return result;
    }

    private static void collectFiles(File dir, List<String> result) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                result.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                collectFiles(file, result);
            }
        }
    }
}

