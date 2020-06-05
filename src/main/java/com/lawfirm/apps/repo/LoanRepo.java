/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.repo.interfaces.LoanRepoIface;
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
@Repository("loanRepo")
public class LoanRepo implements LoanRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Loan create(Loan entity) {
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
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Loan update(Loan entity) {
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
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    @SuppressWarnings("unused")
    public Loan delete(Loan entity) {
        try {
            entity.setIsActive("2");
            entityManager.merge(entity);
            if (entity != null) {
// 	                CreateLog.createJson(entity, "fieldservice");
                return entity;
            }
        } catch (Exception ex) {
// 	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
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
    public void remove(Loan entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
// 	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Loan findById(Long paramLong) {
        try {
            return (Loan) entityManager.find(Loan.class, paramLong);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Loan findByName(String namaVisit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Loan> listLoan() {
        try {
            List<Loan> listAcquire = entityManager.createQuery("SELECT l FROM Loan l ")
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> listLoanPaging(int max, int start) {
        try {
            List<Loan> listAcquire = entityManager.createQuery("SELECT l FROM Loan l ")
                    .setMaxResults(max)
                    .setFirstResult(start)
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Loan> findByEmployee(int max, int start, String param) {
        try {
            List<Loan> listAcquire = entityManager.createQuery("SELECT l FROM Loan l WHERE "
                    + " LOWER(l.employee.name) = :name OR "
                    + " l.employee.nik  = :nik OR "
                    + " LOWER(l.employee.email) = :email OR "
                    + " l.employee.npwp = :npwp OR "
                    + " LOWER(l.userName) = :userName ")
                    .setParameter("name", param.toLowerCase())
                    .setParameter("nik", param.toLowerCase())
                    .setParameter("email", param.toLowerCase())
                    .setParameter("npwp", param.toLowerCase())
                    .setParameter("userName", param.toLowerCase())
                    .setMaxResults(max)
                    .setFirstResult(start)
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> listActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Loan> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer count() {
        Query queryMax = entityManager.createQuery("SELECT COUNT(l) FROM Loan l");
        return Integer.parseInt(queryMax.getSingleResult().toString());
    }

    @Override
    public EntityManager getEntityManager() {
        // TODO Auto-generated method stub
        return entityManager;
    }
}
