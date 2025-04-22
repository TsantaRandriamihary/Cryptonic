package itu.p16.crypto.repository.views;


import itu.p16.crypto.model.views.ViewTransactions;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewTransactionsRepository extends JpaRepository<ViewTransactions, Integer> {
    List<ViewTransactions> findByIdUser(int idUser);
}
