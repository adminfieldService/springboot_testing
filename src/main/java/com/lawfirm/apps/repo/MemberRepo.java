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
import org.slf4j.LoggerFactory;
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
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

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
    public Integer deleteBy(Long team_member_id) {
        int nilai = entityManager.createQuery(
                "DELETE FROM Member m "
                + "WHERE "
                + "m.teamMember.teamMemberId = :teamMemberId ")
                .setParameter("teamMemberId", team_member_id).executeUpdate();
        return nilai;
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
    public Member findById(String paramString) {
        try {
            String sql = "SELECT m FROM Member m "
                    + " JOIN FETCH m.teamMember AS t "
                    + " JOIN FETCH m.employee AS e "
                    + " WHERE "
                    + " m.memberId = :memberId ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("memberId", paramString);
            return (Member) query.getSingleResult();
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
    public List<Member> findByIdTeam(Long paramLong) {
        try {
            String sql = "SELECT m FROM Member m "
                    + " JOIN FETCH m.teamMember AS t "
                    + " JOIN FETCH m.employee AS e "
                    + " WHERE "
                    + " t.teamMemberId = :teamMemberId ";
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

    @Override
    public Member findBy(Long idTeamMember, String employeeId) {
        try {
            String sql = "SELECT m FROM Member m JOIN FETCH m.employee AS e WHERE "
                    + " m.teamMember.teamMemberId = :teamMemberId AND "
                    + " e.employeeId = :employeeId ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("teamMemberId", idTeamMember);
            query.setParameter("employeeId", employeeId);
            if (query != null) {
                return (Member) query.getSingleResult();
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
    public Integer updateFeeMember(Long teamMemberId, Long idEmployee, Double feeShare) {
//        int updateCount = 0;
//        try {
//            Query query = entityManager.createQuery(
//                    "UPDATE Member SET feeShare = :feeShare "
//                    + "WHERE "
//                    + "teamMember.teamMemberId = :teamMemberId AND "
//                    + "employee.idEmployee = :idEmployee")
//                    .setParameter("teamMemberId", teamMemberId)
//                    .setParameter("idEmployee", idEmployee);
//            updateCount = entityManager.executeUpdate();
//        } catch (Exception ex) {
//            logger.error(ex.getMessage());
//            System.out.println("ERROR: " + ex.getMessage());
//            return updateCount;
//        } finally {
//            if ((entityManager != null) && (entityManager.isOpen())) {
//                entityManager.close();
//            }
//        }
        return null;
    }

//    @Override
//    public List<Member> listMemberDisburse() {
//        try {
//            String sql = "SELECT DISTINCT m FROM Member m "
//                    + " JOIN FETCH m.teamMember t "
//                    + " LEFT JOIN FETCH m.employee e "
//                    + " RIGHT JOIN FETCH t.engagement n ";
//            Query query = entityManager.createQuery(sql);
//            return query.getResultList();
//        } catch (Exception ex) {
//            logger.error(ex.getMessage());
//            CreateLog.createJson(ex.getMessage(), "ERROR_memberRepo");
//            System.out.println("ERROR: " + ex.getMessage());
//            return null;
//        } finally {
//            if ((entityManager != null) && (entityManager.isOpen())) {
//                entityManager.close();
//            }
//        }
//    }
    @Override
    public List<Member> listMemberDisburse(Object parameter) {
        try {
            String sql = "SELECT DISTINCT m FROM Member m "
                    + " JOIN FETCH m.teamMember t "
                    + " LEFT JOIN FETCH m.employee e "
                    + " RIGHT JOIN FETCH t.engagement n "
                    + "WHERE n.engagementId = :engagementId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("engagementId", parameter);
            return query.getResultList();
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
}
