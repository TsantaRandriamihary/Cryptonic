package itu.p16.crypto.service.transaction;

import itu.p16.crypto.model.transaction.DemandeTransaction;
import itu.p16.crypto.model.transaction.Transaction;
import itu.p16.crypto.model.commission.Commission;
import itu.p16.crypto.model.crypto.MvtCrypto;
import itu.p16.crypto.model.views.ViewFonds;
import itu.p16.crypto.model.views.ViewPortefeuille;
import itu.p16.crypto.repository.transaction.DemandeTransactionRepository;
import itu.p16.crypto.repository.transaction.TransactionRepository;
import itu.p16.crypto.repository.crypto.MvtCryptoRepository;
import itu.p16.crypto.repository.views.ViewFondsRepository;
import itu.p16.crypto.repository.views.ViewPortefeuilleRepository;
import itu.p16.crypto.service.commission.CommissionService;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeTransactionService {

    @Autowired
    private DemandeTransactionRepository demandeTransactionRepository;

    @Autowired
    private CommissionService commissionService;
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MvtCryptoRepository mvtCryptoRepository;

    @Autowired
    private ViewFondsRepository viewFondsRepository;

    @Autowired
    private ViewPortefeuilleRepository viewPortefeuilleRepository;

    @Transactional(readOnly = true)
    public List<DemandeTransaction> getAllDemandesNonTraitees() {
        return demandeTransactionRepository.findByEstTraiteFalse();
    }

    /**
     * Créer une demande de transaction avec vérifications de fonds et quantité.
     */
    @Transactional
    public DemandeTransaction creerDemandeTransaction(Integer idUser, double montantDepot, double montantRetrait, 
                                                      Double quantite, Integer idCrypto) throws Exception {
        if (montantRetrait > 0) {
            Optional<ViewFonds> fondsOpt = viewFondsRepository.findFondActuelByUserId(idUser);
            if (fondsOpt.isEmpty() || fondsOpt.get().getFondActuel() < montantRetrait) {
                throw new Exception("Fonds insuffisants pour effectuer cette transaction.");
            }
        }
        if (montantDepot > 0) {
            if (quantite != null && idCrypto != null) {
                List<ViewPortefeuille> portefeuille = viewPortefeuilleRepository.findByIdUser(idUser);
                boolean quantiteSuffisante = true;
                for (ViewPortefeuille p : portefeuille) {
                    if (p.getIdCrypto() == idCrypto && p.getQuantiteActuelle() < quantite ) {
                        quantiteSuffisante = false;
                        break;
                    }
                }
    
                if (!quantiteSuffisante ) {
                    throw new Exception("Quantité insuffisante pour effectuer cette transaction.");
                }
            }
        }
        
        DemandeTransaction demande = new DemandeTransaction();
        demande.setIdUser(idUser);
        demande.setMontantDepot(montantDepot);
        demande.setMontantRetrait(montantRetrait);
        if(quantite != null && idCrypto != null) {
            demande.setQuantite(quantite);
            demande.setIdCrypto(idCrypto);
        }        
        demande.setEstTraite(false);
        demande.setDateCreation(new Timestamp(System.currentTimeMillis()));

        return demandeTransactionRepository.save(demande);
    }

    /**
     * Valider une demande de transaction, en créant une transaction et un mouvement de crypto si nécessaire.
     */
    @Transactional
    public void validerDemande(int idDemande) throws Exception {
        Optional<DemandeTransaction> demandeOpt = demandeTransactionRepository.findById(idDemande);
        if (demandeOpt.isEmpty()) {
            throw new Exception("Demande de transaction introuvable.");
        }
        DemandeTransaction demande = demandeOpt.get();
        if (demande.isEstTraite()) {
            throw new Exception("Cette demande a déjà été traitée.");
        }
        demande.setEstTraite(true);
        demandeTransactionRepository.save(demande);

        Commission commission = commissionService.getCurrentCommission();
        double commissionAchat = 0;
        double commissionVente = 0;

        if (commission != null) {
            commissionAchat = (demande.getMontantRetrait() > 0) 
                ? round(demande.getMontantRetrait() * (commission.getPourcentAchat() / 100.0), 2) 
                : 0;
                
            commissionVente = (demande.getMontantDepot() > 0) 
                ? round(demande.getMontantDepot() * (commission.getPourcentVente() / 100.0), 2) 
                : 0;
        }
        Transaction transaction = new Transaction();
        transaction.setIdUser(demande.getIdUser());
        transaction.setMontantDepot(demande.getMontantDepot());
        transaction.setMontantRetrait(demande.getMontantRetrait());
        transaction.setDateTransaction(demande.getDateCreation());

        if (demande.getIdCrypto() != null && demande.getQuantite() > 0) {
            double quantite = demande.getQuantite();
            int idCrypto = demande.getIdCrypto();
            
            if (demande.getMontantRetrait() > 0) {
                MvtCrypto mvtCrypto = new MvtCrypto();
                mvtCrypto.setIdUser(demande.getIdUser());
                mvtCrypto.setIdCrypto(idCrypto);
                mvtCrypto.setQuantite(quantite);
                mvtCrypto.setMontantVente(0);
                mvtCrypto.setMontantAchat(demande.getMontantRetrait());
                mvtCrypto.setDateMvt(demande.getDateCreation());
                mvtCrypto.setCommissionAchat(commissionAchat);
                mvtCrypto.setCommissionVente(commissionVente);
                mvtCrypto = mvtCryptoRepository.save(mvtCrypto);
                transaction.setIdMvt(mvtCrypto.getIdMvt());

            } else if (demande.getMontantDepot() > 0) { 
                transaction.setMontantDepot(demande.getMontantDepot() - commissionVente);              
                MvtCrypto mvtCrypto = new MvtCrypto();
                mvtCrypto.setIdUser(demande.getIdUser());
                mvtCrypto.setIdCrypto(idCrypto);
                mvtCrypto.setQuantite(quantite);
                mvtCrypto.setMontantVente(demande.getMontantDepot() - commissionVente);
                mvtCrypto.setMontantAchat(0);
                mvtCrypto.setDateMvt(demande.getDateCreation());
                mvtCrypto.setCommissionAchat(commissionAchat);
                mvtCrypto.setCommissionVente(commissionVente);
                mvtCrypto = mvtCryptoRepository.save(mvtCrypto);

                transaction.setIdMvt(mvtCrypto.getIdMvt());
            }
        }
        transactionRepository.save(transaction);
    }

    /**
     * Refuser une demande de transaction.
     */
    @Transactional
    public void refuserDemande(int idDemande) throws Exception {
        Optional<DemandeTransaction> demandeOpt = demandeTransactionRepository.findById(idDemande);
        if (demandeOpt.isEmpty()) {
            throw new Exception("Demande de transaction introuvable.");
        }
        DemandeTransaction demande = demandeOpt.get();
        if (demande.isEstTraite()) {
            throw new Exception("Cette demande a déjà été traitée.");
        }
        demande.setEstTraite(true);
        demandeTransactionRepository.save(demande);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
    
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
