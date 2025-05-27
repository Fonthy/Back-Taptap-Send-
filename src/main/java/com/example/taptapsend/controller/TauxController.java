package com.example.taptapsend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

//import com.example.taptapsend.model.Taux;
import com.example.taptapsend.model.TauxDTO;
import com.example.taptapsend.service.TauxService;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;




@CrossOrigin
@RestController
@RequestMapping("/api/taux")
public class TauxController{

    /*@Autowired
    Patcher patcher;*/

    private final TauxService TauxService;

    public TauxController(TauxService TauxService){
        this.TauxService=TauxService;
    }

    @GetMapping
    public List<TauxDTO> getAllTaux() {
        return TauxService.getAllTaux();
    }
    
    @GetMapping("/{idTaux}")
    public ResponseEntity<TauxDTO> getTauxByIdTaux(@PathVariable String idTaux) {
        Optional<TauxDTO> Taux=TauxService.getTauxByIdTaux(idTaux);
        return Taux.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TauxDTO createTaux(@RequestBody TauxDTO TauxDTO) {
        return TauxService.saveTaux(TauxDTO);
    }
    
    @PutMapping("/{idTaux}")
    public ResponseEntity<TauxDTO> updateTaux(@PathVariable String idTaux, @RequestBody TauxDTO TauxDTO) {
        /*Taux Taux = TauxService.getTauxRepository().findById(idTaux).orElseThrow();
        TauxDTO existing=convertToDTO(Taux);*/
        try {
            //patcher.patch(existing,TauxDTO);
            TauxDTO updatedTaux = TauxService.updateTaux(idTaux, TauxDTO);
            return ResponseEntity.ok(updatedTaux);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idTaux}")
    public ResponseEntity<String> deleteTaux(@PathVariable String idTaux){
        try {
            TauxService.deleteTaux(idTaux);
            return ResponseEntity.ok("Suppréssion réussie");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /*private TauxDTO convertToDTO(Taux Taux){
        return new TauxDTO(Taux.getMail(),Taux.getNom(),Taux.getSexe(),Taux.getPays(),Taux.getNumTel(),Taux.getSolde());
    }*/
    
}