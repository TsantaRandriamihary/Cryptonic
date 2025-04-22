package itu.p16.crypto.repository.crypto;

import itu.p16.crypto.model.crypto.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, Integer> {
    List<Crypto> findAll(); 
}
