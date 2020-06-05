/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.DocumentReimburse;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface DocumentReimburseServiceIface {

    DocumentReimburse create(DocumentReimburse entity);

    DocumentReimburse update(DocumentReimburse entity);

    DocumentReimburse delete(DocumentReimburse entity);

    void remove(DocumentReimburse entity);

    DocumentReimburse findById(Long paramLong);

    DocumentReimburse findByName(String paramString, Long paramLong);

    List<DocumentReimburse> findByEmployee(String paramString);

    List<DocumentReimburse> listActive(String param);

    List<DocumentReimburse> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
