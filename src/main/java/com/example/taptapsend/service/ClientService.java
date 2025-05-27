package com.example.taptapsend.service;

import com.example.taptapsend.model.ClientDTO;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List <ClientDTO> getAllClients();
    List <ClientDTO> getAllClientsByNom(String nom);
    Optional<ClientDTO> getClientByNumTel(String numTel);
    ClientDTO saveClient(ClientDTO clientDTO);
    ClientDTO updateClient(String numTel,ClientDTO clientDTO);
    void deleteClient(String numTel);
}
