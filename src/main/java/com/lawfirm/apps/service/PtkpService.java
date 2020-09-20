/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.EntityPTKP;
import com.lawfirm.apps.repo.interfaces.PtkpRepoIface;
import com.lawfirm.apps.service.interfaces.PtkpServiceIface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class PtkpService implements PtkpServiceIface {

    @Autowired
    PtkpRepoIface ptkpRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityPTKP create(EntityPTKP entity) {
        return ptkpRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityPTKP update(EntityPTKP entity) {
        return ptkpRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityPTKP delete(EntityPTKP entity) {
        return ptkpRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(EntityPTKP entity) {
        ptkpRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityPTKP findById(Long paramLong) {
        return ptkpRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityPTKP findPTKPByTaxStatus(String param) {
        return ptkpRepo.findPTKPByTaxStatus(param);
    }

}
