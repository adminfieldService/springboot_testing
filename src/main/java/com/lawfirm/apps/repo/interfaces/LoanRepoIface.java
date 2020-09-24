/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.Loan;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface LoanRepoIface {

    Loan create(Loan entity);

    Loan update(Loan entity);

    Loan delete(Loan entity);

    void remove(Loan entity);

    Loan findById(Long paramLong);

    Loan findByIdLoan(Long paramLong);

    Loan findByLoanId(String param);

    Loan findByLoanIdB(String param);

    List<Loan> listLoan(int max, int start, String type);

//    List<Loan> listLoanB(int max, int start);
    List<Loan> listLoanPaging(int max, int start);

    List<Loan> findByEmployee(String param, String type);

    List<Loan> listBy(String param1, String param2, String type);

    List<Loan> byName(Boolean isActive);

    List<Loan> listDisburse(String type);

    Integer count();

    Double loanEmp(String param1, String param2);

    List<Loan> getLoanB(String param);

    List<Loan> getListLoanB();

    Double sumLoanB(Long paramLong);

    Double sumLoanByCaseId(String param);

//    Integer generateLoanId(String param1, String param2, String param3);
    List<Loan> generateLoanId(String param1, String param2, String param3);

    List<Loan> generateLoanIdB(String param1, String param2, String param3);

    EntityManager getEntityManager();
}
