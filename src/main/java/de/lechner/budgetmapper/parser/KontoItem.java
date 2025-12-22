package de.lechner.budgetmapper.parser;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KontoItem {

    String buchungstag;
    String verwendungszweck;
    Double betrag;
    String name;

    public KontoItem(String buchungstag, String verwendungszweck, Double betrag, String name) {
        this.buchungstag = buchungstag;
        this.verwendungszweck = verwendungszweck;
        this.betrag = betrag;
        this.name = name;
    }
 }
