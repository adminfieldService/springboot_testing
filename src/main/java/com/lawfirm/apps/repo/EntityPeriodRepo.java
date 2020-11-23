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
    public Double previousDisbursement(Integer number, Long userId, String taxYear) {
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
            log.info("isi" + String.format("%.0f", query.getSingleResult()));
            return Double.parseDouble(query.getSingleResult().toString());

//            Query query = null;
//            if (number == 2) {
//                String sql = "SELECT COALESCE(SUM(p.prevDisbursement),0) FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
//                        + " WHERE "
//                        + " p.idEmployee = :idEmployee AND "
//                        + " p.taxYear = :taxYear AND"
//                        + " p.numberDisbursement = :numberDisbursement AND"
//                        + " p.status = :status ";
//                query = entityManager.createQuery(sql);
//                query.setParameter("idEmployee", userId);
//                query.setParameter("taxYear", taxYear);
//                query.setParameter("numberDisbursement", 1);
//                query.setParameter("status", "1");
//                log.info("isi" + String.format("%.0f", query.getSingleResult()));
//
//            }
//            if (number == 3) {
//                String sql = "SELECT COALESCE(SUM(p.prevDisbursement),0) FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
//                        + " WHERE "
//                        + " p.idEmployee = :idEmployee AND "
//                        + " p.taxYear = :taxYear AND"
//                        + " p.status = :status ";
//                query = entityManager.createQuery(sql);
//                query.setParameter("idEmployee", userId);
//                query.setParameter("taxYear", taxYear);
//                query.setParameter("status", "1");
//                log.info("isi" + String.format("%.0f", query.getSingleResult()));
//
//            }
//            return Double.parseDouble(query.getSingleResult().toString());
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
                    + " p.caseId = :caseId AND "
                    + " p.taxYear = :taxYear AND "
                    + " p.status = :status ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("idEmployee", userId);
            query.setParameter("caseId", caseId);
            query.setParameter("taxYear", taxYear);
            query.setParameter("status", "1");
//    String sql = "SELECT p FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
//                    + " WHERE "
//                    + " p.idEmployee = :idEmployee AND "   
//                    + " p.taxYear = :taxYear AND "
//                    + " p.status = :status ";
//            Query query = entityManager.createQuery(sql);
//            query.setParameter("idEmployee", userId);
//            query.setParameter("taxYear", taxYear);
//            query.setParameter("status", "1");
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
//                log.info(" >> " + numberDisbursement + ":" + userId + ":" + taxYear);
//                String sql = "SELECT COALESCE(SUM(p.incomeTaxPaidOnPriorPeriod ),0) FROM EntityPeriod p "
//                        + " WHERE "//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
//                        + " p.numberDisbursement = :numberDisbursement AND "
//                        + " p.idEmployee = :idEmployee AND "
//                        + " p.taxYear = :taxYear AND "
//                        + " p.status = :status ";
                String sql = "SELECT p.incomeTaxPaidOnPriorPeriod FROM EntityPeriod p "
                        + " WHERE "
                        + " p.numberDisbursement = :numberDisbursement AND "
                        + " p.idEmployee = :idEmployee AND "
                        + " p.taxYear = :taxYear AND "
                        + " p.status = :status ";
                query = entityManager.createQuery(sql);
                log.info(" sql >> " + sql);
//                log.info(" query >> " + query);
                query.setParameter("numberDisbursement", 1);
                query.setParameter("idEmployee", userId);
                query.setParameter("taxYear", taxYear);
                query.setParameter("status", "1");
            }
            if (numberDisbursement == 3) {
                String sql = "SELECT p.incomeTaxPaidOnPriorPeriod FROM EntityPeriod p "
                        + " WHERE "//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                        + " p.numberDisbursement = :numberDisbursement AND "
                        + " p.idEmployee = :idEmployee AND "
                        + " p.taxYear = :taxYear AND "
                        + " p.status = :status ";
                query = entityManager.createQuery(sql);
                query.setParameter("numberDisbursement", 2);
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

    @Override
    public Double getPreviousDisbursement(Integer number, Long userId, String taxYear) {
        try {
            Query query = null;
            if (number == 2) {
                String sql = "SELECT COALESCE(SUM(p.prevDisbursement),0) FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                        + " WHERE "
                        + " p.idEmployee = :idEmployee AND "
                        + " p.taxYear = :taxYear AND"
                        + " p.numberDisbursement = :numberDisbursement AND"
                        + " p.status = :status ";
                query = entityManager.createQuery(sql);
                query.setParameter("idEmployee", userId);
                query.setParameter("taxYear", taxYear);
                query.setParameter("numberDisbursement", 1);
                query.setParameter("status", "1");
                log.info("isi" + String.format("%.0f", query.getSingleResult()));

            }
            if (number == 3) {
                String sql = "SELECT COALESCE(SUM(p.prevDisbursement),0) FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                        + " WHERE "
                        + " p.idEmployee = :idEmployee AND "
                        + " p.taxYear = :taxYear AND"
                        + " p.numberDisbursement = :numberDisbursement AND"
                        + " p.status = :status ";
                query = entityManager.createQuery(sql);
                query.setParameter("idEmployee", userId);
                query.setParameter("taxYear", taxYear);
                query.setParameter("numberDisbursement", 2);
                query.setParameter("status", "1");
                log.info("isi" + String.format("%.0f", query.getSingleResult()));

            }
            return Double.parseDouble(query.getSingleResult().toString());
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
    public EntityPeriod getPrevDisbursement(Integer number, Long userId, String taxYear) {
        try {
            Query query = null;
            if (number == 1) {
                String sql = "SELECT COALESCE(SUM(p.prevDisbursement),0) FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                        + " WHERE "
                        + " p.idEmployee = :idEmployee AND "
                        + " p.taxYear = :taxYear AND"
                        + " p.numberDisbursement = :numberDisbursement AND"
                        + " p.status = :status ";
                query = entityManager.createQuery(sql);
                query.setParameter("idEmployee", userId);
                query.setParameter("taxYear", taxYear);
                query.setParameter("numberDisbursement", 1);
                query.setParameter("status", "1");
                log.info("isi" + query.getSingleResult());

            }
            if (number == 2) {
                String sql = "SELECT COALESCE(SUM(p.prevDisbursement),0) FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                        + " WHERE "
                        + " p.idEmployee = :idEmployee AND "
                        + " p.taxYear = :taxYear AND"
                        + " p.numberDisbursement = :numberDisbursement AND"
                        + " p.status = :status ";
                query = entityManager.createQuery(sql);
                query.setParameter("idEmployee", userId);
                query.setParameter("taxYear", taxYear);
                query.setParameter("numberDisbursement", 2);
                query.setParameter("status", "1");
                log.info("isi" + query.getSingleResult());

            }
            if (number == 3) {
                String sql = "SELECT p FROM EntityPeriod p"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                        + " WHERE "
                        + " p.idEmployee = :idEmployee AND "
                        + " p.taxYear = :taxYear AND"
                        + " p.numberDisbursement = :numberDisbursement AND"
                        + " p.status = :status ";
                query = entityManager.createQuery(sql);
                query.setParameter("idEmployee", userId);
                query.setParameter("taxYear", taxYear);
                query.setParameter("numberDisbursement", 3);
                query.setParameter("status", "1");
                log.info("isi" + query.getSingleResult());

            }
            return (EntityPeriod) query.getSingleResult();
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
