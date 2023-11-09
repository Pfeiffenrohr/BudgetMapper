package de.lechner.budgetmapper.kontofiles;

import com.opencsv.bean.CsvBindByPosition;


public class BankKontoLine {

    @CsvBindByPosition(position = 2)
    private String buchungstag;
    @CsvBindByPosition(position = 4)
    private String verwendungszweck;
    @CsvBindByPosition(position = 14)
    private String betrag;
    @CsvBindByPosition(position = 11)
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getBetrag() {
        return betrag;
    }

    public void setBetrag(String betrag) {
        this.betrag = betrag;
    }
}
