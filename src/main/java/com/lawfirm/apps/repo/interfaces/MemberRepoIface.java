/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.Member;
import java.util.List;
import javax.persistence.EntityManager;
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

    Member findByName(String namaVisit);

    List<Member> listMember();

    List<Member> listMemberPaging(int max, int start);

    List<Member> listActive(Boolean isActive);

    List<Member> findByEmployee(int max, int start, String param);

    Integer count();

    EntityManager getEntityManager();
}
