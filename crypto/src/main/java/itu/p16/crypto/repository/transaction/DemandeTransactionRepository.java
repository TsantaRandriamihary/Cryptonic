package itu.p16.crypto.repository.transaction;

import itu.p16.crypto.model.transaction.DemandeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeTransactionRepository extends JpaRepository<DemandeTransaction, Integer> {

    List<DemandeTransaction> findByEstTraiteFalse();
    List<DemandeTransaction> findByIdUser(int idUser);
    List<DemandeTransaction> findByIdUserAndEstTraiteTrue(int idUser);
    List<DemandeTransaction> findByIdUserAndEstTraiteFalse(int idUser);
    List<DemandeTransaction> findByIdCrypto(Integer idCrypto);
    List<DemandeTransaction> findByIdCryptoAndEstTraiteFalse(Integer idCrypto);
}
