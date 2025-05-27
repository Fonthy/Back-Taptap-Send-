package com.example.taptapsend.service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import com.example.taptapsend.model.TauxDTO;
import com.example.taptapsend.model.Taux;

import com.example.taptapsend.repository.TauxRepository;
import org.springframework.stereotype.Service;

@Service
public class TauxServiceImpl implements TauxService {
    
    private final TauxRepository TauxRepository;

    public TauxServiceImpl(TauxRepository TauxRepository){
        this.TauxRepository=TauxRepository;
    }

    /*@Override
    public TauxRepository getTauxRepository(){
        return TauxRepository;
    }*/

    @Override
    public List<TauxDTO> getAllTaux(){
        return TauxRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<TauxDTO> getTauxByIdTaux(String idTaux){
        return TauxRepository.findById(idTaux).map(this::convertToDTO);
    }

    @Override
    public TauxDTO saveTaux(TauxDTO TauxDTO){
        if(TauxRepository.existsById(TauxDTO.idTaux())){
            throw new RuntimeException("Taux déjà existant");
        }
        Taux Taux=TauxRepository.save(convertToEntity(TauxDTO));
        return convertToDTO(Taux);
    }

    @Override
    public TauxDTO updateTaux(String numTel,TauxDTO TauxDTO){
        Taux Taux = TauxRepository.findById(numTel).orElseThrow();
        Taux.setIdTaux(TauxDTO.idTaux());
        Taux.setMontant1(TauxDTO.montant1());
        Taux.setMontant2(TauxDTO.montant2());
        Taux updatedTaux=TauxRepository.save(Taux);
        return convertToDTO(updatedTaux);
    }

    @Override
    public void deleteTaux(String idTaux){
        if(!TauxRepository.existsById(idTaux)){
            throw new RuntimeException("Taux inéxistant");
        }
        TauxRepository.deleteById(idTaux);
    }

    private TauxDTO convertToDTO(Taux Taux){
        return new TauxDTO(Taux.getIdTaux(),Taux.getMontant1(),Taux.getMontant2());
    }

    private Taux convertToEntity(TauxDTO TauxDTO){
        Taux Taux = new Taux();
        Taux.setIdTaux(TauxDTO.idTaux());
        Taux.setMontant1(TauxDTO.montant1());
        Taux.setMontant2(TauxDTO.montant2());
        return Taux;
    }
}

