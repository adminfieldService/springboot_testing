/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.repo.interfaces.CaseDetailsRepoIface;
import com.lawfirm.apps.utils.CreateLog;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
@Repository("caseDetailsRepo")
public class CaseDetailsRepo implements CaseDetailsRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public CaseDetails create(CaseDetails entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
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
    public CaseDetails update(CaseDetails entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
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
    public CaseDetails approved(CaseDetails entity) {
        try {
            entity.setApproved_date(new Date());
            entityManager.merge(entity);
            if (entity != null) {
//                CreateLog.createJson(entity, "fieldservice");
                return entity;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
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
    public CaseDetails delete(CaseDetails entity) {
        try {
            entity.setIsActive("0");
            entityManager.merge(entity);
            if (entity != null) {
//	                CreateLog.createJson(entity, "fieldservice");
                return entity;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            System.out.println("ERROR: " + ex.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
        return null;
    }

    @Override
    public void remove(CaseDetails entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
//	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            System.out.println("ERROR: " + ex.getMessage());

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public CaseDetails findById(Long paramLong) {
        try {
//            return (CaseDetails) entityManager.find(CaseDetails.class, paramLong);
            CaseDetails acquire = (CaseDetails) entityManager.createQuery("SELECT c FROM CaseDetails c WHERE "
                    + " c.engagementId = :engagementId ")
                    .setParameter("engagementId", paramLong)
                    .getSingleResult();
            return acquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<CaseDetails> findByEngagementId(Long paramLong) {
        try {
//            return (CaseDetails) entityManager.find(CaseDetails.class, paramLong);
            List<CaseDetails> list_acquire = entityManager.createQuery("SELECT c FROM CaseDetails c "
                    + " JOIN FETCH c.employee AS e "
                    + " LEFT JOIN FETCH c.client AS t "
                    + " WHERE "
                    + " c.engagementId = :engagementId ")
                    .setParameter("engagementId", paramLong)
                    .getResultList();
            return list_acquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public CaseDetails findByCaseId(String caseID, String paramY) {
        try {
            CaseDetails acquire = (CaseDetails) entityManager.createQuery("SELECT c FROM CaseDetails c WHERE "
                    + " c.caseID = :caseID AND "
                    + " c.tahun_input = :tahun_input AND "
                    + " c.status = :status ")
                    .setParameter("caseID", caseID)
                    .setParameter("tahun_input", paramY)
                    .setParameter("status", "a")
                    .getSingleResult();
            return acquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }

    @Override
    public Optional<CaseDetails> checkCaseId(String caseID, String paramY) {
        try {
            return (Optional<CaseDetails>) entityManager.createQuery("SELECT c FROM CaseDetails c WHERE "
                    + " c.caseID = :caseID AND "
                    + " c.tahun_input = :tahun_input AND "
                    + " c.status = :status ")
                    .setParameter("caseID", caseID)
                    .setParameter("tahun_input", paramY)
                    .setParameter("status", "a")
                    .getSingleResult();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }

    @Override
    public CaseDetails findCaseId(String caseID) {
        try {
            CaseDetails acquire = (CaseDetails) entityManager.createQuery("SELECT c FROM CaseDetails c "
                    + " JOIN FETCH c.employee  e"
                    + " JOIN FETCH c.client t "
                    + " WHERE "
                    + " c.caseID = :caseID")
                    .setParameter("caseID", caseID)
                    .getSingleResult();
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
    public List<CaseDetails> listCaseDetails() {
        try {
            List<CaseDetails> listAcquire = entityManager.createQuery("SELECT c FROM CaseDetails c "
                    + " JOIN FETCH c.employee  e"
                    + " JOIN FETCH c.client t "
                    + " ORDER BY c.engagementId DESC").getResultList();
//            List<CaseDetails> listAcquire = entityManager.createQuery("SELECT distinct c FROM CaseDetails c "
//                    + " JOIN FETCH c.employee AS e").getResultList();
            return (List<CaseDetails>) listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
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
    public List<CaseDetails> listActive(Boolean isActive) {
        try {
            List<CaseDetails> listAcquire = entityManager.createQuery("SELECT c FROM CaseDetails c "
                    + " JOIN FETCH c.employee AS e "
                    + " LEFT JOIN FETCH c.client AS t"
                    + " WHERE "
                    + " c.isActive = :isActive")
                    .setParameter("isActive", isActive)
                    .getResultList();
            return (List<CaseDetails>) listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }

    @Override
    public List<CaseDetails> findByEmployee(Long paramLong) {
        try {
            List<CaseDetails> listAcquire = entityManager.createQuery("SELECT DISTINCT c FROM CaseDetails c"
                    + " JOIN FETCH c.employee AS e "
                    + " LEFT JOIN FETCH c.client AS t "
                    + " JOIN  c.teamMemberCollection AS a "
                    + " JOIN  a.memberCollection  AS b "
                    + " JOIN  b.employee AS d "
                    + "  WHERE "
                    + " e.idEmployee = :idEmployee OR "
                    + " d.idEmployee = :id_employee ")
                    .setParameter("idEmployee", paramLong)
                    .setParameter("id_employee", paramLong)
                    .getResultList();
            return (List<CaseDetails>) listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }

    @Override
    public List<CaseDetails> getCaseId() {
        try {
            List<CaseDetails> listAcquire = entityManager.createQuery("SELECT c FROM CaseDetails c "
                    + " WHERE "
                    + " c.status = :status ")
                    .setParameter("status", "a")
                    .getResultList();
            return (List<CaseDetails>) listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }

    @Override
    public List<CaseDetails> findByAdmin(Long paramLong) {
        try {
            List<CaseDetails> listAcquire = entityManager.createQuery("SELECT c FROM CaseDetails c "
                    + " JOIN FETCH c.employee AS e "
                    + " LEFT JOIN FETCH c.client AS t "
                    + " WHERE  "
                    + " c.approvedBy = :approvedBy")
                    .setParameter("approvedBy", paramLong.toString())
                    .getResultList();
            if (listAcquire == null) {
                return null;
            } else {
                return (List<CaseDetails>) listAcquire;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }
//    Query queryMax = entityManager.createQuery("SELECT COUNT(l) FROM Loan l "
//                    + " WHERE "
//                    + " l.loantype.typeLoan = :typeLoan "
//                    + " AND l.employee.employeeId = :employeeId "
//                    + " AND l.tgl_input = :tgl_input")
//                    .setParameter("typeLoan", param1.toLowerCase())
//                    .setParameter("employeeId", param2.toLowerCase())
//                    .setParameter("tgl_input", param3);
//            return Integer.parseInt(queryMax.getSingleResult().toString());

//    @Override
//    public Integer generateCaseId(String param1) {
//        try {
//            Integer listAcquire = (Integer) entityManager.createQuery("SELECT COUNT(c) FROM CaseDetails c WHERE "
//                    + " c.tahun_input = :tahun_input AND "
//                    + " c.status = :status")
//                    .setParameter("tahun_input", param1)
//                    .setParameter("status", "a")
//                    .getSingleResult();
//            if (listAcquire == null) {
//                return 0;
//            } else {
//                return listAcquire;
//            }
//        } catch (Exception ex) {
//            logger.error(ex.getMessage());
//            System.out.println("ERROR: " + ex.getMessage());
//            return null;
//        } finally {
//            if ((entityManager != null) && (entityManager.isOpen())) {
//                entityManager.close();
//            }
//        }
//
//    }
    @Override
    public List<CaseDetails> generateCaseId(String param1) {
        try {
//              List<CaseDetails> listAcquire = entityManager.createQuery("SELECT COUNT(c) FROM CaseDetails c WHERE "
            List<CaseDetails> listAcquire = entityManager.createQuery("SELECT c FROM CaseDetails c WHERE "
                    + " c.tahun_input = :tahun_input AND "
                    + " c.status = :status")
                    .setParameter("tahun_input", param1)
                    .setParameter("status", "a")
                    .getResultList();
            if (listAcquire == null) {
                return null;
            } else {
                return listAcquire;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_caseDetailsRepo");
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }

    @Override
    public List<CaseDetails> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer count() {
        Query queryMax = entityManager.createQuery("SELECT COUNT(c) FROM CaseDetails c");
        return Integer.parseInt(queryMax.getSingleResult().toString());
    }

    @Override
    public EntityManager getEntityManager() {
        // TODO Auto-generated method stub
        return entityManager;
    }

}
