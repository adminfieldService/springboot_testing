/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.CaseDocument;
import com.lawfirm.apps.repo.interfaces.CaseDocumentRepoIface;
import com.lawfirm.apps.service.interfaces.CaseDocumentServiceIface;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class CaseDocumentService implements CaseDocumentServiceIface {

    @Autowired
    CaseDocumentRepoIface caseDocumentRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDocument create(CaseDocument entity) {
        return caseDocumentRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDocument update(CaseDocument entity) {
        return caseDocumentRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDocument delete(CaseDocument entity) {
        return caseDocumentRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(CaseDocument entity) {
        caseDocumentRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDocument findById(String paramString) {
        return caseDocumentRepo.findById(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDocument> findDocByCaseId(Long paramLong) {
        return caseDocumentRepo.findDocByCaseId(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDocument> listCaseDocument() {
        return caseDocumentRepo.listCaseDocument();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDocument> listActive(String isActive) {
        return caseDocumentRepo.listActive(isActive);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<CaseDocument> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return caseDocumentRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return caseDocumentRepo.getEntityManager();
    }

}
