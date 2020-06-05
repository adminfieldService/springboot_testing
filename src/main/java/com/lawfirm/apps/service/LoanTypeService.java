/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.LoanType;
import com.lawfirm.apps.repo.interfaces.LoanTypeRepoIface;
import com.lawfirm.apps.service.interfaces.LoanTypeServiceIface;
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
public class LoanTypeService implements LoanTypeServiceIface {

    @Autowired
    LoanTypeRepoIface loanTypeRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public LoanType create(LoanType entity) {
        return loanTypeRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public LoanType update(LoanType entity) {
        return loanTypeRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public LoanType delete(LoanType entity) {
        return loanTypeRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(LoanType entity) {
        loanTypeRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public LoanType findById(Long paramLong) {
        return loanTypeRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public LoanType findByName(String param) {
        return loanTypeRepo.findByName(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<LoanType> listLoanType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<LoanType> listLoanTypePaging(int max, int start) {
        return loanTypeRepo.listLoanTypePaging(max, start);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<LoanType> listActive(int max, int start, Boolean param) {
        return loanTypeRepo.listActive(max, start, param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<LoanType> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return loanTypeRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return loanTypeRepo.getEntityManager();
    }

}
