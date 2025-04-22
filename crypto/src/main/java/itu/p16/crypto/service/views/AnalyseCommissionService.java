package itu.p16.crypto.service.views;

import itu.p16.crypto.model.views.ViewMouvementCrypto;
import itu.p16.crypto.repository.views.ViewMouvementCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyseCommissionService {

    @Autowired
    private ViewMouvementCryptoRepository viewMouvementCryptoRepository;

    public Map<String, List<ViewMouvementCrypto>> analyserCommissions(Timestamp dateMin, Timestamp dateMax, Integer idCrypto) {
        List<ViewMouvementCrypto> mouvements = viewMouvementCryptoRepository.findAll();
        if (dateMin != null) {
            mouvements = mouvements.stream()
                    .filter(mvt -> mvt.getDateMvt().after(dateMin) || mvt.getDateMvt().equals(dateMin))
                    .collect(Collectors.toList());
        }
        if (dateMax != null) {
            mouvements = mouvements.stream()
                    .filter(mvt -> mvt.getDateMvt().before(dateMax) || mvt.getDateMvt().equals(dateMax))
                    .collect(Collectors.toList());
        }

        if (idCrypto != null) {
            mouvements = mouvements.stream()
                    .filter(mvt -> mvt.getIdCrypto() == idCrypto)
                    .collect(Collectors.toList());
        }

        return mouvements.stream()
                .collect(Collectors.groupingBy(ViewMouvementCrypto::getCryptoNom));
    }



    public Map<String, Object> analyserCommissionsParType(Map<String, List<ViewMouvementCrypto>> resultatsAnalyse, String typeAnalyse) {
        Map<String, Object> analyseResultat = new HashMap<>();

        for (Map.Entry<String, List<ViewMouvementCrypto>> entry : resultatsAnalyse.entrySet()) {
            String cryptoNom = entry.getKey();
            List<ViewMouvementCrypto> mouvements = entry.getValue();

            double sommeCommissionAchat = mouvements.stream()
                    .mapToDouble(mvt -> mvt.getCommissionAchat() != null ? mvt.getCommissionAchat() : 0)
                    .sum();

            double sommeCommissionVente = mouvements.stream()
                    .mapToDouble(mvt -> mvt.getCommissionVente() != null ? mvt.getCommissionVente() : 0)
                    .sum();

            double moyenneCommissionAchat = mouvements.isEmpty() ? 0 : sommeCommissionAchat / mouvements.size();
            double moyenneCommissionVente = mouvements.isEmpty() ? 0 : sommeCommissionVente / mouvements.size();

            sommeCommissionAchat = Math.round(sommeCommissionAchat * 100.0) / 100.0;
            sommeCommissionVente = Math.round(sommeCommissionVente * 100.0) / 100.0;
            moyenneCommissionAchat = Math.round(moyenneCommissionAchat * 100.0) / 100.0;
            moyenneCommissionVente = Math.round(moyenneCommissionVente * 100.0) / 100.0;

            switch (typeAnalyse) {
                case "somme":
                    analyseResultat.put(cryptoNom, Map.of(
                            "sommeCommissionAchat", sommeCommissionAchat,
                            "sommeCommissionVente", sommeCommissionVente
                    ));
                    break;

                case "moyenne":
                    analyseResultat.put(cryptoNom, Map.of(
                            "moyenneCommissionAchat", moyenneCommissionAchat,
                            "moyenneCommissionVente", moyenneCommissionVente
                    ));
                    break;

                case "tous":
                    analyseResultat.put(cryptoNom, Map.of(
                            "sommeCommissionAchat", sommeCommissionAchat,
                            "sommeCommissionVente", sommeCommissionVente,
                            "moyenneCommissionAchat", moyenneCommissionAchat,
                            "moyenneCommissionVente", moyenneCommissionVente
                    ));
                    break;
            }
        }
        return analyseResultat;
    }
}
