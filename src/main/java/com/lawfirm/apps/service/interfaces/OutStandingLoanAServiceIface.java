/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.OutStandingLoanA;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface OutStandingLoanAServiceIface {
     OutStandingLoanA create(OutStandingLoanA entity);

    OutStandingLoanA update(OutStandingLoanA entity);

    OutStandingLoanA approved(OutStandingLoanA entity);

    OutStandingLoanA delete(OutStandingLoanA entity);

    OutStandingLoanA checkLoan(String param, Long userId);

    void remove(OutStandingLoanA entity);

    OutStandingLoanA findByIdLoan(String outstandingAUUId);

    Double sumLoanA(Long paramLong);

//    List<OutStandingLoanB> findByCaseId(Long paramLong);
    OutStandingLoanA findByCaseId(String param);
}
