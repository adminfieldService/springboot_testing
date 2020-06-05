/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Financial;
import com.lawfirm.apps.repo.interfaces.FinancialRepoIface;
import com.lawfirm.apps.service.interfaces.FinancialServiceIface;
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
public class FinancialService implements FinancialServiceIface {

    @Autowired
    FinancialRepoIface financialRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Financial create(Financial entity) {
        return financialRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Financial update(Financial entity) {
        return financialRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Financial delete(Financial entity) {
        return financialRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(Financial entity) {
        financialRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Financial findById(Long paramLong) {
        return financialRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Financial> findByTeamMember(String param) {
        return financialRepo.findByTeamMember(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Financial> findByCaseId(String param) {
        return financialRepo.findByCaseId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Financial> listFinancial() {
        return financialRepo.listFinancial();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Financial> listActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Financial> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return financialRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return financialRepo.getEntityManager();
    }

}
