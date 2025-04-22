package itu.p16.crypto.service.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itu.p16.crypto.model.transaction.Transaction;
import itu.p16.crypto.model.views.ViewFonds;
import itu.p16.crypto.repository.transaction.TransactionRepository;
import itu.p16.crypto.repository.views.ViewFondsRepository;
import itu.p16.crypto.service.email.PinEmailService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private DemandeTransactionService demandeTransactionService;


    @Autowired
    private ViewFondsRepository viewFondsRepository;


    @Transactional
    public Transaction createTransaction(Integer idUser, Double montantDepot, Double montantRetrait) {
        Transaction transaction = new Transaction();
        transaction.setIdUser(idUser);
        transaction.setMontantDepot(montantDepot);
        transaction.setMontantRetrait(montantRetrait);
        transaction.setDateTransaction(new Timestamp(System.currentTimeMillis()));

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByUser(Integer idUser) {
        return transactionRepository.findByIdUser(idUser);
    }

    public Transaction getTransactionById(Integer idTransaction) {
        return transactionRepository.findById(idTransaction).orElse(null); 
    }


    @Transactional
    public void tenterTransaction(Integer idUser, Double montantDepot, Double montantRetrait, String email) throws Exception {
        demandeTransactionService.creerDemandeTransaction(idUser, montantDepot, montantRetrait, null, null);

    }

    
    

}
