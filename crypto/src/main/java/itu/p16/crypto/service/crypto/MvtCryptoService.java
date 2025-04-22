package itu.p16.crypto.service.crypto;

import itu.p16.crypto.model.crypto.CryptoHisto;
import itu.p16.crypto.model.crypto.MvtCrypto;
import itu.p16.crypto.model.transaction.Transaction;
import itu.p16.crypto.model.views.ViewFonds;
import itu.p16.crypto.model.views.ViewPortefeuille;
import itu.p16.crypto.repository.crypto.CryptoHistoRepository;
import itu.p16.crypto.repository.crypto.MvtCryptoRepository;
import itu.p16.crypto.repository.transaction.TransactionRepository;
import itu.p16.crypto.repository.views.ViewFondsRepository;
import itu.p16.crypto.repository.views.ViewPortefeuilleRepository;
import itu.p16.crypto.service.transaction.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MvtCryptoService {

    @Autowired
    private MvtCryptoRepository mvtCryptoRepository;

    @Autowired
    private CryptoHistoRepository cryptoHistoRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ViewFondsRepository viewFondsRepository;

    
    @Autowired
    private DemandeTransactionService demandeTransactionService;

    @Autowired
    private ViewPortefeuilleRepository viewPortefeuilleRepository;


    @Transactional
    public void tenterAchat(Integer userId, Integer idCrypto, Double quantite, Double montantAchat, String email) throws Exception {
        demandeTransactionService.creerDemandeTransaction(userId, 0, montantAchat * quantite, quantite, idCrypto);
    }

    
    @Transactional
    public void tenterVente(Integer userId, Integer idCrypto, Double quantite, Double montantVente, String email) throws Exception {
        demandeTransactionService.creerDemandeTransaction(userId, montantVente * quantite, 0, quantite, idCrypto);
    }



    public List<Map<String, Object>> getPortefeuille(Timestamp dateMax) {
        List<MvtCrypto> mouvements = mvtCryptoRepository.findAll();

        List<MvtCrypto> mouvementsFiltres = mouvements;
        if (dateMax != null) {
            mouvementsFiltres = mouvements.stream()
                .filter(mvt -> mvt.getDateMvt().before(dateMax) || mvt.getDateMvt().equals(dateMax))
                .collect(Collectors.toList());
        }
        
        Map<String, Map<String, Double>> portefeuille = new HashMap<>();

        for (MvtCrypto mvt : mouvementsFiltres) {
            String key = mvt.getIdUser() + "-" + mvt.getIdCrypto();
            portefeuille.putIfAbsent(key, new HashMap<>());
            Map<String, Double> details = portefeuille.get(key);

            double quantiteActuelle = details.getOrDefault("quantite", 0.0);
            if (mvt.getMontantAchat() > 0) {
                quantiteActuelle += mvt.getQuantite();
            } else if (mvt.getMontantVente() > 0) {
                quantiteActuelle -= mvt.getQuantite();
            }
            details.put("quantite", quantiteActuelle);
        }

        // Construire la liste des résultats
        List<Map<String, Object>> resultats = new ArrayList<>();
        for (String key : portefeuille.keySet()) {
            String[] parts = key.split("-");
            int idUser = Integer.parseInt(parts[0]);
            int idCrypto = Integer.parseInt(parts[1]);

            double quantite = portefeuille.get(key).get("quantite");
            CryptoHisto dernierPrix = null;
            if (dateMax != null) {
                dernierPrix = (cryptoHistoRepository.findLatestPriceBeforeDate(idCrypto, dateMax)).get();
            }
            else {
                dernierPrix = (cryptoHistoRepository.findLatestPrice(idCrypto)).get();
            }

            Map<String, Object> row = new HashMap<>();
            row.put("idUser", idUser);
            row.put("idCrypto", idCrypto);
            row.put("quantiteActuelle", quantite);
            row.put("prixUnitaireActuel", (dernierPrix != null) ? dernierPrix.getPrix() : 0.0);

            resultats.add(row);
        }

        return resultats;
    }


    public double getValeurTotalePortefeuille(int idUser, Timestamp dateMax) {
        List<Map<String, Object>> portefeuille = getPortefeuille(dateMax);
        double valeurTotale = portefeuille.stream()
                .filter(entry -> (int) entry.get("idUser") == idUser)  
                .mapToDouble(entry -> {
                    double quantite = (double) entry.get("quantiteActuelle");
                    double prixUnitaire = (double) entry.get("prixUnitaireActuel");
                    return quantite * prixUnitaire;  
                })
                .sum();

        return Math.round(valeurTotale * 100.0) / 100.0;
    }

}
