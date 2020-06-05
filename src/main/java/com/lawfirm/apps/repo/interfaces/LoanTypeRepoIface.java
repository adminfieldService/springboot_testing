/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.LoanType;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface LoanTypeRepoIface {

    LoanType create(LoanType entity);

    LoanType update(LoanType entity);

    LoanType delete(LoanType entity);

    void remove(LoanType entity);

    LoanType findById(Long paramLong);

    LoanType findByName(String namaVisit);

    List<LoanType> listLoanType();

    List<LoanType> listLoanTypePaging(int max, int start);

    List<LoanType> listActive(int max, int start,Boolean param);

    List<LoanType> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
