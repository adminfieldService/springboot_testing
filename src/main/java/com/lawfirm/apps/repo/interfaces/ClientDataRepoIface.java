/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.ClientData;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface ClientDataRepoIface {

    ClientData create(ClientData entity);

    ClientData update(ClientData entity);

    ClientData delete(ClientData entity);

    void remove(ClientData entity);

    ClientData findById(Long paramLong);

    ClientData findByName(String namaVisit);

    ClientData findBydataClient(String paramString1, String paramString2, String paramString3);

    List<ClientData> listClient();

    List<ClientData> listActive(String isActive);

    List<ClientData> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
