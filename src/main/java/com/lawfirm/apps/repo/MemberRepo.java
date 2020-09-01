/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Member;
import com.lawfirm.apps.repo.interfaces.MemberRepoIface;
import com.lawfirm.apps.utils.CreateLog;
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
@Repository("memberRepo")
public class MemberRepo implements MemberRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Member create(Member entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_memberRepo");
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
    public Member update(Member entity) {
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
            CreateLog.createJson(ex.getMessage(), "ERROR_memberRepo");
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
    public Member delete(Member entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Member entity) {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
// 	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_memberRepo");
            System.out.println("ERROR: " + ex.getMessage());

        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Member findById(Long paramLong) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Member> findByIdTeam(Long paramLong) {
        try {
            String sql = "SELECT m FROM Member m "
                    + " JOIN FETCH m.teamMember AS t "
                    + " JOIN FETCH m.employee AS e "
                    + " WHERE "
                    + " t.teamMemberId = :teamMemberId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("teamMemberId", paramLong);
            if (query != null) {
                return query.getResultList();
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_memberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Member> findByCaseId(String param) {
        try {
            String sql = "SELECT m FROM Member m "
                    + " JOIN FETCH m.teamMember AS t "
                    + " LEFT JOIN FETCH t.engagement AS e"
                    + " WHERE "
                    + " e.caseID = :caseID";
            Query query = entityManager.createQuery(sql);
            query.setParameter("caseID", param);
            if (query != null) {
                return query.getResultList();
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_memberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Member> findByEmpId(String param) {
        try {
            String sql = "SELECT m FROM Member m JOIN FETCH m.employee AS e WHERE "
                    + " e.employeeId = :employeeId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("employeeId", param);
            if (query != null) {
                return query.getResultList();
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_memberRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

//    @Override
//    public List<Member> findByTeam(Long param) {
//        try {
//            String sql = "SELECT m FROM Member m JOIN FETCH m.teamMember AS t WHERE "
//                    + " t.team_member_id = :team_member_id";
//            Query query = entityManager.createQuery(sql);
//            query.setParameter("team_member_id", param);
//            if (query != null) {
//                return query.getResultList();
//            } else {
//                return null;
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
//    }
}
