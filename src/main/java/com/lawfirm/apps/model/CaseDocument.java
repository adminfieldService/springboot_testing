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
@Table(name = "tbl_doc_case")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "CaseDocument.findAll", query = "SELECT d FROM CaseDocument d")})
public class CaseDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "casedoc_seq", sequenceName = "casedoc_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "casedoc_seq")
    @Column(name = "case_document_id")
    private Long case_document_id;
    @Column(name = "link_document")
    private String linkDocument;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)// fetch = FetchType.LAZY
    @JoinColumn(name = "engagement_id", referencedColumnName = "engagement_id")
    private CaseDetails caseDetails;

    @Basic(optional = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Column(name = "date_input", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date date_input;
    @Column(name = "is_active", length = 1, nullable = true)
    protected String isActive;

    @PrePersist
    protected void onCreate() {
        isActive = "1";
        date_input = new Date();
    }

    public CaseDocument() {
    }

    public Long getCase_document_id() {
        return case_document_id;
    }

    public void setCase_document_id(Long case_document_id) {
        this.case_document_id = case_document_id;
    }

    public String getLinkDocument() {
        return linkDocument;
    }

    public void setLinkDocument(String linkDocument) {
        this.linkDocument = linkDocument;
    }

    public CaseDetails getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(CaseDetails caseDetails) {
        this.caseDetails = caseDetails;
    }

    public Date getDate_input() {
        return date_input;
    }

    public void setDate_input(Date date_input) {
        this.date_input = date_input;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.CaseDocument[case_document_id=" + this.case_document_id + " ]";
    }
}
