/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.LoanHistory;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface LoanHistoryServiceIface {

    LoanHistory create(LoanHistory entity);

    LoanHistory update(LoanHistory entity);

    LoanHistory delete(LoanHistory entity);

    void remove(LoanHistory entity);

    LoanHistory findById(Long paramLong);

    List<LoanHistory> findByIdLoan(Long paramLong);

    List<LoanHistory> findByLoanId(String param);

    List<LoanHistory> findByUserId(Long paramLong, String param);
}
