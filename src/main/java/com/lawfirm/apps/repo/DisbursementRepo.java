/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Disbursement;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.repo.interfaces.DisbursementRepoIface;
import com.lawfirm.apps.utils.CreateLog;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository("disbursementRepo")
@Slf4j
public class DisbursementRepo implements DisbursementRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public List<Loan> listDisburse(String type) {
        try {
            List<Loan> listAcquire = null;
            if (type.contentEquals("0")) {

                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l"
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " l.status = :status "
                        + " ORDER BY l.date_created Desc ")
                        .setParameter("status", "d")
                        .getResultList();
            }
            if (type.contentEquals("a")) {

                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l"
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " t.typeLoan = :typeLoan AND "
                        + " l.status = :status "
                        + " ORDER BY l.date_created Desc ")
                        .setParameter("typeLoan", "a")
                        .setParameter("status", "d")
                        .getResultList();
            }
            if (type.contentEquals("b")) {
                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l"
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " t.typeLoan = :typeLoan AND "
                        + " l.status = :status "
                        + " ORDER BY l.date_created Desc ")
                        .setParameter("typeLoan", "b")
                        .setParameter("status", "d")
                        .getResultList();
            }

            return listAcquire;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> viewDisburseByFinance(String empId, String type) {
        try {
            List<Loan> listAcquire = null;
            if (type.contentEquals("a")) {

                listAcquire = entityManager.createQuery("SELECT DISTINCT l FROM Loan l "
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " l.aprovedByFinance = :aprovedByFinance AND "
                        + " t.typeLoan = :typeLoan "
                        + " ORDER BY l.date_created Desc ")
                        .setParameter("aprovedByFinance", empId)
                        .setParameter("typeLoan", "a")
                        .getResultList();
                //                        .setParameter("status", "d")

            }
            if (type.contentEquals("b")) {
                listAcquire = entityManager.createQuery("SELECT DISTINCT l FROM Loan l "
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " l.aprovedByFinance = :aprovedByFinance AND "
                        + " t.typeLoan = :typeLoan AND "
                        + " ORDER BY l.date_created Desc ")
                        .setParameter("aprovedByFinance", empId)
                        .setParameter("typeLoan", "b")
                        .getResultList();
                //                        .setParameter("status", "d")
            }
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

//    @Override
//    public Disbursement disbursementbyCaseId(String param) {
//        try {
//            Disbursement listAcquire = (Disbursement) entityManager.createQuery("SELECT d FROM Disbursement d "
//                    + " JOIN FETCH d.loan AS l "
//                    + " LEFT JOIN FETCH l.loantype AS t "
//                    + " JOIN FETCH l.engagement AS e "
//                    + " WHERE "
//                    + " e.caseID = :caseID AND "
//                    + " e.status = :status")
//                    .setParameter("caseID", param)
//                    .setParameter("status", "closed")
//                    .getSingleResult();
//            //                        .setParameter("status", "d")
//
//            return listAcquire;
//        } catch (Exception ex) {
//            logger.error(ex.getMessage());
//            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
//            System.out.println("ERROR: " + ex.getMessage());
//            return null;
//        } finally {
//            if ((entityManager != null) && (entityManager.isOpen())) {
//                entityManager.close();
//            }
//        }
//    }
    @Override
    public List<Loan> disbursementbyCaseId(String param) {
        try {
            List<Loan> listAcquire = entityManager.createQuery("SELECT l FROM Loan l "
                    + " LEFT JOIN FETCH l.loantype AS t "
                    + " JOIN FETCH l.engagement AS e "
                    + " WHERE "
                    + " e.caseID = :caseID AND "
                    + " e.status = :status")
                    .setParameter("caseID", param)
                    .setParameter("status", "closed")
                    .getResultList();
            //                        .setParameter("status", "d")

            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Loan findLoanAById(Long param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Loan findLoanBById(Long param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
