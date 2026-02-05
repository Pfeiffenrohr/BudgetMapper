package de.lechner.budgetmapper.scheduler;


import de.lechner.budgetmapper.kontofiles.KontofileService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class MapperJob implements Job{
    String fileKonto1 = "C:/temp/20250206-539619-umsatz.CSV";
    @Autowired
    private KontofileService kontofileService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            kontofileService.readCsvFile(fileKonto1);
        } catch (Exception ex ) {
            System.err.println("Exception " +ex );
        }
    }
}