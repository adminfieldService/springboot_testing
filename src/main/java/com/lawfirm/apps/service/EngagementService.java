/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.Engagement;
import com.lawfirm.apps.repo.interfaces.EngagementRepoIface;
import com.lawfirm.apps.service.interfaces.EngagementServiceIface;
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
public class EngagementService implements EngagementServiceIface {

    @Autowired
    EngagementRepoIface engagementRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Engagement create(Engagement entity) {
        return engagementRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Engagement update(Engagement entity) {
        return engagementRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Engagement delete(Engagement entity) {
        return engagementRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(Engagement entity) {
        engagementRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Engagement findById(Long paramLong) {
        return engagementRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public CaseDetails findByCaseID(String paramString) {
        return engagementRepo.findByCaseID(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Engagement> listEngagement() {
        return engagementRepo.listEngagement();
    }

    @Override
    public List<Engagement> listActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Engagement> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return engagementRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return engagementRepo.getEntityManager();
    }

}
