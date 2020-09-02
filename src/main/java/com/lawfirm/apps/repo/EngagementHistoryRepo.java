/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.EngagementHistory;
import com.lawfirm.apps.repo.interfaces.EngagementHistoryRepoIface;
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
@Repository("engagementHistoryRepo")
public class EngagementHistoryRepo implements EngagementHistoryRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public EngagementHistory create(EngagementHistory entity) {
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
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_engagementHistoryRepo");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public EngagementHistory update(EngagementHistory entity) {
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
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_engagementHistoryRepo");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public EngagementHistory delete(EngagementHistory entity) {
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
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_engagementHistoryRepo");
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
        return null;
    }

    @Override
    public void remove(EngagementHistory entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
// 	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_engagementHistoryRepo");

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<EngagementHistory> findByCaseId(Long paramLong) {
        try {
//         from CellPhoneBaiturridho as c JOIN fetch c.officer as o where o.email = :email";
//            Loan entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l JOIN FETCH l.employee e WHERE "
//                    + " l.loanId = :loanId")
//                    .setParameter("loanId", paramLong)
//                    .getSingleResult();
//            String sql = "SELECT h FROM EngagementHistory h JOIN FETCH h.id_loan AS l WHERE "
//                    + " l.Id = :Id";
//            Query query = entityManager.createQuery(sql);
//            query.setParameter("Id", paramLong);
//            if (query != null) {
//                return query.getResultList();
//            } else {
            String sql = "SELECT h FROM EngagementHistory h "
                    + " JOIN FETCH h.engagement AS l WHERE "
                    + " l.caseID = :caseID";
            Query query = entityManager.createQuery(sql);
            query.setParameter("caseID", paramLong);
            if (query != null) {
                return query.getResultList();
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_engagementHistoryRepo");
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
//        return null;
    }

    @Override
    public List<EngagementHistory> findByEngagementId(String param) {
        try {
//         from CellPhoneBaiturridho as c JOIN fetch c.officer as o where o.email = :email";
//            Loan entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l JOIN FETCH l.employee e WHERE "
//                    + " l.loanId = :loanId")
//                    .setParameter("loanId", paramLong)
//                    .getSingleResult();
            String sql = "SELECT h FROM EngagementHistory h "
                    + " JOIN FETCH h.engagement AS l WHERE "
                    + " l.engagementId = :engagementId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("engagementId", param);
            if (query != null) {
                return query.getResultList();
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_engagementHistoryRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<EngagementHistory> findByUserId(Long paramLong, String param) {
        try {
//         from CellPhoneBaiturridho as c JOIN fetch c.officer as o where o.email = :email";
//            Loan entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l JOIN FETCH l.employee e WHERE "
//                    + " l.loanId = :loanId")
//                    .setParameter("loanId", paramLong)
//                    .getSingleResult();
            String sql = "SELECT h FROM EngagementHistory h JOIN FETCH h.engagement AS l WHERE "
                    + " h.employee.idEmployee = :idEmployee OR "
                    + " h.response = :response ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("idEmployee", paramLong);
            query.setParameter("response", param);
            if (query != null) {
                return query.getResultList();
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_engagementHistoryRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

}
