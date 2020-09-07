/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Reimbursement;
import com.lawfirm.apps.repo.interfaces.ReimbursementRepoIface;
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
@Repository("reimbursementRepo")
public class ReimbursementRepo implements ReimbursementRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Reimbursement create(Reimbursement entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_reimbursementRepo");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Reimbursement update(Reimbursement entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_reimbursementRepo");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Reimbursement delete(Reimbursement entity) {
        try {
            entity.setStatus("del");
            entity.setIsActive("4");
            entityManager.merge(entity);
            if (entity != null) {
// 	                CreateLog.createJson(entity, "fieldservice");
                return entity;
            }
        } catch (Exception ex) {
// 	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_reimbursementRepo");
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
        return null;
    }

    @Override
    public void remove(Reimbursement entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
// 	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_reimbursementRepo");

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Reimbursement findById(Long paramLong) {
        try {
            Reimbursement listAcquire = (Reimbursement) entityManager.createQuery("SELECT r FROM Reimbursement r"
                    + " LEFT JOIN FETCH r.loan AS l "
                    + " LEFT JOIN FETCH r.employee AS e "
                    + " RIGHT JOIN FETCH l.engagement AS n "
                    + " WHERE "
                    + " r.reimburseId = :reimburseId ")
                    .setParameter("reimburseId", paramLong)
                    .getSingleResult();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_reimbursementRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Reimbursement findByName(String namaVisit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Reimbursement> listReimbursement() {
        try {
            List<Reimbursement> listAcquire = entityManager.createQuery("SELECT r FROM Reimbursement r "
                    + " LEFT JOIN FETCH r.loan AS l "
                    + " LEFT JOIN FETCH r.employee AS e "
                    + " RIGHT JOIN FETCH l.engagement AS n "
                    + " ORDER BY r.tgInput DESC ")
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_reimbursementRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

//        try {
//            List<Reimbursement> listAcquire = entityManager.createQuery("SELECT r FROM Reimbursement r "
//                    + " JOIN FETCH r.employee AS e "
//                    + " LEFT JOIN FETCH r.loan AS l "
//                    + " RIGHT JOIN FETCH l.engagement AS n "
//                    + " ORDER BY r.tgInput Desc")
//                    .getResultList();
//            return (List<Reimbursement>) listAcquire;
//        } catch (Exception ex) {
//            logger.error(ex.getMessage());
//            CreateLog.createJson(ex.getMessage(), "ERROR_reimbursementRepo");
//            System.out.println("ERROR: " + ex.getMessage());
//            return null;
//        } finally {
//            if ((entityManager != null) && (entityManager.isOpen())) {
//                entityManager.close();
//            }
//        }
//    }
    @Override
    public List<Reimbursement> listBy(String paramBy, Long empId) {
        try {
            List<Reimbursement> listAcquire = null;
            if (paramBy.contains("admin")) {
                listAcquire = entityManager.createQuery("SELECT r FROM Reimbursement r"
                        + " LEFT JOIN FETCH r.loan AS l "
                        + " LEFT JOIN FETCH r.employee AS e "
                        + " RIGHT JOIN FETCH l.engagement AS n "
                        + " WHERE "
                        + " r.approvedBy = :approvedBy "
                        + " ORDER BY r.tgInput Desc")
                        .setParameter("approvedBy", empId)
                        .getResultList();
            }
            if (paramBy.contains("finance")) {
                listAcquire = entityManager.createQuery("SELECT r FROM Reimbursement r"
                        + " LEFT JOIN FETCH r.loan AS l "
                        + " LEFT JOIN FETCH r.employee AS e "
                        + " RIGHT JOIN FETCH l.engagement AS n "
                        + " WHERE "
                        + " r.reimbursedBy = :reimbursedBy "
                        + " ORDER BY r.tgInput Desc")
                        .setParameter("reimbursedBy", empId)
                        .getResultList();
            }

            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_reimbursementRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Reimbursement> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer count() {
        Query queryMax = entityManager.createQuery("SELECT COUNT(r) FROM Reimbursement r");
        return Integer.parseInt(queryMax.getSingleResult().toString());
    }

    @Override
    public EntityManager getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
