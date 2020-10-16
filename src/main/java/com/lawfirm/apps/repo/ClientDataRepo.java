/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.ClientData;
import com.lawfirm.apps.repo.interfaces.ClientDataRepoIface;
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
@Repository("clientDataRepo")
public class ClientDataRepo implements ClientDataRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientData create(ClientData entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_clientDataRepo");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public ClientData update(ClientData entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_clientDataRepo");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public ClientData delete(ClientData entity) {
        try {
            entity.setIsActive("0");
            entityManager.merge(entity);
            if (entity != null) {
//	                CreateLog.createJson(entity, "fieldservice");
                return entity;
            }
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_clientDataRepo");
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
    public void remove(ClientData entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_clientDataRepo");
            System.out.println("ERROR: " + ex.getMessage());

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public ClientData findById(Long paramLong) {
        try {
            return (ClientData) entityManager.find(ClientData.class, paramLong);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_clientDataRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public ClientData findByName(String paramString) {
        try {
            ClientData listAcquire = (ClientData) entityManager.createQuery("SELECT c FROM ClientData c WHERE "
                    + " LOWER(c.clientName) = :clientName OR "
                    + " LOWER(c.address) = :address OR "
                    + " c.npwp = :npwp ")
                    .setParameter("clientName", paramString.toLowerCase())
                    .setParameter("address", paramString.toLowerCase())
                    .setParameter("npwp", paramString)
                    .getSingleResult();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_clientDataRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public ClientData checkCI(String paramString) {
        try {
            ClientData listAcquire = (ClientData) entityManager.createQuery("SELECT c FROM ClientData c WHERE "
                    + " c.clientId = :clientId ")
                    .setParameter("clientId", paramString.toLowerCase())
                    .getSingleResult();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_clientDataRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public ClientData findBydataClient(String paramString1, String paramString2, String paramString3) {
        try {
            ClientData listAcquire = (ClientData) entityManager.createQuery("SELECT c FROM ClientData c WHERE "
                    + " LOWER(c.clientName) = :clientName AND "
                    + " LOWER(c.address) = :address AND "
                    + " c.npwp = :npwp ")
                    .setParameter("clientName", paramString1.toLowerCase())
                    .setParameter("address", paramString2.toLowerCase())
                    .setParameter("npwp", paramString3)
                    .getSingleResult();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_clientDataRepo");
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
    public List<ClientData> listClient() {
        try {
            List<ClientData> listAcquire = entityManager.createQuery("SELECT c FROM ClientData c").getResultList();
            return (List<ClientData>) listAcquire;
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
    public List<ClientData> listActive(String isActive) {
        try {
            List<ClientData> listAcquire = entityManager.createQuery("SELECT c FROM ClientData c WHERE "
                    + " c.isActive = :isActive ")
                    .setParameter("isActive", isActive)
                    .getResultList();
            return (List<ClientData>) listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_clientDataRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }

    @Override
    public List<ClientData> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer count() {
        Query queryMax = entityManager.createQuery("SELECT COUNT(c) FROM ClientData c");
        return Integer.parseInt(queryMax.getSingleResult().toString());
    }

    @Override
    public EntityManager getEntityManager() {
        // TODO Auto-generated method stub
        return entityManager;
    }

    @Override
    public Integer generateCleintId(String npwp) {
        Query queryMax = entityManager.createQuery("SELECT COUNT(c) FROM ClientData c ");
//           Query queryMax = entityManager.createQuery("SELECT COUNT(c) FROM ClientData c WHERE c.npwp = :npwp")
//                .setParameter("npwp",npwp);
        return Integer.parseInt(queryMax.getSingleResult().toString());
    }
}
