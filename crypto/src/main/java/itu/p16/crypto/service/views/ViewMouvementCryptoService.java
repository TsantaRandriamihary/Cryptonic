package itu.p16.crypto.service.views;


import itu.p16.crypto.model.views.ViewMouvementCrypto;
import itu.p16.crypto.repository.views.ViewMouvementCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewMouvementCryptoService {

    @Autowired
    private ViewMouvementCryptoRepository repository;

    public List<ViewMouvementCrypto> getMouvements() {
        return repository.findAll();
    }

    public List<ViewMouvementCrypto> getMouvementsByIdCrypto(int idCrypto) {
        return repository.findByIdCrypto(idCrypto);
    }

    public List<ViewMouvementCrypto> getMouvementsByIdUser(int idUser) {
        return repository.findByIdUser(idUser);
    }
}
