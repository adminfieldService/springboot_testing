/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.EngagementHistory;
import com.lawfirm.apps.repo.interfaces.EngagementHistoryRepoIface;
import com.lawfirm.apps.service.interfaces.EngagementHistoryServiceIface;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class EngagementHistoryService implements EngagementHistoryServiceIface {

    @Autowired
    EngagementHistoryRepoIface engagementHistoryRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EngagementHistory create(EngagementHistory entity) {
        return engagementHistoryRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EngagementHistory update(EngagementHistory entity) {
        return engagementHistoryRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EngagementHistory delete(EngagementHistory entity) {
        return engagementHistoryRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(EngagementHistory entity) {
        engagementHistoryRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<EngagementHistory> findByCaseId(Long paramLong) {
        return engagementHistoryRepo.findByCaseId(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<EngagementHistory> findByEngagementId(String param) {
        return engagementHistoryRepo.findByEngagementId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<EngagementHistory> findByUserId(Long paramLong, String param) {
        return engagementHistoryRepo.findByUserId(paramLong, param);
    }

}
