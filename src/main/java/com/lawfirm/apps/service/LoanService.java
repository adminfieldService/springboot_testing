/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.repo.interfaces.LoanRepoIface;
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
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
public class LoanService implements LoanServiceIface {

    @Autowired
    LoanRepoIface loanRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan create(Loan entity) {
        return loanRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan update(Loan entity) {
        return loanRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan delete(Loan entity) {
        return loanRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(Loan entity) {
        loanRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan findById(Long paramLong) {
        return loanRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan findByName(String param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> listLoan() {
        return loanRepo.listLoan();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> listLoanPaging(int max, int start) {
        return loanRepo.listLoanPaging(max, start);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> findByEmployee(int max, int start, String param) {
        return loanRepo.findByEmployee(max, start, param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> listActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return loanRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return loanRepo.getEntityManager();
    }

}
