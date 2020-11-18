/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.EntityPeriod;
import com.lawfirm.apps.repo.interfaces.EntityPeriodRepoIface;
import com.lawfirm.apps.service.interfaces.EntityPeriodServiceIface;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class EntityPeriodService implements EntityPeriodServiceIface {

    @Autowired
    EntityPeriodRepoIface entityPeriodRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityPeriod create(EntityPeriod entity) {
        return entityPeriodRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityPeriod update(EntityPeriod entity) {
        return entityPeriodRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityPeriod delete(EntityPeriod entity) {
        return entityPeriodRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityPeriod findById(Integer number, String taxYear) {
        return entityPeriodRepo.findById(number, taxYear);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Double previousDisbursement(Integer number, Long userId, String taxYear) {
        return entityPeriodRepo.previousDisbursement(number, userId, taxYear);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityPeriod findBy(Long userId, String caseId, String taxYear) {
        return entityPeriodRepo.findBy(userId, caseId, taxYear);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Double incomeTaxPaidOnPriorPeriod(Integer numberDisbursement, Long userId, String taxYear) {
        return entityPeriodRepo.incomeTaxPaidOnPriorPeriod(numberDisbursement, userId, taxYear);
    }

}
