/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.ReimbursementHistory;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface ReimbursementHistoryServiceIface {
    ReimbursementHistory create(ReimbursementHistory entity);

    ReimbursementHistory update(ReimbursementHistory entity);

    ReimbursementHistory delete(ReimbursementHistory entity);

    void remove(ReimbursementHistory entity);

    List<ReimbursementHistory> findByReimburseId(Long paramLong);

    List<ReimbursementHistory> findByUserId(Long paramLong, String param);
}
