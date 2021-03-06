/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.CaseDetails;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface CaseDetailsServiceIface {

    CaseDetails create(CaseDetails entity);

    CaseDetails update(CaseDetails entity);

    CaseDetails approved(CaseDetails entity);

    CaseDetails delete(CaseDetails entity);

    void remove(CaseDetails entity);

    CaseDetails findById(Long paramLong);

    CaseDetails findByCaseId(String namaVisit);

    List<CaseDetails> listCaseDetails();

    List<CaseDetails> listActive(Boolean isActive);

    List<CaseDetails> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
