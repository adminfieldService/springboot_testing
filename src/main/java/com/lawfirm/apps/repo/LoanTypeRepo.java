/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.LoanType;
import com.lawfirm.apps.repo.interfaces.LoanTypeRepoIface;
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
@Repository("loanTypeRepo")
public class LoanTypeRepo implements LoanTypeRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public LoanType create(LoanType entity) {
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
    public LoanType update(LoanType entity) {
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
    public LoanType delete(LoanType entity) {
        try {
            entity.setIsActive(false);
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
    public void remove(LoanType entity) {
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
    public LoanType findById(Long paramLong) {
        try {
            return (LoanType) entityManager.find(LoanType.class, paramLong);
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
    public LoanType findByName(String param) {
        try {
            LoanType listAcquire = (LoanType) entityManager.createQuery("SELECT l FROM LoanType l WHERE "
                    + " LOWER(l.typeLoan) is not null LIKE :typeLoan OR "
                    + " LOWER(l.dsc) is not null LIKE :dsc ")
                    .setParameter("typeLoan", "%" + param.toLowerCase())
                    .setParameter("dsc", "%" + param.toLowerCase())
                    .getSingleResult();
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
    public List<LoanType> listLoanType() {
        try {
            List<LoanType> listAcquire = entityManager.createQuery("SELECT l FROM LoanType l ")
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
    public List<LoanType> listLoanTypePaging(int max, int start) {
        try {
            List<LoanType> listAcquire = entityManager.createQuery("SELECT l FROM LoanType l ")
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
    public List<LoanType> listActive(int max, int start, Boolean param) {
        try {
            List<LoanType> listAcquire = entityManager.createQuery("SELECT l FROM LoanType l WHERE "
                    + " l.isActive = :isActive")
                    .setParameter("isActive", param)
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
    public List<LoanType> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer count() {
        Query queryMax = entityManager.createQuery("SELECT COUNT(l) FROM LoanType l");
        return Integer.parseInt(queryMax.getSingleResult().toString());
    }

    @Override
    public EntityManager getEntityManager() {
        // TODO Auto-generated method stub
        return entityManager;
    }
}
