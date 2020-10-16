/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.DocumentReimburse;
import com.lawfirm.apps.repo.interfaces.DocumentReimburseRepoIface;
import com.lawfirm.apps.utils.CreateLog;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author newbiecihuy
 */
@Repository("documentReimburseRepo")
public class DocumentReimburseRepo implements DocumentReimburseRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public DocumentReimburse create(DocumentReimburse entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_documentReimburseRepo");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public DocumentReimburse update(DocumentReimburse entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_documentReimburseRepo");
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
    public DocumentReimburse delete(DocumentReimburse entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_documentReimburseRepo");
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
    public void remove(DocumentReimburse entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_documentReimburseRepo");
            System.out.println("ERROR: " + ex.getMessage());

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public DocumentReimburse findById(Long paramLong) {
        try {
            return (DocumentReimburse) entityManager.find(DocumentReimburse.class, paramLong);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_documentReimburseRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public DocumentReimburse findByName(String paramString, Long paramLong) {
        try {
            DocumentReimburse listAcquire = (DocumentReimburse) entityManager.createQuery("SELECT d FROM DocumentReimburse d WHERE "
                    + " d.description = :description AND "
                    + " d.reimbursement.reimburseId = :reimburseId ")
                    .setParameter("nameDocument", paramString)
                    .setParameter("reimburseId", paramLong)
                    .getSingleResult();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_documentReimburseRepo");
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
    public List<DocumentReimburse> findByEmployee(String paramString) {
        try {
            List<DocumentReimburse> listAcquire = entityManager.createQuery("SELECT d FROM DocumentReimburse d WHERE "
                    + " d.reimbursement.employee.name = :name OR "
                    + " d.reimbursement.employee.nik = :nik")
                    .setParameter("name", paramString)
                    .setParameter("nik", paramString)
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_documentReimburseRepo");
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
    public List<DocumentReimburse> listActive(String param) {
        try {
            List<DocumentReimburse> listAcquire = entityManager.createQuery("SELECT d FROM DocumentReimburse d WHERE "
                    + " d.status = :status")
                    .setParameter("status", param)
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_documentReimburseRepo");
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
    public List<DocumentReimburse> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer count() {
        Query queryMax = entityManager.createQuery("SELECT COUNT(d) FROM DocumentReimburse d");
        return Integer.parseInt(queryMax.getSingleResult().toString());
    }

    @Override
    public EntityManager getEntityManager() {
        // TODO Auto-generated method stub
        return entityManager;
    }

}
