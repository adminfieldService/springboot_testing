/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.Account;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface AccountServiceIface {

    Account create(Account entity);

    Account update(Account entity);

    Account approved(Account entity);

    Account delete(Account entity);

    void remove(Account entity);

    Account findById(Long paramLong);

    Account findAccount(String param);

    List<Account> findByEmployee(String param);

    List<Account> listAccount();

    List<Account> listActive(Boolean isActive);

    List<Account> approvedBy(String paramString);

    Integer count();

    EntityManager getEntityManager();
}
