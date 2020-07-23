/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.Loan;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface LoanServiceIface {

    Loan create(Loan entity);

    Loan update(Loan entity);

    Loan delete(Loan entity);

    void remove(Loan entity);

    Loan findById(Long paramLong);

    Loan findByLoanId(String param);

    List<Loan> listLoan(int max, int start, String type);

//    List<Loan> listLoanB(int max, int start);

    List<Loan> listLoanPaging(int max, int start);

    List<Loan> findByEmployee(String param, String type);

    List<Loan> listActive(String param1, String param2, String type);

    List<Loan> byName(Boolean isActive);

    Integer count();

    Loan findByIdLoan(Long paramLong);

    Double loanEmp(String param1, String param2);

    Integer generateLoanId(String param1, String param2, String param3);

    EntityManager getEntityManager();
}
