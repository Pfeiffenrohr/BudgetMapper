package de.lechner.budgetmapper.kontofiles;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.springframework.context.annotation.Bean;


public class Kontofile {

    @CsvBindByPosition(position = 2)
    String buchungstag;
    @CsvBindByPosition(position = 4)
    String verwendungszweck;
    @CsvBindByPosition(position = 14)
    String betrag;
    @CsvBindByPosition(position = 11)
    String name;



    public String getBuchungstag() {
        return buchungstag;
    }

    public void setBuchungstag(String buchungstag) {
        this.buchungstag = buchungstag;
    }

    public String getVerwendungszweck() {
        return verwendungszweck;
    }

    public void setVerwendungszweck(String verwendungszweck) {
        this.verwendungszweck = verwendungszweck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBetrag() {
        return betrag;
    }

    public void setBetrag(String betrag) {
        this.betrag = betrag;
    }
}
