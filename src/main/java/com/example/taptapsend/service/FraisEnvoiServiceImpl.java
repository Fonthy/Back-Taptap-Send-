package com.example.taptapsend.service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import com.example.taptapsend.model.FraisEnvoiDTO;
import com.example.taptapsend.model.FraisEnvoi;

import com.example.taptapsend.repository.FraisEnvoiRepository;
import org.springframework.stereotype.Service;

@Service
public class FraisEnvoiServiceImpl implements FraisEnvoiService {
    
    private final FraisEnvoiRepository FraisEnvoiRepository;

    public FraisEnvoiServiceImpl(FraisEnvoiRepository FraisEnvoiRepository){
        this.FraisEnvoiRepository=FraisEnvoiRepository;
    }

    /*@Override
    public FraisEnvoiRepository getFraisEnvoiRepository(){
        return FraisEnvoiRepository;
    }*/

    @Override
    public List<FraisEnvoiDTO> getAllFraisEnvois(){
        return FraisEnvoiRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<FraisEnvoiDTO> getFraisEnvoiByIdFrais(String idFrais){
        return FraisEnvoiRepository.findById(idFrais).map(this::convertToDTO);
    }

    @Override
    public FraisEnvoiDTO saveFraisEnvoi(FraisEnvoiDTO FraisEnvoiDTO){
        if(FraisEnvoiRepository.existsById(FraisEnvoiDTO.idFrais())){
            throw new RuntimeException("FraisEnvoi déjà existant");
        }
        FraisEnvoi FraisEnvoi=FraisEnvoiRepository.save(convertToEntity(FraisEnvoiDTO));
        return convertToDTO(FraisEnvoi);
    }

    @Override
    public FraisEnvoiDTO updateFraisEnvoi(String idFrais,FraisEnvoiDTO FraisEnvoiDTO){
        FraisEnvoi FraisEnvoi = FraisEnvoiRepository.findById(idFrais).orElseThrow();
        FraisEnvoi.setIdFrais(FraisEnvoiDTO.idFrais());
        FraisEnvoi.setMontant1(FraisEnvoiDTO.montant1());
        FraisEnvoi.setMontant2(FraisEnvoiDTO.montant2());
        FraisEnvoi.setFrais(FraisEnvoiDTO.frais());
        FraisEnvoi updatedFraisEnvoi=FraisEnvoiRepository.save(FraisEnvoi);
        return convertToDTO(updatedFraisEnvoi);
    }

    @Override
    public void deleteFraisEnvoi(String idFrais){
        if(!FraisEnvoiRepository.existsById(idFrais)){
            throw new RuntimeException("FraisEnvoi inéxistant");
        }
        FraisEnvoiRepository.deleteById(idFrais);
    }

    private FraisEnvoiDTO convertToDTO(FraisEnvoi FraisEnvoi){
        return new FraisEnvoiDTO(FraisEnvoi.getIdFrais(),FraisEnvoi.getMontant1(),FraisEnvoi.getMontant2(),FraisEnvoi.getFrais());
    }

    private FraisEnvoi convertToEntity(FraisEnvoiDTO FraisEnvoiDTO){
        FraisEnvoi FraisEnvoi = new FraisEnvoi();
        FraisEnvoi.setIdFrais(FraisEnvoiDTO.idFrais());
        FraisEnvoi.setMontant1(FraisEnvoiDTO.montant1());
        FraisEnvoi.setMontant2(FraisEnvoiDTO.montant2());
        FraisEnvoi.setFrais(FraisEnvoiDTO.frais());
        return FraisEnvoi;
    }
}

