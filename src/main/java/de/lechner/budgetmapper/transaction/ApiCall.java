package de.lechner.budgetmapper.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiCall {
    @Value("${budgetserver.host}")
    private String host;
    @Value("${budgetserver.port}")
    private String port;
    private static final Logger LOG = LoggerFactory.getLogger(ApiCall.class);
    public List<Transaction> getTransactions(String startdate, String enddate ) {
        //   LOG.info("Start getAllAnlagen");
        //   LOG.info("Host = "+ host);
        //   LOG.info("Port = "+ port);
        List<Transaction> list = new ArrayList<Transaction>();
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port)
                .path("//transactionWithDate")
                .queryParam("startdate", startdate)
                .queryParam("enddate", enddate)
                .build();
        String transactions = uriComponents.toUriString();
       // System.out.println(transactions);
        ResponseEntity<Transaction[]> response = restTemplate.getForEntity(transactions, Transaction[].class);
        if (response.hasBody()) {
            Transaction[] transaction = response.getBody();
            for (int i = 0; i < transaction.length; i++) {
                list.add(transaction[i]);
            }
        }
        return list;
    }
}
