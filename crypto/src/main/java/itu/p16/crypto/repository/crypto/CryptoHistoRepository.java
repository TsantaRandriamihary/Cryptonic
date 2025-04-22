package itu.p16.crypto.repository.crypto;

import itu.p16.crypto.model.crypto.CryptoHisto;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CryptoHistoRepository extends JpaRepository<CryptoHisto, Integer> {
    @Query("SELECT ch FROM CryptoHisto ch WHERE ch.crypto.idCrypto = :idCrypto AND ch.dateModif <= :dateMax ORDER BY ch.dateModif DESC LIMIT 1")
    Optional<CryptoHisto> findLatestPriceBeforeDate(@Param("idCrypto") int idCrypto, @Param("dateMax") Timestamp dateMax);

    @Query("SELECT ch FROM CryptoHisto ch WHERE ch.crypto.idCrypto = :idCrypto ORDER BY ch.dateModif DESC LIMIT 1")
    Optional<CryptoHisto> findLatestPrice(@Param("idCrypto") int idCrypto);
}
