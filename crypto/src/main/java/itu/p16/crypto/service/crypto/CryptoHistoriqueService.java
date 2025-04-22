package itu.p16.crypto.service.crypto;


import itu.p16.crypto.model.views.ViewCryptoHistorique;
import itu.p16.crypto.repository.views.ViewCryptoHistoriqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CryptoHistoriqueService {

    @Autowired
    private ViewCryptoHistoriqueRepository viewCryptoHistoriqueRepository;

    public List<ViewCryptoHistorique> getHistoriqueDepuisDate(Timestamp dateChangement) {
        return viewCryptoHistoriqueRepository.findByDateChangementAfter(dateChangement);
    }

    public List<ViewCryptoHistorique> getHistoriqueEntreDates(Timestamp startDate, Timestamp endDate) {
        return viewCryptoHistoriqueRepository.findByDateChangementBetween(startDate, endDate);
    }
}
