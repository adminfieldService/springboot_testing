/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.repo.interfaces.EmployeeRepoIface;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author newbiecihuy
 */
@Repository("employeeRepo")
public class EmployeeRepo implements EmployeeRepoIface {

    @PersistenceContext(unitName = Constants.JPA_UNIT_NAME_LF)
    private EntityManager entityManager;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Employee create(Employee entity) {
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
    public Employee update(Employee entity) {
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
    public Employee approved(Employee entity) {
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
    public Employee delete(Employee entity) {
        try {
            entity.setIsActive(false);
            entity.setIsDelete(true);
            entity.setStatus("d");
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
    public void remove(Employee entity) {
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
    public Employee findById(Long paramLong) {
        try {
            return (Employee) entityManager.find(Employee.class, paramLong);
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

    public Employee findByEmpId(Long paramLong) {
        try {
            return (Employee) entityManager.find(Employee.class, paramLong);
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
    public Employee findByEmployee(String paramString) {
        try {
            Employee listAcquire = (Employee) entityManager.createQuery("SELECT e FROM Employee e WHERE "
                    + " e.nik = :nik OR "
                    + " LOWER(e.name) = :name OR "
                    + " LOWER(e.userName) = :userName OR "
                    + " e.npwp = :npwp OR "
                    + " LOWER(e.email) = :email OR "
                    + " e.employeeId = :employeeId OR"
                    + " e.idEmployee = :idEmployee OR"
                    + " e.mobilePhone = :mobilePhone ")
                    .setParameter("nik", paramString)
                    .setParameter("name", paramString.toLowerCase())
                    .setParameter("userName", paramString.toLowerCase())
                    .setParameter("npwp", paramString)
                    .setParameter("email", paramString.toLowerCase())
                    .setParameter("employeeId", paramString.toLowerCase())
                    .setParameter("idEmployee", Long.parseLong(paramString))
                    .setParameter("mobilePhone", paramString)
                    .getSingleResult();
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
    public Employee findByEmployeeId(String paramString, Long Id) {
        try {
            Employee listAcquire = (Employee) entityManager.createQuery("SELECT e FROM Employee e WHERE "
                    + " e.employeeId = :employeeId AND "
                    + " e.idEmployee = :idEmployee ")
                    .setParameter("employeeId", paramString.toLowerCase())
                    .setParameter("idEmployee", Id)
                    .getSingleResult();
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
    public List<Employee> listEmployee() {
        try {
            List<Employee> listAcquire = entityManager.createQuery("SELECT e FROM Employee e ")
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
    public List<Employee> listEmployeePaging(String paramString, int max, int start) {
        try {
            List<Employee> listAcquire = null;
            if (paramString == null) {
                listAcquire = entityManager.createQuery("SELECT e FROM Employee e ")
                        .setMaxResults(max)
                        .setFirstResult(start)
                        .getResultList();
            } else {
                listAcquire = entityManager.createQuery("SELECT e FROM Employee e WHERE "
                        + " e.nik = :nik OR "
                        + " LOWER(e.name) = :name OR "
                        + " e.npwp = :npwp OR "
                        + " LOWER(e.email) = :email OR "
                        + " e.employeeId = :employeeId OR"
                        + " e.idEmployee = :idEmployee OR"
                        + " e.mobilePhone = :mobilePhone OR"
                        + " LOWER(e.status) = :status ")
                        .setParameter("nik", paramString)
                        .setParameter("name", paramString.toLowerCase())
                        .setParameter("npwp", paramString)
                        .setParameter("email", paramString.toLowerCase())
                        .setParameter("employeeId", paramString.toLowerCase())
                        .setParameter("idEmployee", Long.parseLong(paramString))
                        .setParameter("status", paramString.toLowerCase())
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
    public List<Employee> listActive(Boolean isActive) {
        try {
            List<Employee> listAcquire = entityManager.createQuery("SELECT e FROM Employee e WHERE "
                    + " e.isActive = :isActive ")
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
    public List< Employee> findByApproved(Long paramString) {
        try {
            List<Employee> listAcquire = entityManager.createQuery("SELECT e FROM Employee e WHERE "
                    + " e.parentId.idEmployee = :idEmployee ")
                    .setParameter("idEmployee", paramString)
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
        Query queryMax = entityManager.createQuery("SELECT COUNT(e) FROM Employee e");
        return Integer.parseInt(queryMax.getSingleResult().toString());
    }

    @Override
    public EntityManager getEntityManager() {
        // TODO Auto-generated method stub
        return entityManager;
    }

    @Override
    public Employee chekUserName(String paramString) {
        try {
            Employee listAcquire = (Employee) entityManager.createQuery("SELECT e FROM Employee e WHERE "
                    + " e.userName = :userName AND "
                    + " e.isActive = :isActive AND "
                    + " e.isLogin  = :isLogin ")
                    .setParameter("userName", paramString.toLowerCase())
                    .setParameter("isActive", true)
                    .setParameter("isLogin", false)
                    .getSingleResult();
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
    public Boolean existsByUsername(String username) {
        try {
            Employee listAcquire = (Employee) entityManager.createQuery("SELECT e FROM Employee e WHERE "
                    + " LOWER(e.userName) = :userName")
                    .setParameter("userName", username.toLowerCase())
                    .getSingleResult();
            if (listAcquire != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        try {
            Employee listAcquire = (Employee) entityManager.createQuery("SELECT e FROM Employee e WHERE "
                    + " LOWER(e.email) = :email")
                    .setParameter("email", email.toLowerCase())
                    .getSingleResult();
            if (listAcquire != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        } finally {
            if ((entityManager != null) && (entityManager.isOpen())) {
                entityManager.close();
            }
        }
    }

//    https://stackoverflow.com/questions/40082175/dao-with-null-object-pattern
    @Override
    public Optional<Employee> findByUsername(String username) {
        try {
            Employee listAcquire = (Employee) entityManager.createQuery("SELECT e FROM Employee e WHERE "
                    + " e.userName = :userName AND "
                    + " e.isActive = :isActive AND "
                    + " e.isLogin  = :isLogin ")
                    .setParameter("userName", username.toLowerCase())
                    .setParameter("isActive", true)
                    .setParameter("isLogin", false)
                    .getSingleResult();
            return Optional.ofNullable(listAcquire);
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

}
