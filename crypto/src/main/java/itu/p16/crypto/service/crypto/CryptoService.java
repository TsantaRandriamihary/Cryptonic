package itu.p16.crypto.service.crypto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import itu.p16.crypto.model.crypto.Crypto;
import itu.p16.crypto.model.crypto.CryptoHisto;
import itu.p16.crypto.repository.crypto.CryptoHistoRepository;
import itu.p16.crypto.repository.crypto.CryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

@Service
public class CryptoService {

    
    @Autowired
    private CryptoRepository cryptoRepository;

    @Autowired
    private CryptoHistoRepository cryptoHistoRepository;

    private static Integer dureeCrypto = null;

    public void genererPrixCryptos() {
        if (dureeCrypto == null) {
            dureeCrypto = 0;
        }
    
        List<Crypto> cryptos = cryptoRepository.findAll();
        Random rand = new Random();
    
        for (Crypto crypto : cryptos) {
            double prixMin = crypto.getPrixMin();
            double prixMax = crypto.getPrixMax();
    
            // Générer un prix aléatoire
            double prix = prixMin + (prixMax - prixMin) * rand.nextDouble();
    
            // Arrondir à 2 chiffres après la virgule avec BigDecimal
            BigDecimal prixArrondi = new BigDecimal(prix).setScale(2, RoundingMode.HALF_UP);
    
            // Sauvegarder l'historique avec le prix arrondi
            CryptoHisto cryptoHisto = new CryptoHisto();
            cryptoHisto.setCrypto(crypto);
            cryptoHisto.setPrix(prixArrondi.doubleValue());  // Convertir le prix arrondi en double
            cryptoHisto.setDateModif(new Timestamp(System.currentTimeMillis()));
            cryptoHistoRepository.save(cryptoHisto);
    
            // Mettre à jour le prix de la crypto avec le prix arrondi
            crypto.setPrix(prixArrondi.doubleValue());  // Convertir également ici
            cryptoRepository.save(crypto);
        }
    
        System.out.println("Prix des cryptos générés et mis à jour.");
    }
    
    @Scheduled(fixedRateString = "#{${duree.crypto} * 1000}") 
    public void miseAJourPrixCryptos() {
        genererPrixCryptos(); 
    }


    public List<Crypto> getAll() {
        return cryptoRepository.findAll();
    }
}
