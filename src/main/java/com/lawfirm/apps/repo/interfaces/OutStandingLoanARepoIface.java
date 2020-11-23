/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.OutStandingLoanA;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface OutStandingLoanARepoIface {

    OutStandingLoanA create(OutStandingLoanA entity);

    OutStandingLoanA update(OutStandingLoanA entity);

    OutStandingLoanA approved(OutStandingLoanA entity);

    OutStandingLoanA delete(OutStandingLoanA entity);

    OutStandingLoanA checkLoan(String param, Long userId);

    void remove(OutStandingLoanA entity);

    OutStandingLoanA findByIdLoan(String outstandingAUUId);

    OutStandingLoanA sumLoanA(Long userId, String taxtYear, int number);

//    List<OutStandingLoanB> findByCaseId(Long paramLong);
    OutStandingLoanA findByCaseId(String param);

    OutStandingLoanA findBy(Long idEmployee, String taxyear, Long disburseId);

    Double findByEmployee(Long idEmployee, Date cutOffDate);
}
