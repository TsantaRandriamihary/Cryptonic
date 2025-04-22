package itu.p16.crypto.service.views;


import itu.p16.crypto.dto.analyse.CryptoAnalyseResult;
import itu.p16.crypto.model.views.ViewCryptoHistorique;
import itu.p16.crypto.repository.views.ViewCryptoHistoriqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ViewCryptoHistoriqueService {

    @Autowired
    private ViewCryptoHistoriqueRepository historiqueRepository;

    /**
     * Effectue une analyse statistique des cryptos sur une période donnée
     */
    public List<CryptoAnalyseResult> analyserCryptos(Timestamp startDate, Timestamp endDate) {
        List<ViewCryptoHistorique> historiques = historiqueRepository.findByDateChangementBetween(startDate, endDate);
        Map<Integer, List<ViewCryptoHistorique>> historiquesParCrypto = historiques.stream()
                .collect(Collectors.groupingBy(ViewCryptoHistorique::getIdCrypto));

        List<CryptoAnalyseResult> analyses = new ArrayList<>();

        for (Map.Entry<Integer, List<ViewCryptoHistorique>> entry : historiquesParCrypto.entrySet()) {
            int idCrypto = entry.getKey();
            List<ViewCryptoHistorique> data = entry.getValue();

            List<Double> prixList = data.stream().map(ViewCryptoHistorique::getPrixHistorique).sorted().toList();

            double min = Collections.min(prixList);
            double max = Collections.max(prixList);
            double moyenne = prixList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double ecartType = calculerEcartType(prixList, moyenne);
            double premierQuartile = calculerPremierQuartile(prixList);

            ViewCryptoHistorique premier = data.get(0);
            CryptoAnalyseResult result = new CryptoAnalyseResult(
                    idCrypto,
                    premier.getCryptoNom(),
                    premier.getCryptoSymbole(),
                    min, max, moyenne, ecartType, premierQuartile
            );

            analyses.add(result);
        }

        return analyses;
    }

    /**
     * Calcule l'écart-type d'une liste de prix
     */
    private double calculerEcartType(List<Double> valeurs, double moyenne) {
        double variance = valeurs.stream()
                .mapToDouble(val -> Math.pow(val - moyenne, 2))
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }

    /**
     * Calcule le premier quartile d'une liste triée
     */
    private double calculerPremierQuartile(List<Double> valeurs) {
        if (valeurs.isEmpty()) return 0.0;
        int indexQ1 = (int) Math.floor(valeurs.size() * 0.25);
        return valeurs.get(indexQ1);
    }



    public List<Map<String, Object>> filtrerAnalyse(
            Timestamp startDate, Timestamp endDate, List<Integer> idCryptos, String analyseType) {

        List<CryptoAnalyseResult> analyses = analyserCryptos(startDate, endDate);
        List<CryptoAnalyseResult> analysesFiltrees = analyses.stream()
                .filter(a -> idCryptos.contains(a.getIdCrypto()))
                .toList();

        return analysesFiltrees.stream()
                .map(a -> extraireAnalyse(a, analyseType))
                .toList();
    }

    /**
     * Extrait uniquement l'analyse demandée sous forme de Map
     */
    private Map<String, Object> extraireAnalyse(CryptoAnalyseResult result, String analyseType) {
        Map<String, Object> analyseMap = new HashMap<>();
        analyseMap.put("idCrypto", result.getIdCrypto());
        analyseMap.put("cryptoNom", result.getCryptoNom());
        analyseMap.put("cryptoSymbole", result.getCryptoSymbole());

        switch (analyseType.toLowerCase()) {
            case "min" -> analyseMap.put("Min", result.getMin());
            case "max" -> analyseMap.put("Max", result.getMax());
            case "moyenne" -> analyseMap.put("Moyenne", result.getMoyenne());
            case "ecartype" -> analyseMap.put("EcartType", result.getEcartType());
            case "quartile" -> analyseMap.put("PremierQuartile", result.getPremierQuartile());
            case "tous" -> {
                analyseMap.put("Min", result.getMin());
                analyseMap.put("Max", result.getMax());
                analyseMap.put("Moyenne", result.getMoyenne());
                analyseMap.put("EcartType", result.getEcartType());
                analyseMap.put("PremierQuartile", result.getPremierQuartile());
            }
            default -> throw new IllegalArgumentException("Type d'analyse invalide: " + analyseType);
        }

        return analyseMap;
    }
}
