/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.LoanHistory;
import com.lawfirm.apps.repo.interfaces.LoanHistoryRepoIface;
import com.lawfirm.apps.service.interfaces.LoanHistoryServiceIface;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class LoanHistoryService implements LoanHistoryServiceIface {

    @Autowired
    LoanHistoryRepoIface loanHistoryRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public LoanHistory create(LoanHistory entity) {
        return loanHistoryRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public LoanHistory update(LoanHistory entity) {
        return loanHistoryRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public LoanHistory delete(LoanHistory entity) {
        return loanHistoryRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(LoanHistory entity) {
        loanHistoryRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public LoanHistory findById(Long paramLong) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<LoanHistory> findByIdLoan(Long paramLong) {
        return loanHistoryRepo.findByIdLoan(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<LoanHistory> findByLoanId(String param) {
        return loanHistoryRepo.findByLoanId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<LoanHistory> findByUserId(Long paramLong, String param) {
        return loanHistoryRepo.findByUserId(paramLong, param);
    }

}
