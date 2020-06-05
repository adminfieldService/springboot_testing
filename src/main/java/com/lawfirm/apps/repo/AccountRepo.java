/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Account;
import com.lawfirm.apps.repo.interfaces.AccountRepoIface;
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
@Repository("accountRepo")
public class AccountRepo implements AccountRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Account create(Account entity) {
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
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Account update(Account entity) {
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
    public Account approved(Account entity) {
        try {
            entity.setIsActive(true);
            entityManager.merge(entity);
            if (entity != null) {
//	                CreateLog.createJson(entity, "fieldservice");
                return entity;
            }
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
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
    public Account delete(Account entity) {
        try {
            entity.setIsActive(false);
            entity.setIsDelete(true);
            entityManager.merge(entity);
            if (entity != null) {
//	                CreateLog.createJson(entity, "fieldservice");
                return entity;
            }
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
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
    public void remove(Account entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Account findById(Long paramLong) {
        try {
            return (Account) entityManager.find(Account.class, paramLong);
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
    public List<Account> findByEmployee(String param) {
        try {
            List<Account> acquire = entityManager.createQuery("SELECT a FROM Account a WHERE "
                    + " LOWER(a.employee.name) = :name OR "
                    + " LOWER(a.employee.nik) = :nik OR "
                    + " LOWER(a.employee.npwp) = :npwp OR "
                    + " a.employee.idEmployee = :idEmployee OR "
                    + " LOWER(a.employee.employeeId) = :employeeId OR "
                    + " LOWER(a.employee.approvedBy) = :approvedBy ")
                    .setParameter("name", param.toLowerCase())
                    .setParameter("nik", param.toLowerCase())
                    .setParameter("npwp", param.toLowerCase())
                    .setParameter("idEmployee", Long.parseLong(param))
                    .setParameter("employeeId", param.toLowerCase())
                    .setParameter("approvedBy", param.toLowerCase())
                    .getResultList();
            return acquire;
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
    public List<Account> listAccount() {
        try {
            List<Account> listAcquire = entityManager.createQuery("SELECT a FROM Account a").getResultList();
            return (List<Account>) listAcquire;
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
    public List<Account> listActive(Boolean isActive) {
        try {
            List<Account> listAcquire = entityManager.createQuery("SELECT a FROM Account a WHERE "
                    + " a.isActive = :isActive")
                    .setParameter("isActive", isActive)
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
    public List<Account> approvedBy(String param) {
        try {
            List<Account> listAcquire = entityManager.createQuery("SELECT a FROM Account a WHERE "
                    + " a.employee.approvedBy = :approvedBy")
                    .setParameter("approvedBy", param)
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
    public Integer count() {
        Query queryMax = entityManager.createQuery("SELECT COUNT(a) FROM Account a");
        return Integer.parseInt(queryMax.getSingleResult().toString());
    }

    @Override
    public EntityManager getEntityManager() {
        // TODO Auto-generated method stub
        return entityManager;
    }

}
