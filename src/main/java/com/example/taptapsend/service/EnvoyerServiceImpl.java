package com.example.taptapsend.service;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.taptapsend.model.EnvoyerDTO;
import com.example.taptapsend.model.EnvoyerSelectDTO;
import com.example.taptapsend.model.FraisEnvoi;
import com.example.taptapsend.model.RecetteModel;
import com.example.taptapsend.model.Taux;
import com.example.taptapsend.model.Client;
import com.example.taptapsend.model.Envoyer;
import com.example.taptapsend.repository.ClientRepository;
import com.example.taptapsend.repository.EnvoyerRepository;
import com.example.taptapsend.repository.FraisEnvoiRepository;
import com.example.taptapsend.repository.TauxRepository;

import org.springframework.stereotype.Service;

@Service
public class EnvoyerServiceImpl implements EnvoyerService {
    
    private final FraisEnvoiRepository FraisEnvoiRepository;
    private final TauxRepository TauxRepository;
    private final EnvoyerRepository EnvoyerRepository;
    private final ClientRepository ClientRepository;
    private final MailService mailService;
    private final GetDevise getDevise;

    public EnvoyerServiceImpl(EnvoyerRepository EnvoyerRepository,TauxRepository TauxRepository,FraisEnvoiRepository FraisEnvoiRepository,ClientRepository ClientRepository,MailService mailService,GetDevise getDevise){
        this.EnvoyerRepository=EnvoyerRepository;
        this.ClientRepository=ClientRepository;
        this.TauxRepository=TauxRepository;
        this.FraisEnvoiRepository=FraisEnvoiRepository;
        this.mailService=mailService;
        this.getDevise=getDevise;
    }

    @Override
    public List<EnvoyerSelectDTO> getAllEnvoyer(){
        return EnvoyerRepository.findAll().stream()
            .map(this::convertToDTOSelect)
            .collect(Collectors.toList());
    }

    @Override
    public EnvoyerDTO saveEnvoyer(EnvoyerDTO EnvoyerDTO){
        Envoyer envoyer = convertToEntity(EnvoyerDTO);
        Client envoyeur = ClientRepository.findByNumTel(envoyer.getNumEnvoyeur()).orElseThrow();
        Client recepteur = ClientRepository.findByNumTel(envoyer.getNumRecepteur()).orElseThrow(); 
        BigDecimal montantEnvoyeur = envoyeur.getSolde();
        BigDecimal montantRecepteur = recepteur.getSolde();
        BigDecimal montantRecu=new BigDecimal(0);
        BigDecimal montantEnvoye=new BigDecimal(0);

        String idSearch = envoyeur.getPays()+"-"+recepteur.getPays();
        Taux taux = TauxRepository.findByIdTaux(idSearch).orElse(null);
        FraisEnvoi frais = FraisEnvoiRepository.getFrais(idSearch, EnvoyerDTO.montant());
        BigDecimal fraisEnvoi=frais.getFrais();
        if(taux==null){
            idSearch = recepteur.getPays()+"-"+envoyeur.getPays();
            taux = TauxRepository.findByIdTaux(idSearch).orElseThrow();
            montantEnvoye = EnvoyerDTO.montant().add(fraisEnvoi).setScale(2,RoundingMode.HALF_UP);
            montantRecu = (taux.getMontant1().multiply(EnvoyerDTO.montant())).divide(taux.getMontant2()).setScale(2,RoundingMode.HALF_UP);
        }
       else{
            montantEnvoye = EnvoyerDTO.montant().add(fraisEnvoi).setScale(2,RoundingMode.HALF_UP);
            montantRecu = (taux.getMontant2().multiply(EnvoyerDTO.montant())).divide(taux.getMontant1()).setScale(2,RoundingMode.HALF_UP);
       }
        envoyer.setIdFrais(frais.getIdFrais());
        EnvoyerDTO= convertToDTO(envoyer);
        envoyeur.setSolde(montantEnvoyeur.subtract(montantEnvoye).setScale(2,RoundingMode.HALF_UP));
        recepteur.setSolde(montantRecepteur.add(montantRecu).setScale(2,RoundingMode.HALF_UP));
        if(envoyeur.getSolde().compareTo(new BigDecimal(0))<=0){
            throw new RuntimeException("Solde insuffisant pour l'envoyeur");
        }
        Envoyer Envoyer=EnvoyerRepository.save(convertToEntity(EnvoyerDTO));
        ClientRepository.save(envoyeur);
        ClientRepository.save(recepteur);
        sendMail(envoyeur, montantEnvoyeur,montantEnvoye, fraisEnvoi);
        sendMail(recepteur, montantRecepteur,montantRecu, fraisEnvoi);
        return convertToDTO(Envoyer);
    }

