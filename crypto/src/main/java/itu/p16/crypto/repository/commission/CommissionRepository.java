package itu.p16.crypto.repository.commission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import itu.p16.crypto.model.commission.Commission;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Integer> {
    Commission findFirstByOrderByDateModifDesc();
}
