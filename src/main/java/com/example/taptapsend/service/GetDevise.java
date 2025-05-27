package com.example.taptapsend.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class GetDevise {

    @Autowired
    private RestTemplate restTemplate;

    public GetDevise(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    public String getCurrencyName(String countryName) {
        String url = "https://restcountries.com/v3.1/name/" + countryName;

        try {
            ResponseEntity<JsonNode[]> response = restTemplate.getForEntity(url, JsonNode[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode currencies = response.getBody()[0].get("currencies");
                if (currencies != null && currencies.fields().hasNext()) {
                    Map.Entry<String, JsonNode> entry = currencies.fields().next();
                    return entry.getValue().get("name").asText(); // Ex: "Euro"
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Devise inconnue";
    }
}
