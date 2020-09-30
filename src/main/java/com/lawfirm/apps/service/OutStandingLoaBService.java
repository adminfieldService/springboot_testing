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
    private OutStandingLoanBRepoIface outStandingLoanBRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB create(OutStandingLoanB entity) {
        return outStandingLoanBRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB update(OutStandingLoanB entity) {
        return outStandingLoanBRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB approved(OutStandingLoanB entity) {
        return outStandingLoanBRepo.approved(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB delete(OutStandingLoanB entity) {
        return outStandingLoanBRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(OutStandingLoanB entity) {
        outStandingLoanBRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<OutStandingLoanB> findByIdLoan(Long paramLong) {
        return outStandingLoanBRepo.findByIdLoan(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB findByCaseId(String param) {
        return outStandingLoanBRepo.findByCaseId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Double sumLoan(Long paramLong) {
        return outStandingLoanBRepo.sumLoan(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB checkLoan(String param, Long userId) {
        return outStandingLoanBRepo.checkLoan(param, userId);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanB checkReimburse(String param, Long userId) {
        return outStandingLoanBRepo.checkReimburse(param, userId);
    }

}