    @Override 
    public List<EnvoyerSelectDTO> getEnvoyerByDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate search = LocalDate.parse(date,formatter);
        return EnvoyerRepository.getEnvoyerByDate(search).stream()
            .map(this::convertToDTOSelect)
            .collect(Collectors.toList());

    }

    @Override
    public EnvoyerSelectDTO updateEnvoyer(String idEnv,EnvoyerSelectDTO EnvoyerDTO){
        Envoyer envoyer = convertToEntityUpdate(EnvoyerDTO);
        Envoyer Envoyer = EnvoyerRepository.findOneByIdEnv(idEnv).orElseThrow();
        BigDecimal montantEnvoye=new BigDecimal(0);
        BigDecimal montantRecu=new BigDecimal(0);
        /* Remettre aux conditions initials */
        Client envoyeur = ClientRepository.findByNumTel(Envoyer.getNumEnvoyeur()).orElseThrow();
        Client recepteur = ClientRepository.findByNumTel(Envoyer.getNumRecepteur()).orElseThrow(); 
        String idSearch = envoyeur.getPays()+"-"+recepteur.getPays();
        Taux taux = TauxRepository.findByIdTaux(idSearch).orElse(null);
        FraisEnvoi frais = FraisEnvoiRepository.getFrais(idSearch, EnvoyerDTO.montant());
        BigDecimal fraisEnvoi=frais.getFrais();
        if(taux==null){
            idSearch = recepteur.getPays()+"-"+envoyeur.getPays();
            taux = TauxRepository.findByIdTaux(idSearch).orElseThrow();
            montantEnvoye = EnvoyerDTO.montant().add(fraisEnvoi).setScale(2,RoundingMode.HALF_UP);
            montantRecu = (taux.getMontant1().multiply(EnvoyerDTO.montant())).divide(taux.getMontant2()).setScale(2,RoundingMode.HALF_UP);
        }
       else{
            montantEnvoye = EnvoyerDTO.montant().add(fraisEnvoi).setScale(2,RoundingMode.HALF_UP);
            montantRecu = (taux.getMontant2().multiply(EnvoyerDTO.montant())).divide(taux.getMontant1()).setScale(2,RoundingMode.HALF_UP);
       }
       envoyer.setIdFrais(frais.getIdFrais());
       EnvoyerDTO= convertToDTOSelect(envoyer);
        envoyeur.setSolde((envoyeur.getSolde()).add(montantEnvoye).setScale(2,RoundingMode.HALF_UP));
        recepteur.setSolde((recepteur.getSolde().subtract(montantRecu)).setScale(2,RoundingMode.HALF_UP));

        /* tout mettre à jour y compris les montants */
        envoyeur = ClientRepository.findByNumTel(EnvoyerDTO.numEnvoyeur()).orElseThrow();
        recepteur = ClientRepository.findByNumTel(EnvoyerDTO.numRecepteur()).orElseThrow(); 
        frais = FraisEnvoiRepository.getFrais(idSearch, EnvoyerDTO.montant());
        fraisEnvoi=frais.getFrais();
        envoyeur.setSolde((envoyeur.getSolde().subtract(EnvoyerDTO.montant()).subtract(fraisEnvoi)).setScale(2,RoundingMode.HALF_UP));
        recepteur.setSolde((recepteur.getSolde().add((taux.getMontant2().multiply(EnvoyerDTO.montant()).divide(taux.getMontant1())))).setScale(2,RoundingMode.HALF_UP));
        if(envoyeur.getSolde().compareTo(new BigDecimal(0))<=0){
            throw new RuntimeException("Solde insuffisant pour l'envoyeur");
        }
        Envoyer.setIdEnv(EnvoyerDTO.idEnv());
        Envoyer.setMontant(EnvoyerDTO.montant());
        Envoyer.setRaison(EnvoyerDTO.raison());
        Envoyer.setNumEnvoyeur(EnvoyerDTO.numEnvoyeur());
        Envoyer.setNumRecepteur(EnvoyerDTO.numRecepteur());
        Envoyer.setDate(EnvoyerDTO.date());
        Envoyer updatedEnvoyer=EnvoyerRepository.save(Envoyer);
        ClientRepository.save(envoyeur);
        ClientRepository.save(recepteur);
        return convertToDTOSelect(updatedEnvoyer);
    }

    @Override
    public void deleteEnvoyer(String idEnv){
        Envoyer Envoyer = EnvoyerRepository.findOneByIdEnv(idEnv).orElseThrow();
        BigDecimal montantEnvoye=new BigDecimal(0);
        BigDecimal montantRecu=new BigDecimal(0);
        /* Remettre aux conditions initials */
        Client envoyeur = ClientRepository.findByNumTel(Envoyer.getNumEnvoyeur()).orElseThrow();
        Client recepteur = ClientRepository.findByNumTel(Envoyer.getNumRecepteur()).orElseThrow(); 
        String idSearch = envoyeur.getPays()+"-"+recepteur.getPays();
        Taux taux = TauxRepository.findByIdTaux(idSearch).orElse(null);
        FraisEnvoi frais = FraisEnvoiRepository.getFrais(idSearch, Envoyer.getMontant());
        BigDecimal fraisEnvoi=frais.getFrais();
        if(taux==null){
            idSearch = recepteur.getPays()+"-"+envoyeur.getPays();
            taux = TauxRepository.findByIdTaux(idSearch).orElseThrow();
            montantEnvoye = Envoyer.getMontant().add(fraisEnvoi).setScale(2,RoundingMode.HALF_UP);
            montantRecu = (taux.getMontant1().multiply(Envoyer.getMontant())).divide(taux.getMontant2()).setScale(2,RoundingMode.HALF_UP);
        }
        else{
             montantEnvoye = Envoyer.getMontant().add(fraisEnvoi).setScale(2,RoundingMode.HALF_UP);
             montantRecu = (taux.getMontant2().multiply(Envoyer.getMontant())).divide(taux.getMontant1()).setScale(2,RoundingMode.HALF_UP);
        }  
        envoyeur.setSolde((envoyeur.getSolde()).add(montantEnvoye).setScale(2,RoundingMode.HALF_UP));
        recepteur.setSolde((recepteur.getSolde().subtract(montantRecu)).setScale(2,RoundingMode.HALF_UP));
        if(!EnvoyerRepository.existsByIdEnv(idEnv)){
            throw new RuntimeException("Envoyer inéxistant");
        }
        EnvoyerRepository.delete(Envoyer);
        ClientRepository.save(envoyeur);
        ClientRepository.save(recepteur);
    }

    @Override
    public BigDecimal getRecette(){
        BigDecimal recetteTotal = new BigDecimal(0);
        List<RecetteModel> envoyer = EnvoyerRepository.getRecette();
        for(RecetteModel recette : envoyer){
            String tab = recette.getIdFrais().split("-")[0];
            if (!tab.equals("suisse")) {
                recetteTotal=recetteTotal.add((TauxRepository.findById("suisse-"+tab).get().getMontant1().multiply(recette.getTotal())).divide(TauxRepository.findById("suisse-"+tab).get().getMontant2()));
                //System.out.println((TauxRepository.findById("suisse-"+tab).get().getMontant1().multiply(recette.getTotal())).divide(TauxRepository.findById("suisse-"+tab).get().getMontant2()));
            }
            else{
                recetteTotal=recetteTotal.add(recette.getTotal());
            }
        }
        return recetteTotal;
    }

    
    private EnvoyerDTO convertToDTO(Envoyer Envoyer){
        return new EnvoyerDTO(Envoyer.getIdEnv(),Envoyer.getNumEnvoyeur(),Envoyer.getNumRecepteur(),Envoyer.getMontant(),Envoyer.getRaison(),Envoyer.getIdFrais());
    }

    private EnvoyerSelectDTO convertToDTOSelect(Envoyer Envoyer){
        return new EnvoyerSelectDTO(Envoyer.getIdEnv(),Envoyer.getNumEnvoyeur(),Envoyer.getNumRecepteur(),Envoyer.getDate(),Envoyer.getMontant(),Envoyer.getRaison());
    }

    private Envoyer convertToEntity(EnvoyerDTO EnvoyerDTO){
        Envoyer Envoyer = new Envoyer();
        Envoyer.setIdEnv(EnvoyerDTO.idEnv());
        Envoyer.setMontant(EnvoyerDTO.montant());
        Envoyer.setNumEnvoyeur(EnvoyerDTO.numEnvoyeur());
        Envoyer.setNumRecepteur(EnvoyerDTO.numRecepteur());
        Envoyer.setRaison(EnvoyerDTO.raison());
        Envoyer.setDate(LocalDateTime.now());
        Envoyer.setIdFrais(EnvoyerDTO.idFrais());
        return Envoyer;
    }

    private Envoyer convertToEntityUpdate(EnvoyerSelectDTO EnvoyerDTO){
        Envoyer Envoyer = new Envoyer();
        Envoyer.setIdEnv(EnvoyerDTO.idEnv());
        Envoyer.setMontant(EnvoyerDTO.montant());
        Envoyer.setNumEnvoyeur(EnvoyerDTO.numEnvoyeur());
        Envoyer.setNumRecepteur(EnvoyerDTO.numRecepteur());
        Envoyer.setRaison(EnvoyerDTO.raison());
        Envoyer.setDate(EnvoyerDTO.date());
        return Envoyer;
    }

    public void sendMail(Client  client,BigDecimal montantInitial,BigDecimal montant,BigDecimal frais){/* */
        String subject = "Transaction du "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String body = "\nMontant envoyé : "+montant+" "+getDevise.getCurrencyName(client.getPays())+"\nFrais d'envoi : "+frais+" "+getDevise.getCurrencyName(client.getPays())+"\nSolde initial : "+montantInitial+" "+getDevise.getCurrencyName(client.getPays())+"\nSolde actuel : "+client.getSolde()+" "+getDevise.getCurrencyName(client.getPays());
        mailService.sendMessage(client.getMail(), subject, body);
    }
}

