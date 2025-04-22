package itu.p16.crypto.repository.views;


import itu.p16.crypto.model.views.ViewPortefeuille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewPortefeuilleRepository extends JpaRepository<ViewPortefeuille, Integer> {

    List<ViewPortefeuille> findByIdUser(int idUser);
    List<ViewPortefeuille> findByIdUserAndIdCrypto(int idUser, int idCrypto); 
}
