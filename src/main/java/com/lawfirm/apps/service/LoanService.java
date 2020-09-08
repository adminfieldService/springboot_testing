/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.repo.interfaces.LoanRepoIface;
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
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
public class LoanService implements LoanServiceIface {

    @Autowired
    LoanRepoIface loanRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan create(Loan entity) {
        return loanRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan update(Loan entity) {
        return loanRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan delete(Loan entity) {
        return loanRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(Loan entity) {
        loanRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan findById(Long paramLong) {
        return loanRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan findByIdLoan(Long paramLong) {
        return loanRepo.findByIdLoan(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan findByLoanId(String param) {
        return loanRepo.findByLoanId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Loan findByLoanIdB(String param) {
        return loanRepo.findByLoanIdB(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> listLoan(int max, int start, String type) {
        return loanRepo.listLoan(max, start, type);
    }

//    @Override
//    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
//    public List<Loan> listLoanB(int max, int start) {
//        return loanRepo.listLoanB(max, start);
//    }
    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> listLoanPaging(int max, int start) {
        return loanRepo.listLoanPaging(max, start);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> findByEmployee(String param, String type) {
        return loanRepo.findByEmployee(param, type);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> listActive(String param1, String param2, String type) {
        return loanRepo.listActive(param1, param2, type);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return loanRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Double loanEmp(String param1, String param2) {
        return loanRepo.loanEmp(param1, param2);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> generateLoanId(String typeLoan, String idEmployee, String date_created) {
        return loanRepo.generateLoanId(typeLoan, idEmployee, date_created);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Loan> generateLoanIdB(String typeLoan, String idEmployee, String date_created) {
        return loanRepo.generateLoanIdB(typeLoan, idEmployee, date_created);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return loanRepo.getEntityManager();
    }

    @Override
    public List<Loan> listDisburse(String type) {
        return loanRepo.listDisburse(type);
    }

    @Override
    public List<Loan> getLoanB(String param) {
        return loanRepo.getLoanB(param);
    }

    @Override
    public List<Loan> getListLoanB() {
        return loanRepo.getListLoanB();
    }

}
