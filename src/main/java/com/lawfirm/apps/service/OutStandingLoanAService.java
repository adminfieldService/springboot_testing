/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.OutStandingLoanA;
import com.lawfirm.apps.repo.interfaces.OutStandingLoanARepoIface;
import com.lawfirm.apps.service.interfaces.OutStandingLoanAServiceIface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class OutStandingLoanAService implements OutStandingLoanAServiceIface {

    @Autowired
    private OutStandingLoanARepoIface outStandingLoanARepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanA create(OutStandingLoanA entity) {
        return outStandingLoanARepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanA update(OutStandingLoanA entity) {
        return outStandingLoanARepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanA approved(OutStandingLoanA entity) {
        return outStandingLoanARepo.approved(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanA delete(OutStandingLoanA entity) {
        return outStandingLoanARepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanA checkLoan(String param, Long userId) {
        return outStandingLoanARepo.checkLoan(param, userId);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(OutStandingLoanA entity) {
        outStandingLoanARepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanA findByIdLoan(String outstandingAUUId) {
        return outStandingLoanARepo.findByIdLoan(outstandingAUUId);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Double sumLoanA(Long paramLong) {
        return outStandingLoanARepo.sumLoanA(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanA findByCaseId(String param) {
        return outStandingLoanARepo.findByCaseId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStandingLoanA findBy(Long idEmployee, String taxyear, Integer disburseId) {
        return outStandingLoanARepo.findBy(idEmployee, taxyear, disburseId);
    }
}
