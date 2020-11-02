/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Disbursement;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.repo.interfaces.DisbursementRepoIface;
import com.lawfirm.apps.service.interfaces.DisbursementServiceIface;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class DisbursementService implements DisbursementServiceIface {

    @Autowired
    DisbursementRepoIface disbursementRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Disbursement create(Disbursement entity) {
        return disbursementRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Disbursement update(Disbursement entity) {
        return disbursementRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Disbursement delete(Disbursement entity) {
        return disbursementRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Disbursement> numOfDisbursement(String param) {
        return disbursementRepo.numOfDisbursement(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Disbursement setnumOfDisbursement(String param, String bulan) {
        return disbursementRepo.setnumOfDisbursement(param, bulan);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(Disbursement entity) {
        disbursementRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Disbursement> listDisburse() {
        return disbursementRepo.listDisburse();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> listDisburseByloan(String type) {
        return this.disbursementRepo.listDisburseByloan(type);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> viewDisburseByFinance(String empId, String type) {
        return this.disbursementRepo.viewDisburseByFinance(empId, type);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan findLoanAById(Long param) {
        return this.disbursementRepo.findLoanAById(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan findLoanBById(Long param) {
        return this.disbursementRepo.findLoanBById(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Disbursement> disbursementbyCaseId(String param) {
        return this.disbursementRepo.disbursementbyCaseId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Disbursement disbursementFindbyCaseId(String param) {
        return this.disbursementRepo.disbursementFindbyCaseId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Disbursement disbursement(Integer number, String taxyear) {
        return this.disbursementRepo.disbursement(number, taxyear);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> listDisburseByloanPaging(String type, int max, int start) {
        return this.disbursementRepo.listDisburseByloanPaging(type, max, start);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Disbursement disbursementFindbyEngegmentId(Long engagementId) {
        return this.disbursementRepo.disbursementFindbyEngegmentId(engagementId);
    }

}
