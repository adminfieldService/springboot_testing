/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.Loan;
import java.util.Date;
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

    Loan findByLoanIdB(String param);

    List<Loan> listLoan(int max, int start, String type, Long idEmployee, String role);

//    List<Loan> listLoanB(int max, int start);
    List<Loan> listLoanPaging(int max, int start);

    List<Loan> findByEmployee(String param, String type);

    List<Loan> listBy(String param1, String param2, String type);

    List<Loan> byName(Boolean isActive);

    List<Loan> listDisburse(String type);

    Integer count();

    List<Loan> getLoanB(String param);

    List<Loan> getListLoanB();

    Loan findByIdLoan(Long paramLong);

    Double loanEmp(String param1, String param2);

    Double loanB(Long engagementId);

    Double sumLoanB(Long paramLong);

    Double sumLoanByCaseId(String param);

    Double sumLoanA(Long userId, String taxtYear, Date tgl_cut_off);

    Double sumLoanA2(Long userId, String taxtYear, Date tgl_cut_off, Date old_tgl_cut_off);

    List<Loan> generateLoanId(String param1, String param2, String param3);

    List<Loan> generateLoanIdB(String param1, String param2, String param3);

    Loan chekLoan(String typeLoan, Long idEmployee, Date tanggalPengajuan);

    EntityManager getEntityManager();

    List<Loan> cekLoanBStatusApprove(Object parameter);

    List<Loan> cekLoanAStatusApprove(Object parameter);

    List<Loan> geLoanBCaseId(Object param);
}
