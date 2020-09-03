/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.CaseDetails;
import java.util.List;
import java.util.Optional;
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

    CaseDetails findByCaseId(String namaVisit, String paramY);

    Optional<CaseDetails> checkCaseId(String caseID, String paramY);

    CaseDetails findCaseId(String caseID);

    List<CaseDetails> findByEmployee(Long paramLong);

    List<CaseDetails> getCaseId();

    List<CaseDetails> listCaseDetails();

    List<CaseDetails> listActive(Boolean isActive);

    List<CaseDetails> byName(Boolean isActive);

    Integer count();

//    Integer generateCaseId(String param1);
    List<CaseDetails> generateCaseId(String param1);

    EntityManager getEntityManager();

    List<CaseDetails> findByEngagementId(Long paramLong);

    List<CaseDetails> findByAdmin(Long paramLong);
}
