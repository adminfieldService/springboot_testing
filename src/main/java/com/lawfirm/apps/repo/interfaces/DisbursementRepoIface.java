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

    Disbursement create(Disbursement entity);

    Disbursement update(Disbursement entity);

    Disbursement delete(Disbursement entity);

    List<Disbursement> numOfDisbursement(String param);

    void remove(Disbursement entity);

    List<Disbursement> listDisburse();

    List<Loan> listDisburseByloan(String type);

    List<Loan> viewDisburseByFinance(String empId, String type);

    Loan findLoanAById(Long param);

    Loan findLoanBById(Long param);

    List<Disbursement> disbursementbyCaseId(String param);

    Disbursement disbursementFindbyCaseId(String param);

    Disbursement disbursement(Integer number, String param) ;
//    List<Loan> disbursementbyCaseId(String param);
    //    Disbursement disbursementbyCaseId(String param);

}
