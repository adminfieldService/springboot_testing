/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.Disbursement;
import com.lawfirm.apps.model.Loan;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface DisbursementRepoIface {

    List<Loan> listDisburse(String type);

    List<Loan> viewDisburseByFinance(String empId, String type);

    Loan findLoanAById(Long param);

    Loan findLoanBById(Long param);

    List<Loan> disbursementbyCaseId(String param);
//    Disbursement disbursementbyCaseId(String param);
}
