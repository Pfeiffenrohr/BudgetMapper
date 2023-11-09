package de.lechner.budgetmapper.kontofiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KontofileController {

    @Autowired
    private KontofileService kontofileService;

    @GetMapping(value = "/getFile")
    public String sayHi() throws Exception {


        return kontofileService.readCsvFile("C:/temp/20231108-539619-umsatz.CSV");
    }
    @GetMapping(value = "/getFile2")
    public String giro2() throws Exception {


        return kontofileService.readCsvFile2("C:/temp/20231108-6192967-umsatz.CSV");
    }
}
