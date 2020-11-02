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
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
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
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

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
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
            String sql = "SELECT l FROM Loan l "
                    + " JOIN FETCH l.employee AS e "
                    + " LEFT JOIN FETCH l.loantype AS t "
                    + " WHERE "
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
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Loan chekLoan(String typeLoan, Long idEmployee, Date tanggalPengajuan) {
        try {
            Loan entity = null;
            if (typeLoan.contains("a")) {
                entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l "
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " e.idEmployee = :idEmployee AND "
                        + " l.date_created = :date_created AND "
                        + " t.typeLoan = :typeLoan AND"
                        + " l.status <> :status ")
                        .setParameter("idEmployee", idEmployee)
                        .setParameter("date_created", tanggalPengajuan)
                        .setParameter("typeLoan", "a")
                        .setParameter("status", "r")
                        .getSingleResult();
            }
            if (typeLoan.contains("b")) {
                entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l "
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " e.idEmployee = :idEmployee AND "
                        + " l.date_created = :date_created AND "
                        + " t.typeLoan = :typeLoan AND"
                        + " l.status <> :status ")
                        .setParameter("idEmployee", idEmployee)
                        .setParameter("date_created", tanggalPengajuan)
                        .setParameter("typeLoan", "b")
                        .setParameter("status", "r")
                        .getSingleResult();
            }

            return entity;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> listLoan(int max, int start, String type, Long idEmployee, String role) {
        try {
            List<Loan> listAcquire = null;
            if (type.contentEquals("a")) {
                if (role.contains("finance") || role.contains("admin")) {
                    if (start == 0) {
                        listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                                + " JOIN FETCH l.employee AS e "
                                + " LEFT JOIN FETCH l.loantype AS t "
                                + " WHERE t.typeLoan = :typeLoan AND "
                                + " l.status <> :status"
                                + " ORDER BY l.date_created DESC ")//ASC
                                .setParameter("typeLoan", "a")
                                .setParameter("status", "r")
                                .getResultList();
                    } else {
                        listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                                + " JOIN FETCH l.employee AS e "
                                + " LEFT JOIN FETCH l.loantype AS t"
                                + " WHERE t.typeLoan = :typeLoan AND "
                                + " l.status <> :status"
                                + " ORDER BY l.date_created DESC ")//ASC
                                .setParameter("typeLoan", "a")
                                .setParameter("status", "r")
                                .setMaxResults(max)
                                .setFirstResult(start)
                                .getResultList();
                    }
                }

                if (role.contains("lawyer") || role.contains("support") || role.contains("dmp")) {
                    if (start == 0) {
                        listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                                + " JOIN FETCH l.employee AS e "
                                + " LEFT JOIN FETCH l.loantype AS t "
                                + " WHERE t.typeLoan = :typeLoan AND "
                                + " e.idEmployee = :idEmployee"
                                + " ORDER BY l.date_created DESC ")//ASC
                                .setParameter("typeLoan", "a")
                                .setParameter("idEmployee", idEmployee)
                                .getResultList();
                    } else {
                        listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                                + " JOIN FETCH l.employee AS e "
                                + " LEFT JOIN FETCH l.loantype AS t"
                                + " WHERE t.typeLoan = :typeLoan AND "
                                + " e.idEmployee = :idEmployee"
                                + " ORDER BY l.date_created DESC ")//ASC
                                .setParameter("typeLoan", "a")
                                .setParameter("idEmployee", idEmployee)
                                .setMaxResults(max)
                                .setFirstResult(start)
                                .getResultList();
                    }
                }
            }
            if (type.contentEquals("b")) {
                if (role.contains("finance") || role.contains("admin")) {
                    if (start == 0) {
                        listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                                + " JOIN FETCH l.employee AS e "
                                + " LEFT JOIN FETCH l.loantype AS t "
                                + " JOIN FETCH l.engagement AS c "
                                + " WHERE t.typeLoan = :typeLoan AND "
                                + " l.status <> :status"
                                + " ORDER BY l.date_created DESC ")//ASC
                                .setParameter("typeLoan", "b")
                                .setParameter("status", "r")
                                .getResultList();
                    } else {
                        listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                                + " JOIN FETCH l.employee AS e "
                                + " LEFT JOIN FETCH l.loantype AS t"
                                + " JOIN FETCH l.engagement AS c "
                                + " WHERE t.typeLoan = :typeLoan AND "
                                + " l.status <> :status"
                                + " ORDER BY l.date_created DESC ")//ASC
                                .setParameter("typeLoan", "b")
                                .setParameter("status", "r")
                                .setMaxResults(max)
                                .setFirstResult(start)
                                .getResultList();
                    }
                }
                if (role.contentEquals("dmp")) {
                    if (start == 0) {
                        listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                                + " JOIN FETCH l.employee AS e "
                                + " LEFT JOIN FETCH l.loantype AS t "
                                + " JOIN FETCH l.engagement AS c "
                                + " WHERE t.typeLoan = :typeLoan AND "
                                + " e.idEmployee = :idEmployee"
                                + " ORDER BY l.date_created DESC ")//ASC
                                .setParameter("typeLoan", "b")
                                .setParameter("idEmployee", idEmployee)
                                .getResultList();
                    } else {
                        listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                                + " JOIN FETCH l.employee AS e "
                                + " LEFT JOIN FETCH l.loantype AS t"
                                + " JOIN FETCH l.engagement AS c "
                                + " WHERE t.typeLoan = :typeLoan AND "
                                + " e.idEmployee = :idEmployee"
                                + " ORDER BY l.date_created DESC ")//ASC
                                .setParameter("typeLoan", "b")
                                .setParameter("idEmployee", idEmployee)
                                .setMaxResults(max)
                                .setFirstResult(start)
                                .getResultList();
                    }
                }
            }
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
                        + " ORDER BY l.date_created DESC ")//ASC
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
                        + " ORDER BY l.date_created DESC ")//ASC
                        .setParameter("typeLoan", "b")
                        .setParameter("idEmployee", Long.parseLong(param))
                        .getResultList();
            }

            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
                    + " AND l.status <> :status "
                    + " AND l.tgl_input = :tgl_input")
                    .setParameter("typeLoan", "a")
                    .setParameter("employeeId", param2)
                    .setParameter("status", "r")
                    .setParameter("tgl_input", param3)
                    .getResultList();
            return listAcquire;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
                    + " l.status <> :status AND "
                    + " t.typeLoan = :typeLoan ")
                    .setParameter("caseID", param1)
                    .setParameter("status", "r")
                    .setParameter("typeLoan", "b")
                    .getResultList();

            return listAcquire;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
                    + " l.status <> :status AND "
                    + " l.date_month = :date_month";
            Query query = entityManager.createQuery(sql);
            query.setParameter("idEmployee", Long.parseLong(param1))
                    .setParameter("status", "r")
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
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Double loanB(Long engagementId) {
        try {
            String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l "
                    + " WHERE "
                    + " l.engagement.engagementId = :engagementId AND "
                    + " l.status <> :status AND "
                    + " l.status <> :status2";
            Query query = entityManager.createQuery(sql);
            query.setParameter("engagementId", engagementId)
                    .setParameter("status", "r")
                    .setParameter("status2", "closed");
            if (query != null) {
                log.info("isi" + query.getSingleResult().toString());
                return Double.parseDouble(query.getSingleResult().toString());
            } else {
                log.info("isi" + query.getSingleResult().toString());
                return 0d;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> listBy(String param1, String param2, String type) {
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
                        + " ORDER BY l.date_created DESC ")//ASC
                        .setParameter("aprovedByAdmin", param1)
                        .setParameter("aprovedByFinance", param1)
                        .setParameter("typeLoan", "a")
                        .getResultList();
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
                        + " ORDER BY l.date_created DESC ")//ASC
                        .setParameter("aprovedByAdmin", param1)
                        .setParameter("aprovedByFinance", param1)
                        .setParameter("typeLoan", "b")
                        .getResultList();
            }
            if (listAcquire.isEmpty()) {
                return listAcquire;
            } else {
                return listAcquire;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> listDisburse(String type) {
        try {
            List<Loan> listAcquire = null;
            if (type.contentEquals("0")) {

                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l"
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " t.typeLoan = :typeLoan AND "
                        + " l.status = :status "
                        + " ORDER BY l.date_created DESC ")
                        .setParameter("status", "s")
                        .getResultList();
            }
            if (type.contentEquals("a")) {

                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l"
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " t.typeLoan = :typeLoan AND "
                        + " l.status = :status "
                        + " ORDER BY l.date_created DESC ")//ASC
                        .setParameter("typeLoan", "a")
                        .setParameter("status", "d")
                        .getResultList();
            }
            if (type.contentEquals("b")) {
                listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l"
                        + " JOIN FETCH l.employee AS e "
                        + " LEFT JOIN FETCH l.loantype AS t "
                        + " WHERE "
                        + " t.typeLoan = :typeLoan AND "
                        + " l.status = :status "
                        + " ORDER BY l.date_created DESC ")//ASC
                        .setParameter("typeLoan", "b")
                        .setParameter("status", "d")
                        .getResultList();
            }
            if (listAcquire.isEmpty()) {
                return listAcquire;
            } else {
                return listAcquire;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> getLoanB(String caseID) {
        try {
            List<Loan> listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                    + " JOIN FETCH l.employee AS e "
                    + " LEFT JOIN FETCH l.loantype AS t "
                    + " RIGHT JOIN FETCH l.engagement AS n "
                    + " WHERE "
                    + " t.typeLoan = :typeLoan AND "
                    + " n.caseID = :caseID AND "
                    + " ( l.status <> :status OR "
                    + " l.status <> :status2 ) "
                    + " ORDER BY l.date_created DESC ")//ASC
                    .setParameter("caseID", caseID)
                    .setParameter("typeLoan", "b")
                    .setParameter("status", "s")
                    .setParameter("status2", "r")
                    .getResultList();
            return listAcquire;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Loan> getListLoanB() {
        try {
            List<Loan> listAcquire = entityManager.createQuery("SELECT DISTINCT  l FROM Loan l "
                    + " JOIN FETCH l.employee AS e "
                    + " LEFT JOIN FETCH l.loantype AS t "
                    + " RIGHT JOIN FETCH l.engagement AS n "
                    + " WHERE "
                    + " t.typeLoan = :typeLoan AND "
                    + " n.caseID = :caseID AND "
                    + " ( l.status <> :status OR "
                    + " l.status <> :status2 ) "
                    + " ORDER BY l.date_created DESC ")
                    .setParameter("typeLoan", "b")
                    .setParameter("status", "s")
                    .setParameter("status2", "r")
                    .getResultList();
            return listAcquire;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Double sumLoanB(Long paramLong) {
        try {
            String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                    + " WHERE "
                    + " l.loantype.typeLoan = :typeLoan AND "
                    + " l.engagement.engagementId = :engagementId AND "
                    + " (l.status <> :status AND "
                    + " l.status <> :status2 ) ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("typeLoan", "b");
            query.setParameter("engagementId", paramLong);
            query.setParameter("status", "s");
            query.setParameter("status2", "r");
//            if (query != null) {
            log.info("isi" + String.format("%.0f", query.getSingleResult()));
            return Double.parseDouble(query.getSingleResult().toString());
//            } else {
//                log.info("isi" + query.getSingleResult().toString());
//                return 0d;
//            }
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Double sumLoanByCaseId(String param) {
        try {
            String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l "
                    + " JOIN FETCH l.engagement AS e "//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                    + " WHERE "
                    + " l.loantype.typeLoan = :typeLoan AND "
                    + " e.caseID = :caseID AND "
                    + " (l.status <> :status AND "
                    + "  l.status <> :status2 ) ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("typeLoan", "b");
            query.setParameter("caseID", param);
            query.setParameter("status", "s");
            query.setParameter("status2", "r");
//            if (query != null) {
            log.info("isi sumLoanByCaseId : " + String.format("%.0f", query.getSingleResult()));
            return Double.parseDouble(query.getSingleResult().toString());
//            } else {
//                log.info("isi" + query.getSingleResult().toString());
//                return 0d;
//            }
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Double sumLoanA(Long userId, String taxtYear, Date tgl_cut_off) {
        try {
            String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                    + " WHERE "
                    + " l.loantype.typeLoan = :typeLoan AND "
                    + " l.date_created <= :tgl_cut_off AND "
                    + " l.employee.idEmployee = :idEmployee AND "
                    + " l.tgl_input = :taxtYear AND "
                    + " l.isDelete = :isDelete AND "
                    + " (l.status <> :status AND "
                    + " l.status <> :status2 ) ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("typeLoan", "a");
            query.setParameter("tgl_cut_off", tgl_cut_off);
            query.setParameter("idEmployee", userId);
            query.setParameter("taxtYear", taxtYear);
            query.setParameter("isDelete", false);
            query.setParameter("status", "s");
            query.setParameter("status2", "r");
//            if (query != null) {
            log.info("isi" + String.format("%.0f", query.getSingleResult()));
            return Double.parseDouble(query.getSingleResult().toString());
//            } else {
//                log.info("isi" + query.getSingleResult().toString());
//                return 0d;
//            }
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Double sumLoanA2(Long userId, String taxtYear, Date tgl_cut_off, Date old_tgl_cut_off) {
        try {
            String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"//  String sql = "SELECT COALESCE(SUM(l.loanAmount),0) FROM Loan l"
                    + " WHERE "
                    + " l.loantype.typeLoan = :typeLoan AND "
                    + " l.date_created > :old_tgl_cut_off AND "
                    + " l.date_created <= :tgl_cut_off AND "
                    + " l.employee.idEmployee = :idEmployee AND "
                    + " l.tgl_input = :taxtYear AND "
                    + " l.isDelete = :isDelete AND "
                    + " (l.status <> :status AND "
                    + " l.status <> :status2 ) ";
            Query query = entityManager.createQuery(sql);
            query.setParameter("typeLoan", "a");
            query.setParameter("old_tgl_cut_off", old_tgl_cut_off);
            query.setParameter("tgl_cut_off", tgl_cut_off);
            query.setParameter("idEmployee", userId);
            query.setParameter("taxtYear", taxtYear);
            query.setParameter("isDelete", false);
            query.setParameter("status", "s");
            query.setParameter("status2", "r");
//            if (query != null) {
            log.info("isi" + String.format("%.0f", query.getSingleResult()));
            return Double.parseDouble(query.getSingleResult().toString());
//            } else {
//                log.info("isi" + query.getSingleResult().toString());
//                return 0d;
//            }
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "ERROR_loanRepo");
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
