package com.example.taptapsend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

//import com.example.taptapsend.model.FraisEnvoi;
import com.example.taptapsend.model.FraisEnvoiDTO;
import com.example.taptapsend.service.FraisEnvoiService;

//import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/fraisEnvoi")
public class FraisEnvoiController{

    /*@Autowired
    Patcher patcher;*/

    private final FraisEnvoiService FraisEnvoiService;

    public FraisEnvoiController(FraisEnvoiService FraisEnvoiService){
        this.FraisEnvoiService=FraisEnvoiService;
    }

    @GetMapping
    public List<FraisEnvoiDTO> getAllFraisEnvois() {
        return FraisEnvoiService.getAllFraisEnvois();
    }
    
    @GetMapping("/{idFrais}")
    public ResponseEntity<FraisEnvoiDTO> getFraisEnvoiByIdFrais(@PathVariable String idFrais) {
        Optional<FraisEnvoiDTO> FraisEnvoi=FraisEnvoiService.getFraisEnvoiByIdFrais(idFrais);
        return FraisEnvoi.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public FraisEnvoiDTO createFraisEnvoi(@RequestBody FraisEnvoiDTO FraisEnvoiDTO) {
        return FraisEnvoiService.saveFraisEnvoi(FraisEnvoiDTO);
    }
    
    @PutMapping("/{idFrais}")
    public ResponseEntity<FraisEnvoiDTO> updateFraisEnvoi(@PathVariable String idFrais, @RequestBody FraisEnvoiDTO FraisEnvoiDTO) {
        /*FraisEnvoi FraisEnvoi = FraisEnvoiService.getFraisEnvoiRepository().findById(idFrais).orElseThrow();
        FraisEnvoiDTO existing=convertToDTO(FraisEnvoi);*/
        try {
            //patcher.patch(existing,FraisEnvoiDTO);
            FraisEnvoiDTO updatedFraisEnvoi = FraisEnvoiService.updateFraisEnvoi(idFrais, FraisEnvoiDTO);
            return ResponseEntity.ok(updatedFraisEnvoi);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idFrais}")
    public ResponseEntity<String> deleteFraisEnvoi(@PathVariable String idFrais){
        try {
            FraisEnvoiService.deleteFraisEnvoi(idFrais);
            return ResponseEntity.ok("Suppréssion réussie");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /*private FraisEnvoiDTO convertToDTO(FraisEnvoi FraisEnvoi){
        return new FraisEnvoiDTO(FraisEnvoi.getMail(),FraisEnvoi.getNom(),FraisEnvoi.getSexe(),FraisEnvoi.getPays(),FraisEnvoi.getNumTel(),FraisEnvoi.getSolde());
    }*/
    
}
