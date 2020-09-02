/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.ReimbursementHistory;
import com.lawfirm.apps.repo.interfaces.ReimbursementHistoryRepoIface;
import com.lawfirm.apps.service.interfaces.ReimbursementHistoryServiceIface;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class ReimbursementHistoryService implements ReimbursementHistoryServiceIface {

    @Autowired
    ReimbursementHistoryRepoIface reimbursementHistoryRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public ReimbursementHistory create(ReimbursementHistory entity) {
        return reimbursementHistoryRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public ReimbursementHistory update(ReimbursementHistory entity) {
        return reimbursementHistoryRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public ReimbursementHistory delete(ReimbursementHistory entity) {
        return reimbursementHistoryRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(ReimbursementHistory entity) {
        reimbursementHistoryRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<ReimbursementHistory> findByReimburseId(Long paramLong) {
        return reimbursementHistoryRepo.findByReimburseId(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<ReimbursementHistory> findByUserId(Long paramLong, String param) {
        return reimbursementHistoryRepo.findByUserId(paramLong, param);

    }

}
