package de.lechner.budgetmapper.kontofiles;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import de.lechner.budgetmapper.transaction.ApiCall;
import de.lechner.budgetmapper.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


@Service
public class KontofileService {

    @Autowired
    ApiCall apicall = new ApiCall();

    private static final Logger LOG = LoggerFactory.getLogger(KontofileService.class);
    public String readCsvFile(String str) throws Exception {
        List <BankKontoLine> list = new ArrayList<BankKontoLine>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        list = readAllLines(str);
        LOG.info("Read All Lines done!" + list.size() +" Lines");
        for (BankKontoLine konto : list )
        {
            Boolean found = false;
            Calendar calstart = Calendar.getInstance();
            Calendar calend = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
            calstart.setTime(sdf.parse(konto.getBuchungstag()));
            calstart.add(Calendar.DATE,-3);
            calend.setTime(sdf.parse(konto.getBuchungstag()));
            calend.add(Calendar.DATE,3);
            //LOG.info("Startdate = "+konto.getBuchungstag());
            List <Transaction> transactions = apicall.getTransactions(formatter.format(calstart.getTime()),
                    formatter.format(calend.getTime()));
            //LOG.info("Found " + transactions.size() +" Transactions  for " +konto.getVerwendungszweck());
            for (Transaction trans : transactions)
            {
              //LOG.info("transWert = " + trans.getWert());
              //LOG.info("Kontowert = " + new Double(konto.getBetrag().replaceAll(",",".")));
                if (Objects.equals(trans.getWert(), new Double(konto.getBetrag().replaceAll(",", ".")))) {
                    //System.out.println("Found Match: " +trans.getName() + " <=> "+konto.getVerwendungszweck());
                    found = true;
                }
                if (konto.getVerwendungszweck().contains("Ihr Einkauf bei PayPal Inc")) {
                    found = true;
                }
                if (konto.getVerwendungszweck().contains("Ihr Einkauf bei EDEKA")) {
                    found = true;
                }
                if (konto.getVerwendungszweck().startsWith("102")) {
                    found = true;
                }
                if (konto.getVerwendungszweck().contains("Grundsteuer")) {
                    found = true;
                }
            }
            if ( ! found) {
                System.out.println(konto.getVerwendungszweck());
            }
            //System.out.println(konto.getVerwendungszweck());

        }

        LOG.info("ReadCSVFile done!");
        return "File read";
    }

    public List<BankKontoLine> readAllLines(String filePath) throws Exception {
        CsvToBean kontofile = new CsvToBeanBuilder(new FileReader(filePath))
                .withType(BankKontoLine.class)
                .withSeparator(';')
                .withIgnoreLeadingWhiteSpace(true)
                .withSkipLines(1)
                .build();


      /*  for (Kontofile  konto : (Iterable<Kontofile>) kontofile) {
            System.out.println("Betrag: " + konto.getBetrag());
            System.out.println("Verwendungszweck: " + konto.getVerwendungszweck());
            System.out.println("Buchungstag: " + konto.getBuchungstag());
        }*/
        return (List<BankKontoLine>) kontofile.parse();
        }
}
