/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.OutStandingLoanB;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lawfirm.apps.repo.interfaces.OutStandingLoanBRepoIface;
import com.lawfirm.apps.service.interfaces.OutStandingLoanBServiceIface;

/**
 *
 * @author newbiecihuy
 */
@Service
public class OutStandingLoaBService implements OutStandingLoanBServiceIface {

    @Autowired
    private OutStandingLoanBRepoIface outStandingRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB create(OutStandingLoanB entity) {
        return outStandingRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB update(OutStandingLoanB entity) {
        return outStandingRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB approved(OutStandingLoanB entity) {
        return outStandingRepo.approved(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB delete(OutStandingLoanB entity) {
        return outStandingRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(OutStandingLoanB entity) {
        outStandingRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<OutStandingLoanB> findByIdLoan(Long paramLong) {
        return outStandingRepo.findByIdLoan(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<OutStandingLoanB> findByCaseId(Long paramLong) {
        return outStandingRepo.findByCaseId(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Double sumLoan(Long paramLong) {
        return outStandingRepo.sumLoan(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB checkLoan(String param, Long userId) {
        return outStandingRepo.checkLoan(param, userId);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB checkReimburse(String param, Long userId) {
        return outStandingRepo.checkReimburse(param, userId);
    }

}
