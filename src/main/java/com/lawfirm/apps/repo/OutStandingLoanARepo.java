/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.OutStandingLoanA;
import com.lawfirm.apps.repo.interfaces.OutStandingLoanARepoIface;
import com.lawfirm.apps.utils.CreateLog;
import java.util.Date;
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
@Repository("outStandingLoanARepo")
@Slf4j
public class OutStandingLoanARepo implements OutStandingLoanARepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public OutStandingLoanA create(OutStandingLoanA entity) {
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
            CreateLog.createJson("ERROR_OutStandingLoanARepo", ex.getMessage());
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
    public OutStandingLoanA update(OutStandingLoanA entity) {
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
            CreateLog.createJson("ERROR_OutStandingLoanARepo", ex.getMessage());
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
    public OutStandingLoanA approved(OutStandingLoanA entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OutStandingLoanA delete(OutStandingLoanA entity) {
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
            CreateLog.createJson("ERROR_outStandingLoanARepo", ex.getMessage());
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
    public OutStandingLoanA checkLoan(String param, Long userId) {
        try {
            String sql = "SELECT o FROM OutStandingLoanA o "
                    + " JOIN o.loan AS l "
                    + " WHERE"
                    + " o.idEmployee = :idEmployee";
            Query query = entityManager.createQuery(sql);
            query.setParameter("userId", userId);
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
    public void remove(OutStandingLoanA entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_outStandingLoanARepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public OutStandingLoanA findByIdLoan(String outstandingAUUId) {
        try {
            OutStandingLoanA listAcquire = (OutStandingLoanA) entityManager.createQuery("SELECT o FROM OutStandingLoanA o "
                    + " WHERE "
                    + " o.outstandingAUUId = :outstandingAUUId ")
                    .setParameter("outstandingAUUId", outstandingAUUId)
                    .getSingleResult();
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
    public OutStandingLoanA sumLoanA(Long userId, String taxYear, int number) {
        try {
            String sql = "SELECT o FROM OutStandingLoanA o "//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                    + " WHERE "
                    + " o.idEmployee = :idEmployee AND "
                    + " o.numberDisbursement = :numberDisbursement AND "
                    + " o.taxYear = :taxYear ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("idEmployee", userId);
            query.setParameter("numberDisbursement", Long.valueOf(number));
            query.setParameter("taxYear", taxYear);
//            if (query != null) {
            return (OutStandingLoanA) query.getSingleResult();
//            } else {
//                log.info("isi" + query.getSingleResult().toString());
//                return 0d;
//            }
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
    public OutStandingLoanA findByCaseId(String param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OutStandingLoanA findBy(Long idEmployee, String taxyear, Long disburseId) {
        try {
            OutStandingLoanA listAcquire = (OutStandingLoanA) entityManager.createQuery("SELECT o FROM OutStandingLoanA o "
                    + " WHERE "
                    + " o.idEmployee = :idEmployee AND"
                    + " o.taxYear = :taxYear AND"
                    + " o.numberDisbursement = :numberDisbursement ")
                    .setParameter("idEmployee", idEmployee)
                    .setParameter("taxYear", taxyear)
                    .setParameter("numberDisbursement", disburseId)
                    .getSingleResult();
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
    public Double findByEmployee(Long idEmployee, Date cutOffDate) {
        try {
            String sql = "SELECT COALESCE(SUM(o.disburseableAmount),0) FROM OutStandingLoanA o "
                    + " WHERE "
                    + " o.idEmployee = :idEmployee AND "
                    + " o.cutOffDate = :cutOffDate";
            Query query = entityManager.createQuery(sql);
            query.setParameter("idEmployee", idEmployee)
                    .setParameter("cutOffDate", cutOffDate);
            if (query != null) {
                log.info("isi" + query.getSingleResult().toString());
                return Double.parseDouble(query.getSingleResult().toString());
            } else {
                log.info("isi" + query.getSingleResult().toString());
                return 0d;
            }

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

}
