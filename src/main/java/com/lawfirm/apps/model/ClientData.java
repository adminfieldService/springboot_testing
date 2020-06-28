/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_client_data")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "ClientData.findAll", query = "SELECT c FROM ClientData c")})
public class ClientData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "client_seq", sequenceName = "client_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "address")
    private String address;
    @Column(name = "npwp")
    private String npwp;
    @Column(name = "pic", nullable = true)
    private String pic;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Basic(optional = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Column(name = "tgl_input", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date tgInput;
    @Column(name = "is_active", length = 1, nullable = true)
    private String isActive;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "client")
//    private Collection<Engagement> engagementCollection;
    private List<Engagement> engagementCollection = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        tgInput = new Date();
        isActive = "1";
    }

    public void addEngagement(Engagement engagement) {
        engagement.setClient(this);
        engagementCollection.add(engagement);
    }

    public ClientData() {
    }

    public ClientData(Long clientId, String uuid, String clientName, String address, String npwp, String pic, Date tgInput, String isActive) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.address = address;
        this.npwp = npwp;
        this.pic = pic;
        this.tgInput = tgInput;
        this.isActive = isActive;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Date getTgInput() {
        return tgInput;
    }

    public void setTgInput(Date tgInput) {
        this.tgInput = tgInput;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Engagement> getEngagementCollection() {
        return engagementCollection;
    }

    public void setEngagementCollection(List<Engagement> engagementCollection) {
        this.engagementCollection = engagementCollection;
    }

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.ClientData[clientId=" + this.clientId + " ]";
    }
}
