/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.Member;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface MemberRepoIface {

    Member create(Member entity);

    Member update(Member entity);

    Member delete(Member entity);

    void remove(Member entity);

    Member findById(Long paramLong);

    List<Member> findByIdTeam(Long paramLong);

    List<Member> findByCaseId(String param);

    List<Member> findByEmpId(String param);//@Repository

//    List<Member> findByTeam(Long param);
}
