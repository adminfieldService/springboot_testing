/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.TeamMember;
import com.lawfirm.apps.repo.interfaces.TeamMemberRepoIface;
import com.lawfirm.apps.utils.CreateLog;
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
@Repository("teamMemberRepo")
public class TeamMemberRepo implements TeamMemberRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public TeamMember create(TeamMember entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
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
    public TeamMember update(TeamMember entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public TeamMember delete(TeamMember entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
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
    public void remove(TeamMember entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
// 	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
            System.out.println("ERROR: " + ex.getMessage());

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public TeamMember findById(Long paramLong) {
        try {
            return (TeamMember) entityManager.find(TeamMember.class, paramLong);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public TeamMember findByEngId(Long engID) {
        try {
            TeamMember acquire = (TeamMember) entityManager.createQuery("SELECT t FROM TeamMember t "
                    + " JOIN FETCH t.engagement AS e "
                    + " WHERE "
                    + " e.engagementId = :engagementId ")
                    .setParameter("engagementId", engID)
                    .getSingleResult();
            return acquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }

    @Override
    public TeamMember findByName(String namaVisit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TeamMember> listTeamMember() {
        try {
            List<TeamMember> listAcquire = entityManager.createQuery("SELECT t FROM TeamMember t ")
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<TeamMember> generateTeamCaseId(String param1) {
        try {
//              List<CaseDetails> listAcquire = entityManager.createQuery("SELECT COUNT(c) FROM CaseDetails c WHERE "
            List<TeamMember> listAcquire = entityManager.createQuery("SELECT t FROM TeamMember t WHERE "
                    + " c.tahun_input = :tahun_input ")
                    .setParameter("tahun_input", param1)
                    .getResultList();
            if (listAcquire == null) {
                return null;
            } else {
                return listAcquire;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }

    @Override
    public TeamMember findByTeamCaseId(String caseID, String paramY) {
        try {
            TeamMember acquire = (TeamMember) entityManager.createQuery("SELECT t FROM TeamMember t WHERE "
                    + " t.description = :description AND "
                    + " t.tahun_input = :tahun_input ")
                    .setParameter("description", caseID)
                    .setParameter("tahun_input", paramY)
                    .getSingleResult();
            return acquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }

    }

    @Override
    public List<TeamMember> listActive(Boolean param) {
        try {
            List<TeamMember> listAcquire = entityManager.createQuery("SELECT t FROM TeamMember t WHERE "
                    + " t.isActive = :isActive")
                    .setParameter("isActive", param)
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<TeamMember> findByDmp(Long dmpId) {
        try {
            List<TeamMember> listAcquire = entityManager.createQuery("SELECT t FROM TeamMember t WHERE "
                    + " t.dmpId = :dmpId")
                    .setParameter("dmpId", dmpId)
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<TeamMember> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer count() {
        Query queryMax = entityManager.createQuery("SELECT COUNT(t) FROM TeamMember t");
        return Integer.parseInt(queryMax.getSingleResult().toString());
    }

    @Override
    public EntityManager getEntityManager() {
        // TODO Auto-generated method stub
        return entityManager;
    }

    @Override
    public List<TeamMember> listTeamMemberByEngagement(Long param) {
        try {
            List<TeamMember> listAcquire = entityManager.createQuery("SELECT t FROM TeamMember t "
                    + " JOIN FETCH t.engagement AS e "
                    + " WHERE "
                    + " e.engagementId  = :engagementId")
                    .setParameter("engagementId", param)
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_teamMemberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public TeamMember updateFeeDmp(Long engagementId, Long dmpId, Double feeShare) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
