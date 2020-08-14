/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.LoanHistory;
import com.lawfirm.apps.repo.interfaces.LoanHistoryRepoIface;
import com.lawfirm.apps.utils.CreateLog;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author newbiecihuy
 */
@Repository("loanHistoryRepo")
public class LoanHistoryRepo implements LoanHistoryRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public LoanHistory create(LoanHistory entity) {
        try {
            entityManager.persist(entity);
            if (entity != null) {
//                 CreateLog.createJson(entity, "bpr_baiturridho");
                return entity;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanHistoryRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public LoanHistory update(LoanHistory entity) {
        try {
            entityManager.merge(entity);
            if (entity != null) {
//                CreateLog.createJson(entity, "fieldservice");
                return entity;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanHistoryRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public LoanHistory delete(LoanHistory entity) {
        try {
            entity.setIsActive("3");
            entityManager.merge(entity);
            if (entity != null) {
// 	                CreateLog.createJson(entity, "fieldservice");
                return entity;
            }
        } catch (Exception ex) {
// 	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanHistoryRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
        return null;
    }

    @Override
    public void remove(LoanHistory entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
// 	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanHistoryRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public LoanHistory findById(Long paramLong) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LoanHistory> findByIdLoan(Long paramLong) {
        try {
//         from CellPhoneBaiturridho as c JOIN fetch c.officer as o where o.email = :email";
//            Loan entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l JOIN FETCH l.employee e WHERE "
//                    + " l.loanId = :loanId")
//                    .setParameter("loanId", paramLong)
//                    .getSingleResult();
            String sql = "SELECT h FROM LoanHistory h JOIN FETCH h.id_loan AS l WHERE "
                    + " l.Id = :Id";
            Query query = entityManager.createQuery(sql);
            query.setParameter("Id", paramLong);
            if (query != null) {
                return query.getResultList();
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanHistoryRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<LoanHistory> findByLoanId(String param) {
        try {
//         from CellPhoneBaiturridho as c JOIN fetch c.officer as o where o.email = :email";
//            Loan entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l JOIN FETCH l.employee e WHERE "
//                    + " l.loanId = :loanId")
//                    .setParameter("loanId", paramLong)
//                    .getSingleResult();
            String sql = "SELECT h FROM LoanHistory h JOIN FETCH h.id_loan AS l WHERE "
                    + " l.loanId = :loanId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("loanId", param);
            if (query != null) {
                return query.getResultList();
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanHistoryRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<LoanHistory> findByUserId(Long paramLong, String param) {
        try {
//         from CellPhoneBaiturridho as c JOIN fetch c.officer as o where o.email = :email";
//            Loan entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l JOIN FETCH l.employee e WHERE "
//                    + " l.loanId = :loanId")
//                    .setParameter("loanId", paramLong)
//                    .getSingleResult();
            String sql = "SELECT h FROM LoanHistory h JOIN FETCH h.id_loan AS l WHERE "
                    + " h.userId = :userId OR "
                    + " h.response = :response ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("userId", paramLong);
            query.setParameter("response", param);
            if (query != null) {
                return query.getResultList();
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanHistoryRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

}
