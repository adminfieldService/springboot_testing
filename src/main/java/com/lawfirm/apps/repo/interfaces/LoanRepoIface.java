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

    Loan findByName(String namaVisit);

    List<Loan> listLoan();

    List<Loan> listLoanPaging(int max, int start);

    List<Loan> findByEmployee(int max, int start, String param);

    List<Loan> listActive();

    List<Loan> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
