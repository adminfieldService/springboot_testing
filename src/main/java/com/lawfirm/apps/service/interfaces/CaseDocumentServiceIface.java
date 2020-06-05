/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.CaseDocument;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface CaseDocumentServiceIface {

    CaseDocument create(CaseDocument entity);

    CaseDocument update(CaseDocument entity);

    CaseDocument delete(CaseDocument entity);

    void remove(CaseDocument entity);

    CaseDocument findById(Long paramLong);

    List<CaseDocument> findByCaseId(String paramString);

    List<CaseDocument> listCaseDocument();

    List<CaseDocument> listActive(String isActive);

    List<CaseDocument> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
