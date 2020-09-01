/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.repo.interfaces.DisbursementRepoIface;
import com.lawfirm.apps.repo.interfaces.LoanRepoIface;
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
    public List<Loan> listDisburse(String type) {
        return this.disbursementRepo.listDisburse(type);
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

}
