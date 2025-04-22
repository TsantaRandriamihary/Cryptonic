package itu.p16.crypto.repository.favori;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import itu.p16.crypto.model.favori.Favori;

@Repository
public interface FavoriRepository extends JpaRepository<Favori, Integer> {
}
