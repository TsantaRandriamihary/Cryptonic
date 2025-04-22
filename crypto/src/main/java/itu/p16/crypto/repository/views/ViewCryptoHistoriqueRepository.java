package itu.p16.crypto.repository.views;


import itu.p16.crypto.model.views.ViewCryptoHistorique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ViewCryptoHistoriqueRepository extends JpaRepository<ViewCryptoHistorique, Integer> {

    List<ViewCryptoHistorique> findByDateChangementAfter(Timestamp dateChangement);
    List<ViewCryptoHistorique> findByDateChangementBetween(Timestamp startDate, Timestamp endDate);
}
