/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.CaseDetails;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface CaseDetailsRepoIface {

    CaseDetails create(CaseDetails entity);

    CaseDetails update(CaseDetails entity);

    CaseDetails approved(CaseDetails entity);

    CaseDetails delete(CaseDetails entity);

    void remove(CaseDetails entity);

    CaseDetails findById(Long paramLong);

//    Integer generateCaseId(String param1);
    List<CaseDetails> generateCaseId(String param1);

    CaseDetails findByCaseId(String namaVisit, String paramY);

    Optional<CaseDetails> checkCaseId(String caseID, String paramY);

    CaseDetails findCaseId(String caseID);

    List<CaseDetails> findByEmployee(Long paramLong);

    List<CaseDetails> getCaseId();

    List<CaseDetails> findByAdmin(Long paramLong);

    List<CaseDetails> listCaseDetails(String role, Long idEmployee);

    List<CaseDetails> listCaseDetailsPaging(String role, Long idEmployee, int max, int start);

    List<CaseDetails> listActive(Boolean isActive);

    List<CaseDetails> byName(Boolean isActive);

    List<CaseDetails> findByEngagementId(Long paramLong);

    Integer count();

    EntityManager getEntityManager();

    List<CaseDetails> findByDate(Date cutoff);
}
