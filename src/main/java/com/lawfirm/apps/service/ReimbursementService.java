/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Reimbursement;
import com.lawfirm.apps.repo.interfaces.ReimbursementRepoIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
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
public class ReimbursementService implements ReimbursementServiceIface {

    @Autowired
    ReimbursementRepoIface reimbursementRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Reimbursement create(Reimbursement entity) {
        return reimbursementRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Reimbursement update(Reimbursement entity) {
        return reimbursementRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Reimbursement delete(Reimbursement entity) {
        return reimbursementRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(Reimbursement entity) {
        reimbursementRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Reimbursement findById(Long paramLong) {
        return reimbursementRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Reimbursement findByName(String namaVisit) {
        return reimbursementRepo.findByName(namaVisit);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Reimbursement> listReimbursement() {
        return reimbursementRepo.listReimbursement();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Reimbursement> listBy(String paramBy, Long empId) {
        return reimbursementRepo.listBy(paramBy, empId);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Reimbursement> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return reimbursementRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Reimbursement> cekReimbusementStatusApprove(Object parameter) {
        return reimbursementRepo.cekReimbusementStatusApprove(parameter);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Reimbursement> getReimbusementByCseId(Object parameter) {
        return reimbursementRepo.getReimbusementByCseId(parameter);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Reimbursement> listReimbursementPaging(int max, int start) {
        return reimbursementRepo.listReimbursementPaging(max, start);
    }

}
