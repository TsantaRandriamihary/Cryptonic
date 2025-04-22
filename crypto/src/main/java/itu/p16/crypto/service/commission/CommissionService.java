package itu.p16.crypto.service.commission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itu.p16.crypto.model.commission.Commission;
import itu.p16.crypto.repository.commission.CommissionRepository;

import java.util.List;

@Service
public class CommissionService {
    @Autowired
    private CommissionRepository commissionRepository;

    public Commission save(Commission commission) {
        return commissionRepository.save(commission);
    }

    public List<Commission> findAll() {
        return commissionRepository.findAll();
    }

    public Commission getCurrentCommission() {
        return commissionRepository.findFirstByOrderByDateModifDesc();
    }

    public Commission findById(Integer id) {
        return commissionRepository.findById(id).orElse(null);
    }
}

