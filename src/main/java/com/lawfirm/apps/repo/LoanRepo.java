/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.repo.interfaces.LoanRepoIface;
import com.lawfirm.apps.utils.CreateLog;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author newbiecihuy
 */
@Repository("loanRepo")
@Slf4j
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
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
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
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
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
            entity.setIsActive("3");
            entityManager.merge(entity);
            if (entity != null) {
// 	                CreateLog.createJson(entity, "fieldservice");
                return entity;
            }
        } catch (Exception ex) {
// 	            LogSystem.error(getClass(), e);
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
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
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
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
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Loan findByIdLoan(Long paramLong) {
        try {
//         from CellPhoneBaiturridho as c JOIN fetch c.officer as o where o.email = :email";
//            Loan entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l JOIN FETCH l.employee e WHERE "
//                    + " l.loanId = :loanId")
//                    .setParameter("loanId", paramLong)
//                    .getSingleResult();
            String sql = "SELECT l FROM Loan l JOIN FETCH l.employee AS e WHERE "
                    + " l.Id = :Id";
            Query query = entityManager.createQuery(sql);
            query.setParameter("Id", paramLong);
            if (query != null) {
                return (Loan) query.setFirstResult(0).getSingleResult();
            } else {
                return (Loan) null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Loan findByLoanId(String param) {
        try {
            Loan entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l WHERE "
                    + " l.loanId = :loanId")
                    .setParameter("loanId", param)
                    .getSingleResult();
            return entity;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Loan findByLoanIdB(String param) {
        try {
            Loan entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l WHERE "
                    + " l.loanId = :loanId")
                    .setParameter("loanId", param)
                    .getSingleResult();
            return entity;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> listLoan(int max, int start, String type) {
        try {
            List<Loan> listAcquire = null;
            if (type.contentEquals("a")) {
                if (start == 0) {
                    listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                            + " JOIN FETCH l.employee AS e "
                            + " LEFT JOIN FETCH l.loantype AS t "
                            + " WHERE t.typeLoan = :typeLoan "
                            + " ORDER BY l.date_created Desc ")
                            .setParameter("typeLoan", "a")
                            .getResultList();
                } else {
                    listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                            + " JOIN FETCH l.employee AS e "
                            + " LEFT JOIN FETCH l.loantype AS t"
                            + " WHERE t.typeLoan = :typeLoan "
                            + " ORDER BY l.date_created Desc")
                            .setParameter("typeLoan", "a")
                            .setMaxResults(max)
                            .setFirstResult(start)
                            .getResultList();
                }
            }
            if (type.contentEquals("b")) {
                if (start == 0) {
                    listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                            + " JOIN FETCH l.employee AS e "
                            + " LEFT JOIN FETCH l.loantype AS t "
                            + " RIGHT JOIN FETCH l.engagement AS n"
                            + " WHERE t.typeLoan = :typeLoan "
                            + " ORDER BY l.date_created Desc ")
                            .setParameter("typeLoan", "b")
                            .getResultList();
                } else {
                    listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                            + " JOIN FETCH l.employee AS e "
                            + " LEFT JOIN FETCH l.loantype AS t "
                            + " RIGHT JOIN FETCH l.engagement AS n "
                            + " WHERE t.typeLoan = :typeLoan "
                            + " ORDER BY l.date_created Desc")
                            .setParameter("typeLoan", "b")
                            .setMaxResults(max)
                            .setFirstResult(start)
                            .getResultList();
                }
            }
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

//    @Override
//    public List<Loan> listLoanB(int max, int start) {
//        try {
//            List<Loan> listAcquire = null;
//            if (start == 0) {
//                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
//                        + " JOIN FETCH l.employee AS e "
//                        + " LEFT JOIN FETCH l.loantype AS t "
//                        + " RIGHT JOIN FETCH l.engagement AS n"
//                        + " ORDER BY l.date_created Desc ")
//                        .getResultList();
//            } else {
//                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
//                        + " JOIN FETCH l.employee AS e "
//                        + " LEFT JOIN FETCH l.loantype AS t "
//                        + " RIGHT JOIN FETCH l.engagement AS n "
//                        + " ORDER BY l.date_created Desc")
//                        .setMaxResults(max)
//                        .setFirstResult(start)
//                        .getResultList();
//            }
//            return listAcquire;
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
    @Override
    public List<Loan> listLoanPaging(int max, int start) {
        try {
            List<Loan> listAcquire = null;
            if (start == 0) {
                listAcquire = entityManager.createQuery("SELECT l FROM Loan l ORDER BY l.date_created Desc ")
                        .getResultList();
            } else {
                listAcquire = entityManager.createQuery("SELECT l FROM Loan l ORDER BY l.date_created Desc ")
                        .setMaxResults(max)
                        .setFirstResult(start)
                        .getResultList();
            }
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
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
    public List<Loan> findByEmployee(String param, String type) {
        try {
            List<Loan> listAcquire = null;
            if (type.contentEquals("a")) {
                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " e.idEmployee = :idEmployee  AND "
                        + " t.typeLoan = :typeLoan "
                        + " ORDER BY l.date_created Desc ")
                        .setParameter("typeLoan", "a")
                        .setParameter("idEmployee", Long.parseLong(param))
                        .getResultList();
            }
            if (type.contentEquals("b")) {
                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " RIGHT JOIN FETCH l.engagement AS n "
                        + " WHERE "
                        + " e.idEmployee = :idEmployee AND "
                        + " t.typeLoan = :typeLoan "
                        + " ORDER BY l.date_created Desc ")
                        .setParameter("typeLoan", "b")
                        .setParameter("idEmployee", Long.parseLong(param))
                        .getResultList();
            }

            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

//    @Override
//    public Integer generateLoanId(String param1, String param2, String param3) {//typeLoan/idEmployee/tgl_input(yy)
//        try {
//            Query queryMax = entityManager.createQuery("SELECT COUNT(l) FROM Loan l WHERE "
//                    + " l.loantype.typeLoan = :typeLoan "
//                    + " AND l.employee.employeeId = :employeeId "
//                    + " AND l.tgl_input = :tgl_input")
//                    .setParameter("typeLoan", param1.toLowerCase())
//                    .setParameter("employeeId", param2.toLowerCase())
//                    .setParameter("tgl_input", param3);
//            return Integer.parseInt(queryMax.getSingleResult().toString());
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
    @Override
    public List<Loan> generateLoanId(String param1, String param2, String param3) {//typeLoan/idEmployee/tgl_input(yy)
        try {
//            Query queryMax = entityManager.createQuery("SELECT COUNT(l) FROM Loan l "
//                    + " WHERE "
//                    + " l.loantype.typeLoan = :typeLoan "
//                    + " AND l.employee.employeeId = :employeeId "
//                    + " AND l.tgl_input = :tgl_input")
//                    .setParameter("typeLoan", param1.toLowerCase())
//                    .setParameter("employeeId", param2.toLowerCase())
//                    .setParameter("tgl_input", param3);
//            return Integer.parseInt(queryMax.getSingleResult().toString());
            List<Loan> listAcquire = entityManager.createQuery("SELECT l FROM Loan l "
                    + " WHERE "
                    + " l.loantype.typeLoan = :typeLoan "
                    + " AND l.employee.employeeId = :employeeId "
                    + " AND l.tgl_input = :tgl_input")
                    .setParameter("typeLoan", "a")
                    .setParameter("employeeId", param2)
                    .setParameter("tgl_input", param3)
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> generateLoanIdB(String param1, String param2, String param3) {//typeLoan/idEmployee/tgl_input(yy)
        try {
//            Query queryMax = entityManager.createQuery("SELECT COUNT(l) FROM Loan l "
//                    + " WHERE "
//                    + " l.loantype.typeLoan = :typeLoan "
//                    + " AND l.employee.employeeId = :employeeId "
//                    + " AND l.tgl_input = :tgl_input")
//                    .setParameter("typeLoan", param1.toLowerCase())
//                    .setParameter("employeeId", param2.toLowerCase())
//                    .setParameter("tgl_input", param3);
//            return Integer.parseInt(queryMax.getSingleResult().toString());

            List<Loan> listAcquire = entityManager.createQuery("SELECT l FROM Loan l "
                    + " JOIN FETCH l.employee AS e "
                    + " LEFT JOIN FETCH l.loantype AS t "
                    + " RIGHT JOIN FETCH l.engagement AS n "
                    + " WHERE "
                    + " n.caseID = :caseID AND "
                    + " t.typeLoan = :typeLoan ")
                    .setParameter("caseID", param1)
                    .setParameter("typeLoan", "b")
                    .getResultList();

            return listAcquire;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Double loanEmp(String param1, String param2) {
//        Query queryMax = entityManager.createQuery("SELECT SUM(l.loanAmount) FROM Loan l WHERE "
//                + " l.employee.idEmployee = :idEmployee AND "
//                + " l.date_month = :date_month")
//                .setParameter("idEmployee", Long.parseLong(param1))
//                .setParameter("date_month", param2);
//        Query queryMax = entityManager.createQuery("SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l WHERE "
//                + " l.employee.idEmployee = :idEmployee AND "
//                + " l.date_month = :date_month")
//                .setParameter("idEmployee", Long.parseLong(param1))
//                .setParameter("date_month", param2);
        try {
            String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l "
                    + " WHERE "
                    + " l.employee.idEmployee = :idEmployee AND "
                    + " l.date_month = :date_month";
            Query query = entityManager.createQuery(sql);
            query.setParameter("idEmployee", Long.parseLong(param1))
                    .setParameter("date_month", param2);
            if (query != null) {
                log.info("isi" + query.getSingleResult().toString());
                return Double.parseDouble(query.getSingleResult().toString());
            } else {
                log.info("isi" + query.getSingleResult().toString());
                return 0d;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> listActive(String param1, String param2, String type) {
        try {
            List<Loan> listAcquire = null;
            if (type.contentEquals("a")) {

                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l"
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " ( l.aprovedByAdmin = :aprovedByAdmin OR "
                        + " l.aprovedByFinance = :aprovedByFinance ) AND"
                        + " t.typeLoan = :typeLoan "
                        + " ORDER BY l.date_created Desc ")
                        .setParameter("aprovedByAdmin", param1)
                        .setParameter("aprovedByFinance", param1)
                        .setParameter("typeLoan", "a")
                        .getResultList();
//                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l"
//                        + " JOIN FETCH l.employee AS e "
//                        + " LEFT JOIN FETCH l.loantype AS t "
//                        + " WHERE "
//                        + " l.aprovedByAdmin = :aprovedByAdmin OR "
//                        + " l.aprovedByFinance = :aprovedByFinance OR"
//                        + " l.status = :status AND "
//                        + " t.typeLoan = :typeLoan "
//                        + " ORDER BY l.date_created Desc ")
//                        .setParameter("aprovedByAdmin", param1)
//                        .setParameter("aprovedByFinance", param1)
//                        .setParameter("status", param2)
//                        .setParameter("typeLoan", "a")
//                        .getResultList();
            }
            if (type.contentEquals("b")) {
                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l"
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " RIGHT JOIN FETCH l.engagement AS n "
                        + " WHERE "
                        + " ( l.aprovedByAdmin = :aprovedByAdmin OR "
                        + " l.aprovedByFinance = :aprovedByFinance ) AND"
                        + " t.typeLoan = :typeLoan "
                        + " ORDER BY l.date_created Desc ")
                        .setParameter("aprovedByAdmin", param1)
                        .setParameter("aprovedByFinance", param1)
                        .setParameter("typeLoan", "b")
                        .getResultList();
//                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l"
//                        + " JOIN FETCH l.employee AS e "
//                        + " LEFT JOIN FETCH l.loantype AS t "
//                        + " RIGHT JOIN FETCH l.engagement AS n "
//                        + " WHERE "
//                        + " l.aprovedByAdmin = :aprovedByAdmin OR "
//                        + " l.aprovedByFinance = :aprovedByFinance OR"
//                        + " l.status = :status AND "
//                        + " t.typeLoan = :typeLoan "
//                        + " ORDER BY l.date_created Desc ")
//                        .setParameter("aprovedByAdmin", param1)
//                        .setParameter("aprovedByFinance", param1)
//                        .setParameter("status", param2)
//                        .setParameter("typeLoan", "b")
//                        .getResultList();
            }
            if (listAcquire.isEmpty()) {
                return listAcquire;
            } else {
                return listAcquire;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson("ERROR_loanRepo", ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
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
