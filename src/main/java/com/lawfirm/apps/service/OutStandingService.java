/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.OutStanding;
import com.lawfirm.apps.repo.interfaces.OutStandingRepoIface;
import com.lawfirm.apps.service.interfaces.OutStandingServiceIface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class OutStandingService implements OutStandingServiceIface {

    @Autowired
    private OutStandingRepoIface outStandingRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStanding create(OutStanding entity) {
        return outStandingRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStanding update(OutStanding entity) {
        return outStandingRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStanding approved(OutStanding entity) {
        return outStandingRepo.approved(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStanding delete(OutStanding entity) {
        return outStandingRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(OutStanding entity) {
        outStandingRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStanding findByIdLoan(Long paramLong) {
        return outStandingRepo.findByIdLoan(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public OutStanding findByCaseId(Long paramLong) {
        return outStandingRepo.findByCaseId(paramLong);
    }

}
