package itu.p16.crypto.repository.views;


import itu.p16.crypto.model.views.ViewMouvementCrypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewMouvementCryptoRepository extends JpaRepository<ViewMouvementCrypto, Integer> {

    List<ViewMouvementCrypto> findByIdCrypto(int idCrypto);

    List<ViewMouvementCrypto> findByIdUser(int idUser);

}
