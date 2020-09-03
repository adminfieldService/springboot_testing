/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.repo.interfaces.CaseDetailsRepoIface;
import com.lawfirm.apps.service.interfaces.CaseDetailsServiceIface;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class CaseDetailsService implements CaseDetailsServiceIface {

    @Autowired
    CaseDetailsRepoIface caseDetailsRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDetails create(CaseDetails entity) {
        return caseDetailsRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDetails update(CaseDetails entity) {
        return caseDetailsRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDetails approved(CaseDetails entity) {
        return caseDetailsRepo.approved(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDetails delete(CaseDetails entity) {
        return caseDetailsRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(CaseDetails entity) {
        caseDetailsRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDetails findById(Long paramLong) {
        return caseDetailsRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDetails findByCaseId(String namaVisit, String paramY) {
        return caseDetailsRepo.findByCaseId(namaVisit, paramY);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Optional<CaseDetails> checkCaseId(String caseID, String paramY) {
        return caseDetailsRepo.checkCaseId(caseID, paramY);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDetails findCaseId(String namaVisit) {
        return caseDetailsRepo.findCaseId(namaVisit);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDetails> listCaseDetails() {
        return caseDetailsRepo.listCaseDetails();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDetails> listActive(Boolean isActive) {
        return caseDetailsRepo.listActive(isActive);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDetails> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return caseDetailsRepo.count();
    }

//    @Override
//    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
//    public Integer generateCaseId(String param1) {
//        return caseDetailsRepo.generateCaseId(param1);
//    }
    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDetails> generateCaseId(String param1) {
        return caseDetailsRepo.generateCaseId(param1);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return caseDetailsRepo.getEntityManager();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDetails> findByEmployee(Long paramLong) {
        return caseDetailsRepo.findByEmployee(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDetails> findByEngagementId(Long paramLong) {
        return caseDetailsRepo.findByEngagementId(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDetails> findByAdmin(Long paramLong) {
        return caseDetailsRepo.findByAdmin(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDetails> getCaseId() {
        return caseDetailsRepo.getCaseId();
    }

}
