package com.example.taptapsend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import com.example.taptapsend.model.ClientDTO;
import com.example.taptapsend.service.ClientService;

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
@RequestMapping("/api/clients")
public class ClientController{

    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService=clientService;
    }

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }
    
    @GetMapping("/{numTel}")
    public ResponseEntity<ClientDTO> getClientByNumTel(@PathVariable String numTel) {
        Optional<ClientDTO> client=clientService.getClientByNumTel(numTel);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nom/{nom}")
    public List<ClientDTO> getClientByNom(@PathVariable String nom) {
        return clientService.getAllClientsByNom(nom);
    }

    @PostMapping
    public ClientDTO createClient(@RequestBody ClientDTO clientDTO) {
        return clientService.saveClient(clientDTO);
    }
    
    @PutMapping("/{numTel}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable String numTel, @RequestBody ClientDTO clientDTO) {
        try {
            ClientDTO updatedClient = clientService.updateClient(numTel, clientDTO);
            return ResponseEntity.ok(updatedClient);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{numTel}")
    public ResponseEntity<String> deleteClient(@PathVariable String numTel){
        try {
            clientService.deleteClient(numTel);
            return ResponseEntity.ok("Suppréssion réussie");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}