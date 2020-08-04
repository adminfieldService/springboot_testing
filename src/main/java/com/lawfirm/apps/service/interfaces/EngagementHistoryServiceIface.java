/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.EngagementHistory;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface EngagementHistoryServiceIface {

    EngagementHistory create(EngagementHistory entity);

    EngagementHistory update(EngagementHistory entity);

    EngagementHistory delete(EngagementHistory entity);

    void remove(EngagementHistory entity);

    List<EngagementHistory> findByCaseId(Long paramLong);

    List<EngagementHistory> findByEngagementId(String param);

    List<EngagementHistory> findByUserId(Long paramLong, String param);
}
