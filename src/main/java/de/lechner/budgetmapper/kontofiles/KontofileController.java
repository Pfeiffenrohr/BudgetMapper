package de.lechner.budgetmapper.kontofiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KontofileController {
    String fileKonto1 = "C:/temp/20250206-539619-umsatz.CSV";
    String fileKonto2 = "C:/temp/20250206-6192967-umsatz.CSV";

    @Autowired
    private KontofileService kontofileService;

    @GetMapping(value = "/getFile")
    public String sayHi() throws Exception {
        return kontofileService.readCsvFile(fileKonto1);
    }
    @GetMapping(value = "/getFile2")
    public String giro2() throws Exception {
        return kontofileService.readCsvFile2(fileKonto2);
    }
}
