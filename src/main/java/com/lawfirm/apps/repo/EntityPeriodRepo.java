/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.EntityPeriod;
import com.lawfirm.apps.repo.interfaces.EntityPeriodRepoIface;
import com.lawfirm.apps.utils.CreateLog;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author newbiecihuy
 */
@Repository("periodRepo")
@Slf4j
public class EntityPeriodRepo implements EntityPeriodRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
     private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public EntityPeriod create(EntityPeriod entity) {
        try {
            entityManager.persist(entity);
            if (entity != null) {
//                CreateLog.createJson(entity, "bpr_baiturridho");
                return entity;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_periodRepo");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public EntityPeriod update(EntityPeriod entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_periodRepo");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public EntityPeriod delete(EntityPeriod entity) {
        try {
            entity.setStatus("2");
            entityManager.merge(entity);
            if (entity != null) {
//	                CreateLog.createJson(entity, "fieldservice");
                return entity;
            }
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_engagementRepo");
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
    public EntityPeriod findById(Integer number, String taxYear) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double previousDisbursement(Long userId, String taxYear) {
        try {
            String sql = "SELECT COALESCE(SUM(p.prevDisbursement),0) FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                    + " WHERE "
                    + " p.idEmployee = :idEmployee AND "
                    + " p.taxYear = :taxYear AND"
                    + " p.status = :status ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("idEmployee", userId);
            query.setParameter("taxYear", taxYear);
            query.setParameter("status", "1");
//            if (query != null) {
            log.info("isi" + String.format("%.0f", query.getSingleResult()));
            return Double.parseDouble(query.getSingleResult().toString());
//            } else {
//                log.info("isi" + query.getSingleResult().toString());
//                return 0d;
//            }
        } catch (NumberFormatException ex) {
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
    public EntityPeriod findBy(Long userId, String caseId, String taxYear) {
        try {
            String sql = "SELECT p FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                    + " WHERE "
                    + " p.idEmployee = :idEmployee AND "
                    + " p.taxYear = :taxYear AND "
                    + " p.caseId = :caseId AND "
                    + " p.status = :status ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("idEmployee", userId);
            query.setParameter("caseId", caseId);
            query.setParameter("taxYear", taxYear);
            query.setParameter("status", "1");
//            if (query != null) {
            log.info("isi" + query.getSingleResult());
            return (EntityPeriod) query.getSingleResult();
//            } else {
//                log.info("isi" + query.getSingleResult().toString());
//                return 0d;
//            }
        } catch (NumberFormatException ex) {
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
    public Double incomeTaxPaidOnPriorPeriod(Integer numberDisbursement, Long userId, String taxYear) {
        try {
            Query query = null;
            if (numberDisbursement == 1) {
                String sql = "SELECT COALESCE(SUM(p.income_tax_paid_on_prior_period),0) FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                        + " WHERE "
                        + " p.numberDisbursement = :numberDisbursement"
                        + " p.idEmployee = :idEmployee AND "
                        + " p.taxYear = :taxYear AND"
                        + " p.status = :status ";
                query = entityManager.createQuery(sql);
                query.setParameter("numberDisbursement", numberDisbursement);
                query.setParameter("idEmployee", userId);
                query.setParameter("taxYear", taxYear);
                query.setParameter("status", "1");
            }
            if (numberDisbursement == 3) {
                String sql = "SELECT COALESCE(SUM(p.income_tax_paid_on_prior_period),0) FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                        + " WHERE "
                        + " p.idEmployee = :idEmployee AND "
                        + " p.taxYear = :taxYear AND "
                        + " p.status = :status ";
                query = entityManager.createQuery(sql);
                query.setParameter("idEmployee", userId);
                query.setParameter("taxYear", taxYear);
                query.setParameter("status", "1");
            }

            log.info("isi" + String.format("%.0f", query.getSingleResult()));
            return Double.parseDouble(query.getSingleResult().toString());
//            } else {
//                log.info("isi" + query.getSingleResult().toString());
//                return 0d;
//            }
        } catch (NumberFormatException ex) {
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
}
