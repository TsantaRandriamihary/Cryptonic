package itu.p16.crypto.repository.crypto;

import itu.p16.crypto.model.crypto.MvtCrypto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MvtCryptoRepository extends JpaRepository<MvtCrypto, Integer> {
    List<MvtCrypto> findAll(); 
}
