/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.TeamMember;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface TeamMemberRepoIface {

    TeamMember create(TeamMember entity);

    TeamMember update(TeamMember entity);

    TeamMember delete(TeamMember entity);

    void remove(TeamMember entity);

    TeamMember findById(Long paramLong);

    TeamMember findByEngId(Long engID);

    TeamMember findByName(String namaVisit);

    List<TeamMember> findByDmp(Long dmpId);

    List<TeamMember> listTeamMember();

    List<TeamMember> generateTeamCaseId(String param1);

    TeamMember findByTeamCaseId(String caseID, String paramY);

    List<TeamMember> listActive(Boolean param);

    List<TeamMember> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();

    List<TeamMember> listTeamMemberByEngagement(Long param);
}
