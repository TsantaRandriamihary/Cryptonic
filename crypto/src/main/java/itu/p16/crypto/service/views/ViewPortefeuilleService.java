package itu.p16.crypto.service.views;


import itu.p16.crypto.model.views.ViewPortefeuille;
import itu.p16.crypto.repository.views.ViewPortefeuilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewPortefeuilleService {

    private final ViewPortefeuilleRepository portefeuilleRepository;

    @Autowired
    public ViewPortefeuilleService(ViewPortefeuilleRepository portefeuilleRepository) {
        this.portefeuilleRepository = portefeuilleRepository;
    }

    public List<ViewPortefeuille> getPortefeuilleByUser(int idUser) {
        return portefeuilleRepository.findByIdUser(idUser);
    }
}
