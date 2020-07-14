/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.repo.interfaces.LoanRepoIface;
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
@Repository("loanRepo")
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
            entity.setIsActive("2");
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
    public void remove(Loan entity) {
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
    public Loan findById(Long paramLong) {
        try {
            return (Loan) entityManager.find(Loan.class, paramLong);
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
    public Loan findByLoanId(String param) {
        try {
            Loan entity = (Loan) entityManager.createQuery("SELECT l FROM Loan l WHERE "
                    + " l.loanId = :loanId")
                    .setParameter("loanId", param)
                    .getSingleResult();
            return entity;
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
    public List<Loan> listLoan(int max, int start) {
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
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

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
    public List<Loan> findByEmployee(int max, int start, String param) {
        try {
            List<Loan> listAcquire = null;
            if (start == 0) {
                listAcquire = entityManager.createQuery("SELECT l FROM Loan l WHERE "
                        + " LOWER(l.employee.name) = :name OR "
                        + " l.employee.nik  = :nik OR "
                        + " LOWER(l.employee.email) = :email OR "
                        + " l.employee.npwp = :npwp OR "
                        + " l.employee.idEmployee = :idEmployee OR "
                        + " l.employee.employeeId = :employeeId OR "
                        + " LOWER(l.employee.userName) = :userName ")
                        .setParameter("name", param.toLowerCase())
                        .setParameter("nik", param.toLowerCase())
                        .setParameter("email", param.toLowerCase())
                        .setParameter("npwp", param.toLowerCase())
                        .setParameter("idEmployee", Long.parseLong(param))
                        .setParameter("employeeId", param.toUpperCase())
                        .setParameter("userName", param.toLowerCase())
                        .getResultList();
            } else {
                listAcquire = entityManager.createQuery("SELECT l FROM Loan l WHERE "
                        + " LOWER(l.employee.name) = :name OR "
                        + " l.employee.nik  = :nik OR "
                        + " LOWER(l.employee.email) = :email OR "
                        + " l.employee.npwp = :npwp OR "
                        + " l.employee.idEmployee = :idEmployee OR "
                        + " LOWER(l.employee.employeeId) = :employeeId OR "
                        + " LOWER(l.employee.userName) = :userName ")
                        .setParameter("name", param.toLowerCase())
                        .setParameter("nik", param.toLowerCase())
                        .setParameter("email", param.toLowerCase())
                        .setParameter("npwp", param.toLowerCase())
                        .setParameter("idEmployee", Long.parseLong(param))
                        .setParameter("employeeId", param.toUpperCase())
                        .setParameter("userName", param.toLowerCase())
                        .setMaxResults(max)
                        .setFirstResult(start)
                        .getResultList();
            }
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
    public Integer generateLoanId(String param1, String param2, String param3) {//roleemploye/year/count
        Query queryMax = entityManager.createQuery("SELECT COUNT(l) FROM Loan l WHERE "
                + " l.loantype.typeLoan = :typeLoan "
                + " AND l.employee.idEmployee = :idEmployee "
                + " AND l.date_created = :date_created")
                .setParameter("typeLoan", param1.toLowerCase())
                .setParameter("idEmployee", param2.toLowerCase())
                .setParameter("date_created", param3.toLowerCase());

        return Integer.parseInt(queryMax.getSingleResult().toString());
    }

    @Override
    public List<Loan> listActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
