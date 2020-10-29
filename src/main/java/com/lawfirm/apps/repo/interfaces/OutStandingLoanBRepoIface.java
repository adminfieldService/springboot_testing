/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.OutStandingLoanB;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface OutStandingLoanBRepoIface {

    OutStandingLoanB create(OutStandingLoanB entity);

    OutStandingLoanB update(OutStandingLoanB entity);

    OutStandingLoanB approved(OutStandingLoanB entity);

    OutStandingLoanB delete(OutStandingLoanB entity);

    OutStandingLoanB checkLoan(String param, Long userId);

    OutStandingLoanB checkReimburse(String param, Long userId);

    void remove(OutStandingLoanB entity);

    List<OutStandingLoanB> findByIdLoan(Long paramLong);

    Double sumLoan(Long paramLong);

//    List<OutStandingLoanB> findByCaseId(Long paramLong);
    OutStandingLoanB findByCaseId(String param);

    List<OutStandingLoanB> lsitByCaseId(String param);

    Double sumLoanByCaseId(String param);
}
