/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_document_reimburse")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "DocumentReimbursement.findAll", query = "SELECT d FROM DocumentReimburse d")})
public class DocumentReimburse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @SequenceGenerator(name = "reimbursedoc_seq", sequenceName = "reimbursedoc_seq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reimbursedoc_seq")
    @Column(name = "reimburse_document_id")
    protected String reimburse_document_id;
    
    @Column(name = "link_document")
    private String linkDocument;

    @Column(name = "status", length = 1)
    private String status;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "description")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @Temporal(TemporalType.DATE)
    @Column(name = "aprroved_date", nullable = true)
    private Date aprroved_date;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Temporal(TemporalType.DATE)
    @Column(name = "record_date", nullable = true)
    private Date record_date;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)// fetch = FetchType.LAZY
    @JoinColumn(name = "reimburse_id", referencedColumnName = "reimburse_id")
    private Reimbursement reimbursement;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Column(name = "date_input", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date date_input;

    @PrePersist
    protected void onCreate() {
        date_input = new Date();
        status = "0";
        this.setReimburse_document_id(UUID.randomUUID().toString());
//        this.setUuid(UUID.randomUUID().toString());
    }

    public DocumentReimburse() {
    }

    public String getReimburse_document_id() {
        return reimburse_document_id;
    }

    public void setReimburse_document_id(String reimburse_document_id) {
        this.reimburse_document_id = reimburse_document_id;
    }

//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }
    public String getLinkDocument() {
        return linkDocument;
    }

    public void setLinkDocument(String linkDocument) {
        this.linkDocument = linkDocument;
    }

    public Reimbursement getReimbursement() {
        return reimbursement;
    }

    public void setReimbursement(Reimbursement reimbursement) {
        this.reimbursement = reimbursement;
    }

    public Date getDate_input() {
        return date_input;
    }

    public void setDate_input(Date date_input) {
        this.date_input = date_input;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getAprroved_date() {
        return aprroved_date;
    }

    public void setAprroved_date(Date aprroved_date) {
        this.aprroved_date = aprroved_date;
    }

    public Date getRecord_date() {
        return record_date;
    }

    public void setRecord_date(Date record_date) {
        this.record_date = record_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.DocumentReimburse[reimburse_document_id=" + this.reimburse_document_id + " ]";
    }

}
