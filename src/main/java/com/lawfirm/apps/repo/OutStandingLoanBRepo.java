/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.OutStandingLoanB;
import com.lawfirm.apps.utils.CreateLog;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.lawfirm.apps.repo.interfaces.OutStandingLoanBRepoIface;
import org.slf4j.LoggerFactory;

/**
 *
 * @author newbiecihuy
 */
@Repository("outStandingLoanBRepo")
@Slf4j
public class OutStandingLoanBRepo implements OutStandingLoanBRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public OutStandingLoanB create(OutStandingLoanB entity) {
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
            CreateLog.createJson("ERROR_outStandingLoanBRepo", ex.getMessage());
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
    public OutStandingLoanB update(OutStandingLoanB entity) {
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
            CreateLog.createJson("ERROR_outStandingLoanBRepo", ex.getMessage());
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
    public OutStandingLoanB approved(OutStandingLoanB entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OutStandingLoanB delete(OutStandingLoanB entity) {
        try {
            entity.setIsActive(false);
            entityManager.merge(entity);
            if (entity != null) {
//	                CreateLog.createJson(entity, "fieldservice");
                return entity;
            }
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_outStandingLoanBRepo", ex.getMessage());
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
    public void remove(OutStandingLoanB entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_outStandingLoanBRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<OutStandingLoanB> findByIdLoan(Long paramLong) {
        try {
            List<OutStandingLoanB> listAcquire = entityManager.createQuery("SELECT o FROM OutStandingLoanB o "
                    + " JOIN FETCH o.loan AS l "
                    + " WHERE "
                    + " l.Id = :Id ")
                    .setParameter("Id", paramLong)
                    .getResultList();
            //                        .setParameter("status", "d")

            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_outStandingLoanBRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public OutStandingLoanB findByCaseId(String param) {
        try {
            String sql = "SELECT o FROM OutStandingLoanB o "
                    + " WHERE "
                    + "o.caseId = :caseId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("caseId", param);
            return (OutStandingLoanB) query.getSingleResult();
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_outStandingLoanBRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }
    
     @Override
    public List<OutStandingLoanB> lsitByCaseId(String param) {
        try {
            String sql = "SELECT o FROM OutStandingLoanB o "
                    + " WHERE "
                    + "o.caseId = :caseId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("caseId", param);
            return query.getResultList();
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_outStandingLoanBRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Double sumLoanByCaseId(String param) {
        try {
            String sql = "SELECT COALESCE(SUM(o.reimburseAmount),0) FROM OutStandingLoanB o "
                    + " JOIN o.loan AS l "
                    + " WHERE "
                    + " o.caseId = :caseId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("caseId", param);
            if (query != null) {
                log.info("isi" + query.getSingleResult().toString());
                return Double.parseDouble(query.getSingleResult().toString());
            } else {
                log.info("isi" + query.getSingleResult().toString());
                return 0d;
            }
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_outStandingLoanBRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }
    @Override
    public Double sumLoan(Long paramLong) {
        try {
            String sql = "SELECT COALESCE(SUM(o.reimburseAmount),0) FROM OutStandingLoanB o "
                    + " JOIN o.loan AS l "
                    + " WHERE "
                    + " l.Id = :Id";
            Query query = entityManager.createQuery(sql);
            query.setParameter("Id", paramLong);
            if (query != null) {
                log.info("isi" + query.getSingleResult().toString());
                return Double.parseDouble(query.getSingleResult().toString());
            } else {
                log.info("isi" + query.getSingleResult().toString());
                return 0d;
            }
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_outStandingLoanBRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public OutStandingLoanB checkLoan(String param, Long userId) {
        try {
            String sql = "SELECT o FROM OutStandingLoanB o "
                    + " JOIN o.loan AS l "
                    + " WHERE"
                    + " o.caseId = :caseId AND "
                    + " o.idEmployee = :idEmployee";
            Query query = entityManager.createQuery(sql);
            query.setParameter("caseId", param);
            query.setParameter("idEmployee", userId);
            return null;
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_outStandingLoanBRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public OutStandingLoanB checkReimburse(String param, Long userId) {
        try {
            String sql = "SELECT o FROM OutStandingLoanB o "
                    + " JOIN o.loan AS l "
                    + " WHERE"
                    + " o.caseId = :caseId AND "
                    + " o.idEmployee = :idEmployee";
            Query query = entityManager.createQuery(sql);
            query.setParameter("caseId", param);
            query.setParameter("idEmployee", userId);
            return null;
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_outStandingLoanBRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }
}
