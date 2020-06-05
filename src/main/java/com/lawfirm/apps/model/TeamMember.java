/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_team_member")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "TeamMember.findAll", query = "SELECT t FROM TeamMember t")})
public class TeamMember implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "team_member_seq", sequenceName = "team_member_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_member_seq")
    @Column(name = "team_member_id")
    private Long teamMemberId;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "description")
    private String description;
//    private Employee employeeId;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teamMember")
    private Collection<Engagement> engagementCollection;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teamMember")
    private Collection<Member> membertCollection;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teamMember")
    private Collection<Financial> financialCollection;

    public TeamMember() {
    }

    public Long getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(Long teamMemberId) {
        this.teamMemberId = teamMemberId;
    }

    public Collection<Engagement> getEngagementCollection() {
        return engagementCollection;
    }

    public void setEngagementCollection(Collection<Engagement> engagementCollection) {
        this.engagementCollection = engagementCollection;
    }

    public Collection<Member> getMembertCollection() {
        return membertCollection;
    }

    public Collection<Financial> getFinancialCollection() {
        return financialCollection;
    }

    public void setFinancialCollection(Collection<Financial> financialCollection) {
        this.financialCollection = financialCollection;
    }

    public void setMembertCollection(Collection<Member> membertCollection) {
        this.membertCollection = membertCollection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
                .replaceAll("<script>(.*?)</script>", "")
                .replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?/>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "")
                .replaceAll("vbscript", "")
                .replaceAll("encode", "")
                .replaceAll("decode", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", "")
                .replaceAll("</script>", "")
                .replaceAll("<script(.*?)>", "")
                .replaceAll("eval\\((.*?)\\)", "")
                .replaceAll("expression\\((.*?)\\)", "");
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.TeamMember[teamMemberId=" + this.teamMemberId + " ]";
    }

}
