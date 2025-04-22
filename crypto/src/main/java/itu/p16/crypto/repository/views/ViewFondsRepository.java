package itu.p16.crypto.repository.views;


import itu.p16.crypto.model.views.ViewFonds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewFondsRepository extends JpaRepository<ViewFonds, Integer> {

    @Query("SELECT v FROM ViewFonds v WHERE v.idUser = :idUser")
    Optional<ViewFonds> findFondActuelByUserId(int idUser);
}
