/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.DocumentReimburse;
import com.lawfirm.apps.repo.interfaces.DocumentReimburseRepoIface;
import com.lawfirm.apps.service.interfaces.DocumentReimburseServiceIface;
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
public class DocumentReimburseService implements DocumentReimburseServiceIface {

    @Autowired
    DocumentReimburseRepoIface documentReimburseRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public DocumentReimburse create(DocumentReimburse entity) {
        return documentReimburseRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public DocumentReimburse update(DocumentReimburse entity) {
        return documentReimburseRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public DocumentReimburse delete(DocumentReimburse entity) {
        return documentReimburseRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(DocumentReimburse entity) {
        documentReimburseRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public DocumentReimburse findById(Long paramLong) {
        return documentReimburseRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public DocumentReimburse findByName(String paramString, Long paramLong) {
        return documentReimburseRepo.findByName(paramString, paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<DocumentReimburse> findByEmployee(String paramString) {
        return documentReimburseRepo.findByEmployee(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<DocumentReimburse> listActive(String param) {
        return documentReimburseRepo.listActive(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<DocumentReimburse> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return documentReimburseRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return documentReimburseRepo.getEntityManager();
    }

}
