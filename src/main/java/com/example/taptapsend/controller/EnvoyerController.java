package com.example.taptapsend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import com.example.taptapsend.model.Envoyer;
import com.example.taptapsend.model.EnvoyerDTO;
import com.example.taptapsend.model.EnvoyerSelectDTO;
import com.example.taptapsend.model.RecetteModel;
import com.example.taptapsend.service.EnvoyerService;
import java.math.BigDecimal;
import com.example.taptapsend.service.GeneratePdfService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@CrossOrigin
@RestController
@RequestMapping("/api/envoyer")
public class EnvoyerController{

    private final EnvoyerService envoyerService;
    @Autowired
    private GeneratePdfService pdfService;

    public EnvoyerController(EnvoyerService envoyerService){
        this.envoyerService=envoyerService;
    }

    @GetMapping
    public List<EnvoyerSelectDTO> getAllEnvoyer() {
        return envoyerService.getAllEnvoyer();
    }

    @GetMapping("/{date}")
    public List<EnvoyerSelectDTO> getEnvoyerByDate(@PathVariable String date) {
        return envoyerService.getEnvoyerByDate(date);
    }

    @GetMapping("/recette")
    public BigDecimal getRecette() {
        return envoyerService.getRecette();
    }
    
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> genererReleve(
        @RequestParam String numTel,
        @RequestParam int mois,
        @RequestParam int annee
        ) {
        byte[] pdfBytes = pdfService.genererRelevePDF(numTel, mois,annee);
        
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "releve.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @PostMapping
    public EnvoyerDTO createEnvoyer(@RequestBody EnvoyerDTO EnvoyerDTO) {
        return envoyerService.saveEnvoyer(EnvoyerDTO);
    }
    
    @PutMapping("/{idEnv}")
    public ResponseEntity<EnvoyerSelectDTO> updateClient(@PathVariable String idEnv, @RequestBody EnvoyerSelectDTO EnvoyerSelectDTO) {
        try {
            EnvoyerSelectDTO updateEnvoyer = envoyerService.updateEnvoyer(idEnv, EnvoyerSelectDTO);
            return ResponseEntity.ok(updateEnvoyer);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{idEnv}")
    public ResponseEntity<String> deleteClient(@PathVariable String idEnv){
        try {
            envoyerService.deleteEnvoyer(idEnv);
            return ResponseEntity.ok("Suppréssion réussie");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}