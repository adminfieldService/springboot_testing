/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.ClientData;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface ClientDataServiceIface {

    ClientData create(ClientData entity);

    ClientData update(ClientData entity);

    ClientData delete(ClientData entity);

    void remove(ClientData entity);

    ClientData findById(Long paramLong);

    ClientData findByName(String paramString);

    ClientData findBydataClient(String paramString1, String paramString2, String paramString3);

    List<ClientData> listClient();

    List<ClientData> listActive(String isActive);

    List<ClientData> byName(Boolean isActive);

    Integer count();

    Integer generateCleintId(String npwp);

    EntityManager getEntityManager();

    ClientData checkCI(String paramString);
}
