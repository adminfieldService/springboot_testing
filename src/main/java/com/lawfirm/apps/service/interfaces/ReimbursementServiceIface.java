/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.Reimbursement;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface ReimbursementServiceIface {

    Reimbursement create(Reimbursement entity);

    Reimbursement update(Reimbursement entity);

    Reimbursement delete(Reimbursement entity);

    void remove(Reimbursement entity);

    Reimbursement findById(Long paramLong);

    Reimbursement findByName(String namaVisit);

    List<Reimbursement> listClient();

    List<Reimbursement> listActive();

    List<Reimbursement> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
