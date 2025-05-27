package com.example.taptapsend.service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import com.example.taptapsend.model.ClientDTO;
import com.example.taptapsend.model.Client;

import com.example.taptapsend.repository.ClientRepository;

import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository){
        this.clientRepository=clientRepository;
    }

    @Override
    public List<ClientDTO> getAllClients(){
        return clientRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ClientDTO> getAllClientsByNom(String nom){
        return clientRepository.findByNomContains(nom).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<ClientDTO> getClientByNumTel(String numTel){
        return clientRepository.findById(numTel).map(this::convertToDTO);
    }

    @Override
    public ClientDTO saveClient(ClientDTO clientDTO){
        if(clientRepository.existsById(clientDTO.numTel())){
            throw new RuntimeException("Client déjà existant");
        }
        Client client=clientRepository.save(convertToEntity(clientDTO));
        return convertToDTO(client);
    }

    @Override
    public ClientDTO updateClient(String numTel,ClientDTO clientDTO){
        Client client = clientRepository.findById(numTel).orElseThrow();
        client.setNom(clientDTO.nom());
        client.setSexe(clientDTO.sexe());
        client.setPays(clientDTO.pays());
        client.setSolde(clientDTO.solde());
        client.setMail(clientDTO.mail());
        client.setNumTel(clientDTO.numTel());
        Client updatedClient=clientRepository.save(client);
        return convertToDTO(updatedClient);
    }

    @Override
    public void deleteClient(String numTel){
        if(!clientRepository.existsById(numTel)){
            throw new RuntimeException("Client inéxistant");
        }
        clientRepository.deleteById(numTel);
    }

    private ClientDTO convertToDTO(Client client){
        return new ClientDTO(client.getNumTel(),client.getMail(),client.getNom(),client.getPays(),client.getSexe(),client.getSolde());
    }

    private Client convertToEntity(ClientDTO clientDTO){
        Client client = new Client();
        client.setNom(clientDTO.nom());
        client.setSexe(clientDTO.sexe());
        client.setPays(clientDTO.pays());
        client.setSolde(clientDTO.solde());
        client.setMail(clientDTO.mail());
        client.setNumTel(clientDTO.numTel());
        return client;
    }
}

