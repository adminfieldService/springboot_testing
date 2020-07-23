/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_engagement_history")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "EngagementHistory.findAll", query = "SELECT h FROM EngagementHistory h")})
public class EngagementHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @SequenceGenerator(name = "loan_history_seq", sequenceName = "loan_history_seq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_history_seq")
    @Column(name = "id")
    private String Id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "engagement_id", referencedColumnName = "engagement_id")
    protected Engagement engagement;

    @Column(name = "user_id")
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Column(name = "tgl_input", nullable = false)
    private Date tgl_input;

    @Column(name = "response", nullable = true)
    private String response;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @PrePersist
    public void onCreate() {
        tgl_input = new Date();
        this.setId(UUID.randomUUID().toString());
    }

    public EngagementHistory() {

    }

    public EngagementHistory(String Id, Engagement engagement, Long userId, Date tgl_input, String response, String isActive) {
        this.Id = Id;
        this.engagement = engagement;
        this.userId = userId;
        this.tgl_input = tgl_input;
        this.response = response;
        this.isActive = isActive;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public Engagement getEngagement() {
        return engagement;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getTgl_input() {
        return tgl_input;
    }

    public void setTgl_input(Date tgl_input) {
        this.tgl_input = tgl_input;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}
